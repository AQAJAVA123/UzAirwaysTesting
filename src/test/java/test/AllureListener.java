package test;

import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.AllureUtils;

public class AllureListener implements ITestListener {

    private void attachArtifacts(ITestResult result) {
        Object instance = result.getInstance();
        if (instance instanceof BaseTest) {
            BaseTest base = (BaseTest) instance;
            if (base.driver != null) {
                AllureUtils.takeScreenshot();
                AllureUtils.attachPageSource();
                AllureUtils.attachConsoleLogs();
            }
        }
    }

    @Override
    public void onTestFailure(ITestResult result) { attachArtifacts(result); }

    @Override
    public void onTestSkipped(ITestResult result) { attachArtifacts(result); }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) { attachArtifacts(result); }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) { attachArtifacts(result); }
}