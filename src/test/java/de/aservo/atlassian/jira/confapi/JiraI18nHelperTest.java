package de.aservo.atlassian.jira.confapi;

import com.atlassian.jira.user.preferences.PreferenceKeys;
import com.atlassian.jira.web.bean.MockI18nBean;
import com.opensymphony.module.propertyset.PropertySet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JiraI18nHelperTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private JiraUserHelper userHelper;

    private JiraI18nHelper i18nHelper;

    @Before
    public void setup() {
        userHelper = spy(new MockJiraUserHelper());

        i18nHelper = new JiraI18nHelper(
                new MockI18nBean.MockI18nBeanFactory(),
                userHelper);
    }

    @Test
    public void test() {
        assertNull(i18nHelper.getText(null));
    }

    @Test
    public void testGetI18nHelper() {
        assertNotNull(i18nHelper.getI18nHelper());
    }

    @Test
    public void testGetLocaleDefault() {
        final Locale locale = i18nHelper.getLocale();
        assertThat(Locale.getDefault(), equalTo(locale));
    }

    @Test
    public void testGetLocaleFromUser() {
        final String localeTag = "en_GB";
        final PropertySet propertySet = mock(PropertySet.class);
        when(propertySet.getString(PreferenceKeys.USER_LOCALE)).thenReturn(localeTag);

        when(userHelper.getUserProperties()).thenReturn(propertySet);

        final Locale locale = i18nHelper.getLocale();
        assertThat(locale.toString(), equalToIgnoringCase(localeTag));
    }

}
