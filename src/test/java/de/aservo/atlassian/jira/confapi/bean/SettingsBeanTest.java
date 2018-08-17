package de.aservo.atlassian.jira.confapi.bean;

import de.aservo.atlassian.jira.confapi.JiraApplicationHelper;
import de.aservo.atlassian.jira.confapi.MockJiraApplicationHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static de.aservo.atlassian.jira.confapi.MockJiraApplicationHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class SettingsBeanTest {

    private JiraApplicationHelper applicationHelper;

    @Before
    public void setup() {
        applicationHelper = new MockJiraApplicationHelper();
    }

    @Test
    public void testConstructWithDefaultConstructor() {
        final SettingsBean settingsBean = new SettingsBean();

        assertNull(settingsBean.getBaseUrl());
        assertNull(settingsBean.getMode());
        assertNull(settingsBean.getTitle());
    }

    @Test
    public void testConstruct() {
        final SettingsBean settingsBean = new SettingsBean(
                BASE_URL,
                MODE,
                TITLE);

        assertThat(settingsBean.getBaseUrl(), equalTo(BASE_URL));
        assertThat(settingsBean.getMode(), equalTo(MODE));
        assertThat(settingsBean.getTitle(), equalTo(TITLE));
    }

    @Test
    public void testConstructFromApplicationHelper() {
        final SettingsBean settingsBean = new SettingsBean(applicationHelper);

        assertThat(settingsBean.getBaseUrl(), equalTo(BASE_URL));
        assertThat(settingsBean.getMode(), equalTo(MODE));
        assertThat(settingsBean.getTitle(), equalTo(TITLE));
    }

}
