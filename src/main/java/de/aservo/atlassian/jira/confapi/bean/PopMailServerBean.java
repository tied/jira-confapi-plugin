package de.aservo.atlassian.jira.confapi.bean;

import com.atlassian.mail.MailProtocol;
import com.atlassian.mail.server.PopMailServer;
import com.atlassian.mail.server.impl.PopMailServerImpl;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean for POP / IMAP mail server in REST requests.
 */
@XmlRootElement(name = "pop")
public class PopMailServerBean {

    public static final String PASSWORD_HIDDEN = "<HIDDEN>";
    public static final Long DEFAULT_TIMEOUT = 10000L;
    public static final MailProtocol DEFAULT_PROTOCOL = MailProtocol.POP;

    @XmlElement
    private final String name;

    @XmlElement
    private final String description;

    @XmlElement
    private final String protocol;

    @XmlElement
    private final String host;

    @XmlElement
    private final Integer port;

    @XmlElement
    private final long timeout;

    @XmlElement
    private final String username;

    @XmlElement
    private final String password;

    /**
     * The default constructor is needed for JSON request deserialization.
     */
    public PopMailServerBean() {
        this.name = null;
        this.description = null;
        this.protocol = null;
        this.host = null;
        this.port = null;
        this.timeout = DEFAULT_TIMEOUT;
        this.username = null;
        this.password = null;
    }

    private PopMailServerBean(
            final String name,
            final String description,
            final String protocol,
            final String host,
            final Integer port,
            final long timeout,
            final String username,
            final String password) {

        this.name = name;
        this.description = StringUtils.isNoneBlank(description)
                ? description
                : null;
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.username = username;
        this.password = StringUtils.isNotBlank(password)
                ? PASSWORD_HIDDEN
                : null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getHost() {
        return host;
    }

    public String getProtocol() {
        return protocol;
    }

    public Integer getPort() {
        return port;
    }

    public long getTimeout() {
        return timeout;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // TODO: More specific exception
    public static PopMailServerBean from(
            final PopMailServer popMailServer) throws Exception {

        if (popMailServer == null) {
            throw new Exception("No POP mail server defined");
        }

        Integer port = null;

        if (StringUtils.isNotBlank(popMailServer.getPort())) {
            port = Integer.parseInt(popMailServer.getPort());
        }

        return new PopMailServerBean(
                popMailServer.getName(),
                popMailServer.getDescription(),
                popMailServer.getMailProtocol().getProtocol(),
                popMailServer.getHostname(),
                port,
                popMailServer.getTimeout(),
                popMailServer.getUsername(),
                popMailServer.getPassword());
    }

    public PopMailServer toPopMailServer() {
        MailProtocol mailProtocol = null;

        if (StringUtils.isNotBlank(protocol)) {
            mailProtocol = MailProtocol.getMailProtocol(protocol.toLowerCase());
        }

        if (mailProtocol == null) {
            mailProtocol = DEFAULT_PROTOCOL;
        }

        String mailPort;

        if (port != null) {
            mailPort = String.valueOf(port);
        } else {
            mailPort = mailProtocol.getDefaultPort();
        }

        return new PopMailServerImpl(
                null,
                name,
                description,
                mailProtocol,
                host,
                mailPort,
                null,
                null,
                timeout);
    }

}
