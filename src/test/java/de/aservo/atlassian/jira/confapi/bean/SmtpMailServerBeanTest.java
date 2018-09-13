package de.aservo.atlassian.jira.confapi.bean;

import com.atlassian.mail.MailProtocol;
import com.atlassian.mail.server.SMTPMailServer;
import com.atlassian.mail.server.impl.SMTPMailServerImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertNull;

public class SmtpMailServerBeanTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private SMTPMailServer smtpMailServer;

    @Before
    public void setup() {
        smtpMailServer = new SMTPMailServerImpl(
                null,
                "SMTP Mail Server",
                null,
                "jira@localhost",
                "JIRA",
                false,
                MailProtocol.SMTP,
                "localhost",
                "25",
                false,
                "username",
                "password"
        );
    }

    @Test
    public void testConstructNull() {
        final SmtpMailServerBean smtpMailServerBean = new SmtpMailServerBean();

        assertNull(smtpMailServerBean.getName());
        assertNull(smtpMailServerBean.getDescription());
        assertNull(smtpMailServerBean.getFrom());
        assertNull(smtpMailServerBean.getPrefix());
        assertNull(smtpMailServerBean.getProtocol());
        assertNull(smtpMailServerBean.getHost());
        assertNull(smtpMailServerBean.getPort());
        // TODO
        // assertThat(smtpMailServerBean.getTimeout(), equalTo(10000L));
        assertNull(smtpMailServerBean.getUsername());
        assertNull(smtpMailServerBean.getPassword());
    }

    @Test
    public void testFrom() throws Exception {

        final SmtpMailServerBean smtpMailServerBean = SmtpMailServerBean.from(smtpMailServer);

        assertThat(smtpMailServerBean.getName(), equalTo(
                smtpMailServer.getName()));
        assertThat(smtpMailServerBean.getDescription(), equalTo(
                smtpMailServer.getDescription()));
        assertThat(smtpMailServerBean.getProtocol(), equalToIgnoringCase(
                smtpMailServer.getMailProtocol().getProtocol()));
        assertThat(smtpMailServerBean.getHost(), equalTo(
                smtpMailServer.getHostname()));
        assertThat(String.valueOf(smtpMailServerBean.getPort()), equalTo(
                smtpMailServer.getPort()));
        assertThat(smtpMailServerBean.getTimeout(), equalTo(
                PopMailServerBean.DEFAULT_TIMEOUT));
        assertThat(smtpMailServerBean.getUsername(), equalTo(
                smtpMailServer.getUsername()));
        assertThat(smtpMailServerBean.getPassword(), equalTo(
                PopMailServerBean.PASSWORD_HIDDEN));
    }

}
