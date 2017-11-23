package org.acial.SeFramework;

import org.acial.SeFramework.Selenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class UIPage {
	public WebDriver driver=null;
	public String Name="";
	public String Type="";
	public List<UIObject> UIObjects = new ArrayList<UIObject>();
	
	public UIObject GetBy(String guiObject) {
		return (GetBy(guiObject, ""));
	}
	public UIObject GetBy(String guiObject, String param) {
		By by=null;
		String image="";
		for (int i=0;i<UIObjects.size();i++) {
			if ( UIObjects.get(i).Name.equals(guiObject) ) {
				UIObject obj = UIObjects.get(i);
				for (int j=0; j<5; j++) {
					if (obj.Value[j].indexOf(":")>0) {
						String[] parts = obj.Value[j].split(":");
						parts[1] = parts[1].replace("$1$", param);
					    switch (parts[0]) {
					    	case "id" :
					    		by = By.id(parts[1]);
					    		break;
					    	case "xpath" :
					    		by = By.xpath(parts[1]);
					    		break;
					    	case "css" :
					    		by = By.cssSelector(parts[1]);	
					    		break;
					    	case "name" :
					    		by = By.name(parts[1]);	
					    		break;
					    	case "link" :
					    		by = By.linkText(parts[1]);	
					    		break;
					    }
				    	obj.identifiers[j] = by;
					}
				}
				obj.by = obj.identifiers[0];
				return (obj);
			}
		}
		Selenium.logger.error("Object not in respository : " + guiObject);
		return(null);
		
	}
	public UIObject Element(String guiObject) {
		return (Element(guiObject, ""));
	}
	public UIObject Element(String guiObject, String param) {
		UIObject obj = GetBy (guiObject, param);
		try {
			obj.Element = driver.findElement(obj.by);
		}
		catch (Exception e) {
			try {
				Selenium.logger.error("Element not found : " + obj.identifiers[0].toString());
				if (!(obj.identifiers[1]==null)) {
					obj.Element = driver.findElement(obj.identifiers[1]);
					obj.by = obj.identifiers[1];
				}
			}
			catch (Exception e1) {
				try {
					Selenium.logger.error("Element not found : " + obj.identifiers[1].toString());
					if (!(obj.identifiers[2]==null)) {
						obj.Element = driver.findElement(obj.identifiers[2]);
						obj.by = obj.identifiers[2];
					}
				}
				catch (Exception e2) {
					try {
						Selenium.logger.error("Element not found : " + obj.identifiers[2].toString());
						if (!(obj.identifiers[3]==null)) {
							obj.Element = driver.findElement(obj.identifiers[3]);
							obj.by = obj.identifiers[3];
						}
					}
					catch (Exception e3) {
						try {
							Selenium.logger.error("Element not found : " + obj.identifiers[3].toString());
						}
						catch (Exception e4) {
							e4.printStackTrace();
							Selenium.logger.error("Element not found by Image " );
						}
					}
				}
			}
		}
		return (obj);
	}
}
