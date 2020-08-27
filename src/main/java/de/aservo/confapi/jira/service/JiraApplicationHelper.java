package de.aservo.confapi.jira.service;

import com.atlassian.jira.bc.license.JiraLicenseService;
import com.atlassian.jira.bc.license.JiraLicenseService.ValidationResult;
import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.license.JiraLicenseManager;
import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.jira.util.UrlValidator;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.google.common.collect.Lists;
import com.opensymphony.util.TextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Locale;

@Component
public class JiraApplicationHelper {

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @ComponentImport
    private final I18nHelper.BeanFactory i18nBeanFactory;

    @ComponentImport
    private final JiraLicenseManager licenseManager;

    @ComponentImport
    private final JiraLicenseService licenseService;

    /**
     * Constructor.
     *
     * @param applicationProperties injected {@link ApplicationProperties}
     * @param i18nBeanFactory       injected {@link I18nHelper.BeanFactory}
     * @param licenseManager        injected {@link JiraLicenseManager}
     * @param licenseService        injected {@link JiraLicenseService}
     */
    @Inject
    public JiraApplicationHelper(
            final ApplicationProperties applicationProperties,
            final I18nHelper.BeanFactory i18nBeanFactory,
            final JiraLicenseManager licenseManager,
            final JiraLicenseService licenseService) {

        this.applicationProperties = applicationProperties;
        this.i18nBeanFactory = i18nBeanFactory;
        this.licenseManager = licenseManager;
        this.licenseService = licenseService;
    }

    public String getBaseUrl() {
        return applicationProperties.getString(APKeys.JIRA_BASEURL);
    }

    public void setBaseUrl(
            @Nonnull final String baseUrl) {

        if (!UrlValidator.isValid(baseUrl)) {
            throw new IllegalArgumentException("You must set a valid base url");
        }

        applicationProperties.setString(APKeys.JIRA_BASEURL, baseUrl);
    }

    public String getMode() {
        return applicationProperties.getString(APKeys.JIRA_MODE);
    }

    public void setMode(
            @Nonnull final String mode) {

        if (!mode.equalsIgnoreCase("public") && !mode.equalsIgnoreCase("private")) {
            throw new IllegalArgumentException("Invalid mode");
        }

        if (mode.equalsIgnoreCase("public") && hasExternalUserManagement()) {
            throw new IllegalArgumentException("Invalid mode / external user management combination");
        }

        applicationProperties.setString(APKeys.JIRA_MODE, mode);
    }

    public String getTitle() {
        return applicationProperties.getString(APKeys.JIRA_TITLE);
    }

    public void setTitle(
            @Nonnull final String title) {

        if (!TextUtils.stringSet(title)) {
            throw new IllegalArgumentException("You must set an application title");
        }

        if (StringUtils.length(title) > 255) {
            throw new IllegalArgumentException("Invalid length of application title");
        }

        applicationProperties.setString(APKeys.JIRA_TITLE, title);
    }

    private boolean hasExternalUserManagement() {
        return applicationProperties.getOption(APKeys.JIRA_OPTION_USER_EXTERNALMGT);
    }

    /**
     * Get all licenses.
     *
     * @return licenses details
     */
    public Collection<LicenseDetails> getLicenses() {
          return Lists.newArrayList(licenseManager.getLicenses());
    }

    /**
     * Set a new license key and clear all licenses before if wanted.
     *
     * @param key   the license key
     * @param clear whether to remove all licenses before setting the new license
     * @return      license details
     */
    public LicenseDetails setLicense(
            final String key,
            boolean clear) {

        final ValidationResult validationResult = licenseService.validate(i18nBeanFactory.getInstance(Locale.getDefault()), key);

        if (validationResult.getErrorCollection().hasAnyErrors()) {
            throw new IllegalArgumentException("Specified license was invalid.");
        }

        final String licenseString = validationResult.getLicenseString();

        return clear
                ? licenseManager.clearAndSetLicenseNoEvent(licenseString)
                : licenseManager.setLicenseNoEvent(licenseString);
    }

}
