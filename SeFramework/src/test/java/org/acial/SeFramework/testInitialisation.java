package org.acial.SeFramework;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.junit.runner.RunWith;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.acial.SeFramework.Selenium.*;

@RunWith(JUnitParamsRunner.class)
public class testInitialisation {

	  private Object[] pages() {
		    return new Object[]{
		                 new Object[]{"Authentication"},
		                 new Object[]{"Home"},
		                 new Object[]{"Menu"},
		                 new Object[]{"Admin_Organization_GeneralInformation"},
		                 new Object[]{"Admin_Job_Titles"},
		                 new Object[]{"PIM_EmployeeList"},
		                 new Object[]{"PIM_AddEmployee"},
		                 new Object[]{"Dashbord"},
		            };
		}  

    
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
	@Parameters(method = "pages")
	public void checkPage (String name) {
		UIPage p = Page(name);
		assertNotNull(p);
		if (p!=null)
			assertEquals(p.Name, name);
		
	}
	@Test
	public void checkObject() {
		UIPage p = Page("Authentication");
		if (p!=null) {
			UIObject e = p.UIObjects.get(0);
			assertNotNull(e);
			assertEquals(e.Type, "Champ");
			assertEquals(e.Name, "Username");
			assertEquals(e.Values[0], "ID:txtUsername");
		}
	}
	
	@Test
	public void loginLogout() throws Exception {
		  Page("Authentication").Element("Username").WaitForElement();
		  Page("Authentication").Element("Username").Set("admin");
		  Page("Authentication").Element("Password").Set("test");
		  Page("Authentication").Element("Login").Click();
		  Page("Home").Element("WelcomeMenu").WaitForElement();
		  Page("Dashbord").Element("DashbordMenu").Click();
		  Page("Home").Element("WelcomeMenu").Click();
		  Page("Home").Element("Logout").Click();
	}
}
