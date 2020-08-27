package de.aservo.confapi.jira.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confapi.model.SettingsBean;
import de.aservo.atlassian.confapi.rest.api.SettingsResource;
import de.aservo.confapi.jira.filter.SysadminOnlyResourceFilter;
import de.aservo.confapi.jira.service.JiraApplicationHelper;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(ConfAPI.SETTINGS)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class SettingsResourceImpl implements SettingsResource {

    private final JiraApplicationHelper applicationHelper;

    @Inject
    public SettingsResourceImpl(
            final JiraApplicationHelper applicationHelper) {

        this.applicationHelper = applicationHelper;
    }

    @Override
    public Response getSettings() {
        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setTitle(applicationHelper.getTitle());
        settingsBean.setBaseUrl(applicationHelper.getBaseUrl());
        return Response.ok(settingsBean).build();
    }

    @Override
    public Response setSettings(
            final SettingsBean settingsBean) {

        final ErrorCollection errorCollection = ErrorCollection.of();

        if (settingsBean.getBaseUrl() != null) {
            try {
                applicationHelper.setBaseUrl(settingsBean.getBaseUrl());
            } catch (Exception e) {
                errorCollection.addErrorMessage(e.getMessage());
            }
        }

        if (settingsBean.getMode() != null) {
            try {
                applicationHelper.setMode(settingsBean.getMode());
            } catch (Exception e) {
                errorCollection.addErrorMessage(e.getMessage());
            }
        }

        if (settingsBean.getTitle() != null) {
            try {
                applicationHelper.setTitle(settingsBean.getTitle());
            } catch (Exception e) {
                errorCollection.addErrorMessage(e.getMessage());
            }
        }

        final SettingsBean result = new SettingsBean();
        result.setTitle(applicationHelper.getTitle());
        result.setBaseUrl(applicationHelper.getBaseUrl());
        return Response.ok(result).build();
    }

}
