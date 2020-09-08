package de.aservo.confapi.jira.rest;

import com.atlassian.application.api.ApplicationKey;
import com.atlassian.jira.license.LicenseDetails;
import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.LicenseBean;
import de.aservo.confapi.commons.model.LicensesBean;
import de.aservo.confapi.commons.rest.api.LicensesResource;
import de.aservo.confapi.jira.filter.SysadminOnlyResourceFilter;
import de.aservo.confapi.jira.service.JiraApplicationHelper;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.stream.Collectors;

@Path(ConfAPI.LICENSES)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class LicensesResourceImpl implements LicensesResource {

    private final JiraApplicationHelper applicationHelper;

    @Inject
    public LicensesResourceImpl(
            final JiraApplicationHelper applicationHelper) {

        this.applicationHelper = applicationHelper;
    }

    @Override
    public Response getLicenses() {
        final Collection<LicenseBean> licenseBeans = applicationHelper.getLicenses().stream()
                .map(this::createLicenseBean)
                .collect(Collectors.toList());

        return Response.ok(new LicensesBean(licenseBeans)).build();
    }

    @Override
    public Response setLicenses(LicensesBean licensesBean) {
        // TODO: Only clear before setting the first one
        licensesBean.getLicenses().forEach(
                l -> applicationHelper.setLicense(l.getKey(), false)
        );

        return getLicenses();
    }

    @Override
    public Response addLicense(LicenseBean licenseBean) {
        applicationHelper.setLicense(licenseBean.getKey(), false);
        // TODO: Do we really want to return ALL licenses here?
        return getLicenses();
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
