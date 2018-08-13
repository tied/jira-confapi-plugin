package de.aservo.atlassian.jira.confapi;

import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.rest.exception.ForbiddenWebException;
import com.atlassian.jira.rest.exception.NotAuthorisedWebException;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.MockApplicationUser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JiraWebAuthenticationHelperTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private JiraAuthenticationContext authenticationContext;

    @Mock
    private GlobalPermissionManager globalPermissionManager;

    private MockApplicationUser user = new MockApplicationUser("user");

    private JiraWebAuthenticationHelper webAuthenticationHelper;

    @Before
    public void setup() {
        webAuthenticationHelper = new JiraWebAuthenticationHelper(
                authenticationContext,
                globalPermissionManager);
    }

    @Test
    public void testMustBeAdminAsSysAdmin() {
        when(authenticationContext.getLoggedInUser()).thenReturn(user);
        when(globalPermissionManager.hasPermission(GlobalPermissionKey.SYSTEM_ADMIN, user)).thenReturn(true);

        // no exception may be thrown
        webAuthenticationHelper.mustBeSysAdmin();
    }

    @Test
    public void testMustBeAdminAsNonAdmin() {
        when(authenticationContext.getLoggedInUser()).thenReturn(user);

        expectedException.expect(ForbiddenWebException.class);
        webAuthenticationHelper.mustBeSysAdmin();
    }

    @Test
    public void testMustBeAdminWithNullUser() {
        when(authenticationContext.getLoggedInUser()).thenReturn(null);

        expectedException.expect(NotAuthorisedWebException.class);
        webAuthenticationHelper.mustBeSysAdmin();
    }

}
