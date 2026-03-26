/*
 * Copyright 2023 Marc Liberatore.
 */

package lists;

public class LinkedList<E> implements List<E> {
    // Note: do not declare any additional instance variables
    Node<E> head;
    Node<E> tail;
    int size;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        Node<E> n = head;
        while (n != null) {
            result = prime * result + n.data.hashCode();
            n = n.next;
        }
        result = prime * result + size;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof List))
            return false;
        List other = (List) obj;
        if (size != other.size())
            return false;

        // TODO before returning true, make sure each element of the lists are equal!

        Node<E> n = head;
        for (int i = 0; i < size; i++) {
            Object otherElement = other.get(i);
            E thisElement = n.data;

            if (thisElement == null) {
                if (otherElement != null) {
                    return false;
                }
            } else if (!thisElement.equals(otherElement)) {
                return false;
            }

            n = n.next;

        }

        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        Node<E> n = head;
        for (int i = 0; i < index; i++) {
            n = n.next;
        }

        return n.data;
    }

    @Override
    public void add(E e) {

        Node<E> newNode = new Node<>(e);

        if (size == 0) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    @Override
    public void add(int index, E e) throws IndexOutOfBoundsException {

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        Node<E> newNode = new Node<>(e);

        if (index == 0) {
            newNode.next = head;
            head = newNode;

            if (size == 0) {
                tail = newNode;
            }
        } else if (index == size) {
            tail.next = newNode;
            tail = newNode;
        } else {
            Node<E> prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
            }

            newNode.next = prev.next;
            prev.next = newNode;

        }

        size++;

    }

    @Override
    public E remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        E removed;
        if (index == 0) {
            removed = head.data;
            head = head.next;
            if (size == 1) {
                tail = null;
            }
        } else {
            Node<E> prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
            }
            removed = prev.next.data;
            prev.next = prev.next.next;
            if (index == size - 1) {
                tail = prev;
            }
        }

        size--;
        return removed;
    }

    @Override
    public E set(int index, E e) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        Node<E> n = head;
        for (int i = 0; i < index; i++) {
            n = n.next;
        }
        E old = n.data;
        n.data = e;
        return old;

    }

    @Override
    public int indexOf(E e) {
        Node<E> n = head;
        for (int i = 0; i < size; i++) {
            if (e == null) {
                if (n.data == null) {
                    return i;
                }
            } else {
                if (e.equals(n.data)) {
                    return i;
                }
            }
            n = n.next;
        }

        return -1;
    }
}
