package my.selenium;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestMMA {
    private WebDriver driver;
    private String baseUrl;
    private DesiredCapabilities ieCapabilities;
    private StringBuffer verificationErrors = new StringBuffer();
    @Before
    public void setUp() throws Exception {
    	System.setProperty("webdriver.ie.driver","C:\\workspace\\myworkspace\\IEDriverServer.exe");
    	ieCapabilities = DesiredCapabilities.internetExplorer();
	    ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
	    ieCapabilities.setBrowserName("Internet Explorer");
	    ieCapabilities.setVersion("9");
	    ieCapabilities.setPlatform(Platform.WINDOWS);
    	driver = new InternetExplorerDriver(ieCapabilities);
        baseUrl = "http://localhost:8090";
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
    }

    @Before
    public void login() throws Exception {
    	driver.get(baseUrl + "/claims/login.jsp");
    	assertEquals("Regional Claim System", driver.getTitle());
    	driver.findElement(By.name("userName")).clear();
        driver.findElement(By.name("userName")).sendKeys("ABC");
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("DEF");
        
        driver.findElement(By.xpath("//img[@src='images/login.gif']")).click();
    }
    
    @Ignore
    public void dummyTest() throws Exception {
    	driver.navigate().to(baseUrl + "/claims/myTask.do?actionMethod=doShowTask&operateType=showPersonalTask&claimId=472577");
    	WebElement medCaseCheckbox = driver.findElement(By.xpath("//input[@name='claimCaseInfoView.ifMedicalCaseIndicator']"));
    	assertTrue(medCaseCheckbox.isDisplayed());
    	medCaseCheckbox.click();
    	
    	WebElement addInPatientBtn = driver.findElement(By.xpath("//input[@name='addx' and @onclick='addInPatientInfo();']"));
    	assertTrue(addInPatientBtn.isDisplayed());
    	addInPatientBtn.click();
    }
    
    @Test
    public void test2() throws Exception {
    	WebElement divContent = new WebDriverWait(driver, 3).until(new ExpectedCondition<WebElement>() {
    		public WebElement apply(WebDriver d) {
    			return d.findElement(By.xpath("//div[@class='content']"));
    		}
    	});
    	assertTrue(divContent.isDisplayed());
    	
    	driver.switchTo().frame("main");
    	driver.switchTo().frame("lmain");
    	
    	String result = driver.findElement(By.xpath("//tr[td='37170']")).getText();
        assertEquals(true, StringUtils.contains(result, "37170"));
        assertTrue(driver.findElement(By.xpath("//tr[td='37170']/td[a='Do Task']/a")).isDisplayed());
        driver.findElement(By.xpath("//tr[td='37170']/td[a='Do Task']/a")).click();
        
    	//Step 1
    	WebElement medCaseCheckbox = new WebDriverWait(driver, 5).until(new ExpectedCondition<WebElement>() {
    		public WebElement apply(WebDriver d) {
    			return d.findElement(By.xpath("//input[@name='claimCaseInfoView.ifMedicalCaseIndicator' and @id='chk_Medical']"));
    		}
    	});
    	assertTrue(medCaseCheckbox.isDisplayed());
    	medCaseCheckbox.click();
    	
    	//Step 2
    	WebElement addInPatientBtn = new WebDriverWait(driver, 3).until(new ExpectedCondition<WebElement>() {
    		public WebElement apply(WebDriver d) {
    			return d.findElement(By.xpath("//input[@name='addx' and @onclick='addInPatientInfo();']"));
    		}
    	});
    	assertTrue(addInPatientBtn.isDisplayed());
    	addInPatientBtn.click();
    	
    	//Step 3
    	WebElement addReceiptItemBtn = new WebDriverWait(driver, 3).until(new ExpectedCondition<WebElement>() {
    		public WebElement apply(WebDriver d) {
    			return d.findElement(By.xpath("//input[@name='btn' and starts-with(@onclick,'addReceiptItem') and @type='button' and @value='Add Item']"));
    		}
    	});
    	assertTrue(addReceiptItemBtn.isDisplayed());
    	addReceiptItemBtn.click();
    	
    	//Step 4
    	WebElement doGetMMAScheduleFeeImg = new WebDriverWait(driver, 3).until(new ExpectedCondition<WebElement>() {
    		public WebElement apply(WebDriver d) {
    			return d.findElement(By.xpath("//img[starts-with(@onclick,'doGetMMAScheduleFee')]"));
    		}
    	});
    	assertTrue(doGetMMAScheduleFeeImg.isDisplayed());
    	String addReceiptItemHandle = driver.getWindowHandle();
    	doGetMMAScheduleFeeImg.click();
    	
    	//Step 5
    	String mainWinHander = driver.getWindowHandle();
    	// code for clicking button to open new window is ommited
    	//Now the window opened. So here reture the handle with size = 2
    	Set<String> handles = driver.getWindowHandles();
    	for(String handle : handles)
    	{
    	    if(!mainWinHander.equals(handle))
    	    {
    	        // Here will block for ever. No exception and timeout!
    	        WebDriver popup = driver.switchTo().window(handle);
    	        WebElement doMMASearchBtn = new WebDriverWait(popup, 3).until(new ExpectedCondition<WebElement>() {
    	    		public WebElement apply(WebDriver d) {
    	    			return d.findElement(By.xpath("//input[@name='search' and starts-with(@onclick,'doSearch')]"));
    	    		}
    	    	});
    	    	assertTrue(doMMASearchBtn.isDisplayed());
    	    	doMMASearchBtn.click();
    	    	
    	    	List<WebElement> resultRadios = new WebDriverWait(popup, 3).until(new ExpectedCondition<List<WebElement>>() {
    	    		public List<WebElement> apply(WebDriver d) {
    	    			return d.findElements(By.xpath("//input[@name='select' and @type='radio' and starts-with(@onclick,'setReturnValue(')]"));
    	    		}
    	    	});
    	    	WebElement resultRadio = resultRadios.get(0);
    	    	assertTrue(resultRadio.isDisplayed());
    	    	resultRadio.click();
    	    	
    	    	WebElement doMMASearchOkBtn = new WebDriverWait(popup, 3).until(new ExpectedCondition<WebElement>() {
    	    		public WebElement apply(WebDriver d) {
    	    			return d.findElement(By.xpath("//input[@name='ok' and starts-with(@onclick,'doSubmit')]"));
    	    		}
    	    	});
    	    	assertTrue(doMMASearchOkBtn.isDisplayed());
    	    	doMMASearchOkBtn.click();
    	    	
    	    	//TODO
//    	        popup.close();
    	    }
    	}
    	
    	//Step 6
    	WebDriver addReceiptItemWeb = driver.switchTo().window(addReceiptItemHandle);
    	
    	divContent = new WebDriverWait(addReceiptItemWeb, 3).until(new ExpectedCondition<WebElement>() {
    		public WebElement apply(WebDriver d) {
    			return d.findElement(By.xpath("//div[@class='content']"));
    		}
    	});
    	assertTrue(divContent.isDisplayed());
    	
    	driver.switchTo().frame("main");
    	driver.switchTo().frame("lmain");
    	
    	WebElement mmaScheduleFeeText = new WebDriverWait(addReceiptItemWeb, 5).until(new ExpectedCondition<WebElement>() {
    		public WebElement apply(WebDriver d) {
    			return d.findElement(By.xpath("//input[@id='MMAFee' and @type='text']"));
    		}
    	});
    	assertTrue(mmaScheduleFeeText.isDisplayed());
    	assertTrue(StringUtils.isNotBlank(mmaScheduleFeeText.getAttribute("value")));
    }

	protected void printWebDriverContext(WebDriver driver) {
		System.out.println("getCurrentUrl::"+driver.getCurrentUrl());
    	System.out.println("getTitle::"+driver.getTitle());
	}

	protected void listWindowHandles(WebDriver driver) {
		Set<String> handles = driver.getWindowHandles();
    	for (String handle : handles) {
    		System.out.println("handle - ::"+handle);
    	}
	}

    @After
    public void tearDown() throws Exception {
    	driver.close();
    	driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
    
    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
