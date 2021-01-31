package in.bytehue.osgi.scr.graph.api;

import java.util.List;

import org.osgi.dto.DTO;
import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;

public class ScrComponent extends DTO {

    public String name;
    public List<Long> configurationsIds;
    public ComponentDescriptionDTO description;

}
