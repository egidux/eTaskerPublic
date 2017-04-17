package org.eTasker.html;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class LoginPageTest {
	private WebDriver driver ;
	
	@Before
	public void setup() {
		driver = new HtmlUnitDriver();
	}
	
	@Test
	public void testLoginPage() {
		driver.get("https://cpssd1-etasker.computing.dcu.ie");
        WebElement element = driver.findElement(By.className("btn-login"));
		Assert.assertNotNull(element);
		
		driver.quit();
	}
}
