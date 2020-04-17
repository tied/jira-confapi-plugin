package de.aservo.atlassian.jira.confapi.rest;

import com.atlassian.application.api.ApplicationKey;
import com.atlassian.jira.license.LicenseDetails;
import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.model.LicenseBean;
import de.aservo.atlassian.confapi.model.LicensesBean;
import de.aservo.atlassian.confapi.rest.LicenseResourceInterface;
import de.aservo.atlassian.jira.confapi.service.JiraApplicationHelper;
import de.aservo.atlassian.jira.confapi.helper.JiraWebAuthenticationHelper;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Licenses resource to get the licenses.
 */
@Path(ConfAPI.LICENSES)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class LicensesResource implements LicenseResourceInterface {

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
    @Override
    public Response getLicenses() {
        webAuthenticationHelper.mustBeSysAdmin();

        final Collection<LicenseBean> licenseBeans = applicationHelper.getLicenses().stream()
                .map(this::createLicenseBean)
                .collect(Collectors.toList());

        return Response.ok(new LicensesBean(licenseBeans)).build();
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Override
    public Response setLicense(
            @QueryParam("clear") @DefaultValue("false") boolean clear,
            final String licenseKey) throws WebApplicationException {

        webAuthenticationHelper.mustBeSysAdmin();

        if (licenseKey == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        final LicenseDetails licenseDetails = applicationHelper.setLicense(licenseKey, clear);
        return Response.ok(createLicenseBean(licenseDetails)).build();
    }

    protected LicenseBean createLicenseBean(
            final LicenseDetails licenseDetails) {

        final LicenseBean licenseBean = new LicenseBean();

        licenseBean.setLicenseType(licenseDetails.getLicenseType().name());
        licenseBean.setOrganization(licenseDetails.getOrganisation());
        licenseBean.setDescription(licenseDetails.getDescription());
        licenseBean.setExpiryDate(licenseDetails.getMaintenanceExpiryDate());
        licenseBean.setKey(licenseDetails.getLicenseString());
        licenseBean.setProducts(licenseDetails.getLicensedApplications().getKeys().stream()
                .map(ApplicationKey::value)
                .collect(Collectors.toList())
        );

        return licenseBean;
    }

}
