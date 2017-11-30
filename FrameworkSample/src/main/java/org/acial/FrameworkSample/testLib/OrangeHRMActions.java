package org.acial.FrameworkSample.testLib;

import static org.acial.SeFramework.Selenium.Page;

import java.io.IOException;

public final class OrangeHRMActions {
	  public static void login(String login, String pwd) throws Exception {
		  Page("Authentication").Element("Username").WaitForElement();
		  Page("Authentication").Element("Username").Set(login);
		  Page("Authentication").Element("Password").Set(pwd);
		  Page("Authentication").Element("Login").Click();
	 }  
	  
	 public static void logout() throws Exception {
		  Page("Dashbord").Element("DashbordMenu").Click();
		  Page("Home").Element("WelcomeMenu").Click();
		  Page("Home").Element("Logout").Click();
	 }
	 public static void AjouterSalarie (String nom, String prenom) throws Exception {
		  Page("Menu").Element("PIM").WaitForElement();
		  Page("Menu").Element("PIM").Click();
		  Page("Menu").Element("PIM_AddEmployee").Click();


		  Page("PIM_AddEmployee").Element("FirstName").Set(prenom);
		  Page("PIM_AddEmployee").Element("LastName").Set(nom);
		  Page("PIM_AddEmployee").Element("EditSaveBtn").Click();

	
	  }
	 public static void DetailsSalarie (String dateOfBirth, String marital, String nation, Boolean male) throws Exception {
			Page("DetailsEmployee").Element("EditSaveBtn").Click();
			Page("DetailsEmployee").Element("DateOfBirth").Set(dateOfBirth);
			Page("DetailsEmployee").Element("MaritalStatus").SelectByText(marital);
			Page("DetailsEmployee").Element("Nationality").SelectByText(nation);
			if (male)
				Page("DetailsEmployee").Element("Male").Click();
			else
				Page("DetailsEmployee").Element("Female").Click();
		
		    Page("DetailsEmployee").Element("EditSaveBtn").Click();
	
	  }

}
