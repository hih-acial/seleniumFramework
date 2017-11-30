/*
 * 
 */
package org.acial.SeFramework;

import static org.acial.SeFramework.Selenium.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Author : Hassan IMHAH
 * Last modified : 11/11/2017
 * Class UIObject : Selenium Web Element or Sikuli Image
 * 		Value 		: as defined is dom xml file
 * 		Identifiers : convert value to selenium identifiers
 * 		WebElement 	: selenium web element if found
 * 	Methods :
 * 		Set : Replace selenium set or simulate it with sikuli
 * 		TypeEnter : send enter key
 * 		Click : click on element
 * 		Text : get text of element
 * 		Value : get value of element
 * 		SelectByText : select element by text
 * 	
 */
public class UIObject {
	public UIObject() {
		Uuid = UUID.randomUUID().toString();
	}
		public UIObject(String name, String type, String value) {
			Uuid = UUID.randomUUID().toString();
			Name = name;
			Type = type;
			Values[0] = value;
		}

		@Override
		public String toString() {
			return Name;
		}
		public String Uuid;
		public UIPage Parent;
		
		/** Name of web object */
		public String Name="";
		/** Value : values as recorded is DOM XML file. */
		public String[] Values= {"", "", "", "", ""};
		
		/** Type : element type (input, select, link ...) */
		public String Type="";
		
		/** Selenium by object of element when found */
		public By by = null;
		
		/** Identifiers : list of selenium identifiers from values. */
		public By[] identifiers = new By[4];
		
		
		/** The Element. */
		public WebElement Element = null;

		
		/**
		 * Sets the.
		 *
		 * @param value the value
		 * @throws IOException 
		 */
		public void Set(String value) throws IOException {
			if (screenShotOnAction) 
				ScreenShot (Type + "_" + Name ) ;
				WaitForElement();
				try {
					Element.clear();
					Element.sendKeys(value);
					Thread.sleep(thinkTime);
					logger.debug( "Element Set " + Name + ". " + by.toString() + " Value : " + value);
				}
				catch (Exception e) {
					if (screenShotOnError) 
						ScreenShot (Type + "_" + Name ) ;
					logger.error(e.getLocalizedMessage());
				}
		}
		
		/**
		 * Type enter.
		 * @throws IOException 
		 * @throws InterruptedException 
		 */
		public void TypeEnter() throws IOException, InterruptedException {
			if (screenShotOnAction) 
				ScreenShot (Type + "_" + Name ) ;
			WaitForElement();
			Element.sendKeys(Keys.RETURN);
			Thread.sleep(thinkTime);
			logger.debug( "Element Key type " + Name + ". " + by.toString() + " RETURN");
		}

		/**
		 * Type 
		 * @throws IOException 
		 * @throws InterruptedException 
		 */
		public void Type(String text) throws IOException, InterruptedException {
			if (screenShotOnAction) 
				ScreenShot (Type + "_" + Name ) ;
			WaitForElement();
			Element.sendKeys(text);
			Thread.sleep(thinkTime);
			logger.debug( "Element Key type " + Name + ". " + by.toString() + " " + text);
		}
		/**
		 * Click.
		 * @throws Exception 
		 */
		public void Click() throws Exception {
			if (screenShotOnAction) 
				ScreenShot (Type + "_" + Name ) ;
				try {
					WaitForElementClickable();
					Element.click();
					Thread.sleep(thinkTime);
					logger.debug( "Element Clicked " + Name + ". " + by.toString());
					logger.debug("Current URL :" + driver.getCurrentUrl());
				}
				catch (Exception e) {
					if (screenShotOnError) 
						ScreenShot (Type + "_" + Name ) ;
					logger.error(e.getLocalizedMessage());
					if (!continueOnError) 
						throw e;
				}
		}
		
