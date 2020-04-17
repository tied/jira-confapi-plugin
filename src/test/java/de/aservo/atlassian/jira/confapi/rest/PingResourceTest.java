package de.aservo.atlassian.jira.confapi.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static de.aservo.atlassian.jira.confapi.rest.PingResourceImpl.PONG;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PingResourceTest {

    private PingResourceImpl pingResource;

    @Before
    public void setup() {
        pingResource = new PingResourceImpl();
    }

    @Test
    public void testGetPing() {
        final Response pingResponse = pingResource.getPing();
        assertEquals(PONG, pingResponse.getEntity().toString());
    }

}
