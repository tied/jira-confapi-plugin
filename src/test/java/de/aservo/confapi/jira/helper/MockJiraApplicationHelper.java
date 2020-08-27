package de.aservo.confapi.jira.helper;

import com.atlassian.jira.bc.license.JiraLicenseService;
import com.atlassian.jira.license.JiraLicenseManager;
import com.atlassian.jira.mock.MockApplicationProperties;
import com.atlassian.jira.web.bean.MockI18nBean;
import de.aservo.confapi.jira.service.JiraApplicationHelper;

import static org.mockito.Mockito.mock;

public class MockJiraApplicationHelper extends JiraApplicationHelper {

    public static final String BASE_URL = "http://localhost:2990/jira";
    public static final String MODE = "private";
    public static final String TITLE = "Your Company JIRA";

    public MockJiraApplicationHelper() {
        super(
                new MockApplicationProperties(),
                new MockI18nBean.MockI18nBeanFactory(),
                mock(JiraLicenseManager.class),
                mock(JiraLicenseService.class));

        setBaseUrl(BASE_URL);
        setMode(MODE);
        setTitle(TITLE);
    }

}
