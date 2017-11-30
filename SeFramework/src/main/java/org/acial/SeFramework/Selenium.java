package org.acial.SeFramework;


import org.apache.commons.io.FileUtils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
	private static Properties prop = new Properties();
	public static WebDriver driver = null;
	public static WebDriverWait wait= null;
	public static List<UIPage> GuiPages = new ArrayList<UIPage>();
	public final static Logger logger = Logger.getLogger("OrangeHRM");
	

	public static String ApplicationName = "";
	public static String browser="";
	public static int implicitWait=10;
	public static int thinkTime=10;
	public static int explicitWait=10;
	public static boolean continueOnError=false;
	public static boolean screenShotOnError=false;
	public static boolean screenShotOnAction=false;

	public static String baseUrl="";
	public static String driversPath="";
	public static String screenShotPath="";

	private enum Browsers {
	    Remote, Firefox, IE, Chrome, HtmlUnit
	}
	private static String remote="";
	public static String dataPath="";
	public static String domFile="";

	public static void Initialize() throws IOException, XPathExpressionException {
		Initialize(true);
	}
	public static void Initialize(Boolean setDriver) throws IOException, XPathExpressionException {
		
		logger.info( "Start" );
		
		
		InputStream input = null;
		input = new FileInputStream("config.properties");
		// load a properties file
		prop.load(input);
	    baseUrl = getStringProperty("BaseUrl");
	    browser = getStringProperty("Browser");
		logger.debug( "Browser : " + browser );
		thinkTime = getIntProperty("ThinkTime");
	    implicitWait = getIntProperty("ImplicitWait");
	    explicitWait = getIntProperty("ExplicitWait");
	    driversPath = getStringProperty("DriversPath");
	    remote= getStringProperty("Remote");
	    dataPath = getStringProperty("DataPath");
	    screenShotPath = getStringProperty("ScreenShotPath");
	    continueOnError = getBooleanProperty ("ContinueOnError");
	    screenShotOnError = getBooleanProperty ("ScreenShotOnError");
	    screenShotOnAction = getBooleanProperty ("ScreenShotOnAction");
	    
	    domFile = getStringProperty("DomFile");
	    if (setDriver) {
	    	loadBrowser(); 
	    }
	}
	public static void loadBrowser() throws XPathExpressionException {
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
	
	private static int getIntProperty (String property) {
		if (!(prop.getProperty(property)==null) && (!prop.getProperty(property).isEmpty())) 
			return Integer.parseInt(prop.getProperty(property));
		else
			return 0;
	}
	private static String getStringProperty (String property) {
		return prop.getProperty(property);
	}
	private static Boolean getBooleanProperty (String property) {
		if (!(prop.getProperty(property)==null) && (!prop.getProperty(property).isEmpty())) 
			return prop.getProperty(property).toLowerCase().equals ("true");
		else
			return false;
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
	public static void SetDriver () throws XPathExpressionException {
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
		    wait = new WebDriverWait(driver, explicitWait);

		    driver.manage().window().maximize();
		    LoadGuiPages ();
		}
	}
	public static void LoadGuiPages () throws XPathExpressionException {
		File fXmlFile = new File(domFile);
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder;
	    GuiPages.clear();
		try {
			XPathFactory xpf = XPathFactory.newInstance();
			XPath path = xpf.newXPath();
			
			dBuilder = dbFactory.newDocumentBuilder();
		
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("page");
			ApplicationName = (String)path.evaluate("/main/name", doc.getDocumentElement());
			
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
		            	guiObject.Values[0] = objelem.getElementsByTagName("value").item(0).getTextContent();
		            	guiObject.Values[1] = (objelem.getElementsByTagName("value1").item(0)==null) ? "" : objelem.getElementsByTagName("value1").item(0).getTextContent();
		            	guiObject.Values[2] = (objelem.getElementsByTagName("value2").item(0)==null) ? "" : objelem.getElementsByTagName("value2").item(0).getTextContent();
		            	guiObject.Values[3] = (objelem.getElementsByTagName("value3").item(0)==null) ? "" : objelem.getElementsByTagName("value3").item(0).getTextContent();
		            	guiObject.Values[4] = (objelem.getElementsByTagName("value4").item(0)==null) ? "" : objelem.getElementsByTagName("value4").item(0).getTextContent();
		            	guiObject.Parent = page;
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

	// Only with chrome driver
    public static List<UIObject> parseElements (String typeFilter) {
    	String identifier="", type="";
    	List<UIObject> uiObjects = new ArrayList<UIObject>();
    	
    	List<WebElement> elements = ((ChromeDriver) driver).findElementsByTagName(typeFilter);
		for (WebElement element:elements) {
			identifier = "";
            String elementType = element.getAttribute("type");
            if (((!elementType.equals("hidden")) && element.isDisplayed() && element.isEnabled()) || 
            	(typeFilter.equals("a") && element.isEnabled()))
            {
            	if (typeFilter.equals("a"))
            		type = "Lien";
            	else {
	                if (elementType.equals("text")) type = "Champ";
	                if (elementType.equals("password")) type = "Champ";
	                if (elementType.equals("checkbox")) type = "CaseACocher";
	                if (elementType.equals("select-one")) type = "Liste";
	                if (elementType.equals("button")) type = "Bouton";
	                if (elementType.equals("submit")) type = "Bouton";
            	}
                
                String id = element.getAttribute("id");
                String name = element.getAttribute("name");
                String text = element.getText();
                String href = element.getAttribute("href");
                if (!id.isEmpty()) identifier="id:" + id;
                else if (!name.isEmpty()) identifier="name:" + name;

                if (identifier.isEmpty()) {
	                if ((href != null) && !href.isEmpty())
	                {
	                	System.out.println("Found href : " + href);
	                    if (!(href.lastIndexOf(('/') + 1) == href.length())) {
	                        href = href.substring(href.lastIndexOf('/') + 1);
	                        if (!href.isEmpty()) {
	                        	name = href;
	                        	identifier = "xpath://a[contains(@href, '" + href + "')]";
	                        }
	                    }
	
	                }
                }

                
                if (!identifier.isEmpty()) {
                	UIObject uiObject = new UIObject(name.isEmpty() ? (text.isEmpty() ? id : text) : name,
                							type, identifier);
                	uiObjects.add(uiObject);
                }
            }
		}
    	return ( uiObjects );
    }
    
    public static void parseElements (UIPage page, String type) {
    	Boolean exists = false;
    	List<UIObject>  objs = parseElements(type);
    	for (UIObject obj:objs) {
    		exists = false;
    		obj.Parent = page;
    		for (UIObject o:page.UIObjects) {
    			if (o.Values [0].equals(obj.Values[0]))
    				exists = true;
    		}
    		if (!exists)
    			page.UIObjects.add(obj);
    	}
    }    
    public static void parseChamps (UIPage page) {
    	parseElements (page, "input");
    	parseElements (page, "select");
    	parseElements (page, "button");
    	parseElements (page, "textarea");
    }
    public static void parseMenus (UIPage page) {
    	parseElements (page, "a");
    }
    public static void ScreenShot (String name) throws IOException {
    	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    	
    	File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
    	// Now you can do whatever you need to do with it, for example copy somewhere
    	Files.copy(scrFile, new File(screenShotPath + "\\" + name + "_" + timeStamp  + ".png"));
    }
    public static void HighlightElement(String identification) throws InterruptedException 
    {
    	if (driver!=null) {
	    	WebElement element = null;
	    	String[] part = identification.split(":");
	        switch (part[0])
	        {
	            case "id":
	                element = driver.findElement(By.id(part[1]));
	                break;
	            case "xpath":
	                element = driver.findElement(By.xpath(part[1]));
	                break;
	            case "css":
	                element = driver.findElement(By.cssSelector(part[1]));
	                break;
	            case "name":
	                element = driver.findElement(By.name(part[1]));
	                break;
	        }
	        HighlightElement (element);
        }
    }
    private static void HighlightElement(WebElement element) throws InterruptedException
    {
    	JavascriptExecutor jsDriver = (JavascriptExecutor)driver;
        for (int i = 0; i < 2; i++)
        {
        	String highlightJavascript = "arguments[0].style.cssText = \"background-color: LawnGreen; 	border-style: solid; border-width: 4px; border-color: LawnGreen\";";
            jsDriver.executeScript(highlightJavascript, new Object[] { element });
            Thread.sleep(200);
            highlightJavascript = "arguments[0].style.cssText = \"background-color: White; border-color: White;\"";
            jsDriver.executeScript(highlightJavascript, new Object[] { element });
            Thread.sleep(200);
        }
    }
}
