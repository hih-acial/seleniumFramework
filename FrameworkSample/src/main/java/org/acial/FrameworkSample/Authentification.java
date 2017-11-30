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
	public static Object[][] logins() {
		return TestData.wrongCredentials;
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
  @Parameters(method = "logins")
  public void aLoginKO(String login, String pwd) throws Exception {
	  OrangeHRMActions.login (login, pwd);
	  Page("Authentication").Element("Message").VerifyText (TestData.loginFailedMessage);
 }  

  @Test 
  public void bLoginOK() throws Exception {
	  OrangeHRMActions.login (TestData.adminLogin, TestData.adminPwd);
	  Page("Home").Element("WelcomeMenu").VerifyText (TestData.welcome);
  }
  
  @Test
  public void cLogout() throws Exception {
	  OrangeHRMActions.logout();
  }


}
