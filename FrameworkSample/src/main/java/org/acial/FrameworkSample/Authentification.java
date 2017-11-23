package org.acial.FrameworkSample;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.acial.SeFramework.Selenium.*;

@RunWith(JUnitParamsRunner.class)
@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class Authentification {
	public static Object[][] wrongCredentials() {
		return new Object[][] { { "admin", "test2" }, { "admin2", "test" }};
	}

	@BeforeClass
	public static void setUp() throws Exception {
		Initialize();
	}
	@AfterClass
	public static void tearDown() throws Exception {
        Destroy();
	}
  
  
  @Test 
  @Parameters(method = "wrongCredentials")
  public void aLoginKO(String login, String pwd) throws Exception {
	  Login (login, pwd);
	  VerifyText (Page("Authentication").Element("Message"), "Informations d'identification valides");
 }  

  @Test 
  public void bLoginOK() throws Exception {
	  Login ("admin", "test");
	  VerifyText (Page("Home").Element("WelcomeMenu"), "Welcome Admin");
  }
  
  @Test
  public void cLogout() throws Exception {
	  Page("Dashbord").Element("DashbordMenu").Click();
	  Page("Home").Element("WelcomeMenu").Click();
	  Page("Home").Element("Logout").Click();
  }

  private void Login(String login, String pwd) throws Exception {
	  WaitForElement (Page("Authentication").Element("Username"));
	  Page("Authentication").Element("Username").Set(login);
	  Page("Authentication").Element("Password").Set(pwd);
	  Page("Authentication").Element("Login").Click();
 }  

}
