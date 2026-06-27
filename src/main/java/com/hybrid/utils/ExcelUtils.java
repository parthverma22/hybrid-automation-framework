package com.hybrid.utils;

import com.hybrid.constants.FrameworkConstants;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ExcelUtils {

    private ExcelUtils() {}

    /**
     * Reads all rows from the given sheet into a TestNG-compatible Object[][].
     * Each element is a Map<String, String> keyed by column headers (row 0).
     */
    public static Object[][] getTestData(String sheetName) {
        List<Map<String, String>> rows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(FrameworkConstants.TEST_DATA_PATH);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("Sheet '" + sheetName + "' not found.");

            Row header = sheet.getRow(0);
            int colCount = header.getLastCellNum();

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                Map<String, String> rowData = new HashMap<>();
                for (int c = 0; c < colCount; c++) {
                    String key = header.getCell(c).getStringCellValue().trim();
                    Cell cell  = row.getCell(c);
                    rowData.put(key, cell == null ? "" : getCellValue(cell));
                }
                rows.add(rowData);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + e.getMessage(), e);
        }

        Object[][] data = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) data[i][0] = rows.get(i);
        return data;
    }

    /**
     * Generates the test data .xlsx if it doesn't already exist.
     * Called in @BeforeSuite so the file is always present before tests run.
     */
    public static void generateIfAbsent() {
        File file = new File(FrameworkConstants.TEST_DATA_PATH);
        if (file.exists()) return;
        file.getParentFile().mkdirs();

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("LoginData");

            String[] headers = {"username", "password", "expectedResult"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            Object[][] data = {
                {"standard_user",   "secret_sauce", "success"},
                {"locked_out_user", "secret_sauce", "failure"},
                {"problem_user",    "secret_sauce", "success"},
            };
            for (int r = 0; r < data.length; r++) {
                Row row = sheet.createRow(r + 1);
                for (int c = 0; c < data[r].length; c++) {
                    row.createCell(c).setCellValue(data[r][c].toString());
                }
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate test data file: " + e.getMessage(), e);
        }
    }

    private static String getCellValue(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default      -> "";
        };
    }
}
