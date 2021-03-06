package de.aservo.confapi.jira.rest;

import com.atlassian.mail.MailException;
import com.atlassian.mail.MailProtocol;
import com.atlassian.mail.server.MailServerManager;
import com.atlassian.mail.server.PopMailServer;
import com.atlassian.mail.server.SMTPMailServer;
import com.atlassian.mail.server.impl.PopMailServerImpl;
import com.atlassian.mail.server.impl.SMTPMailServerImpl;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.ErrorCollection;
import de.aservo.confapi.commons.model.MailServerPopBean;
import de.aservo.confapi.commons.model.MailServerSmtpBean;
import de.aservo.confapi.commons.rest.api.MailServerResource;
import de.aservo.confapi.jira.filter.SysadminOnlyResourceFilter;
import de.aservo.confapi.jira.model.util.MailServerPopBeanUtil;
import de.aservo.confapi.jira.model.util.MailServerSmtpBeanUtil;
import de.aservo.confapi.jira.util.MailProtocolUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(ConfAPI.MAIL_SERVER)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class MailServerResourceImpl implements MailServerResource {

    private static final Logger log = LoggerFactory.getLogger(MailServerResourceImpl.class);

    @ComponentImport
    private final MailServerManager mailServerManager;

    @Inject
    public MailServerResourceImpl(
            final MailServerManager mailServerManager) {

        this.mailServerManager = mailServerManager;
    }

    @Override
    public Response getMailServerPop() {
        final PopMailServer popMailServer = mailServerManager.getDefaultPopMailServer();
        final MailServerPopBean bean = MailServerPopBeanUtil.toMailServerPopBean(popMailServer);

        if (bean != null) {
            return Response.ok(bean).build();
        }

        final ErrorCollection errorCollection = new ErrorCollection();
        errorCollection.addErrorMessage("No default POP mail server configured");
        return Response.status(Response.Status.NO_CONTENT).entity(errorCollection).build();
    }

    @Override
    public Response setMailServerPop(
            @Nonnull final MailServerPopBean bean) {

        final ErrorCollection errorCollection = new ErrorCollection();

        final PopMailServer popMailServer = mailServerManager.getDefaultPopMailServer() != null
                ? mailServerManager.getDefaultPopMailServer()
                : new PopMailServerImpl();

        assert popMailServer != null;

        if (StringUtils.isNotBlank(bean.getName())) {
            popMailServer.setName(bean.getName());
        }

        if (StringUtils.isNotBlank(bean.getDescription())) {
            popMailServer.setDescription(bean.getDescription());
        }

        popMailServer.setMailProtocol(MailProtocolUtil.find(bean.getProtocol(), MailProtocol.POP));

        if (StringUtils.isNotBlank(bean.getHost())) {
            popMailServer.setHostname(bean.getHost());
        }

        if (bean.getPort() != null) {
            popMailServer.setPort(String.valueOf(bean.getPort()));
        } else {
            popMailServer.setPort(popMailServer.getMailProtocol().getDefaultPort());
        }

        if (StringUtils.isNotBlank(bean.getUsername())) {
            popMailServer.setUsername(bean.getUsername());
        }

        popMailServer.setTimeout(bean.getTimeout());

        try {
            if (mailServerManager.getDefaultPopMailServer() != null) {
                mailServerManager.update(popMailServer);
            } else {
                popMailServer.setId(mailServerManager.create(popMailServer));
            }

            return Response.ok(bean).build();
        } catch (MailException e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(errorCollection).build();
    }

    @Override
    public Response getMailServerSmtp() {
        final SMTPMailServer smtpMailServer = mailServerManager.getDefaultSMTPMailServer();
        final MailServerSmtpBean bean = MailServerSmtpBeanUtil.toMailServerSmtpBean(smtpMailServer);

        if (bean != null) {
            return Response.ok(bean).build();
        }

        final ErrorCollection errorCollection = new ErrorCollection();
        errorCollection.addErrorMessage("No default SMTP mail server configured");
        return Response.status(Response.Status.NO_CONTENT).entity(errorCollection).build();
    }

    @Override
    public Response setMailServerSmtp(
            @Nonnull final MailServerSmtpBean bean) {

        final ErrorCollection errorCollection = new ErrorCollection();

        final SMTPMailServer smtpMailServer = mailServerManager.isDefaultSMTPMailServerDefined()
                ? mailServerManager.getDefaultSMTPMailServer()
                : new SMTPMailServerImpl();

        assert smtpMailServer != null;

        if (StringUtils.isNotBlank(bean.getName())) {
            smtpMailServer.setName(bean.getName());
        }

        if (StringUtils.isNotBlank(bean.getDescription())) {
            smtpMailServer.setDescription(bean.getDescription());
        }

        if (StringUtils.isNotBlank(bean.getFrom())) {
            smtpMailServer.setDefaultFrom(bean.getFrom());
        }

        if (StringUtils.isNotBlank(bean.getPrefix())) {
            smtpMailServer.setPrefix(bean.getPrefix());
        }

        smtpMailServer.setMailProtocol(MailProtocolUtil.find(bean.getProtocol(), MailProtocol.SMTP));

        if (StringUtils.isNotBlank(bean.getHost())) {
            smtpMailServer.setHostname(bean.getHost());
        }

        if (bean.getPort() != null) {
            smtpMailServer.setPort(String.valueOf(bean.getPort()));
        } else {
            smtpMailServer.setPort(smtpMailServer.getMailProtocol().getDefaultPort());
        }

        smtpMailServer.setTlsRequired(bean.isTls());

        if (StringUtils.isNotBlank(bean.getUsername())) {
            smtpMailServer.setUsername(bean.getUsername());
        }

        smtpMailServer.setTimeout(bean.getTimeout());

        try {
            if (mailServerManager.isDefaultSMTPMailServerDefined()) {
                mailServerManager.update(smtpMailServer);
            } else {
                smtpMailServer.setId(mailServerManager.create(smtpMailServer));
            }

            return Response.ok(bean).build();
        } catch (MailException e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(errorCollection).build();
    }

}
