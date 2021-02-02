package in.bytehue.osgi.scr.graph.example;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = ComponentC.class)
public class ComponentC {

    @Reference
    private ComponentA componentA;

}
