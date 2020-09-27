package testcase;

import java.io.IOException;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import reporter.ExtentBase;

public class SampleTest extends ExtentBase {

	@Test
	public void loginTest() throws IOException {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://www.google.com");
		addComment("Step 1 : Title is " + driver.getTitle());
		addComment("This comment is for google page");
		takeScreenshot();
		Assert.assertTrue(true);
		
		addComment("End of test case reached");
	}
	
	@Test
	public void loginTest1() throws IOException {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://github.com/learn-automation-selenium/cucumber-extent-report");
		addComment("Step 1 : Title is " + driver.getTitle());
		addComment("This comment is for github page");
		takeScreenshot();
		Assert.assertTrue(false);
		
		addComment("End of test case reached");
	}
	
}
