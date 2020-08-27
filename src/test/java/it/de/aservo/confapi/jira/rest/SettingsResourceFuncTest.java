package it.de.aservo.confapi.jira.rest;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.model.SettingsBean;
import it.de.aservo.confapi.jira.rest.client.ClientApplication;
import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.RestClient;
import org.apache.wink.client.handlers.BasicAuthSecurityHandler;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SettingsResourceFuncTest {

    private RestClient getRestClient() {
        final ClientApplication clientApplication = new ClientApplication();
        clientApplication.setSingletons(Collections.singleton(new JacksonJsonProvider()));

        final ClientConfig config = new ClientConfig().applications(clientApplication);
        final BasicAuthSecurityHandler basicAuthHandler = new BasicAuthSecurityHandler();
        basicAuthHandler.setUserName("admin");
        basicAuthHandler.setPassword("admin");
        config.handlers(basicAuthHandler);

        return new RestClient(config);
    }

    private String getResourceUrl(
            final String path) {

        final String baseUrl = System.getProperty("baseurl");
        return baseUrl + "/rest/confapi/1/" + path;
    }

    @Test
    public void testGetSettings() {
        final RestClient client = getRestClient();
        final String resourceUrl = getResourceUrl(ConfAPI.SETTINGS);

        final SettingsBean responseBean = client.resource(resourceUrl)
                .accept(MediaType.APPLICATION_JSON)
                .get(SettingsBean.class);

        assertNotNull(responseBean);
    }

    @Test
    public void testSetSettings() {
        final RestClient client = getRestClient();
        final String resourceUrl = getResourceUrl(ConfAPI.SETTINGS);

        final SettingsBean requestBean = new SettingsBean();
        requestBean.setTitle("ASERVO Jira");

        final SettingsBean responseBean = client.resource(resourceUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .put(SettingsBean.class, requestBean);

        assertNotNull(responseBean);
        assertEquals(requestBean.getTitle(), responseBean.getTitle());
    }

}
