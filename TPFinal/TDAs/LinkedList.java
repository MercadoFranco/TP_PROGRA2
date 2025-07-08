package TPFinal.TDAs;

public class LinkedList<T> {
    private Node<T> head;

    public void add(T data) {
        Node<T> nuevo = new Node<>(data);

        if (head == null) {
            head = nuevo;
            return;
        }
        Node<T> actual = head;
        while (actual.next != null) {
            actual = actual.next;
        }
        actual.next = nuevo;
    }

    public Node<T> getFirst() {
        return head;
    }

    public boolean delete(T data) {
        if (head == null) return false;
        if (head.data.equals(data)) {
            head = head.next;
            return true;
        }
        Node<T> actual = head;
        while (actual.next != null && !actual.next.data.equals(data)) {
            actual = actual.next;
        }
        if (actual.next == null) return false;
        actual.next = actual.next.next;
        return true;
    }

    public void print() {
        Node<T> actual = head;
        while (actual != null) {
            System.out.println(actual.data);
            actual = actual.next;
        }
        System.out.println("null");
    }

    public boolean isEmpty() {
        return head == null;
    }

}