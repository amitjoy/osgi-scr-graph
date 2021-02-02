package in.bytehue.osgi.scr.graph.gogo;

import static in.bytehue.osgi.scr.graph.gogo.GraphScrCommand.PID;
import static java.util.Collections.emptyMap;
import static org.osgi.framework.namespace.PackageNamespace.PACKAGE_NAMESPACE;

import java.io.IOException;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component
public final class GogoCommandActivator {

    private static final String GOGO_PACKAGE = "org.apache.felix.service.command";

    private final BundleContext bundleContext;
    private final ConfigurationAdmin configAdmin;

    // @formatter:off
    @Activate
    public GogoCommandActivator(
            final BundleContext bundleContext,
            @Reference
            final ConfigurationAdmin configAdmin) {

        this.configAdmin = configAdmin;
        this.bundleContext = bundleContext;

        if (isGogoPackageImported()) {
            createGogoCommandConfig();
        }
    }
    // @formatter:on

    private boolean isGogoPackageImported() {
        final BundleWiring wiring = bundleContext.getBundle().adapt(BundleWiring.class);
        for (final BundleWire wire : wiring.getRequiredWires(PACKAGE_NAMESPACE)) {
            final String pkg = (String) wire.getCapability().getAttributes().get(PACKAGE_NAMESPACE);
            if (pkg.equals(GOGO_PACKAGE)) {
                return true;
            }
        }
        return false;
    }

    private void createGogoCommandConfig() {
        try {
            final Configuration configuration = configAdmin.getConfiguration(PID, "?");
            configuration.updateIfDifferent(new Hashtable<>(emptyMap()));
        } catch (final IOException e) {
            // ignore due to location check as it's never gonna happen
        }
    }

    @Deactivate
    private void deleteGogoCommandConfig() {
        try {
            final Configuration configuration = configAdmin.getConfiguration(PID, "?");
            configuration.delete();
        } catch (final IOException e) {
            // ignore due to location check as it's never gonna happen
        }
    }

}