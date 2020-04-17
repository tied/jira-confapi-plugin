package de.aservo.atlassian.jira.confapi.rest;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import de.aservo.atlassian.confapi.constants.ConfAPI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(ConfAPI.PING)
@AnonymousAllowed
@Produces({MediaType.APPLICATION_JSON})
public class PingResource {

    public static final String PONG = "pong";

    /**
     * Simple ping method for probing the REST api. Returns 'pong' upon success
     *
     * @return response
     */
    @GET
    public Response get() {
        return Response.ok(PONG).build();
    }

}
