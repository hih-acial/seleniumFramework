set Prj=C:\Formation\Selenium\WebDriver\OrangeHRMWeb
cd %Prj%
set classpath=%Prj%\..\lib\*;%Prj%\target\classes
java org.testng.TestNG %Prj%\NonReg.xml
pause

