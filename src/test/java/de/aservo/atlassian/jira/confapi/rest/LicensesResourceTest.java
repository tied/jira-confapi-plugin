package de.aservo.atlassian.jira.confapi.rest;

import com.atlassian.jira.application.ApplicationKeys;
import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.jira.license.LicensedApplications;
import com.atlassian.jira.license.MockLicensedApplications;
import com.atlassian.jira.rest.exception.BadRequestWebException;
import de.aservo.atlassian.jira.confapi.JiraApplicationHelper;
import de.aservo.atlassian.jira.confapi.JiraWebAuthenticationHelper;
import de.aservo.atlassian.jira.confapi.bean.LicenseBean;
import de.aservo.atlassian.jira.confapi.bean.LicensesBean;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LicensesResourceTest {

    private static final String LICENSE = "Aaa...";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private JiraApplicationHelper applicationHelper;

    @Mock
    private JiraWebAuthenticationHelper webAuthenticationHelper;

    private LicenseDetails mockLicenseDetail;

    private LicensesResource licenseResource;

    @Before
    public void setup() {
        /*
         * There is no need to setup an user an an admin, because he is assumed to be an admin as
         * long as no exception is thrown in webAuthenticationHelper.mustBeSysAdmin()
         */

        final LicensedApplications licensedApplications = new MockLicensedApplications(ApplicationKeys.CORE);

        mockLicenseDetail = mock(LicenseDetails.class);
        when(mockLicenseDetail.getLicenseString()).thenReturn(LICENSE);
        when(mockLicenseDetail.getLicensedApplications()).thenReturn(licensedApplications);

        when(applicationHelper.setLicense(Matchers.anyString())).thenReturn(mockLicenseDetail);

        licenseResource = new LicensesResource(
                applicationHelper,
                webAuthenticationHelper);
    }

    @Test
    public void testGetLicenses() {
        final Collection<LicenseDetails> mockLicenseDetails = Collections.singleton(mockLicenseDetail);

        when(applicationHelper.getLicenses()).thenReturn(mockLicenseDetails);

        final Response response = licenseResource.getLicenses();
        final Object responseEntity = response.getEntity();

        assertThat(responseEntity, instanceOf(LicensesBean.class));

        final LicensesBean licensesBean = (LicensesBean) responseEntity;
        final LicenseBean licenseBean = LicenseBean.from(mockLicenseDetail);

        assertThat(licensesBean.getLicenses(), containsInAnyOrder(licenseBean));
    }

    @Test
    public void testSetLicense() {
        final Response response = licenseResource.setLicense(LICENSE);
        final Object responseEntity = response.getEntity();

        assertThat(responseEntity, instanceOf(LicenseBean.class));

        final LicenseBean licenseBean = (LicenseBean) responseEntity;
        final LicenseBean mockLicenseBean = LicenseBean.from(mockLicenseDetail);

        assertThat(licenseBean, equalTo(mockLicenseBean));
    }

    @Test
    public void testSetNullLicense() {
        expectedException.expect(BadRequestWebException.class);
        licenseResource.setLicense(null);
    }

}
