package TPFinal.TDAs;

public class Stack<T> {
    private Node<T> top;

    public Stack() {
        this.top = null;
    }

    public void push(T value) {
        Node<T> newNode = new Node<>(value);
        newNode.next = top;
        top = newNode;
    }

    public T pop() {
        if (isEmpty()) {
            System.out.println("La pila está vacía.");
            return null;
        }
        T value = top.data;
        top = top.next;
        return value;
    }

    public T peek() {
        if (isEmpty()) {
            System.out.println("La pila está vacía.");
            return null;
        }
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void printStack() {
        Node<T> current = top;
        System.out.print("Top -> ");
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }
}
