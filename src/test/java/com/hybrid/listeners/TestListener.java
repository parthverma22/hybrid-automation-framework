package com.hybrid.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.hybrid.reports.ExtentManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentManager.getInstance()
                .createTest(result.getMethod().getMethodName(),
                            result.getMethod().getDescription());
        ExtentManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().log(Status.PASS, "PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentManager.getTest().log(Status.FAIL, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.getTest().log(Status.SKIP,
                "Skipped — " + (result.getThrowable() != null ? result.getThrowable().getMessage() : ""));
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flush();
        ExtentManager.removeTest();
    }
}
