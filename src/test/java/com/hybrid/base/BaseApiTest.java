package com.hybrid.base;

import com.hybrid.api.RestClient;
import com.hybrid.config.ConfigReader;
import com.hybrid.reports.ExtentManager;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {

    protected RestClient reqresClient;
    protected RestClient petClient;

    @BeforeClass(alwaysRun = true)
    public void setUpApiClients() {
        reqresClient = new RestClient(ConfigReader.get("reqres.base.url"));
        petClient    = new RestClient(ConfigReader.get("petstore.base.url"));
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport() {
        ExtentManager.flush();
    }
}
