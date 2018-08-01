package de.aservo.atlassian.jira.confapi.rest;

import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import de.aservo.atlassian.jira.confapi.JiraApplicationHelper;
import de.aservo.atlassian.jira.confapi.JiraWebAuthenticationHelper;
import de.aservo.atlassian.jira.confapi.bean.LicensesBean;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * Licenses resource to get the licenses.
 */
@Path("/licenses")
@AnonymousAllowed
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class LicensesResource {

    private final JiraApplicationHelper applicationHelper;

    private final JiraWebAuthenticationHelper webAuthenticationHelper;

    /**
     * Constructor.
     *
     * @param applicationHelper       the injected {@link JiraApplicationHelper}
     * @param webAuthenticationHelper the injected {@link JiraWebAuthenticationHelper}
     */
    @Inject
    public LicensesResource(
            final JiraApplicationHelper applicationHelper,
            final JiraWebAuthenticationHelper webAuthenticationHelper) {

        this.applicationHelper = applicationHelper;
        this.webAuthenticationHelper = webAuthenticationHelper;
    }

    @GET
    public Response getLicenses() {
        webAuthenticationHelper.mustBeSysAdmin();

        final Collection<LicenseDetails> licenseDetails = applicationHelper.getLicenses();
        return Response.ok(LicensesBean.from(licenseDetails)).build();
    }

}
