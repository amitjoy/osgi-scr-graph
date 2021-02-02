package in.bytehue.osgi.scr.graph.example;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = ComponentA.class)
public class ComponentA {

    @Reference
    private ComponentB compB;

}
