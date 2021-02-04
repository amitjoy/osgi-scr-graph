<p align="center">
  <img width="560" alt="logo" src="https://user-images.githubusercontent.com/76659596/106566061-10d28600-6530-11eb-8c3f-f2a3a579a005.png" />
</p>

This repository comprises the API and optional Apache Felix Gogo Command to generate a DOT graph representation of the Service Component Runtime (SCR).

----------------------------------------------------------------------------------------------------------

[![amitjoy - osgi-scr-graph](https://img.shields.io/static/v1?label=amitjoy&message=osgi-scr-graph&color=blue&logo=github)](https://github.com/amitjoy/osgi-scr-graph)
[![stars - osgi-scr-graph](https://img.shields.io/github/stars/amitjoy/osgi-scr-graph?style=social)](https://github.com/amitjoy/osgi-scr-graph)
[![forks - osgi-scr-graph](https://img.shields.io/github/forks/amitjoy/osgi-scr-graph?style=social)](https://github.com/amitjoy/osgi-scr-graph)
[![License - Apache](https://img.shields.io/badge/License-Apache-blue)](#license)
[![Build - Passing](https://img.shields.io/badge/Build-Passing-brightgreen)](https://github.com/amitjoy/osgi-scr-graph/runs/1485969918)
[![GitHub release](https://img.shields.io/github/release/amitjoy/osgi-scr-graph?include_prereleases&sort=semver)](https://github.com/amitjoy/osgi-scr-graph/releases/)

-----------------------------------------------------------------------------------------------------------

### Minimum Requirements

1. Java 8
2. OSGi R7

-----------------------------------------------------------------------------------------------------------

### Dependencies

This project comprises the following list of bundles - 

1. `in.bytehue.osgi.scr.graph` - The Service Component Runtime(SCR) Graph API and Implementation
2. `in.bytehue.osgi.scr.graph.example` - Example project for usages

---------------------------------------------------------------------------------------------------------------

### Installation

To use it in the OSGi environment, you only need to install `in.bytehue.osgi.scr.graph`.

--------------------------------------------------------------------------------------------------------------

#### Project Import

**Import as Eclipse Projects**

1. Install Bndtools
2. Import all the projects (`File -> Import -> General -> Existing Projects into Workspace`)

--------------------------------------------------------------------------------------------------------------

#### Building from Source

Run `./gradlew clean build` in the project root directory

--------------------------------------------------------------------------------------------------------------

### Developer

Amit Kumar Mondal (admin@amitinside.com)

--------------------------------------------------------------------------------------------------------------

### Contribution [![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/amitjoy/osgi-scr-graph/issues)

Want to contribute? Great! Check out [Contribution Guide](https://github.com/amitjoy/osgi-scr-graph/blob/master/CONTRIBUTING.md)

--------------------------------------------------------------------------------------------------------------

### License

This project is licensed under Apache License Version 2.0 [![License](http://img.shields.io/badge/license-Apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

--------------------------------------------------------------------------------------------------------------

### Usage

--------------------------------------------------------------------------------------------------------------

#### Apache Felix Gogo Command

--------------------------------------------------------------------------------------------------------------

Note that, the command will only work if and only if Felix Gogo bundles are installed in the runtime.

##### Graph Generation

<img width="503" alt="help-graph" src="https://user-images.githubusercontent.com/13380182/106591064-1a6ae680-654e-11eb-9efe-a460bbdd6d6f.png">

<img width="522" alt="gogo" src="https://user-images.githubusercontent.com/13380182/106575751-b4299800-653c-11eb-8378-81012ef2554a.png">

##### Cycle Detection

<img width="666" alt="help-cycle" src="https://user-images.githubusercontent.com/13380182/106591152-37071e80-654e-11eb-8e44-98a222345085.png">

<img width="470" alt="cycle_gogo" src="https://user-images.githubusercontent.com/13380182/106576234-35812a80-653d-11eb-9aaf-ac634498e263.png">

<img width="1131" alt="plain-cycle" src="https://user-images.githubusercontent.com/13380182/106585582-c5c46d00-6547-11eb-9bde-260c5d669821.png">

<img width="474" alt="Screenshot 2021-02-02 at 12 01 20" src="https://user-images.githubusercontent.com/13380182/106591300-66b62680-654e-11eb-860d-c875e872ba7f.png">

--------------------------------------------------------------------------------------------------------------

#### DOT Graph

Copy the DOT graph representation format to a file having an extension of `.dot` and execute the following to convert them to PNG/SVG

* `dot -Tpng filename.dot -o filename.png`
* `dot -Tsvg filename.dot -o filename.svg`

To install `dot`, please follow this [link](https://graphviz.org/download/)

--------------------------------------------------------------------------------------------------------------

##### SCR Runtime

![graph](https://user-images.githubusercontent.com/13380182/106575825-cc99b280-653c-11eb-894d-3d4e65de88fc.png)

##### Cycle Detection

![cycle](https://user-images.githubusercontent.com/13380182/106576461-6cefd700-653d-11eb-83af-da80272a5fb3.png)

--------------------------------------------------------------------------------------------------------------

#### API Usage Examples

##### Graph Generation:

```java
@Component
public final class GraphGenrator {

    @Reference
    private ScrGraph scrGraph;

    public String graph() {
        final Graph<ScrComponent, DefaultEdge> graph = scrGraph.getGraph();

        final Writer writer = new StringWriter();
        scrGraph.exportGraph(graph, writer);

        return writer.toString();
    }
}
```

##### Cycle Detection:

```java
@Component
public final class GraphCycleFinder {

    @Reference
    private ScrGraph scrGraph;

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
```
--------------------------------------------------------------------------------------------------------------
