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
public class Salaries {
	public static Object[][] salaries() {
		return TestData.salaries;
	}

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
  @Parameters(method = "salaries")
  public void aAjouterSalarie (String nom, String prenom, String dateOfBirth, String marital, String nation, Boolean male) throws Exception {
	  OrangeHRMActions.AjouterSalarie(nom, prenom);
	  OrangeHRMActions.DtailsSalarie(dateOfBirth, marital, nation, male);
  }  

  @Test 
  public void zSupprimerSalaries() throws Exception {
	  Page("Menu").Element("PIM").Click();
	  Page("Menu").Element("PIM_EmployeeList").Click();
	  Page("PIM_EmployeeList").Element("EmployeeName").Set("IMHAH");
	  Page("PIM_EmployeeList").Element("SearchBtn").Click();
	  Page("PIM_EmployeeList").Element("SelectAllEmployees").Click();
	  Page("PIM_EmployeeList").Element("DeleteEmployees").Click();
	  Page("PIM_EmployeeList").Element("ConfirmDeleteEmployees").Click();
  }
  
  @Test
  public void bRechercherSalarie() throws Exception {
	  Page("Menu").Element("PIM").Click();
	  Page("Menu").Element("PIM_EmployeeList").Click();
	  Page("PIM_EmployeeList").Element("EmployeeName").Set("IMHAH");
	  Page("PIM_EmployeeList").Element("SearchBtn").Click();
	  Page("PIM_EmployeeList").Element("Ligne1_Nom").Click();

	  Page("DetailsEmployee").Element("btnSave").Click();
	  Page("DetailsEmployee").Element("LastName").VerifyText("IMHAH");
	  Page("DetailsEmployee").Element("btnSave").Click();

  }

}
