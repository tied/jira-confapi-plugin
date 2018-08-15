package de.aservo.atlassian.jira.confapi;

import com.atlassian.jira.user.preferences.PreferenceKeys;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.opensymphony.module.propertyset.PropertySet;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Locale;

@Component
public class JiraI18nHelper {

    @ComponentImport
    private final I18nHelper.BeanFactory i18nBeanFactory;

    private final JiraUserHelper userHelper;

    /**
     * Constructor.
     *
     * @param i18nBeanFactory injected {@link I18nHelper.BeanFactory}
     * @param userHelper      injected {@link JiraUserHelper}
     */
    @Inject
    public JiraI18nHelper(
            final I18nHelper.BeanFactory i18nBeanFactory,
            final JiraUserHelper userHelper) {

        this.i18nBeanFactory = i18nBeanFactory;
        this.userHelper = userHelper;
    }

    public String getText(
            final String key) {

        return getI18nHelper().getText(key);
    }

    public I18nHelper getI18nHelper() {
        return i18nBeanFactory.getInstance(getLocale());
    }

    private Locale getLocale() {
        final PropertySet userProperties = userHelper.getUserProperties();

        if (userProperties != null) {
            final String locale = userProperties.getString(PreferenceKeys.USER_LOCALE);

            if (locale != null) {
                return new Locale(locale);
            }
        }

        return Locale.getDefault();
    }

}
