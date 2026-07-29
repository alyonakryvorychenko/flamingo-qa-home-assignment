package com.flamingo.qa.tests.ui;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.io.ByteArrayInputStream;

public abstract class BaseUiTest {

    private static Playwright playwright;
    private static Browser browser;

    protected BrowserContext context;
    protected Page page;

    // Must be a TestExecutionExceptionHandler, not a TestWatcher: AllureJunit5 wraps the test
    // method in an InvocationInterceptor and closes the Allure test case as soon as the method
    // returns/throws. TestWatcher.testFailed() fires after that point, so Allure.addAttachment()
    // has nothing open to attach to. TestExecutionExceptionHandler fires while the test case is
    // still open, so the screenshot actually lands in the report.
    @RegisterExtension
    private final TestExecutionExceptionHandler screenshotOnFailure = new TestExecutionExceptionHandler() {
        @Override
        public void handleTestExecutionException(ExtensionContext extensionContext, Throwable throwable) throws Throwable {
            if (page != null) {
                byte[] screenshot = page.screenshot();
                Allure.addAttachment(extensionContext.getDisplayName() + " - failure screenshot",
                        "image/png", new ByteArrayInputStream(screenshot), ".png");
            }
            throw throwable;
        }
    };

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        if (context != null) {
            context.close();
        }
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
