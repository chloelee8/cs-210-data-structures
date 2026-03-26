/*
 * Copyright 2025 Marc Liberatore.
 */
package trees;

import java.util.Iterator;

/**
 * A (partial) implementation of AVL tree. You'll need to complete the rotations to make
 * insertion work. This project doesn't require you to implement remove(), but you're welcome
 * to do so if you want -- it requires a slight modification to `insertionCheck` as written.
 * 
 * If you're feeling like challenging yourself, reimplement the add-and-rotation-fix algorithm
 * the "traditional" way -- recursively.
 */

import java.util.NoSuchElementException;
import java.util.Stack;

public class AVLTree<E extends Comparable<E>> implements Iterable<E> {

    Node<E> root;
    int size;

    public AVLTree() {}

    /**
     * Returns the number of elements in the tree.
     * @return the size of the tree
     */
    public int size() {
        return size;
    }

    /**
     * Returns the height of the node n.
     * null has a height of -1; otherwise, the height is 1 + max(leftHeight, rightHeight)
     * @param n
     * @return the height of node n
     */
    private int height(Node<E> n) {
        if (n == null) return -1;
        return 1 + Math.max(height(n.left), height(n.right));
    }

    /**
     * Checks if a node satisfies the AVL property.
     * @param n
     * @return true if node n is balanced
     */
    private boolean isAVL(Node<E> n) {
        if (n == null) return true;
        int lh = height(n.left);
        int rh = height(n.right);
        return Math.abs(lh - rh) <= 1;
    }

    /**
     * Checks whether the tree contains the element e.
     * @param e
     * @return true if the element exists in the tree
     */
    public boolean contains(E e) {
        return find(e) != null;
    }

    private Node<E> find(E e, Node<E> n) {
        if (n == null) return null;
        if (e.equals(n.data)) return n;
        return e.compareTo(n.data) < 0 ? find(e, n.left) : find(e, n.right);
    }

    private Node<E> find(E e) {
        return find(e, root);
    }

    /**
     * Returns the element in the tree equal to e, or null if not found.
     * @param e
     * @return the element in the tree
     */
    public E get(E e) {
        Node<E> n = find(e);
        return n == null ? null : n.data;
    }

    /**
     * Adds a new element e to the AVL tree.
     * @param e
     */
    public void add(E e) {
        if (root == null) {
            root = new Node<>(e);
            size = 1;
        } else {
            add(e, root);
        }
    }

    private void add(E e, Node<E> node) {
        if (e.equals(node.data)) {
            node.data = e; // replace duplicates
        } else if (e.compareTo(node.data) < 0) {
            if (node.left == null) {
                node.left = new Node<>(e, node);
                size++;
                insertionCheck(node.left);
            } else {
                add(e, node.left);
            }
        } else {
            if (node.right == null) {
                node.right = new Node<>(e, node);
                size++;
                insertionCheck(node.right);
            } else {
                add(e, node.right);
            }
        }
    }

    /**
     * After insertion, walks up the tree to rebalance nodes violating AVL property
     * @param node
     */
    private void insertionCheck(Node<E> node) {
        Node<E> n = node;
        String path = "";

        while (n != null) {
            if (!isAVL(n)) {
                String rot = path.substring(0, 2);
                if (rot.equals("LL")) rotateRight(n);
                else if (rot.equals("RR")) rotateLeft(n);
                else if (rot.equals("LR")) {
                    rotateLeft(n.left);
                    rotateRight(n);
                } else if (rot.equals("RL")) {
                    rotateRight(n.right);
                    rotateLeft(n);
                } else {
                    throw new IllegalStateException("Unknown rotation case");
                }
                break;
            }
            if (n.parent == null) break;
            path = (n == n.parent.left ? "L" : "R") + path;
            n = n.parent;
        }
    }

    /**
     * Removes an element from the tree.
     * @param e
     * @return the removed element, or null if not found
     */
    public E remove(E e) {
        Node<E> node = find(e);
        if (node == null) return null;
        E data = node.data;
        size--;

        Node<E> rebalanceNode;

        if (node.left != null && node.right != null) {
            Node<E> successor = node.right;
            while (successor.left != null) successor = successor.left;
            node.data = successor.data;
            rebalanceNode = successor.parent;
            splice(successor);
        } else {
            rebalanceNode = node.parent;
            splice(node);
        }

        if (rebalanceNode != null) deletionCheck(rebalanceNode);
        return data;
    }

    private void splice(Node<E> n) {
        Node<E> subNode = n.left != null ? n.left : n.right;
        Node<E> parent = n.parent;

        if (parent == null) root = subNode;
        else if (parent.left == n) parent.left = subNode;
        else parent.right = subNode;

        if (subNode != null) subNode.parent = parent;
    }

    private void deletionCheck(Node<E> node) {
        Node<E> n = node;
        while (n != null) {
            if (!isAVL(n)) {
                int leftHeight = height(n.left);
                int rightHeight = height(n.right);

                if (leftHeight > rightHeight) {
                    if (height(n.left.left) >= height(n.left.right)) rotateRight(n);
                    else {
                        rotateLeft(n.left);
                        rotateRight(n);
                    }
                } else {
                    if (height(n.right.right) >= height(n.right.left)) rotateLeft(n);
                    else {
                        rotateRight(n.right);
                        rotateLeft(n);
                    }
                }
            }
            n = n.parent;
        }
    }

    /**
     * Right rotation at node n
     * @param n
     */
    private void rotateRight(Node<E> n) {
        Node<E> A = n.left;
        Node<E> p = n.parent;
        Node<E> T2 = A.right;

        if (p == null) root = A;
        else if (p.left == n) p.left = A;
        else p.right = A;

        A.parent = p;
        A.right = n;
        n.parent = A;
        n.left = T2;
        if (T2 != null) T2.parent = n;
    }

    /**
     * Left rotation at node n
     * @param n
     */
    private void rotateLeft(Node<E> n) {
        Node<E> A = n.right;
        Node<E> p = n.parent;
        Node<E> T2 = A.left;

        if (p == null) root = A;
        else if (p.left == n) p.left = A;
        else p.right = A;

        A.parent = p;
        A.left = n;
        n.parent = A;
        n.right = T2;
        if (T2 != null) T2.parent = n;
    }

    /**
     * Returns an iterator over the elements of the tree (in-order)
     * @return an iterator
     */
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Stack<Node<E>> stack = new Stack<>();
            Node<E> current = root;

            private void pushLeft(Node<E> node) {
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
            }

            {
                pushLeft(current);
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                Node<E> node = stack.pop();
                pushLeft(node.right);
                return node.data;
            }
        };
    }
}
