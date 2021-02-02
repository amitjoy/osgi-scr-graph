package in.bytehue.osgi.scr.graph.example;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class ComponentD {

    @Reference
    private ComponentC componentC;

}
