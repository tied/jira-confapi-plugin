package de.aservo.confapi.jira.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.ErrorCollection;
import de.aservo.confapi.commons.model.SettingsBean;
import de.aservo.confapi.commons.rest.api.SettingsResource;
import de.aservo.confapi.jira.filter.SysadminOnlyResourceFilter;
import de.aservo.confapi.jira.service.JiraApplicationHelper;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

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
                applicationHelper.setBaseUrl(URI.create(settingsBean.getBaseUrl()).toString());
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
