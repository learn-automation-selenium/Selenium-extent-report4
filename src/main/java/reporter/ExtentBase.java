package reporter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import base.TestBase;
import commonhelper.Utility;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ExtentBase extends TestBase {

	public ExtentSparkReporter extentHtmlReporter;
	public ExtentReports extentReports;
	public ExtentTest extentTest;
	

	@BeforeClass
	public void prerequisite() {
		WebDriverManager.chromedriver().setup();
	}
	
	@BeforeTest
	public void extentReportSetUp() {
		extentHtmlReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/reports/extent-report.html");
		extentHtmlReporter.loadXMLConfig(System.getProperty("user.dir") + "/config/extent-config.xml");
		
		
		extentReports = new ExtentReports();
		extentReports.attachReporter(extentHtmlReporter);
		extentReports.setSystemInfo("Automation Tester", System.getProperty("user.name"));
		extentReports.setSystemInfo("Build Id", "Test-1234");
	}
	
	@AfterTest
	public void extentReportCleanUp() {
		extentReports.flush();
	}
	
	@BeforeMethod
	public void setup(Method method) {
		myList.clear();
	}

	
	@AfterMethod
	public void tearDown(ITestResult result) throws IOException {
		
		// iterate over the comments stored in arraylist and print it in the report
		for(int i=0; i<myList.size(); i++) {
			if (result.getStatus() == ITestResult.SKIP) {
				String logText = "<b><font color=\"blue\">" + myList.get(i) +"</font></b>";
				if (myList.get(i).contains(".png")) {
					extentTest.skip("", MediaEntityBuilder.createScreenCaptureFromPath(myList.get(i)).build());
				} else {
					extentTest.skip(logText);
				}
			} else {
				String logText = "<b><font color=\"blue\">" + myList.get(i) +"</font></b>";
				if (myList.get(i).contains(".png")) {
					extentTest.pass("", MediaEntityBuilder.createScreenCaptureFromPath(myList.get(i)).build());
				} else {
					extentTest.pass(logText);
				}
			}
		}
		
		// if any assertion failure occurs print the details in the report
		if (result.getStatus() == ITestResult.FAILURE) {
			String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
			String exceptionText = "<details><summary><b><font color=\"red\">Click to see exception details"
					+ "</font></b></summary>"+exceptionMessage.replaceAll(",", "<br>")+"</details> \n";
			System.out.println("exceptionMessage" + exceptionMessage);
			extentTest.fail(exceptionText);
		}
		
		driver.quit();
	}
	
	/**
	 * This method will hold all the comments for a test method
	 * @param comment
	 */
	public static void addComment(String comment) {
		myList.add(comment);
	}
	
	/**
	 * Call this method to attach the screenshot in the report
	 */
	public static void takeScreenshot() {
		captureScreenshot();
	}
	
	/**
	 * This method will be called to capture screenshot and store the path details
	 * 
	 */
	private static void captureScreenshot(){
		String screenshotPath = Utility.getScreenshot(driver);
		myList.add(screenshotPath);
	}
}
