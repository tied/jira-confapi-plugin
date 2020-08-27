package de.aservo.confapi.jira.service;

import com.atlassian.jira.mock.security.MockAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.MockApplicationUser;
import com.atlassian.jira.user.MockUserPropertyManager;
import com.opensymphony.module.propertyset.PropertySet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class JiraUserHelperTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ApplicationUser user = new MockApplicationUser("user");

    private JiraUserHelper userHelper;

    @Before
    public void setup() {
        userHelper = new JiraUserHelper(
                new MockAuthenticationContext(user),
                new MockUserPropertyManager());
    }

    @Test
    public void testGetLoggedInUser() {
        assertThat(userHelper.getLoggedInUser(), equalTo(user));
    }

    @Test
    public void testGetPropertySetOfLoggedInUser() {
        final PropertySet propertySet = userHelper.getUserProperties(user);

        assertThat(userHelper.getUserProperties(), equalTo(propertySet));
    }

    @Test
    public void testGetPropertySetOfNullUser() {
        assertNull(userHelper.getUserProperties(null));
    }

}
