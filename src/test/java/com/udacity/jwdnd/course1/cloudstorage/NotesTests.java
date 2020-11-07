package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.NotesPage;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotesTests {

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
	public void canAddAndViewNote(){
		var noteTitle = "Note Title";
		var noteDescription = "Note Description";

		NotesPage notesPage = new NotesPage(driver);
		notesPage.switchToNotesTab();
		notesPage.openNewNoteModal();

		notesPage.submitNote(noteTitle, noteDescription);
		var notes = notesPage.getNotes().stream()
				.map(this::getNoteTitle)
				.collect(toList());

		assertThat(notes.contains(noteTitle));

		notesPage.deleteNote();
	}

	@Test
	public void canEditNote(){
		var noteTitle = "Edit Title";
		var noteDescription = "Edit Description";

		NotesPage notesPage = new NotesPage(driver);
		notesPage.switchToNotesTab();
		notesPage.openNewNoteModal();

		notesPage.submitNote(noteTitle, noteDescription);
		var notes = notesPage.getNotes().stream()
				.map(this::getNoteTitle)
				.collect(toList());

		assertThat(notes.contains(noteTitle));

		notesPage.openEditNoteModal();

		var editedNoteTitle = "Title (Edited)";
		var editedNoteDescription = "Description (Edited)";

		notesPage.submitNote(editedNoteTitle, editedNoteDescription);

		notes = notesPage.getNotes().stream()
				.map(this::getNoteTitle)
				.collect(toList());

		assertThat(!notes.contains(noteTitle));
		assertThat(notes.contains(editedNoteTitle));

		notesPage.deleteNote();
	}

	@Test
	public void canDeleteNote(){
		var noteTitle = "Delete Title";
		var noteDescription = "Delete Description";

		NotesPage notesPage = new NotesPage(driver);
		notesPage.switchToNotesTab();
		notesPage.openNewNoteModal();

		notesPage.submitNote(noteTitle, noteDescription);
		var notes = notesPage.getNotes().stream()
				.map(this::getNoteTitle)
				.collect(toList());

		assertThat(notes.contains(noteTitle));

		notesPage.deleteNote();

		notes = notesPage.getNotes().stream()
				.map(this::getNoteTitle)
				.collect(toList());

		assertThat(!notes.contains(noteTitle));
	}

	private String getNoteTitle(WebElement noteElement){
		return noteElement.findElements(By.tagName("th")).get(0).getText();
	}
}
