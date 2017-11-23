package org.acial.SeFramework;


import org.apache.commons.io.FileUtils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.ScreenshotException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.io.Files;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.acial.SeFramework.Selenium.*;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public final class Selenium {
	public static WebDriver driver = null;
	public static List<UIPage> GuiPages = new ArrayList<UIPage>();
	public final static Logger logger = Logger.getLogger("OrangeHRM");
	
	private static String baseUrl="";
	public static String browser="";
	private static int implicitWait=10;
	private static int explicitWait=10;
	private static String driversPath="";
	private static String screenShotPath="";
	public static boolean screenShotOnError=false;
	public static boolean screenShotOnAction=false;

	private enum Browsers {
	    Remote, Firefox, IE, Chrome, HtmlUnit
	}
	private static String remote="";
	public static String dataPath="";
	private static String domFile="";

	public static void Initialize() throws IOException {
		
		logger.info( "Start" );
		
		Properties prop = new Properties();
		InputStream input = null;
		input = new FileInputStream("config.properties");
		// load a properties file
		prop.load(input);
	    baseUrl = prop.getProperty("BaseUrl");
	    browser = prop.getProperty("Browser");
		logger.debug( "Browser : " + browser );
	    implicitWait = Integer.parseInt(prop.getProperty("ImplicitWait"));
	    explicitWait = Integer.parseInt(prop.getProperty("ExplicitWait"));
	    driversPath = prop.getProperty("DriversPath");
	    remote= prop.getProperty("Remote");
	    dataPath = prop.getProperty("DataPath");
	    screenShotPath = prop.getProperty("ScreenShotPath");
	    screenShotOnError = prop.getProperty("ScreenShotOnError").toLowerCase().equals ("true");
	    screenShotOnAction = prop.getProperty("ScreenShotOnAction").toLowerCase().equals ("true");
	    
	    domFile = prop.getProperty("DomFile");
	    SetDriver();
	    try {
	    	
	    	driver.get(baseUrl);
		}
	    catch (Exception e) {
	    	// Contournement pour �viter les erreurs javascript
	    	// avec HtmlUnitDriver en Remote
	    	if (e.getCause().getClass().equals(ScreenshotException.class))
	    		logger.error("Javascript Error : " + e.getMessage());
	    	else
	    		throw e;
	    }
		logger.info("Current URL :" + driver.getCurrentUrl());
	}
	public static void Destroy() throws IOException {
		driver.close();
		driver.quit();
		driver = null;
		logger.info( "End" );
	}
	public static UIPage Page(String page) {
		for (int i=0;i<GuiPages.size();i++) {
			if ( GuiPages.get(i).Name.equals(page) ) {
				UIPage p = GuiPages.get(i);
				p.driver = driver;
				logger.debug("Page : " + p.Name);
				return (p);
			}
		}
		return(null);
	}
	public static void SetDriver () {
		if (driver==null)
		{
			Browsers selectedBrowser = Browsers.valueOf(browser);
			switch(selectedBrowser) {
			    case Firefox:
			    	if ((remote==null) || (remote.isEmpty())) {
			    		// DesiredCapabilities caps = new DesiredCapabilities();
			    		// caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			    		// caps.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			    		 
			    		driver = new FirefoxDriver();
			    	}
			    	else {
						DesiredCapabilities capabilities = DesiredCapabilities.firefox();
						capabilities.setBrowserName("firefox");
						capabilities.setPlatform(Platform.WINDOWS);
						try {
							driver = new RemoteWebDriver(new URL(remote), capabilities);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
			    	}
			        break;
			    case Chrome:
			        System.setProperty("webdriver.chrome.driver", driversPath + "/chromedriver.exe");
					ChromeOptions options = new ChromeOptions();
					options.addArguments("disable-infobars");
					options.addArguments("--start-minimized");

					if ((remote==null) || (remote.isEmpty())) {
				        driver = new ChromeDriver(options);
			        }
			    	else {
						DesiredCapabilities capabilities = DesiredCapabilities.chrome();
						capabilities.setBrowserName("chrome");
						capabilities.setPlatform(Platform.WINDOWS);
						try {
							driver = new RemoteWebDriver(new URL(remote), capabilities);
							
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
			    	}
			        break;
			    case IE:
			        System.setProperty("webdriver.ie.driver", driversPath + "/IEDriverServer.exe");
			        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			        capabilities.setCapability("ignoreZoomSetting", true);
			        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			    	if ((remote==null) || (remote.isEmpty())) {
				    	driver = new InternetExplorerDriver(capabilities);
			        }
			    	else {
			    		capabilities.setBrowserName("internet explorer");
			    		capabilities.setPlatform(Platform.WINDOWS);
						try {
							driver = new RemoteWebDriver(new URL(remote), capabilities);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
			    	}
			        break;
			    default:
		    		driver = new FirefoxDriver();
			    	break;
			}
		    driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
		    driver.manage().window().maximize();
		    LoadGuiPages ();
		}
	}
	public static void LoadGuiPages () {
		File fXmlFile = new File(domFile);
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("page");
			for (int temp = 0; temp < nList.getLength(); temp++) {
		        Node nNode = nList.item(temp);
		        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		           Element eElement = (Element) nNode;
		           
		           UIPage page = new UIPage();
		           page.Name = eElement.getElementsByTagName("name").item(0).getTextContent();
		           page.Type= eElement.getElementsByTagName("type").item(0).getTextContent();
		           GuiPages.add(page);
		           
		           NodeList objects = eElement.getElementsByTagName("object");
		           for (int k = 0; k < objects.getLength(); k++) {
		           		Node nobj = objects.item(k);
		            	Element objelem = (Element) nobj;
		            		
		            	UIObject guiObject = new UIObject();
		            	guiObject.Name = objelem.getElementsByTagName("name").item(0).getTextContent();
		            	guiObject.Type = objelem.getElementsByTagName("type").item(0).getTextContent();
		            	guiObject.Value[0] = objelem.getElementsByTagName("value").item(0).getTextContent();
		            		
		            	guiObject.Value[1] = (objelem.getElementsByTagName("value2").item(0)==null) ? "" : objelem.getElementsByTagName("value2").item(0).getTextContent();
		            	guiObject.Value[2] = (objelem.getElementsByTagName("value3").item(0)==null) ? "" : objelem.getElementsByTagName("value3").item(0).getTextContent();
		            	guiObject.Value[3] = (objelem.getElementsByTagName("value4").item(0)==null) ? "" : objelem.getElementsByTagName("value4").item(0).getTextContent();
		            	guiObject.Value[4] = (objelem.getElementsByTagName("value5").item(0)==null) ? "" : objelem.getElementsByTagName("value5").item(0).getTextContent();

		            	page.UIObjects.add(guiObject);
		            	
		           }
		            	
		           }
		    }
		} catch (ParserConfigurationException e) {
			logger.error(e.getMessage());
		} catch (SAXException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

    public static boolean isElementPresent(UIObject obj) {
	    try {
	    		
	    		obj.Element = Selenium.driver.findElement(obj.by);
	    		return obj.Element.isDisplayed();
		    } catch (NoSuchElementException e) {
		      return false;
    		} catch (Exception e) {
				logger.error(e.getMessage());
  		        return false;
    		}
  }
    public static void WaitForElement(UIObject obj) {
		Selenium.logger.debug( "Waiting for element " + obj.Name + ". " + obj.by.toString());
		for (int second = 0; second<implicitWait; second++) {
	        if (isElementPresent(obj)) {
	    		logger.debug( "Element found " + obj.Name + ". " + obj.by.toString());
	        	break;
	        }
	        try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}
    }
    
    public static void WaitForElementClickable(UIObject obj) {
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.elementToBeClickable(obj.by));
		logger.debug( "Element found " + obj.Name + ". " + obj.by.toString());
    }
	
    public static void WaitForText(UIObject obj, String text) {
		Selenium.logger.debug( "Waiting for text " + obj.Element.getText() + ". Expected : " + text);
		for (int second = 0; second<implicitWait; second++) {
	        if (obj.Element.getText().equals(text)) {
	    		logger.debug( "Text found " + obj.Element.getText()  + ". " + obj.by.toString());
	        	break;
	        }
	        try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}
  }
    public static void WaitForValue(UIObject obj, String text) {
		Selenium.logger.debug( "Waiting for value " + obj.Element.getAttribute("value") + ". Expected : " + text);
		for (int second = 0; second<implicitWait; second++) {
	        if (obj.Element.getAttribute("value").equals(text)) {
	    		logger.debug( "Value found " + obj.Element.getAttribute("value")  + ". " + obj.by.toString());
	        	break;
	        }
	        try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}
    }
    
    public static void VerifyText(UIObject obj, String text) {
	  try {
		  assertEquals(text, obj.Element.getText());
		} catch (AssertionError e) {
		  logger.error(e.getLocalizedMessage());
		}
    }
    public static void ScreenShot (String name) throws IOException {
    	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    	
    	File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
    	// Now you can do whatever you need to do with it, for example copy somewhere
    	Files.copy(scrFile, new File(screenShotPath + "\\" + name + "_" + timeStamp  + ".png"));
    }
}
