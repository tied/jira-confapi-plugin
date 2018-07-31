package de.aservo.atlassian.jira.confapi.rest;

import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.jira.rest.v2.issue.RESTException;
import de.aservo.atlassian.jira.confapi.JiraApplicationHelper;
import de.aservo.atlassian.jira.confapi.bean.LicenseBean;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * License API to get and set the license.
 */
@Path("/license")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class LicenseRest {

    private final JiraApplicationHelper applicationHelper;

    /**
     * Constructor.
     *
     * @param applicationHelper the injected {@link JiraApplicationHelper}
     */
    @Inject
    public LicenseRest(
            final JiraApplicationHelper applicationHelper) {

        this.applicationHelper = applicationHelper;
    }

    @POST
    public Response setLicense(
            @Encoded @QueryParam("key") final String key) {

        if (key == null) {
            throw new RESTException(Response.Status.BAD_REQUEST, "No key given");
        }

        final LicenseDetails licenseDetails = applicationHelper.setLicense(key);
        return Response.ok(LicenseBean.from(licenseDetails)).build();
    }

    @GET
    public Response getLicense() {
        final LicenseDetails licenseDetails = applicationHelper.getLicense();
        return Response.ok(LicenseBean.from(licenseDetails)).build();
    }

}
