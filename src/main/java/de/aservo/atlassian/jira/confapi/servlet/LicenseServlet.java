package de.aservo.atlassian.jira.confapi.servlet;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.upm.api.license.PluginLicenseManager;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LicenseServlet extends HttpServlet {

    @ComponentImport
    private final PluginLicenseManager pluginLicenseManager;

    @Inject
    public LicenseServlet(
            final PluginLicenseManager pluginLicenseManager) {

        this.pluginLicenseManager = pluginLicenseManager;
    }

    @Override
    protected void doGet(
            final HttpServletRequest req,
            final HttpServletResponse resp) throws IOException {

        final PrintWriter w = resp.getWriter();

        if (pluginLicenseManager.getLicense().isDefined()) {
            w.println(pluginLicenseManager.getLicense().get().getRawLicense());
        } else {
            w.println("License missing!");
        }

        w.close();
    }

}
