package de.aservo.atlassian.jira.confapi.rest;

import com.atlassian.application.api.ApplicationKey;
import com.atlassian.jira.license.LicenseDetails;
import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.model.LicenseBean;
import de.aservo.atlassian.confapi.model.LicensesBean;
import de.aservo.atlassian.confapi.rest.api.LicenseResource;
import de.aservo.atlassian.jira.confapi.helper.JiraWebAuthenticationHelper;
import de.aservo.atlassian.jira.confapi.service.JiraApplicationHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.stream.Collectors;

@Path(ConfAPI.LICENSES)
@Component
public class LicensesResourceImpl implements LicenseResource {

    private final JiraApplicationHelper applicationHelper;

    private final JiraWebAuthenticationHelper webAuthenticationHelper;

    @Inject
    public LicensesResourceImpl(
            final JiraApplicationHelper applicationHelper,
            final JiraWebAuthenticationHelper webAuthenticationHelper) {

        this.applicationHelper = applicationHelper;
        this.webAuthenticationHelper = webAuthenticationHelper;
    }

    @Override
    public Response getLicenses() {
        webAuthenticationHelper.mustBeSysAdmin();

        final Collection<LicenseBean> licenseBeans = applicationHelper.getLicenses().stream()
                .map(this::createLicenseBean)
                .collect(Collectors.toList());

        return Response.ok(new LicensesBean(licenseBeans)).build();
    }

    @Override
    public Response setLicense(
            @QueryParam("clear") @DefaultValue("false") boolean clear,
            @Nonnull final String licenseKey) throws WebApplicationException {

        webAuthenticationHelper.mustBeSysAdmin();

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
