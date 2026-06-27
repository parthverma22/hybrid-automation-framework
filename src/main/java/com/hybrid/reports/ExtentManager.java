package com.hybrid.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.hybrid.constants.FrameworkConstants;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ExtentManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> TEST_THREAD = new ThreadLocal<>();

    private ExtentManager() {}

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            new File(FrameworkConstants.REPORTS_PATH).mkdirs();

            String timestamp  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String reportPath = FrameworkConstants.REPORTS_PATH + "Report_" + timestamp + ".html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle("Hybrid Automation Report");
            spark.config().setReportName("UI + API Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Framework", "Hybrid UI + API Automation");
            extent.setSystemInfo("Author",    "Parth Verma");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("OS",   System.getProperty("os.name"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
        }
        return extent;
    }

    public static ExtentTest getTest()              { return TEST_THREAD.get(); }
    public static void       setTest(ExtentTest t)  { TEST_THREAD.set(t); }
    public static void       removeTest()           { TEST_THREAD.remove(); }

    public static synchronized void flush() {
        if (extent != null) extent.flush();
    }
}
