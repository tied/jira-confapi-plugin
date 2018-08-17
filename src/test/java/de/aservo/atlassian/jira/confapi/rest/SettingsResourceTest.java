package de.aservo.atlassian.jira.confapi.rest;

import com.atlassian.jira.rest.api.util.ErrorCollection;
import de.aservo.atlassian.jira.confapi.JiraApplicationHelper;
import de.aservo.atlassian.jira.confapi.JiraWebAuthenticationHelper;
import de.aservo.atlassian.jira.confapi.MockJiraApplicationHelper;
import de.aservo.atlassian.jira.confapi.bean.SettingsBean;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class SettingsResourceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private JiraApplicationHelper applicationHelper;

    @Mock
    private JiraWebAuthenticationHelper webAuthenticationHelper;

    private SettingsResource settingsResource;

    @Before
    public void setup() {
        applicationHelper = new MockJiraApplicationHelper();

        settingsResource = new SettingsResource(
                applicationHelper,
                webAuthenticationHelper);
    }

    @Test
    public void testGetSettings() {
        final Response response = settingsResource.getSettings();
        final Object responseEntity = response.getEntity();

        assertThat(responseEntity, instanceOf(SettingsBean.class));

        final SettingsBean settingsBean = (SettingsBean) responseEntity;

        assertThat(settingsBean.getBaseUrl(), equalTo(applicationHelper.getBaseUrl()));
        assertThat(settingsBean.getMode(), equalTo(applicationHelper.getMode()));
        assertThat(settingsBean.getTitle(), equalTo(applicationHelper.getTitle()));
    }

    @Test
    public void testSetSettings() {
        final String baseUrl = "https://jira.atlassian.com";
        final String mode = "public";
        final String title = "Atlassian Public JIRA";

        final SettingsBean settingsBean = new SettingsBean(
                baseUrl,
                mode,
                title);

        final Response response = settingsResource.setSettings(settingsBean);
        final Object responseEntity = response.getEntity();

        assertThat(responseEntity, instanceOf(ErrorCollection.class));

        final ErrorCollection errorCollection = (ErrorCollection) responseEntity;

        assertThat(errorCollection.getErrorMessages(), empty());
        assertThat(errorCollection.getErrors(), equalTo(Collections.EMPTY_MAP));
    }

    @Test
    public void testSetSettingsCausingExceptions() {
        final String baseUrl = "thisUrlIsNotValid";
        final String mode = "invalid";
        final String title = StringUtils.repeat("A", 256);

        final SettingsBean settingsBean = new SettingsBean(
                baseUrl,
                mode,
                title);

        final Response response = settingsResource.setSettings(settingsBean);
        final Object responseEntity = response.getEntity();

        assertThat(responseEntity, instanceOf(ErrorCollection.class));

        final ErrorCollection errorCollection = (ErrorCollection) responseEntity;

        assertThat(errorCollection.getErrorMessages(), hasSize(3));
        assertThat(errorCollection.getErrors(), equalTo(Collections.EMPTY_MAP));
    }

}
