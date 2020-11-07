package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class FilesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id="nav-files-tab")
    private WebElement filesTab;

    @FindBy(id="fileUpload")
    private WebElement fileUploadElement;

    @FindBy(id="uploadFileButton")
    private WebElement uploadFileButton;

    @FindBy(id="downloadFileButton")
    private WebElement downloadFileButton;

    @FindBy(id="deleteFileButton")
    private WebElement deleteFileButton;

    @FindBy(xpath = "id('fileTable')/tbody/tr")
    private List<WebElement> files;

    public FilesPage(WebDriver webDriver) {
        this.driver = webDriver;
        this.wait = new WebDriverWait(driver,30);
        PageFactory.initElements(webDriver, this);
    }

    public void switchToFilesTab(){
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].click();", this.filesTab);
    }

    public void uploadFile(String filePath){
        this.fileUploadElement.sendKeys(filePath);
        this.uploadFileButton.click();
    }

    public void downloadFile(){
        this.downloadFileButton.click();
    }

    public void deleteFile(){
        this.wait.until(ExpectedConditions.elementToBeClickable(this.deleteFileButton));
        this.deleteFileButton.click();
    }

    public List<WebElement> getFiles(){
        //this.wait.until(ExpectedConditions.visibilityOfAllElements(this.files));
        return this.files;
    }
}