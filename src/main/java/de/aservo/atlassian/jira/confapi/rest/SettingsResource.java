package de.aservo.atlassian.jira.confapi.rest;

import com.atlassian.jira.rest.api.util.ErrorCollection;
import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.jira.confapi.JiraApplicationHelper;
import de.aservo.atlassian.jira.confapi.JiraWebAuthenticationHelper;
import de.aservo.atlassian.confapi.model.SettingsBean;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Settings resource to get the licenses.
 */
@Path(ConfAPI.SETTINGS)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SettingsResource {

    private final JiraApplicationHelper applicationHelper;

    private final JiraWebAuthenticationHelper webAuthenticationHelper;

    /**
     * Constructor.
     *
     * @param applicationHelper       the injected {@link JiraApplicationHelper}
     * @param webAuthenticationHelper the injected {@link JiraWebAuthenticationHelper}
     */
    @Inject
    public SettingsResource(
            final JiraApplicationHelper applicationHelper,
            final JiraWebAuthenticationHelper webAuthenticationHelper) {

        this.applicationHelper = applicationHelper;
        this.webAuthenticationHelper = webAuthenticationHelper;
    }

    @GET
    public Response getSettings() {
        webAuthenticationHelper.mustBeSysAdmin();

        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setTitle(applicationHelper.getTitle());
        settingsBean.setBaseUrl(applicationHelper.getBaseUrl());
        return Response.ok(settingsBean).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setSettings(
            final SettingsBean settingsBean) {

        webAuthenticationHelper.mustBeSysAdmin();

        final ErrorCollection errorCollection = ErrorCollection.of();

        if (settingsBean.getBaseUrl() != null) {
            try {
                applicationHelper.setBaseUrl(settingsBean.getBaseUrl());
            } catch (Exception e) {
                errorCollection.addErrorMessage(e.getMessage());
            }
        }

        /*
        if (settingsBean.getMode() != null) {
            try {
                applicationHelper.setMode(settingsBean.getMode());
            } catch (Exception e) {
                errorCollection.addErrorMessage(e.getMessage());
            }
        }
         */

        if (settingsBean.getTitle() != null) {
            try {
                applicationHelper.setTitle(settingsBean.getTitle());
            } catch (Exception e) {
                errorCollection.addErrorMessage(e.getMessage());
            }
        }

        return Response.ok(errorCollection).build();
    }

}
