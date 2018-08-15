package de.aservo.atlassian.jira.confapi;

import com.atlassian.jira.bc.license.JiraLicenseService;
import com.atlassian.jira.bc.license.JiraLicenseService.ValidationResult;
import com.atlassian.jira.license.JiraLicenseManager;
import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Locale;

@Component
public class JiraApplicationHelper {

    @ComponentImport
    private final I18nHelper.BeanFactory i18nBeanFactory;

    @ComponentImport
    private final JiraLicenseManager licenseManager;

    @ComponentImport
    private final JiraLicenseService licenseService;

    /**
     * Constructor.
     *
     * @param i18nBeanFactory injected {@link com.atlassian.jira.util.I18nHelper.BeanFactory}
     * @param licenseManager  injected {@link JiraLicenseManager}
     * @param licenseService  injected {@link JiraLicenseService}
     */
    @Inject
    public JiraApplicationHelper(
            final I18nHelper.BeanFactory i18nBeanFactory,
            final JiraLicenseManager licenseManager,
            final JiraLicenseService licenseService) {

        this.i18nBeanFactory = i18nBeanFactory;
        this.licenseManager = licenseManager;
        this.licenseService = licenseService;
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

        final I18nHelper i18nHelper = i18nBeanFactory.getInstance(Locale.getDefault());
        final ValidationResult validationResult = licenseService.validate(i18nHelper, key);

        if (validationResult.getErrorCollection().hasAnyErrors()) {
            throw new IllegalArgumentException("Specified license was invalid.");
        }

        final String licenseString = validationResult.getLicenseString();

        if (clear) {
            return licenseManager.clearAndSetLicenseNoEvent(licenseString);
        }

        return licenseManager.setLicenseNoEvent(licenseString);
    }
}
