package de.aservo.confapi.jira.filter;

import com.atlassian.plugins.rest.common.security.AuthenticationRequiredException;
import com.atlassian.plugins.rest.common.security.AuthorisationException;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SysadminOnlyResourceFilterTest {

    @Mock
    private UserManager userManager;

    private SysadminOnlyResourceFilter sysadminOnlyResourceFilter;

    @Before
    public void setup() {
        sysadminOnlyResourceFilter = new SysadminOnlyResourceFilter(userManager);
    }

    @Test
    public void testFilterDefaults() {
        assertNull(sysadminOnlyResourceFilter.getResponseFilter());
        assertEquals(sysadminOnlyResourceFilter, sysadminOnlyResourceFilter.getRequestFilter());
    }

    @Test(expected = AuthenticationRequiredException.class)
    public void testAdminAccessNoUser() {
        sysadminOnlyResourceFilter.filter(null);
    }

    @Test(expected = AuthorisationException.class)
    public void testNonSysadminAccess() {
        final UserProfile userProfile = mock(UserProfile.class);
        doReturn(userProfile).when(userManager).getRemoteUser();

        sysadminOnlyResourceFilter.filter(null);
    }

    @Test
    public void testSysadminAccess() {
        final UserProfile userProfile = mock(UserProfile.class);
        doReturn(new UserKey("user")).when(userProfile).getUserKey();
        doReturn(userProfile).when(userManager).getRemoteUser();
        doReturn(true).when(userManager).isSystemAdmin(any(UserKey.class));

        assertNull(sysadminOnlyResourceFilter.filter(null));
    }

}
