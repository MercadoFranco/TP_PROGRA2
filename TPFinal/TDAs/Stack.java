package TPFinal.TDAs;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

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

    @JsonIgnore
    public boolean isEmpty() {
        return top == null;
    }

    public List<T> getElementsAsList() {
        List<T> elements = new ArrayList<>();
        Node<T> current = top;
        java.util.Stack<T> tempStack = new java.util.Stack<>();
        while (current != null) {
            tempStack.push(current.data);
            current = current.next;
        }
        while (!tempStack.isEmpty()) {
            elements.add(tempStack.pop());
        }
        return elements;
    }

    public void setElementsAsList(List<T> elements) {
        this.top = null;
        for (int i = elements.size() - 1; i >= 0; i--) {
            this.push(elements.get(i));
        }
    }

}
