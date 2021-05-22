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

import static in.bytehue.osgi.scr.graph.provider.ScrGraphHelper.createVertexLabel;
import static java.util.stream.Collectors.toList;
import static org.jgrapht.nio.DefaultAttribute.createAttribute;
import static org.osgi.service.component.runtime.dto.ComponentConfigurationDTO.ACTIVE;
import static org.osgi.service.component.runtime.dto.ComponentConfigurationDTO.SATISFIED;

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
import in.bytehue.osgi.scr.graph.provider.ScrGraphHelper.CircularLinkedList;
import in.bytehue.osgi.scr.graph.provider.ScrGraphHelper.CircularLinkedList.Node;

@Component
public final class ScrGraphProvider implements ScrGraph {

    @Reference
    private ServiceComponentRuntime scr;

    @Override
    public Graph<ScrComponent, DefaultEdge> getGraph() {

        final List<ScrComponent> components = new ArrayList<>();
        final List<Pair<ScrComponent, ScrComponent>> edges = new ArrayList<>();

        prepareComponents(components);
        prepareEdges(components, edges);

        return buildGraph(components, edges);
    }

    @Override
    public List<List<ScrComponent>> getCycles() {

        final Graph<ScrComponent, DefaultEdge> graph = getGraph();
        final TarjanSimpleCycles<ScrComponent, DefaultEdge> tarjan = new TarjanSimpleCycles<>(graph);

        return tarjan.findSimpleCycles();
    }

    @Override
    public Graph<ScrComponent, DefaultEdge> getCycleAsGraph(final List<ScrComponent> components) {

        final List<Pair<ScrComponent, ScrComponent>> edges = new ArrayList<>();
        Node<ScrComponent> node = CircularLinkedList.create(components);

        for (int i = 0; i < components.size(); i++) {
            node = node.getNext();
            final Pair<ScrComponent, ScrComponent> pair = new Pair<>(node.getData(), node.getNext().getData());
            edges.add(pair);
        }
        return buildGraph(components, edges);
    }

    @Override
    public Graph<ScrComponent, DefaultEdge> getCyclesAsGraph() {

        final List<List<ScrComponent>> cycles = getCycles();
        final List<Pair<ScrComponent, ScrComponent>> edges = new ArrayList<>();

        for (final List<ScrComponent> group : cycles) {
            Node<ScrComponent> node = CircularLinkedList.create(group);
            for (int i = 0; i < group.size(); i++) {
                node = node.getNext();
                final Pair<ScrComponent, ScrComponent> pair = new Pair<>(node.getData(), node.getNext().getData());
                edges.add(pair);
            }
        }
        return buildGraph(cycles.stream().flatMap(List::stream).collect(toList()), edges);
    }

    @Override
    public void exportGraph(final Graph<ScrComponent, DefaultEdge> graph, final Writer writer) {
        final DOTExporter<ScrComponent, DefaultEdge> exporter = new DOTExporter<>();

        exporter.setVertexAttributeProvider(v -> {
            final Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", createAttribute(createVertexLabel(v)));
            return map;
        });
        exporter.exportGraph(graph, writer);
    }

    private void prepareComponents(final List<ScrComponent> components) {
        for (final ComponentDescriptionDTO desc : scr.getComponentDescriptionDTOs()) {
            for (final ComponentConfigurationDTO configurationDTO : scr.getComponentConfigurationDTOs(desc)) {
                if (configurationDTO.state == SATISFIED || configurationDTO.state == ACTIVE) {
                    final ScrComponent componentName = createComponent(desc, configurationDTO);
                    components.add(componentName);
                }
            }
        }
    }

    private void prepareEdges( //
            final List<ScrComponent> components, //
            final List<Pair<ScrComponent, ScrComponent>> edges) {

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
                        break;
                    }
                    endComponent = findComponentByName(components, componentNameProperty);
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
            final ComponentConfigurationDTO configuration) {

        final ScrComponent component = new ScrComponent();

        component.description = description;
        component.configuration = configuration;

        return component;
    }

    private ScrComponent findComponentByName(final List<ScrComponent> components, final String componentName) {
        return components.stream() //
                .filter(c -> componentName.equals(c.description.name)) //
                .findAny() //
                .orElse(null);
    }

}
