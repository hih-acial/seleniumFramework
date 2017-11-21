/*
 * 
 */
package acial.selenium.dom;

import static acial.selenium.utils.Selenium.*;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.sikuli.script.FindFailed;

/**
 * Author : Hassan IMHAH
 * Last modified : 11/11/2017
 * Class UIObject : Selenium Web Element or Sikuli Image
 * 		Value 		: as defined is dom xml file
 * 		Identifiers : convert value to selenium identifiers
 * 		Image 		: sikuli image file
 * 		WebElement 	: selenium web element if found
 * 	Methods :
 * 		Set : Replace selenium set or simulate it with sikuli
 * 		TypeEnter : send enter key
 * 		Click : click on element
 * 		Text : get text of element
 * 		Value : get value of element
 * 		SelectByTest : select element by text
 * 	
 */
public class UIObject {
		
		/** Name of web object */
		public String Name="";
		
		/** Value : values as recorded is DOM XML file. */
		public String[] Value= {"", "", "", "", ""};
		
		/** Type : element type (input, select, link ...) */
		public String Type="";
		
		/** Selenium by object of element when found */
		public By by = null;
		
		/** Identifiers : list of selenium identifiers from values. */
		public By[] identifiers = new By[4];
		
		/** The image. */
		public String image = "";
		
		/** The Element. */
		public WebElement Element = null;
		
		/** The is image. */
		public boolean isImage = false;

		/**
		 * Sets the.
		 *
		 * @param value the value
		 * @throws IOException 
		 */
		public void Set(String value) throws IOException {
			if (screenShotOnAction) 
				ScreenShot (Type + "_" + Name ) ;
			if (!isImage) {
				WaitForElement(this);
				try {
					Element.clear();
					Element.sendKeys(value);
					logger.debug( "Element Set " + Name + ". " + by.toString() + " Value : " + value);
				}
				catch (Exception e) {
					if (screenShotOnError) 
						ScreenShot (Type + "_" + Name ) ;
					logger.error(e.getMessage());
				}
			}
			else
			{
				try {
					screen.wait(image, 20);
					screen.click(image);
					screen.type(value);
					logger.debug( "Type in image  " + image + ".  Value : " + value);
				} catch (FindFailed e) {
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * Type enter.
		 * @throws IOException 
		 */
		public void TypeEnter() throws IOException {
			if (screenShotOnAction) 
				ScreenShot (Type + "_" + Name ) ;
			WaitForElement(this);
			Element.sendKeys(Keys.RETURN);
			logger.debug( "Element Key type " + Name + ". " + by.toString() + " RETURN");
		}

		/**
		 * Click.
		 * @throws IOException 
		 */
		public void Click() throws IOException {
			if (screenShotOnAction) 
				ScreenShot (Type + "_" + Name ) ;
			if (!isImage) {
				try {
					WaitForElementClickable(this);
					Element.click();
		
					logger.debug( "Element Clicked " + Name + ". " + by.toString());
					logger.debug("Current URL :" + driver.getCurrentUrl());
				}
				catch (Exception e) {
					if (screenShotOnError) 
						ScreenShot (Type + "_" + Name ) ;
					logger.error(e.getMessage());
				}
			}
			else {
				try {
					screen.wait(image, 20);
					screen.click(image);
					logger.debug( "Click on image : " + image);
				} catch (FindFailed e) {
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * Text.
		 *
		 * @return the string
		 * @throws IOException 
		 */
		public String Text() throws IOException {
			if (screenShotOnAction) 
				ScreenShot (Type + "_" + Name ) ;
			try {
				WaitForElement(this);

				logger.debug( "Element Get Text " + Name + ". " + by.toString() + ". Texte : " + Element.getText());
				return(Element.getText());
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				return null;
			}
		}
		
		/**
		 * Value.
		 *
		 * @return the string
		 * @throws IOException 
		 */
		public String Value() throws IOException {
			if (screenShotOnAction) 
				ScreenShot (Type + "_" + Name ) ;
			try {
				WaitForElement(this);
				logger.debug( "Element Get Value " + Name + ". " + by.toString() + ". Value : " + Element.getAttribute("value"));
				
				return(Element.getAttribute("value"));
			}
			catch (Exception e) {
				logger.error(e.getMessage());
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
				WaitForElement(this);
				logger.debug( "Element Select By Visible Text " + Name + ". " + by.toString() + ". Text : " + value);
				
				new Select(Element).selectByVisibleText(value);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
}
