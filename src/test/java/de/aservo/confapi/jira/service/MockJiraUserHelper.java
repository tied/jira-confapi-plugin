package de.aservo.confapi.jira.service;

import com.atlassian.jira.mock.security.MockAuthenticationContext;
import com.atlassian.jira.user.MockApplicationUser;
import com.atlassian.jira.user.MockUserPropertyManager;

public class MockJiraUserHelper extends JiraUserHelper {

    public MockJiraUserHelper() {
        super(
                new MockAuthenticationContext(new MockApplicationUser("user")),
                new MockUserPropertyManager());
    }

}
