package de.aservo.atlassian.jira.confapi.bean;

import com.atlassian.jira.application.ApplicationKeys;
import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.jira.license.LicensedApplications;
import com.atlassian.jira.license.MockLicensedApplications;
import org.junit.Before;
import org.junit.Test;

import static com.atlassian.jira.license.ProductLicense.LICENSE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LicenseBeanTest {

    private LicenseDetails mockLicenseDetail;

    private LicenseBean licenseBean1;
    private LicenseBean licenseBean2;

    @Before
    public void setup() {
        final LicensedApplications licensedApplications = new MockLicensedApplications(ApplicationKeys.CORE);

        mockLicenseDetail = mock(LicenseDetails.class);
        when(mockLicenseDetail.getLicenseString()).thenReturn(LICENSE);
        when(mockLicenseDetail.getLicensedApplications()).thenReturn(licensedApplications);

        licenseBean1 = LicenseBean.from(mockLicenseDetail);
        licenseBean2 = LicenseBean.from(mockLicenseDetail);
    }

    @Test
    public void testFrom() {
        assertThat(licenseBean1.getKey(), equalTo(mockLicenseDetail.getLicenseString()));
        assertThat(licenseBean1.getApplicationKeys(), containsInAnyOrder(
                mockLicenseDetail.getLicensedApplications().getKeys().toArray()));
    }

    @Test
    public void testEquals() {
        assertThat(licenseBean1, equalTo(licenseBean2));
    }

    @Test
    public void testHashCode() {
        assertThat(licenseBean1.hashCode(), equalTo(licenseBean2.hashCode()));
    }

}
