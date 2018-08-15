package de.aservo.atlassian.jira.confapi;

import com.atlassian.jira.application.ApplicationKeys;
import com.atlassian.jira.bc.license.JiraLicenseService;
import com.atlassian.jira.license.JiraLicenseManager;
import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.jira.license.LicensedApplications;
import com.atlassian.jira.license.MockLicensedApplications;
import com.atlassian.jira.mock.MockApplicationProperties;
import com.atlassian.jira.util.ErrorCollection;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.jira.util.SimpleErrorCollection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JiraApplicationHelperTest {

    private static final String LICENSE = "AAA...";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private JiraLicenseManager licenseManager;

    @Mock
    private JiraLicenseService licenseService;

    private LicenseDetails mockLicenseDetail;

    private JiraApplicationHelper applicationHelper;

    @Before
    public void setup() {
        final LicensedApplications licensedApplications = new MockLicensedApplications(ApplicationKeys.CORE);

        mockLicenseDetail = mock(LicenseDetails.class);
        when(mockLicenseDetail.getLicenseString()).thenReturn(LICENSE);
        when(mockLicenseDetail.getLicensedApplications()).thenReturn(licensedApplications);

        applicationHelper = new JiraApplicationHelper(
                new MockApplicationProperties(),
                new MockJiraI18nHelper(),
                licenseManager,
                licenseService);
    }

    @Test
    public void testGetLicenses() {
        when(licenseManager.getLicenses()).thenReturn(Collections.singleton(mockLicenseDetail));
        final Collection<LicenseDetails> licenses = applicationHelper.getLicenses();
        assertThat(licenses, containsInAnyOrder(mockLicenseDetail));
    }

    @Test
    public void testSetLicense() {
        final JiraLicenseService.ValidationResult mockValidationResult = mock(JiraLicenseService.ValidationResult.class);
        when(mockValidationResult.getErrorCollection()).thenReturn(new SimpleErrorCollection());
        when(mockValidationResult.getLicenseString()).thenReturn(LICENSE);

        when(licenseService.validate(any(I18nHelper.class), anyString())).thenReturn(mockValidationResult);
        when(licenseManager.setLicenseNoEvent(mockValidationResult.getLicenseString())).thenReturn(mockLicenseDetail);

        final LicenseDetails licenseDetails = applicationHelper.setLicense("AAA...", false);
        assertThat(licenseDetails, equalTo(mockLicenseDetail));
    }

    @Test
    public void testClearAndSetLicense() {
        final JiraLicenseService.ValidationResult mockValidationResult = mock(JiraLicenseService.ValidationResult.class);
        when(mockValidationResult.getErrorCollection()).thenReturn(new SimpleErrorCollection());
        when(mockValidationResult.getLicenseString()).thenReturn(LICENSE);

        when(licenseService.validate(any(I18nHelper.class), anyString())).thenReturn(mockValidationResult);
        when(licenseManager.clearAndSetLicenseNoEvent(mockValidationResult.getLicenseString())).thenReturn(mockLicenseDetail);

        final LicenseDetails licenseDetails = applicationHelper.setLicense("AAA...", true);
        assertThat(licenseDetails, equalTo(mockLicenseDetail));
    }

    @Test
    public void testSetLicenseNotValid() {
        final JiraLicenseService.ValidationResult mockValidationResult = mock(JiraLicenseService.ValidationResult.class);
        when(mockValidationResult.getErrorCollection()).thenReturn(new SimpleErrorCollection("License not valid", ErrorCollection.Reason.VALIDATION_FAILED));
        when(mockValidationResult.getLicenseString()).thenReturn(LICENSE);

        when(licenseService.validate(any(I18nHelper.class), anyString())).thenReturn(mockValidationResult);
        when(licenseManager.setLicenseNoEvent(mockValidationResult.getLicenseString())).thenReturn(mockLicenseDetail);

        expectedException.expect(IllegalArgumentException.class);
        applicationHelper.setLicense("AAA...", false);
    }

}
