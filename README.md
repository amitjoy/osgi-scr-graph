<p align="center">
  <img width="563" alt="logo" src="https://user-images.githubusercontent.com/13380182/101778008-90754300-3af3-11eb-95da-91c54608f277.png" />
</p>

## OSGi Service Component Runtime (SCR) Graph

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

1. `in.bytehue.osgi.scr.graph` - The SCR Graph API and Implementation
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

#### Examples in Action


* Generating Graph:

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

* Cycle Detection:

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
