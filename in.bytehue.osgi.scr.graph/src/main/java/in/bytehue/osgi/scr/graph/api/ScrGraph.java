package in.bytehue.osgi.scr.graph.api;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.osgi.annotation.versioning.ProviderType;

/**
 * The {@link ScrGraph} service is the application access point to the
 * Service Component runtime (SCR) graph functionality. It is used to
 * create a graph from the SCR. It is also used to inspect the SCR
 * cycles. Since the API depends of JGraphT, this bundle also
 * packages the APIs of <b>JGraphT</b> so that you can make use of other
 * graph algorithms to perform your custom operations.
 *
 * <p>
 * Access to this service requires the
 * {@code ServicePermission[ScrGraph, GET]} permission. It is intended
 * that only administrative bundles should be granted this permission to limit
 * access to the potentially intrusive methods provided by this service.
 * </p>
 *
 * @noimplement This interface is not intended to be implemented by consumers.
 * @noextend This interface is not intended to be extended by consumers.
 *
 * @ThreadSafe
 */
@ProviderType
public interface ScrGraph {

    /**
     * Returns a graph from the Service Component Runtime (SCR).
     *
     * <p>
     * By default, it will not include any service reference which
     * is not exported using OSGi Declarative Services.
     *
     * <p>
     * <b>Note that</b>, this is an <b>idempotent operation</b> and
     * hence multiple invocations of this method will result in same results.
     *
     * @return the {@link Graph} instance
     *
     * @see #getGraph(boolean)
     */
    Graph<ScrComponent, DefaultEdge> getGraph();

    /**
     * Returns a graph from the Service Component Runtime (SCR).
     *
     * <p>
     * <b>Note that</b>, this is an <b>idempotent operation</b> and
     * hence multiple invocations of this method will result in same results.
     *
     * @param includeNonScrServiceReferences flag to include any service reference
     *            which is not exported using OSGi Declarative Services
     *
     * @return the {@link Graph} instance
     *
     * @see #getGraph()
     */
    Graph<ScrComponent, DefaultEdge> getGraph(boolean includeNonScrServiceReferences);

    /**
     * Returns a list containing all the list of components that
     * for a cycle (SCR).
     *
     * <p>
     * <b>Note that</b>, by default it makes use of Tarjan algorithm to
     * find out all the cycles
     *
     * <p>
     * <b>Also note that</b>, this is an <b>idempotent operation</b> and
     * hence multiple invocations of this method will result in same results.
     *
     * @return the {@link List} of all cycles (will never be {@code null})
     *
     * @see #getCycles(boolean)
     */
    List<List<ScrComponent>> getCycles();

    /**
     * Returns a list containing all the list of components that
     * for a cycle (SCR).
     *
     * <p>
     * <b>Note that</b>, by default it makes use of Tarjan algorithm to
     * find out all the cycles and it will not include any service reference
     * which is not exported using OSGi Declarative Services.
     *
     * <p>
     * <b>Also note that</b>, this is an <b>idempotent operation</b> and
     * hence multiple invocations of this method will result in same results.
     *
     * @param includeNonScrServiceReferences flag to include any service reference
     *            which is not exported using OSGi Declarative Services
     *
     * @return the {@link List} of all cycles (will never be {@code null})
     *
     * @see #getCycles()
     */
    List<List<ScrComponent>> getCycles(boolean includeNonScrServiceReferences);

    /**
     * Returns the graph containing all the cycles.
     *
     * <p>
     * <b>Note that</b>, by default it makes use of Tarjan algorithm to
     * find out all the cycles and it will not include any service reference
     * which is not exported using OSGi Declarative Services.
     *
     * <p>
     * <b>Also note that</b>, this is an <b>idempotent operation</b> and
     * hence multiple invocations of this method will result in same results.
     *
     * @return the {@link Graph} instance
     *
     * @see #getCyclesAsGraph(boolean)
     */
    Graph<ScrComponent, DefaultEdge> getCyclesAsGraph();

    /**
     * Returns the graph containing all the cycles.
     *
     * <p>
     * <b>Note that</b>, by default it makes use of Tarjan algorithm to
     * find out all the cycles.
     *
     * <p>
     * <b>Also note that</b>, this is an <b>idempotent operation</b> and
     * hence multiple invocations of this method will result in same results.
     *
     * @param includeNonScrServiceReferences flag to include any service reference
     *            which is not exported using OSGi Declarative Services
     *
     * @return the {@link Graph} instance
     *
     * @see #getCyclesAsGraph()
     */
    Graph<ScrComponent, DefaultEdge> getCyclesAsGraph(boolean includeNonScrServiceReferences);

}