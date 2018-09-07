package de.aservo.atlassian.jira.confapi.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean for mail servers in REST requests.
 */
@XmlRootElement(name = "mail")
public class MailServerBean {

    @XmlElement
    private final SmtpMailServerBean smtp;

    @XmlElement
    private final PopMailServerBean pop;

    /**
     * The default constructor is needed for JSON request deserialization.
     */
    public MailServerBean() {
        this.smtp = null;
        this.pop = null;
    }

}
