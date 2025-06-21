package TPFinal.TDAs;

public class Stack<T> {
    private Node<T> top;

    public Stack() {
        this.top = null;
    }

    // Segundo constructor para poder crear un stack basado en otro
    public Stack(Stack<T> other) {
        if (other.top == null) {
            this.top = null;
        } else {
            Stack<T> tempStack = new Stack<>();
            Node<T> current = other.top;

            while (current != null) {
                tempStack.push(current.data);
                current = current.next;
            }

            current = tempStack.top;
            while (current != null) {
                this.push(current.data);
                current = current.next;
            }
        }
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
