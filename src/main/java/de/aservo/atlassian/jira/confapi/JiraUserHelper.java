package de.aservo.atlassian.jira.confapi;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.UserPropertyManager;
import com.atlassian.jira.user.preferences.UserPreferencesManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.opensymphony.module.propertyset.PropertySet;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;

@Component
public class JiraUserHelper {

    @ComponentImport
    private final JiraAuthenticationContext authenticationContext;

    @ComponentImport
    private final UserPropertyManager userPropertyManager;

    /**
     * Constructor.
     *
     * @param authenticationContext injected {@link JiraAuthenticationContext}
     * @param userPropertyManager   injected {@link UserPreferencesManager}
     */
    @Inject
    public JiraUserHelper(
            final JiraAuthenticationContext authenticationContext,
            final UserPropertyManager userPropertyManager) {

        this.authenticationContext = authenticationContext;
        this.userPropertyManager = userPropertyManager;
    }

    /**
     * Get logged in user.
     *
     * @return user
     */
    @Nullable
    public ApplicationUser getLoggedInUser() {
        return authenticationContext.getLoggedInUser();
    }

    /**
     * Get the property set of the logged in user.
     *
     * @return property set
     */
    @Nullable
    public PropertySet getUserProperties() {
        return getUserProperties(getLoggedInUser());
    }

    /**
     * Get the property set of the given user.
     *
     * @param user the application user
     * @return     property set
     */
    @Nullable
    public PropertySet getUserProperties(
            @Nullable final ApplicationUser user) {

        if (user != null) {
            return userPropertyManager.getPropertySet(user);
        }

        return null;
    }

}
