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
package in.bytehue.osgi.scr.graph.provider;

import static java.util.stream.Collectors.toList;
import static org.osgi.service.component.runtime.dto.ComponentConfigurationDTO.ACTIVE;
import static org.osgi.service.component.runtime.dto.ComponentConfigurationDTO.SATISFIED;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.osgi.framework.dto.ServiceReferenceDTO;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.runtime.ServiceComponentRuntime;
import org.osgi.service.component.runtime.dto.ComponentConfigurationDTO;
import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;
import org.osgi.service.component.runtime.dto.SatisfiedReferenceDTO;

import in.bytehue.osgi.scr.graph.api.ScrComponent;
import in.bytehue.osgi.scr.graph.api.ScrGraph;

@Component
public final class ScrGraphProvider implements ScrGraph {

    @Reference
    private ServiceComponentRuntime scr;

    @Override
    public Graph<ScrComponent, DefaultEdge> getGraph() {
        return getGraph(false);
    }

    @Override
    public Graph<ScrComponent, DefaultEdge> getGraph(final boolean includeNonScrServiceReferences) {
        final List<ScrComponent> components = new ArrayList<>();
        final List<Pair<ScrComponent, ScrComponent>> edges = new ArrayList<>();

        prepareComponents(components);
        prepareEdges(components, edges, includeNonScrServiceReferences);
        return buildGraph(components, edges);
    }

    @Override
    public List<List<ScrComponent>> getCycles() {
        return getCycles(false);
    }

    @Override
    public List<List<ScrComponent>> getCycles(final boolean includeNonScrServiceReferences) {
        final Graph<ScrComponent, DefaultEdge> graph = getGraph(includeNonScrServiceReferences);
        final TarjanSimpleCycles<ScrComponent, DefaultEdge> tarjan = new TarjanSimpleCycles<>(graph);
        return tarjan.findSimpleCycles();
    }

    @Override
    public Graph<ScrComponent, DefaultEdge> getCyclesAsGraph() {
        return getCyclesAsGraph(false);
    }

    @Override
    public Graph<ScrComponent, DefaultEdge> getCyclesAsGraph(final boolean includeNonScrServiceReferences) {
        final List<List<ScrComponent>> cycles = getCycles(includeNonScrServiceReferences);
        final List<Pair<ScrComponent, ScrComponent>> edges = new ArrayList<>();

        for (final List<ScrComponent> group : cycles) {
            for (int i = 0, j = 0; i < group.size(); j = ++i) {
                final Pair<ScrComponent, ScrComponent> pair = new Pair<>(group.get(i), group.get(j));
                edges.add(pair);
                if (j == group.size()) {
                    final Pair<ScrComponent, ScrComponent> cycle = new Pair<>(group.get(j), group.get(0));
                    edges.add(cycle);
                }
            }
        }
        return buildGraph(cycles.stream().flatMap(List::stream).collect(toList()), edges);
    }

    @Override
    public String exportAsDOT(final Graph<ScrComponent, DefaultEdge> graph) {
        final DOTExporter<ScrComponent, DefaultEdge> exporter = new DOTExporter<>();
        exporter.setVertexAttributeProvider(v -> {
            final Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(createGraphLabel(v)));
            return map;
        });
        final Writer writer = new StringWriter();
        exporter.exportGraph(graph, writer);
        return writer.toString();
    }

    private String createGraphLabel(final ScrComponent component) {
        if (component.description != null) {
            return component.description.name + " [" + component.configuration.id + "]";
        }
        return String.valueOf(component.reference.id);
    }

    private void prepareComponents(final List<ScrComponent> components) {
        for (final ComponentDescriptionDTO desc : scr.getComponentDescriptionDTOs()) {
            for (final ComponentConfigurationDTO configurationDTO : scr.getComponentConfigurationDTOs(desc)) {
                if (configurationDTO.state == SATISFIED || configurationDTO.state == ACTIVE) {
                    final ScrComponent componentName = createComponent( //
                            desc, //
                            configurationDTO, //
                            configurationDTO.service); //
                    components.add(componentName);
                }
            }
        }
    }

    private void prepareEdges( //
            final List<ScrComponent> components, //
            final List<Pair<ScrComponent, ScrComponent>> edges, //
            final boolean includeNonScrServiceReferences) {

        final String COMPONENT_NAME_PROPERTY = "component.name";

        for (final ScrComponent component : components) {
            final ComponentConfigurationDTO dto = component.configuration;
            final SatisfiedReferenceDTO[] services = dto.satisfiedReferences;

            for (final SatisfiedReferenceDTO refDTO : services) {
                final ServiceReferenceDTO[] srvRefDTOs = refDTO.boundServices;

                for (final ServiceReferenceDTO srvRefDTO : srvRefDTOs) {
                    final String componentNameProperty = (String) srvRefDTO.properties.get(COMPONENT_NAME_PROPERTY);
                    ScrComponent endComponent = null;
                    if (componentNameProperty == null) { // not an SCR
                        if (includeNonScrServiceReferences) {
                            endComponent = createComponent(null, null, srvRefDTO);
                        } else {
                            break;
                        }
                    } else {
                        endComponent = findComponentByName(components, componentNameProperty);
                    }
                    edges.add(new Pair<>(component, endComponent));
                }
            }
        }
    }

    private Graph<ScrComponent, DefaultEdge> buildGraph( //
            final List<ScrComponent> components, //
            final List<Pair<ScrComponent, ScrComponent>> edges) {

        final Graph<ScrComponent, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        components.forEach(graph::addVertex);
        edges.forEach(edge -> graph.addEdge(edge.getFirst(), edge.getSecond()));
        return graph;
    }

    private ScrComponent createComponent( //
            final ComponentDescriptionDTO description, //
            final ComponentConfigurationDTO configuration, //
            final ServiceReferenceDTO reference) {

        final ScrComponent component = new ScrComponent();
        component.description = description;
        component.configuration = configuration;
        component.reference = reference;

        return component;
    }

    private ScrComponent findComponentByName(final List<ScrComponent> components, final String componentName) {
        return components.stream() //
                .filter(c -> componentName.equals(c.description.name)) //
                .findAny() //
                .orElse(null);
    }

}
