package de.aservo.atlassian.jira.confapi.helper;

import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.atlassian.jira.confapi.service.JiraUserHelper;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static com.atlassian.jira.permission.GlobalPermissionKey.SYSTEM_ADMIN;

@Component
public class JiraWebAuthenticationHelper {

    @ComponentImport
    private final GlobalPermissionManager globalPermissionManager;

    private final JiraUserHelper userHelper;

    /**
     * Constructor.
     *
     * @param userHelper              the injected {@link JiraUserHelper}
     * @param globalPermissionManager the injected {@link GlobalPermissionManager}
     */
    @Inject
    public JiraWebAuthenticationHelper(
            final GlobalPermissionManager globalPermissionManager,
            final JiraUserHelper userHelper) {

        this.globalPermissionManager = globalPermissionManager;
        this.userHelper = userHelper;
    }

    public void mustBeSysAdmin() {
        final ApplicationUser user = userHelper.getLoggedInUser();
        if (user == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        final boolean isSysAdmin = globalPermissionManager.hasPermission(SYSTEM_ADMIN, user);
        if (!isSysAdmin) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
