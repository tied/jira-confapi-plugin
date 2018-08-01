package de.aservo.atlassian.jira.confapi.bean;

import com.atlassian.jira.license.LicenseDetails;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Bean for collection of {@link LicenseDetails} results in REST responses.
 */
@XmlRootElement(name = "licenses")
public class LicensesBean {

    @XmlElement
    private final Collection<LicenseBean> licenses;

    private LicensesBean(
            final Collection<LicenseBean> licenses) {

        this.licenses = licenses;
    }

    public Collection<LicenseBean> getLicenses() {
        return licenses;
    }

    /**
     * Factory method for creating a bean from a collection of {@link LicenseDetails}.
     *
     * @param licenseDetails the license details
     */
    public static LicensesBean from(
            final Collection<LicenseDetails> licenseDetails) {

        final Collection<LicenseBean> licenses = licenseDetails.stream()
                .map(LicenseBean::from)
                .collect(Collectors.toList());

        return new LicensesBean(licenses);
    }

}