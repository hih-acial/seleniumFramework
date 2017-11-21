package acial.selenium.utils;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HtmlUnitDriverExtended extends HtmlUnitDriver {
	    public HtmlUnitDriverExtended(boolean javaScript) {
	        super(javaScript);
	        getWebClient().getOptions().setThrowExceptionOnScriptError(false);
	    }
}
