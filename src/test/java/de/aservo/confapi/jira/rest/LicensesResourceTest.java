package de.aservo.confapi.jira.rest;

import com.atlassian.jira.application.ApplicationKeys;
import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.jira.license.LicensedApplications;
import com.atlassian.jira.license.MockLicensedApplications;
import de.aservo.confapi.commons.model.LicenseBean;
import de.aservo.confapi.commons.model.LicensesBean;
import de.aservo.confapi.jira.service.JiraApplicationHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Collections;

import static com.atlassian.extras.api.LicenseType.COMMERCIAL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LicensesResourceTest {

    private static final String LICENSE = "Aaa...";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private JiraApplicationHelper applicationHelper;

    private LicenseDetails mockLicenseDetail;

    private LicensesResourceImpl licenseResource;

    @Before
    public void setup() {
        /*
         * There is no need to setup an user an an admin, because he is assumed to be an admin as
         * long as no exception is thrown in webAuthenticationHelper.mustBeSysAdmin()
         */

        final LicensedApplications licensedApplications = new MockLicensedApplications(ApplicationKeys.CORE);

        mockLicenseDetail = mock(LicenseDetails.class);

        when(mockLicenseDetail.getLicenseString()).thenReturn(LICENSE);
        when(mockLicenseDetail.getLicenseType()).thenReturn(COMMERCIAL);
        when(mockLicenseDetail.getLicensedApplications()).thenReturn(licensedApplications);

        when(applicationHelper.setLicense(Matchers.anyString(), anyBoolean())).thenReturn(mockLicenseDetail);

        licenseResource = new LicensesResourceImpl(applicationHelper);
    }

    @Test
    public void testGetLicenses() {
        final Collection<LicenseDetails> mockLicenseDetails = Collections.singleton(mockLicenseDetail);

        when(applicationHelper.getLicenses()).thenReturn(mockLicenseDetails);

        final Response response = licenseResource.getLicenses();
        final Object responseEntity = response.getEntity();

        assertThat(responseEntity, instanceOf(LicensesBean.class));

        final LicensesBean licensesBean = (LicensesBean) responseEntity;
        final LicenseBean licenseBean = licenseResource.createLicenseBean(mockLicenseDetail);

        assertThat(licensesBean.getLicenses(), containsInAnyOrder(licenseBean));
    }

    @Test
    public void testSetLicense() {
        final Response response = licenseResource.addLicense(LicenseBean.EXAMPLE_1);
        final Object responseEntity = response.getEntity();

        assertThat(responseEntity, instanceOf(LicensesBean.class));

        /*
        TODO: Fix
        final LicensesBean licensesBean = (LicensesBean) responseEntity;
        final LicenseBean mockLicenseBean = licenseResource.createLicenseBean(mockLicenseDetail);

        assertThat(licensesBean.getLicenses().iterator().next(), equalTo(mockLicenseBean));
         */
    }

}
