package de.aservo.atlassian.jira.confapi.rest;

import com.atlassian.jira.rest.api.util.ErrorCollection;
import com.atlassian.mail.MailException;
import com.atlassian.mail.MailProtocol;
import com.atlassian.mail.server.MailServerManager;
import com.atlassian.mail.server.PopMailServer;
import com.atlassian.mail.server.SMTPMailServer;
import com.atlassian.mail.server.impl.SMTPMailServerImpl;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import de.aservo.atlassian.jira.confapi.JiraWebAuthenticationHelper;
import de.aservo.atlassian.jira.confapi.bean.PopMailServerBean;
import de.aservo.atlassian.jira.confapi.bean.SmtpMailServerBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Mail resource to set mail configuration.
 */
@Path("/mail")
@AnonymousAllowed
@Produces(MediaType.APPLICATION_JSON)
@Component
public class MailServerResource {

    private static final Logger log = LoggerFactory.getLogger(MailServerResource.class);

    @ComponentImport
    private final MailServerManager mailServerManager;

    private final JiraWebAuthenticationHelper webAuthenticationHelper;

    /**
     * Constructor.
     *
     * @param mailServerManager       the injected {@link MailServerManager}
     * @param webAuthenticationHelper the injected {@link JiraWebAuthenticationHelper}
     */
    @Inject
    public MailServerResource(
            final MailServerManager mailServerManager,
            final JiraWebAuthenticationHelper webAuthenticationHelper) {

        this.mailServerManager = mailServerManager;
        this.webAuthenticationHelper = webAuthenticationHelper;
    }

    @GET
    @Path("smtp")
    public Response getMailSmtp() {
        webAuthenticationHelper.mustBeSysAdmin();

        final ErrorCollection errorCollection = ErrorCollection.of();

        try {
            final SMTPMailServer smtpMailServer = mailServerManager.getDefaultSMTPMailServer();
            final SmtpMailServerBean bean = SmtpMailServerBean.from(smtpMailServer);
            return Response.ok(bean).build();
        } catch (Exception e) {
            errorCollection.addErrorMessage(e.getMessage());
            log.error(e.getMessage(), e);
        }

        return Response.status(Response.Status.NOT_FOUND).entity(errorCollection).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setMail(
            final SmtpMailServerBean bean
    ) {

        webAuthenticationHelper.mustBeSysAdmin();

        final ErrorCollection errorCollection = ErrorCollection.of();

        final SMTPMailServer smtpMailServer = mailServerManager.isDefaultSMTPMailServerDefined()
                ? mailServerManager.getDefaultSMTPMailServer()
                : new SMTPMailServerImpl();

        if (smtpMailServer == null) {
            return Response.serverError().build();
        }

        if (StringUtils.isNotBlank(bean.getName())) {
            smtpMailServer.setName(bean.getName());
        }

        if (StringUtils.isNotBlank(bean.getFrom())) {
            smtpMailServer.setDefaultFrom(bean.getFrom());
        }

        if (StringUtils.isNotBlank(bean.getPrefix())) {
            smtpMailServer.setPrefix(bean.getPrefix());
        }

        if (StringUtils.isNotBlank(bean.getHost())) {
            smtpMailServer.setHostname(bean.getHost());
        }

        smtpMailServer.setMailProtocol(MailProtocol.SMTP);
        smtpMailServer.setPort("25");
        smtpMailServer.setTlsRequired(false);
        smtpMailServer.setTimeout(10000L);

        try {
            if (mailServerManager.isDefaultSMTPMailServerDefined()) {
                mailServerManager.update(smtpMailServer);
            } else {
                smtpMailServer.setId(mailServerManager.create(smtpMailServer));
            }
        } catch (MailException e) {
            e.printStackTrace();
        }

        return Response.ok(errorCollection).build();
    }

    @GET
    @Path("pop")
    public Response getPopMailServer() {
        webAuthenticationHelper.mustBeSysAdmin();

        final ErrorCollection errorCollection = ErrorCollection.of();

        try {
            final PopMailServer popMailServer = mailServerManager.getDefaultPopMailServer();
            final PopMailServerBean bean = PopMailServerBean.from(popMailServer);
            return Response.ok(bean).build();
        } catch (Exception e) {
            errorCollection.addErrorMessage(e.getMessage());
            log.error(e.getMessage(), e);
        }

        return Response.status(Response.Status.NOT_FOUND).entity(errorCollection).build();
    }

}
