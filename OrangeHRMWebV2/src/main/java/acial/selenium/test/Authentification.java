package acial.selenium.test;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static acial.selenium.utils.Selenium.*;
import static acial.selenium.test.OrangeHRMData.*;

public class Authentification {
	@DataProvider(name = "WrongAccess")
	public static Object[][] wrongCredentials() {
		return new Object[][] { { "admin", "test2" }, { "admin2", "test" }};
	}

	@BeforeClass(alwaysRun = true)
	public void setUp() throws Exception {
		Initialize();
	}
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
        Destroy();
	}
  
  
  @Test (priority=1, dataProvider = "WrongAccess")
  public void LoginKO(String login, String pwd) throws Exception {
	  Login (login, pwd);
	  VerifyText (Page("Authentication").Element("Message"), "Informations d'identification valides");
 }  

  @Test (priority=2)
  public void LoginOK() throws Exception {
	  Login ("admin", "test");
	  VerifyText (Page("Home").Element("WelcomeMenu"), "Welcome Admin");
  }
  
  @Test (priority=99)
  public void Logout() throws Exception {
	  Page("Dashbord").Element("DashbordMenu").Click();
	  Page("Home").Element("WelcomeMenu").Click();
	  Page("Home").Element("Logout").Click();
  }

  private void Login(String login, String pwd) throws Exception {
	  WaitForElement (Page("Authentication").Element("Username"));
	  Page("Authentication").Element("Username").Set("admin");
	  Page("Authentication").Element("Password").Set("test");
	  Page("Authentication").Element("Login").Click();
 }  

}
