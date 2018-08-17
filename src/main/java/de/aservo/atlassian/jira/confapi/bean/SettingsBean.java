package de.aservo.atlassian.jira.confapi.bean;

import de.aservo.atlassian.jira.confapi.JiraApplicationHelper;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean for setting results in REST responses.
 */
@XmlRootElement(name = "settings")
public class SettingsBean {

    @XmlElement
    private final String baseurl;

    @XmlElement
    private final String mode;

    @XmlElement
    private final String title;

    /**
     * The default constructor is needed for JSON request deserialization.
     */
    public SettingsBean() {
        this.baseurl = null;
        this.mode = null;
        this.title = null;
    }

    public SettingsBean(
            final String baseUrl,
            final String mode,
            final String title) {

        this.baseurl = baseUrl;
        this.mode = mode;
        this.title = title;
    }

    public SettingsBean(
            final JiraApplicationHelper applicationHelper) {

        this.baseurl = applicationHelper.getBaseUrl();
        this.mode = applicationHelper.getMode();
        this.title = applicationHelper.getTitle();
    }

    public String getBaseUrl() {
        return baseurl;
    }

    public String getTitle() {
        return title;
    }

    public String getMode() {
        return mode;
    }

}
