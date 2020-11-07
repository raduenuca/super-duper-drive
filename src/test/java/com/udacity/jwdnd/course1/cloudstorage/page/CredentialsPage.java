package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CredentialsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id="nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id="addCredentialButton")
    private WebElement addCredentialButton;

    @FindBy(id="credentialModal")
    private WebElement credentialModal;

    @FindBy(id="credential-url")
    private WebElement credentialUrlField;

    @FindBy(id="credential-username")
    private WebElement credentialUsernameField;

    @FindBy(id="credential-password")
    private WebElement credentialPasswordField;

    @FindBy(id="credentialSubmitButton")
    private WebElement credentialSubmitButton;

    @FindBy(id="editCredentialButton")
    private WebElement editCredentialButton;

    @FindBy(id="deleteCredentialButton")
    private WebElement deleteCredentialButton;

    @FindBy(xpath = "id('credentialTable')/tbody/tr")
    private List<WebElement> credentials;

    public CredentialsPage(WebDriver webDriver) {
        this.driver = webDriver;
        this.wait = new WebDriverWait(driver,30);
        PageFactory.initElements(webDriver, this);
    }

    public void switchToCredentialsTab(){
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].click();", this.credentialsTab);
    }

    public void openNewCredentialModal(){
        this.wait.until(ExpectedConditions.elementToBeClickable(this.addCredentialButton));
        this.addCredentialButton.click();
    }

    public void openEditCredentialModal(){
        this.wait.until(ExpectedConditions.elementToBeClickable(this.editCredentialButton));
        this.editCredentialButton.click();
    }

    public void deleteCredential(){
        this.wait.until(ExpectedConditions.elementToBeClickable(this.deleteCredentialButton));
        this.deleteCredentialButton.click();
    }

    public void submitCredential(String url, String username, String password){
        this.wait.until(ExpectedConditions.visibilityOf(this.credentialUrlField));
        this.wait.until(ExpectedConditions.visibilityOf(this.credentialUsernameField));
        this.wait.until(ExpectedConditions.visibilityOf(this.credentialPasswordField));
        this.wait.until(ExpectedConditions.elementToBeClickable(this.credentialSubmitButton));

        this.credentialUrlField.clear();
        this.credentialUrlField.sendKeys(url);

        this.credentialUsernameField.clear();
        this.credentialUsernameField.sendKeys(username);

        this.credentialPasswordField.clear();
        this.credentialPasswordField.sendKeys(password);

        this.credentialSubmitButton.click();
    }

    public String getCredentialPassword() {
        this.wait.until(ExpectedConditions.visibilityOf(this.credentialPasswordField));
        return credentialPasswordField.getAttribute("value");
    }

    public List<WebElement> getCredentials(){
        // this.wait.until(ExpectedConditions.visibilityOfAllElements(this.credentials));
        return this.credentials;
    }
}