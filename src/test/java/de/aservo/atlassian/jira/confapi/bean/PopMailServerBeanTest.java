package de.aservo.atlassian.jira.confapi.bean;

import com.atlassian.mail.MailProtocol;
import com.atlassian.mail.server.PopMailServer;
import com.atlassian.mail.server.impl.PopMailServerImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertNull;

public class PopMailServerBeanTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private PopMailServer popMailServer;

    @Before
    public void setup() {
        popMailServer = new PopMailServerImpl(
                null,
                "Pop Mail Server",
                null,
                MailProtocol.POP,
                "localhost",
                "110",
                "username",
                "password"
        );
    }

    @Test
    public void testConstructNull() {
        final PopMailServerBean popMailServerBean = new PopMailServerBean();

        assertNull(popMailServerBean.getName());
        assertNull(popMailServerBean.getDescription());
        assertNull(popMailServerBean.getProtocol());
        assertNull(popMailServerBean.getHost());
        assertNull(popMailServerBean.getPort());
        assertThat(popMailServerBean.getTimeout(), equalTo(10000L));
        assertNull(popMailServerBean.getUsername());
        assertNull(popMailServerBean.getPassword());
    }

    @Test
    public void testFrom() throws Exception {
        final PopMailServerBean popMailServerBean = PopMailServerBean.from(popMailServer);

        assertThat(popMailServerBean.getName(), equalTo(
                popMailServer.getName()));
        assertThat(popMailServerBean.getDescription(), equalTo(
                popMailServer.getDescription()));
        assertThat(popMailServerBean.getProtocol(), equalToIgnoringCase(
                popMailServer.getMailProtocol().getProtocol()));
        assertThat(popMailServerBean.getHost(), equalTo(
                popMailServer.getHostname()));
        assertThat(String.valueOf(popMailServerBean.getPort()), equalTo(
                popMailServer.getPort()));
        assertThat(popMailServerBean.getTimeout(), equalTo(
                PopMailServerBean.DEFAULT_TIMEOUT));
        assertThat(popMailServerBean.getUsername(), equalTo(
                popMailServer.getUsername()));
        assertThat(popMailServerBean.getPassword(), equalTo(
                PopMailServerBean.PASSWORD_HIDDEN));
    }

    @Test
    public void testFromWithNullParams() throws Exception {
        popMailServer.setDescription("");
        popMailServer.setPassword(null);

        final PopMailServerBean popMailServerBean = PopMailServerBean.from(popMailServer);

        assertNull(popMailServerBean.getDescription());
        assertNull(popMailServerBean.getPassword());
    }

    @Test
    public void testFromNull() throws Exception {
        expectedException.expect(Exception.class);
        final PopMailServerBean popMailServerBean = PopMailServerBean.from(null);
    }

    @Test
    public void testToPopMailServer() throws Exception {
        final PopMailServerBean popMailServerBean = PopMailServerBean.from(popMailServer);
        final PopMailServer toPopMailServer = popMailServerBean.toPopMailServer();

        assertThat(toPopMailServer.getName(), equalTo(popMailServer.getName()));
    }

}
