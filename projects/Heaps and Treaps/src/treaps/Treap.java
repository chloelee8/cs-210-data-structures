/*
 * Copyright 2025 Marc Liberatore.
 */

package treaps;

import java.util.ArrayList;
import java.util.List;

public class Treap<E extends Comparable<E>> {
    Node<E> root;
    int size;

    /**
     * Returns the number of nodes in the treap.
     * @return the size of the treap
     */
    public int size() {
        return size;
    }

    /**
     * Return true iff the tree contains the value e.
     * @param e the value to search for
     * @return true iff the tree contains the value e
     */
    public boolean contains(E e) {
        return find(e) != null;
    }

    /**
     * Helper method to find a node containing e in the subtree rooted at n.
     * @param e value to find
     * @param n root of subtree
     * @return node containing e or null if not found
     */
    private Node<E> find(E e, Node<E> n) {
        if (n == null) return null;
        if (e.equals(n.data)) return n;
        if (e.compareTo(n.data) < 0) return find(e, n.left);
        else return find(e, n.right);
    }

    /**
     * Helper method to find a node containing e in the treap.
     * @param e value to find
     * @return node containing e or null if not found
     */
    private Node<E> find(E e) {
        return find(e, root);
    }

    /**
     * Perform an in-order traversal of the tree rooted at the given node, and return
     * a list of the elements in the order they were visited.
     * @param node root of the subtree
     * @return list of elements from in-order traversal
     */
    static <E> List<E> inOrder(Node<E> node) {
        List<E> result = new ArrayList<>();
        inOrderHelper(node, result);
        return result;
    }

    /**
     * Helper method for in-order traversal.
     * @param node current node
     * @param acc accumulator list
     */
    private static <E> void inOrderHelper(Node<E> node, List<E> acc) {
        if (node == null) return;
        inOrderHelper(node.left, acc);
        acc.add(node.data);
        inOrderHelper(node.right, acc);
    }

    /**
     * Returns true iff the tree rooted at n is a Binary Search Tree (based on its node's data values).
     * It must have no more than two children per node.
     * Each node's data value must be greater than all the values in its left subtree, and smaller
     * than all the values in its right subtree. (Duplicates are not allowed.)
     * @param n root of subtree
     * @return true iff the subtree is a BST
     */
    static <E extends Comparable<E>> boolean isBST(Node<E> n) {
        if (n == null) return true;
        List<E> order = inOrder(n);
        for (int i = 1; i < order.size(); i++) {
            if (order.get(i - 1).compareTo(order.get(i)) >= 0) return false;
        }
        return true;
    }

    /**
     * Returns true iff the tree rooted at n is heap (based on its node's priority values).
     * It must have no more than two children per node.
     * Each node's priority value must be greater than or equal to all the values in its children.
     * @param n root of subtree
     * @return true iff the subtree satisfies the heap property
     */
    static <E extends Comparable<E>> boolean isHeap(Node<E> n) {
        if (n == null) return true;
        if (n.left != null) {
            if (n.priority < n.left.priority) return false;
            if (!isHeap(n.left)) return false;
        }
        if (n.right != null) {
            if (n.priority < n.right.priority) return false;
            if (!isHeap(n.right)) return false;
        }
        return true;
    }

    /**
     * Add the value e to the treap, maintaining the BST and heap properties.
     * @param e value to add
     */
    public void add(E e) {
        if (root == null) {
            root = new Node<>(e);
            size = 1;
            return;
        }
        add(e, root);
    }

    /**
     * Add the value e to the treap rooted at the given node, maintaining the BST and heap properties.
     * @param e value to add
     * @param node root of the subtree
     */
    public void add(E e, Node<E> node) {
        if (e.equals(node.data)) {
            node.data = e;
            return;
        }
        if (e.compareTo(node.data) < 0) {
            if (node.left == null) {
                node.left = new Node<>(e, node);
                size++;
                bubbleUp(node.left);
            } else {
                add(e, node.left);
            }
        } else {
            if (node.right == null) {
                node.right = new Node<>(e, node);
                size++;
                bubbleUp(node.right);
            } else {
                add(e, node.right);
            }
        }
    }

    /**
     * Restore heap property by bubbling the node up as needed.
     * @param node node to bubble up
     */
    private void bubbleUp(Node<E> node) {
        while (node.parent != null && node.priority > node.parent.priority) {
            Node<E> p = node.parent;
            if (p.left == node) rotateRight(p);
            else rotateLeft(p);
        }
    }

    /**
     * Perform a right rotation around n.
     * @param n root of subtree
     */
    private void rotateRight(Node<E> n) {
        Node<E> B = n;
        Node<E> A = B.left;
        Node<E> T2 = A.right;
        Node<E> p = B.parent;

        if (B == root) {
            root = A;
            A.parent = null;
        } else {
            if (p.left == B) p.left = A;
            else p.right = A;
            A.parent = p;
        }

        A.right = B;
        B.parent = A;
        B.left = T2;
        if (T2 != null) T2.parent = B;
    }

    /**
     * Perform a left rotation around n.
     * @param n root of subtree
     */
    private void rotateLeft(Node<E> n) {
        Node<E> B = n;
        Node<E> A = B.right;
        Node<E> T2 = A.left;
        Node<E> p = B.parent;

        if (B == root) {
            root = A;
            A.parent = null;
        } else {
            if (p.left == B) p.left = A;
            else p.right = A;
            A.parent = p;
        }

        A.left = B;
        B.parent = A;
        B.right = T2;
        if (T2 != null) T2.parent = B;
    }

    /**
     * Test main to print treap structure and properties.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Treap<Integer> t = new Treap<>();

        for (int i = 0; i < 15; i++) {
            t.add(i);
            TreePrinter.print(t.root);
            System.out.println(isBST(t.root) + "/" + isHeap(t.root));
        }
    }
}

