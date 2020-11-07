package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.page.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HomePageTests {

	@LocalServerPort
	private int port;
	private static WebDriver driver;
	private String baseURL;

	@BeforeAll
	static void beforeAll() {
		var options = new ChromeOptions();
		options.setHeadless(true);

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(options);
	}

	@AfterAll
	public static void afterEach() {
		if (driver != null) {
			driver.quit();
		}
	}

	@BeforeEach
	public void beforeEach() {
		this.baseURL = "http://localhost:" + port;
	}

	@Test
	public void canAccessLoginPage(){
		driver.get(this.baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);

		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void canAccessSignUpPage(){
		driver.get(this.baseURL + "/signup");
		LoginPage loginPage = new LoginPage(driver);

		assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void canSignupLoginAndLogOut() {
		String username = "raduenuca";
		String password = "pa$$w0rd!";

		driver.get(this.baseURL + "/signup");

		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Radu", "Enuca", username, password);

		driver.get(this.baseURL + "/login");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		assertEquals("Home", driver.getTitle());

		HomePage homePage = new HomePage(driver);
		homePage.logout();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.titleIs("Login"));

		assertEquals("Login", driver.getTitle());
	}

}
