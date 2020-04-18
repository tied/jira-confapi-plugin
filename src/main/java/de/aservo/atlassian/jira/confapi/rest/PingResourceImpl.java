package de.aservo.atlassian.jira.confapi.rest;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.rest.api.PingResource;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(ConfAPI.PING)
@AnonymousAllowed
public class PingResourceImpl implements PingResource {

    @Override
    public Response getPing() {
        return Response.ok(PONG).build();
    }

}
