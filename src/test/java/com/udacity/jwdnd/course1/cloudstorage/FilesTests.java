package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.page.FilesPage;
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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilesTests {

	@LocalServerPort
	private int port;
	private WebDriver driver;

	@BeforeAll
	void beforeAll() {
		var downloadFolder = Paths.get("src","test","resources", "downloads").toFile().getAbsolutePath();

		var chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFolder);
		chromePrefs.put("safebrowsing.enabled", "false");

		var options = new ChromeOptions();
		options.setHeadless(true);
		options.setExperimentalOption("prefs", chromePrefs);

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
	public void canAddAndDownloadFile() throws InterruptedException {
		var fileName = "file.png" ;
		var resourceDirectory = Paths.get("src","test","resources", fileName);
		var absolutePath = resourceDirectory.toFile().getAbsolutePath();

		var filesPage = new FilesPage(driver);
		filesPage.switchToFilesTab();

		filesPage.uploadFile(absolutePath);
		var files = filesPage.getFiles().stream()
				.map(this::getFileName)
				.collect(toList());

		assertThat(files.contains(fileName));

		filesPage.downloadFile();
		var downloadPath = Paths.get("src","test","resources", "downloads", fileName);
		new WebDriverWait(driver, 60).until(d -> downloadPath.toFile().exists());

		File downloadedFile = new File(downloadPath.toFile().getAbsolutePath());
		assertTrue(downloadedFile.exists());

		downloadedFile.deleteOnExit();
		filesPage.deleteFile();
	}

	@Test
	public void canDeleteNote(){
		var fileName = "file.png" ;
		var resourceDirectory = Paths.get("src","test","resources", fileName);
		var absolutePath = resourceDirectory.toFile().getAbsolutePath();

		var filesPage = new FilesPage(driver);
		filesPage.switchToFilesTab();

		filesPage.uploadFile(absolutePath);
		var files = filesPage.getFiles().stream()
				.map(this::getFileName)
				.collect(toList());

		assertThat(files.contains(fileName));

		filesPage.deleteFile();

		files = filesPage.getFiles().stream()
				.map(this::getFileName)
				.collect(toList());

		assertThat(!files.contains("file.png"));
	}

	private String getFileName(WebElement fileElement){
		return fileElement.findElements(By.tagName("th")).get(0).getText();
	}
}
