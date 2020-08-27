package de.aservo.atlassian.jira.confapi.filter;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.rest.common.security.AuthenticationRequiredException;
import com.atlassian.plugins.rest.common.security.AuthorisationException;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;

/**
 * The Sysadmin only resource filter.
 */
@Provider
@Component
public class SysadminOnlyResourceFilter implements ResourceFilter, ContainerRequestFilter {

    @ComponentImport
    private final UserManager userManager;

    /**
     * Instantiates a new Sysadmin only resource filter.
     *
     * @param userManager           the user manager
     */
    @Inject
    public SysadminOnlyResourceFilter(
            final UserManager userManager) {

        this.userManager = userManager;
    }

    public ContainerRequestFilter getRequestFilter() {
        return this;
    }

    public ContainerResponseFilter getResponseFilter() {
        return null;
    }

    public ContainerRequest filter(
            final ContainerRequest containerRequest) {

        final UserProfile loggedInUser = userManager.getRemoteUser();

        if (loggedInUser == null) {
            throw new AuthenticationRequiredException();
        } else if (!userManager.isSystemAdmin(loggedInUser.getUserKey())) {
            throw new AuthorisationException("Client must be authenticated as an system administrator to access this resource.");
        }

        return containerRequest;
    }

}
