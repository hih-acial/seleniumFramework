package acial.selenium.test;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import static acial.selenium.utils.Selenium.Destroy;
import static acial.selenium.utils.Selenium.Initialize;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import static acial.selenium.utils.Selenium.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(DataProviderRunner.class)
public class AuthentificationJUnit {

	
	@DataProvider
    public static Object[][] loginError() {
        // @formatter:off
        return OrangeHRMData.loginError ;
        // @formatter:on
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
	  @UseDataProvider("loginError")
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
