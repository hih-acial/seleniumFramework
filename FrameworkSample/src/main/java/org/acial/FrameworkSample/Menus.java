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

@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class Menus {
	@BeforeClass
	public static void setUp() throws Exception {
		Initialize();
		OrangeHRMActions.login(TestData.adminLogin, TestData.adminPwd);
	}
	@AfterClass
	public static void tearDown() throws Exception {
		OrangeHRMActions.logout();
		Destroy();
	}
  
  
  @Test 
  public void NavigationAdmin() throws Exception {
	  Page("Menu").Element("Admin").Click();
	  Page("Menu").Element("PIM").Click();
	  Page("Menu").Element("Conge").Click();
	  Page("Menu").Element("Heure").Click();
	  Page("Menu").Element("Recruitment").Click();
	  Page("Menu").Element("Performance").Click();
 }  

  @Test 
  public void bLoginOK() throws Exception {
  }
  
  @Test
  public void cLogout() throws Exception {
  }

}
