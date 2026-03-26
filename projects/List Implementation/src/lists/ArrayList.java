/*
 * Copyright 2023 Marc Liberatore.
 */

package lists;

public class ArrayList<E> implements List<E> {
    // Note: do not declare any additional instance variables
    E[] array;
    int size;

    @SuppressWarnings("unchecked")
    public ArrayList() {
        size = 0;
        array = (E[]) new Object[10];
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (int i = 0; i < size; i++) {
            result = prime * result + array[i].hashCode();
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

        for (int i = 0; i < size; i++) {
            E thisElement = array[i];
            Object otherElement = other.get(i);

            if (thisElement == null) {
                if (otherElement != null) {
                    return false;
                }
            } else if (!thisElement.equals(otherElement)) {
                return false;
            }
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
        return array[index];
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(E e) {
        if (size == array.length) {
            E[] newArray = (E[]) new Object[array.length * 2];

            for (int i = 0; i < array.length; i++) {
                newArray[i] = array[i];
            }

            array = newArray;
        }
        array[size] = e;
        size++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(int index, E e) throws IndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        if (size == array.length) {
            E[] newArray = (E[]) new Object[array.length * 2];
            for (int i = 0; i < array.length; i++) {
                newArray[i] = array[i];
            }
            array = newArray;
        }

        for (int i = size - 1; i >= index; i--) {
            array[i + 1] = array[i];
        }

        array[index] = e;
        size++;
    }

    @Override
    public E remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        E removed = array[index];
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }

        array[size - 1] = null;
        size--;

        return removed;
    }

    @Override
    public E set(int index, E e) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        E old = array[index];
        array[index] = e;
        return old;
    }

    @Override
    public int indexOf(E e) {
        for (int i = 0; i < size; i++) {
            if (e == null) {
                if (array[i] == null) {
                    return i;
                }
            } else {
                if (e.equals(array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }
}