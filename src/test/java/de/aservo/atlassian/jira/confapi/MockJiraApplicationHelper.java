package de.aservo.atlassian.jira.confapi;

import com.atlassian.jira.bc.license.JiraLicenseService;
import com.atlassian.jira.license.JiraLicenseManager;
import com.atlassian.jira.mock.MockApplicationProperties;

import static org.mockito.Mockito.mock;

public class MockJiraApplicationHelper extends JiraApplicationHelper {

    public static final String BASE_URL = "http://localhost:2990/jira";
    public static final String MODE = "private";
    public static final String TITLE = "Your Company JIRA";

    public MockJiraApplicationHelper() {
        super(
                new MockApplicationProperties(),
                new MockJiraI18nHelper(),
                mock(JiraLicenseManager.class),
                mock(JiraLicenseService.class));

        setBaseUrl(BASE_URL);
        setMode(MODE);
        setTitle(TITLE);
    }

}
