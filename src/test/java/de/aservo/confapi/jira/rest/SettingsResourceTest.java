package de.aservo.confapi.jira.rest;

import com.atlassian.jira.rest.api.util.ErrorCollection;
import de.aservo.confapi.commons.model.SettingsBean;
import de.aservo.confapi.jira.helper.MockJiraApplicationHelper;
import de.aservo.confapi.jira.service.JiraApplicationHelper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SettingsResourceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private JiraApplicationHelper applicationHelper;

    private SettingsResourceImpl settingsResource;

    @Before
    public void setup() {
        applicationHelper = new MockJiraApplicationHelper();

        settingsResource = new SettingsResourceImpl(applicationHelper);
    }

    @Test
    public void testGetSettings() {
        final Response response = settingsResource.getSettings();
        final Object responseEntity = response.getEntity();

        assertThat(responseEntity, instanceOf(SettingsBean.class));

        final SettingsBean settingsBean = (SettingsBean) responseEntity;

        assertThat(settingsBean.getBaseUrl(), equalTo(applicationHelper.getBaseUrl()));
        // assertThat(settingsBean.getMode(), equalTo(applicationHelper.getMode()));
        assertThat(settingsBean.getTitle(), equalTo(applicationHelper.getTitle()));
    }

    @Test
    public void testSetSettings() {
        final String baseUrl = "https://jira.atlassian.com";
        final String mode = "PUBLIC";
        final String title = "Atlassian Public JIRA";

        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setBaseUrl(baseUrl);
        // settingsBean.setMode(mode);
        settingsBean.setTitle(title);

        final Response response = settingsResource.setSettings(settingsBean);
        final Object responseEntity = response.getEntity();

        assertThat(responseEntity, instanceOf(SettingsBean.class));

        final SettingsBean responseSettingsBean = (SettingsBean) responseEntity;

        assertEquals(baseUrl, responseSettingsBean.getBaseUrl());
        assertEquals(title, responseSettingsBean.getTitle());
    }

    @Test
    @Ignore("Currently not causing any exceptions")
    public void testSetSettingsCausingExceptions() {
        final String baseUrl = "thisUrlIsNotValid";
        final String mode = "INVALID";
        final String title = StringUtils.repeat("A", 256);

        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setBaseUrl(baseUrl);
        // settingsBean.setMode(mode);
        settingsBean.setTitle(title);

        final Response response = settingsResource.setSettings(settingsBean);
        final Object responseEntity = response.getEntity();

        assertThat(responseEntity, instanceOf(ErrorCollection.class));

        final ErrorCollection errorCollection = (ErrorCollection) responseEntity;

        assertThat(errorCollection.getErrorMessages(), hasSize(2));
        assertThat(errorCollection.getErrors(), equalTo(Collections.EMPTY_MAP));
    }

}
