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
package in.bytehue.osgi.scr.graph.provider;

import static java.util.Objects.requireNonNull;

import java.util.List;

import in.bytehue.osgi.scr.graph.api.ScrComponent;

public final class ScrGraphHelper {

    private ScrGraphHelper() {
        throw new IllegalAccessError("Cannot be instantiated");
    }

    public static class CircularLinkedList {

        private CircularLinkedList() {
            throw new IllegalAccessError("Cannot be instantiated");
        }

        public static class Node<E> {

            private final E data;
            private Node<E> next;

            public Node(final E data) {
                requireNonNull(data, "'data' cannot be null");
                this.data = data;
            }

            public E getData() {
                return data;
            }

            public Node<E> getNext() {
                return next;
            }

            public void setNext(final Node<E> next) {
                requireNonNull(next, "'next' node cannot be null");
                this.next = next;
            }
        }

        public static Node<ScrComponent> create(final List<ScrComponent> components) {
            Node<ScrComponent> nodeTop = null;
            if (components == null || components.isEmpty()) {
                return nodeTop;
            }
            Node<ScrComponent> nodeBottom = null;
            Node<ScrComponent> nodeCurr = null;

            for (final ScrComponent component : components) {
                nodeCurr = new Node<>(component);
                if (nodeTop == null) {
                    nodeTop = nodeCurr;
                } else {
                    nodeBottom.setNext(nodeCurr);
                }
                nodeBottom = nodeCurr;
                nodeBottom.setNext(nodeTop);
            }
            return nodeTop;
        }

    }

    public static String createVertexLabel(final ScrComponent component) {
        return component.description.name + " [" + component.configuration.id + "]";
    }

}