		/**
		 * Text.
		 *
		 * @return the string
		 * @throws IOException 
		 */
		public String GetText() throws IOException {
			if (screenShotOnAction) 
				ScreenShot (Type + "_" + Name ) ;
			try {
				WaitForElement();

				logger.debug( "Element Get Text " + Name + ". " + by.toString() + ". Texte : " + Element.getText());
				return(Element.getText());
			}
			catch (Exception e) {
				logger.error(e.getLocalizedMessage());
				return null;
			}
		}
		
		/**
		 * Value.
		 *
		 * @return the string
		 * @throws IOException 
		 */
		public String GetValue() throws IOException {
			if (screenShotOnAction) 
				ScreenShot (Type + "_" + Name ) ;
			try {
				WaitForElement();
				logger.debug( "Element Get Value " + Name + ". " + by.toString() + ". Value : " + Element.getAttribute("value"));
				
				return(Element.getAttribute("value"));
			}
			catch (Exception e) {
				logger.error(e.getLocalizedMessage());
				return null;
			}
		}
		
		/**
		 * Select by text.
		 *
		 * @param value the value
		 * @throws IOException 
		 */
		public void SelectByText(String value) throws IOException {
			if (screenShotOnAction) 
				ScreenShot (Type + "_" + Name ) ;
			try {
				WaitForElement();
				logger.debug( "Element Select By Visible Text " + Name + ". " + by.toString() + ". Text : " + value);
				
				new Select(Element).selectByVisibleText(value);
			}
			catch (Exception e) {
				logger.error(e.getLocalizedMessage());
			}
		}
	    public void VerifyText(String text) throws IOException {
	  	  try {
	  		  assertEquals(text, GetText());
	  		} catch (AssertionError e) {
	  		  logger.error(e.getLocalizedMessage());
	  		}
	    }
	    public void CheckPresent() throws IOException {
		  	  try {
		  		  assertEquals(Exists(), true);
		  		} catch (AssertionError e) {
		  		  logger.error(e.getLocalizedMessage());
		  		}
		    }
	    public boolean Exists() {
		    try {
		    	Element = Selenium.driver.findElement(by);
		    		return Element.isDisplayed();
			    } catch (NoSuchElementException e) {
			      return false;
	    		} catch (Exception e) {
					logger.error(e.getMessage());
	  		        return false;
	    		}
	    }
	    public boolean Visible() {
	    	Boolean visible = wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
	    	if (visible) {
	    		logger.debug( "Element visible " + Name + ". " + by.toString());
		    	return true;
		    	}
	    	else {
	    		logger.warn( "Element not visible " + Name + ". " + by.toString());
		    	return false;
	    	}
	    }
	    public void WaitForElement( ) {
			Selenium.logger.debug( "Waiting for element " + Name + ". " + by.toString());
			for (int second = 0; second<implicitWait; second++) {
		        if (Exists()) {
		    		logger.debug( "Element found " + Name + ". " + by.toString());
		        	break;
		        }
		        try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
				}
			}
	    }
	    
	    public void WaitForElementClickable() {
			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.elementToBeClickable(by));
			logger.debug( "Element found " + Name + ". " + by.toString());
	    }
		
	    public void WaitForText(String text) {
			Selenium.logger.debug( "Waiting for text " + Element.getText() + ". Expected : " + text);
			for (int second = 0; second<implicitWait; second++) {
		        if (Element.getText().equals(text)) {
		    		logger.debug( "Text found " + Element.getText()  + ". " + by.toString());
		        	break;
		        }
		        try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
				}
			}
	  }
	    public void WaitForValue(String text) {
			Selenium.logger.debug( "Waiting for value " + Element.getAttribute("value") + ". Expected : " + text);
			for (int second = 0; second<implicitWait; second++) {
		        if (Element.getAttribute("value").equals(text)) {
		    		logger.debug( "Value found " + Element.getAttribute("value")  + ". " + by.toString());
		        	break;
		        }
		        try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
				}
			}
	    }
	    
	    
}
