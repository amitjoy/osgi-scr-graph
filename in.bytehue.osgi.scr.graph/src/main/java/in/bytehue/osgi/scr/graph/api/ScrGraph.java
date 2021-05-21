/*******************************************************************************
 * Copyright 2021 Amit Kumar Mondal
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package in.bytehue.osgi.scr.graph.api;

import java.io.Writer;
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
     * <b>Note that</b>,, it will not include any service reference which
     * is not exported using OSGi Declarative Services.
     *
     * <p>
     * <b>Also note that</b>, this is an <b>idempotent operation</b> and
     * hence multiple invocations of this method will result in same results.
     *
     * @return the {@link Graph} instance
     */
    Graph<ScrComponent, DefaultEdge> getGraph();

    /**
     * Returns a list containing all the list of components that
     * for a cycle (SCR).
     *
     * <p>
     * <b>Note that</b>, by default it makes use of Tarjan algorithm to
     * find out all the cycles and it will not include any service reference
     * which is not exported using OSGi Declarative Services
     *
     * <p>
     * <b>Also note that</b>, this is an <b>idempotent operation</b> and
     * hence multiple invocations of this method will result in same results.
     *
     * @return the {@link List} of all cycles (will never be {@code null})
     */
    List<List<ScrComponent>> getCycles();

    /**
     * Returns the graph cycle containing the specified components
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
     * @param components the components that are part of the cycle
     *
     * @return the {@link Graph} instance
     *
     * @see #getCycles()
     * @see #getCyclesAsGraph()
     */
    Graph<ScrComponent, DefaultEdge> getCycleAsGraph(List<ScrComponent> components);

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
     * @see #getCycles()
     */
    Graph<ScrComponent, DefaultEdge> getCyclesAsGraph();

    /**
     * Exports the specified graph into DOT representation.
     *
     * @param graph the graph to be exported
     * @param writer the writer to which the graph to be exported
     */
    void exportGraph(Graph<ScrComponent, DefaultEdge> graph, Writer writer);

}
