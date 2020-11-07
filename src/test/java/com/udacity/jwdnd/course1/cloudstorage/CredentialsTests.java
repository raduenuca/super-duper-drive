package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.page.CredentialsPage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CredentialsTests {

	@LocalServerPort
	private int port;
	private WebDriver driver;

	@BeforeAll
	void beforeAll() {
		var options = new ChromeOptions();
		options.setHeadless(true);

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		var baseURL = "http://localhost:" + port;
		driver.get(baseURL + "/signup");

		var username = "raduenuca";
		var password = "pa$$w0rd!";

		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Radu", "Enuca", username, password);

		driver.get(baseURL + "/login");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);
	}

	@AfterAll
	void afterEach() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	public void canAddAndViewCredential(){
		var url = "http://localhost:8080";
		var username = "raduenuca";
		var password = "1234%asd!";

		var credentialsPage = new CredentialsPage(driver);
		credentialsPage.switchToCredentialsTab();
		credentialsPage.openNewCredentialModal();

		credentialsPage.submitCredential(url, username, password);
		var credentialsUrls = credentialsPage.getCredentials().stream()
				.map(this::getUrl)
				.collect(toList());

		assertThat(credentialsUrls.contains(url));

		var credentialsPasswords = credentialsPage.getCredentials().stream()
				.map(this::getPassword)
				.collect(toList());

		assertThat(!credentialsPasswords.contains(password));

		credentialsPage.deleteCredential();
	}

	@Test
	public void canEditCredential(){
		var url = "http://localhost:8080";
		var username = "raduenuca";
		var password = "1234%asd!";

		var credentialsPage = new CredentialsPage(driver);
		credentialsPage.switchToCredentialsTab();
		credentialsPage.openNewCredentialModal();

		credentialsPage.submitCredential(url, username, password);
		var credentialsUrls = credentialsPage.getCredentials().stream()
				.map(this::getUrl)
				.collect(toList());

		assertThat(credentialsUrls.contains(url));

		credentialsPage.openEditCredentialModal();
		assertEquals(password, credentialsPage.getCredentialPassword());

		var newUrl = "https://localhost:8080";
		var newUsername = "radu.enuca";

		credentialsPage.submitCredential(newUrl, newUsername, password);

		credentialsUrls = credentialsPage.getCredentials().stream()
				.map(this::getUrl)
				.collect(toList());

		assertThat(!credentialsUrls.contains(url));
		assertThat(credentialsUrls.contains(newUrl));

		credentialsPage.deleteCredential();
	}

	@Test
	public void canDeleteNote(){
		var url = "http://localhost:8080";
		var username = "raduenuca";
		var password = "1234%asd!";

		var credentialsPage = new CredentialsPage(driver);
		credentialsPage.switchToCredentialsTab();
		credentialsPage.openNewCredentialModal();

		credentialsPage.submitCredential(url, username, password);
		var credentialsUrls = credentialsPage.getCredentials().stream()
				.map(this::getUrl)
				.collect(toList());

		assertThat(credentialsUrls.contains(url));

		credentialsPage.deleteCredential();

		credentialsUrls = credentialsPage.getCredentials().stream()
				.map(this::getUrl)
				.collect(toList());

		assertThat(!credentialsUrls.contains(url));
	}

	private String getUrl(WebElement credentialElement){
		return credentialElement.findElements(By.tagName("th")).get(0).getText();
	}

	private String getPassword(WebElement credentialElement){
		return credentialElement.findElements(By.tagName("td")).get(1).getText();
	}
}
