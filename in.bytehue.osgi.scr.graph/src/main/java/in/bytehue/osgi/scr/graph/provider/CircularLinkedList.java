package in.bytehue.osgi.scr.graph.provider;

import java.util.List;

import in.bytehue.osgi.scr.graph.api.ScrComponent;

public final class CircularLinkedList {

    private CircularLinkedList() {
        throw new IllegalAccessError("Cannot be instantiated");
    }

    public static class Node<E> {

        private final E data;
        private Node<E> next;

        public Node(final E data) {
            this.data = data;
        }

        public E getData() {
            return data;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(final Node<E> next) {
            this.next = next;
        }
    }

    public static Node<ScrComponent> createLinkedList(final List<ScrComponent> components) {
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