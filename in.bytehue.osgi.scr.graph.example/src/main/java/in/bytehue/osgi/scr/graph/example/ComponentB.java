package in.bytehue.osgi.scr.graph.example;

import static org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = ComponentB.class)
public class ComponentB {

    @Reference(cardinality = OPTIONAL)
    private ComponentC compC;

}
