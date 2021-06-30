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
package in.bytehue.osgi.scr.graph.api;

import org.osgi.dto.DTO;
import org.osgi.service.component.runtime.dto.ComponentConfigurationDTO;
import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;

/**
 * Data Transfer Object for an instance of SCR component.
 *
 * <p>
 * A SCR component contains associated description and its configuration.
 *
 * @noextend This class is not intended to be extended by consumers.
 *
 * @NotThreadSafe
 *
 * @see ComponentDescriptionDTO
 * @see ComponentConfigurationDTO
 */
public class ScrComponent extends DTO {

    /**
     * The description of the component
     */
    public ComponentDescriptionDTO description;

    /**
     * The configuration of the component
     */
    public ComponentConfigurationDTO configuration;

}
