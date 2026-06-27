package com.hybrid.constants;

public final class FrameworkConstants {

    private FrameworkConstants() {}

    public static final String PROJECT_ROOT      = System.getProperty("user.dir");
    public static final String RESOURCES_PATH    = PROJECT_ROOT + "/src/test/resources/";
    public static final String CONFIG_FILE_PATH  = RESOURCES_PATH + "config.properties";
    public static final String TEST_DATA_PATH    = RESOURCES_PATH + "testdata/test-users.xlsx";
    public static final String REPORTS_PATH      = PROJECT_ROOT + "/test-output/extent-reports/";

    public static final int EXPLICIT_WAIT        = 15;
    public static final int PAGE_LOAD_TIMEOUT    = 30;
}
