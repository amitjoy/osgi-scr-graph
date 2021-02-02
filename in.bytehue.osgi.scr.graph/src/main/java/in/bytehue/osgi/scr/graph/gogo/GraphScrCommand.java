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
package in.bytehue.osgi.scr.graph.gogo;

import static in.bytehue.osgi.scr.graph.gogo.GraphScrCommand.PID;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.annotations.GogoCommand;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import in.bytehue.osgi.scr.graph.api.ScrComponent;
import in.bytehue.osgi.scr.graph.api.ScrGraph;

@GogoCommand(scope = "scr", function = { "graph", "cycle" })
@Component(service = GraphScrCommand.class, configurationPid = PID)
@Descriptor("Comprises Graph Commands for Service Component Runtime (SCR)")
public final class GraphScrCommand {

    public static final String PID = "in.bytehue.osgi.scr.graph.gogo";

    @Reference
    private ScrGraph scrGraph;

    @Descriptor("Returns DOT Representation of Service Component Runtime (SCR)")
    public String graph() {
        final Graph<ScrComponent, DefaultEdge> graph = scrGraph.getGraph();

        final Writer writer = new StringWriter();
        scrGraph.exportGraph(graph, writer);

        return writer.toString();
    }

    @Descriptor("Returns DOT Representation of Cyclic Dependencies of Service Component Runtime (SCR)")
    public String cycle() {
        final List<List<ScrComponent>> cycles = scrGraph.getCycles();
        if (cycles.isEmpty()) {
            return "No SCR cycle exists";
        }
        final Graph<ScrComponent, DefaultEdge> cyclesAsGraph = scrGraph.getCyclesAsGraph();
        final Writer writer = new StringWriter();
        scrGraph.exportGraph(cyclesAsGraph, writer);

        return writer.toString();
    }

}