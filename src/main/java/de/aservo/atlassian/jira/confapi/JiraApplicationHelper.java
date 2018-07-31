package de.aservo.atlassian.jira.confapi;

import com.atlassian.jira.bc.license.JiraLicenseService;
import com.atlassian.jira.bc.license.JiraLicenseService.ValidationResult;
import com.atlassian.jira.bc.license.JiraLicenseUpdaterService;
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
    private final JiraLicenseManager jiraLicenseManager;

    @ComponentImport
    private final JiraLicenseService jiraLicenseService;

    @Inject
    public JiraApplicationHelper(
            final I18nHelper.BeanFactory i18nBeanFactory,
            final JiraLicenseManager jiraLicenseManager,
            final JiraLicenseService jiraLicenseService) {

        this.i18nBeanFactory = i18nBeanFactory;
        this.jiraLicenseManager = jiraLicenseManager;
        this.jiraLicenseService = jiraLicenseService;
    }

    public LicenseDetails getLicense() {
        return jiraLicenseManager.getLicenses().iterator().next();
    }

    public LicenseDetails setLicense(
            final String key) {

        final I18nHelper i18nHelper = i18nBeanFactory.getInstance(Locale.getDefault());
        final ValidationResult validationResult = jiraLicenseService.validate(i18nHelper, key);

        if (validationResult.getErrorCollection().hasAnyErrors()) {
            throw new IllegalArgumentException("Specified license was invalid.");
        }

        final JiraLicenseUpdaterService jiraLicenseUpdaterService = (JiraLicenseUpdaterService) jiraLicenseService;
        return jiraLicenseUpdaterService.setLicense(validationResult);
    }
}
