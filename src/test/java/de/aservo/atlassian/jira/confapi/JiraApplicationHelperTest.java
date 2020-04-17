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
import com.atlassian.jira.web.bean.MockI18nBean;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
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
import static org.junit.Assert.assertNull;
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
                new MockI18nBean.MockI18nBeanFactory(),
                licenseManager,
                licenseService);
    }

    @Test
    public void testGetAndSetBaseUrl() {
        assertNull(applicationHelper.getBaseUrl());
        applicationHelper.setBaseUrl(MockJiraApplicationHelper.BASE_URL);
        assertThat(applicationHelper.getBaseUrl(), equalTo(MockJiraApplicationHelper.BASE_URL));
    }

    @Test
    public void testSetBaseUrlInvalid() {
        final String baseUrl = "invalid";
        expectedException.expect(IllegalArgumentException.class);
        applicationHelper.setBaseUrl(baseUrl);
    }

    @Test
    public void testGetAndSetMode() {
        assertNull(applicationHelper.getMode());
        applicationHelper.setMode(MockJiraApplicationHelper.MODE);
        assertThat(applicationHelper.getMode(), equalTo(MockJiraApplicationHelper.MODE));
    }

    @Test
    public void testSetModeInvalid() {
        final String mode = "other";
        expectedException.expect(IllegalArgumentException.class);
        applicationHelper.setMode(mode);
    }

    @Test
    @Ignore
    public void testSetModeCombinationInvalid() {
        // TODO: Different to solve as currently implemented
    }

    @Test
    public void testGetAndSetTitle() {
        assertNull(applicationHelper.getTitle());
        applicationHelper.setTitle(MockJiraApplicationHelper.TITLE);
        assertThat(applicationHelper.getTitle(), equalTo(MockJiraApplicationHelper.TITLE));
    }

    @Test
    public void testSetTitleUnset() {
        final String title = "";
        // TODO: Get more specific here, as it is the same exception for a too long title
        expectedException.expect(IllegalArgumentException.class);
        applicationHelper.setTitle(title);
    }

    @Test
    public void testSetTitleTooLong() {
        final String title = StringUtils.repeat("A", 256);
        // TODO: Get more specific here, as it is the same exception for an unset title
        expectedException.expect(IllegalArgumentException.class);
        applicationHelper.setTitle(title);
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
