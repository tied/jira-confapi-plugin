package de.aservo.atlassian.jira.confapi.rest;

import com.atlassian.jira.rest.v2.issue.RESTException;
import de.aservo.atlassian.jira.confapi.JiraApplicationHelper;
import de.aservo.atlassian.jira.confapi.JiraWebAuthenticationHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

@RunWith(MockitoJUnitRunner.class)
public class LicensesResourceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private JiraApplicationHelper applicationHelper;

    @Mock
    private JiraWebAuthenticationHelper webAuthenticationHelper;

    private LicensesResource licenseResource;

    @Before
    public void setup() {
        /*
         * There is no need to setup an user an an admin, because he is assumed to be an admin as
         * long as no exception is thrown in webAuthenticationHelper.mustBeSysAdmin()
         */

        licenseResource = new LicensesResource(
                applicationHelper,
                webAuthenticationHelper);
    }

    @Test
    public void testSetNullLicense() {
        expectedException.expect(RESTException.class);

        final Response response = licenseResource.setLicense(null);
    }

}
