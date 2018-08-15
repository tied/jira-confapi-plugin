package de.aservo.atlassian.jira.confapi;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.UserPropertyManager;
import com.atlassian.jira.user.preferences.UserPreferencesManager;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.opensymphony.module.propertyset.PropertySet;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class JiraUserHelper {

    @ComponentImport
    private final JiraAuthenticationContext authenticationContext;

    @ComponentImport
    private final I18nHelper.BeanFactory i18nBeanFactory;

    @ComponentImport
    private final UserPropertyManager userPropertyManager;

    /**
     * Constructor.
     *
     * @param i18nBeanFactory        injected {@link I18nHelper.BeanFactory}
     * @param userPropertyManager injected {@link UserPreferencesManager}
     */
    @Inject
    public JiraUserHelper(
            final JiraAuthenticationContext authenticationContext,
            final I18nHelper.BeanFactory i18nBeanFactory,
            final UserPropertyManager userPropertyManager) {

        this.authenticationContext = authenticationContext;
        this.i18nBeanFactory = i18nBeanFactory;
        this.userPropertyManager = userPropertyManager;
    }

    public ApplicationUser getLoggedInUser() {
        return authenticationContext.getLoggedInUser();
    }

    public PropertySet getUserProperties() {
        return getUserProperties(getLoggedInUser());
    }

    public PropertySet getUserProperties(
            final ApplicationUser user) {

        return userPropertyManager.getPropertySet(user);
    }

}
