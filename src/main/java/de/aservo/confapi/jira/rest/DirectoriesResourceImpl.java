package de.aservo.confapi.jira.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.rest.AbstractDirectoriesResourceImpl;
import de.aservo.confapi.commons.service.api.DirectoryService;
import de.aservo.confapi.jira.filter.SysadminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(ConfAPI.DIRECTORIES)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class DirectoriesResourceImpl extends AbstractDirectoriesResourceImpl {

    @Inject
    public DirectoriesResourceImpl(DirectoryService directoryService) {
        super(directoryService);
    }

}
