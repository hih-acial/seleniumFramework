package org.acial.SeFramework;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.acial.SeFramework.Selenium.*;

@RunWith(JUnitParamsRunner.class)
public class testParser {
    
	@BeforeClass
	public static void start() {
		try {
			Initialize();
			assertNotNull(Selenium.driver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
						e.printStackTrace();
		}
		assertTrue (Selenium.browser.equals("Chrome") || Selenium.browser.equals("IE") && Selenium.browser.equals("Firefox"));
		assertNotNull(Selenium.driver);
	}
	@AfterClass
	public static void quit() {
		try {
			Destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testParseMenus() {
		Selenium.driver.findElement(By.id("txtUsername")).clear();
		Selenium.driver.findElement(By.id("txtUsername")).sendKeys("admin");
		Selenium.driver.findElement(By.id("txtPassword")).clear();
		Selenium.driver.findElement(By.id("txtPassword")).sendKeys("test");
		Selenium.driver.findElement(By.id("btnLogin")).click();

		List<UIObject> uiObjects= parseElements("a");
		for (UIObject obj:uiObjects) {
			System.out.println("Name : " + obj.Name);
			System.out.println("Type : " + obj.Type);
			System.out.println("Identifier : " + obj.Values[0]);
		}
	}
	
	public void textParseChamps() {
		UIPage page = new UIPage ();
		page.Name = "Login";
		Selenium.parseChamps (page);
		for (UIObject obj:page.UIObjects) {
			System.out.println("Name : " + obj.Name);
			System.out.println("Type : " + obj.Type);
			System.out.println("Identifier : " + obj.Values[0]);
		}

	}}
