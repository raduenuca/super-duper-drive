package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class NotesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id="nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id="addNoteButton")
    private WebElement addNoteButton;

    @FindBy(id="noteModal")
    private WebElement noteModal;

    @FindBy(id="note-title")
    private WebElement noteTitleField;

    @FindBy(id="note-description")
    private WebElement noteDescriptionField;

    @FindBy(id="noteSubmitButton")
    private WebElement noteSubmitButton;

    @FindBy(id="editNoteButton")
    private WebElement editNoteButton;

    @FindBy(id="deleteNoteButton")
    private WebElement deleteNoteButton;

    @FindBy(xpath = "id('notesTable')/tbody/tr")
    private List<WebElement> notes;

    public NotesPage(WebDriver webDriver) {
        this.driver = webDriver;
        this.wait = new WebDriverWait(driver,30);
        PageFactory.initElements(webDriver, this);
    }

    public void switchToNotesTab(){
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].click();", this.notesTab);
    }

    public void openNewNoteModal(){
        this.wait.until(ExpectedConditions.elementToBeClickable(this.addNoteButton));
        this.addNoteButton.click();
    }

    public void openEditNoteModal(){
        this.wait.until(ExpectedConditions.elementToBeClickable(this.editNoteButton));
        this.editNoteButton.click();
    }

    public void deleteNote(){
        this.wait.until(ExpectedConditions.elementToBeClickable(this.deleteNoteButton));
        this.deleteNoteButton.click();
    }

    public void submitNote(String title, String description){
        this.wait.until(ExpectedConditions.visibilityOf(this.noteTitleField));
        this.wait.until(ExpectedConditions.visibilityOf(this.noteDescriptionField));
        this.wait.until(ExpectedConditions.elementToBeClickable(this.noteSubmitButton));

        this.noteTitleField.clear();
        this.noteTitleField.sendKeys(title);

        this.noteDescriptionField.clear();
        this.noteDescriptionField.sendKeys(description);

        this.noteSubmitButton.click();
    }

    public List<WebElement> getNotes(){
        // this.wait.until(ExpectedConditions.visibilityOfAllElements(this.notes));
        return this.notes;
    }
}