package de.aservo.atlassian.jira.confapi;

import com.atlassian.jira.rest.exception.ForbiddenWebException;
import com.atlassian.jira.rest.exception.NotAuthorisedWebException;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static com.atlassian.jira.permission.GlobalPermissionKey.SYSTEM_ADMIN;

@Component
public class JiraWebAuthenticationHelper {

    @ComponentImport
    private final JiraAuthenticationContext authenticationContext;

    @ComponentImport
    private final GlobalPermissionManager globalPermissionManager;

    /**
     * Constructor.
     *
     * @param authenticationContext   the injected {@link JiraAuthenticationContext}
     * @param globalPermissionManager the injected {@link GlobalPermissionManager}
     */
    @Inject
    public JiraWebAuthenticationHelper(
            final JiraAuthenticationContext authenticationContext,
            final GlobalPermissionManager globalPermissionManager) {

        this.authenticationContext = authenticationContext;
        this.globalPermissionManager = globalPermissionManager;
    }

    public void mustBeSysAdmin() {
        final ApplicationUser user = authenticationContext.getLoggedInUser();
        if (user == null) {
            throw new NotAuthorisedWebException();
        }

        final boolean isSysAdmin = globalPermissionManager.hasPermission(SYSTEM_ADMIN, user);
        if (!isSysAdmin) {
            throw new ForbiddenWebException();
        }
    }
}
