package de.aservo.atlassian.jira.confapi;

import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.user.MockApplicationUser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JiraWebAuthenticationHelperTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private GlobalPermissionManager globalPermissionManager;

    @Mock
    private JiraUserHelper userHelper;

    private MockApplicationUser user = new MockApplicationUser("user");

    private JiraWebAuthenticationHelper webAuthenticationHelper;

    @Before
    public void setup() {
        webAuthenticationHelper = new JiraWebAuthenticationHelper(
                globalPermissionManager,
                userHelper);
    }

    @Test
    public void testMustBeAdminAsSysAdmin() {
        when(userHelper.getLoggedInUser()).thenReturn(user);
        when(globalPermissionManager.hasPermission(GlobalPermissionKey.SYSTEM_ADMIN, user)).thenReturn(true);

        // no exception may be thrown
        webAuthenticationHelper.mustBeSysAdmin();
    }

    @Test
    public void testMustBeAdminAsNonAdmin() {
        when(userHelper.getLoggedInUser()).thenReturn(user);

        expectedException.expect(WebApplicationException.class);
        webAuthenticationHelper.mustBeSysAdmin();
    }

    @Test
    public void testMustBeAdminWithNullUser() {
        when(userHelper.getLoggedInUser()).thenReturn(null);

        expectedException.expect(WebApplicationException.class);
        webAuthenticationHelper.mustBeSysAdmin();
    }

}
