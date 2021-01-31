package in.bytehue.osgi.scr.graph.api;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultEdge;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ScrGraph {

    Graph<ScrComponent, DefaultEdge> getGraph();

    Graph<ScrComponent, DefaultEdge> getGraph(boolean includeNonScrServices);

    List<List<ScrComponent>> getCycles();

    List<List<ScrComponent>> getCycles(boolean includeNonScrServices);

    List<GraphPath<ScrComponent, DefaultEdge>> getCyclesAsPaths();

    List<GraphPath<ScrComponent, DefaultEdge>> getCyclesAsPaths(boolean includeNonScrServices);

}
