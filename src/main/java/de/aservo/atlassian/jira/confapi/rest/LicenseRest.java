package de.aservo.atlassian.jira.confapi.rest;

import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.jira.rest.v2.issue.RESTException;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.atlassian.jira.permission.GlobalPermissionKey.SYSTEM_ADMIN;

/**
 * License API to get and set the license.
 */
@Path("/license")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class LicenseRest {

    private final JiraApplicationHelper applicationHelper;

    @ComponentImport
    private final JiraAuthenticationContext authenticationContext;

    @ComponentImport
    private final GlobalPermissionManager globalPermissionManager;

    /**
     * Constructor.
     *
     * @param applicationHelper the injected {@link JiraApplicationHelper}
     */
    @Inject
    public LicenseRest(
            final JiraApplicationHelper applicationHelper,
            final JiraAuthenticationContext authenticationContext,
            final GlobalPermissionManager globalPermissionManager) {

        this.applicationHelper = applicationHelper;
        this.authenticationContext = authenticationContext;
        this.globalPermissionManager = globalPermissionManager;
    }

    @POST
    public Response setLicense(
            @Encoded @QueryParam("key") final String key) {

        mustBeSysAdmin();

        if (key == null) {
            throw new RESTException(Response.Status.BAD_REQUEST, "No key given");
        }

        final LicenseDetails licenseDetails = applicationHelper.setLicense(key);
        return Response.ok(LicenseBean.from(licenseDetails)).build();
    }

    @GET
    public Response getLicense() {
        mustBeSysAdmin();
        final LicenseDetails licenseDetails = applicationHelper.getLicense();
        return Response.ok(LicenseBean.from(licenseDetails)).build();
    }

    private void mustBeSysAdmin() {
        final ApplicationUser user = authenticationContext.getLoggedInUser();
        if (user == null) {
            throw new WebApplicationException();
        }

        final boolean isSysAdmin = globalPermissionManager.hasPermission(SYSTEM_ADMIN, user);
        if (!isSysAdmin) {
            throw new WebApplicationException();
        }
    }

}
