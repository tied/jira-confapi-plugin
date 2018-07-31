package de.aservo.atlassian.jira.confapi;

import com.atlassian.jira.bc.license.JiraLicenseService;
import com.atlassian.jira.bc.license.JiraLicenseService.ValidationResult;
import com.atlassian.jira.license.JiraLicenseManager;
import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Locale;

@Component
public class JiraApplicationHelper {

    @ComponentImport
    private final I18nHelper.BeanFactory i18nBeanFactory;

    @ComponentImport
    private final JiraLicenseManager licenseManager;

    @ComponentImport
    private final JiraLicenseService licenseService;

    @Inject
    public JiraApplicationHelper(
            final I18nHelper.BeanFactory i18nBeanFactory,
            final JiraLicenseManager licenseManager,
            final JiraLicenseService licenseService) {

        this.i18nBeanFactory = i18nBeanFactory;
        this.licenseManager = licenseManager;
        this.licenseService = licenseService;
    }

    public LicenseDetails getLicense() {
        return licenseManager.getLicenses().iterator().next();
    }

    public LicenseDetails setLicense(
            final String key) {

        final I18nHelper i18nHelper = i18nBeanFactory.getInstance(Locale.getDefault());
        final ValidationResult validationResult = licenseService.validate(i18nHelper, key);

        if (validationResult.getErrorCollection().hasAnyErrors()) {
            throw new IllegalArgumentException("Specified license was invalid.");
        }

        return licenseManager.clearAndSetLicenseNoEvent(validationResult.getLicenseString());
    }
}
