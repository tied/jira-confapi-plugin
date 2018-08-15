package de.aservo.atlassian.jira.confapi;

import com.atlassian.jira.web.bean.MockI18nBean;

import static org.mockito.Mockito.mock;

public class MockJiraI18nHelper extends JiraI18nHelper {

    public MockJiraI18nHelper() {
        super(new MockI18nBean.MockI18nBeanFactory(), mock(JiraUserHelper.class));
    }

}
