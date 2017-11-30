package org.acial.FrameworkSample;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ Authentification.class, Menus.class, Salaries.class })
public class AllTests {

}
