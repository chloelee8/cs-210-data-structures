package maps;

import java.util.Set;

import java.util.TreeSet;

/**
 * A simple tree-based map implementation using a binary search tree.
 * Keys are stored in sorted order (natural ordering).
 */
public class SimpleTreeMap<K extends Comparable<K>, V> implements SimpleOrderedMap<K, V> {

    /** Inner node class for the BST. */
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node<K, V> root;
    private int size;

    /** Constructs an empty SimpleTreeMap. */
    public SimpleTreeMap() {
        root = null;
        size = 0;
    }

    /** Returns the number of key-value pairs in the map. */
    @Override
    public int size() {
        return size;
    }

    /** Puts a key-value pair into the map, replacing value if key exists. */
    @Override
    public void put(K k, V v) {
        root = put(root, k, v);
    }

    /** Recursive helper for put. */
    private Node<K, V> put(Node<K, V> node, K k, V v) {
        if (node == null) {
            size++;
            return new Node<>(k, v);
        }
        int cmp = k.compareTo(node.key);
        if (cmp < 0) node.left = put(node.left, k, v);
        else if (cmp > 0) node.right = put(node.right, k, v);
        else node.value = v; // replace existing
        return node;
    }

    /** Returns the value associated with key, or null if not found. */
    @Override
    public V get(K k) {
        Node<K, V> node = getNode(root, k);
        return node == null ? null : node.value;
    }

    /** Returns the value for key, or defaultValue if not found. */
    @Override
    public V getOrDefault(K k, V defaultValue) {
        V val = get(k);
        return val != null ? val : defaultValue;
    }

    /** Recursive helper to find a node by key. */
    private Node<K, V> getNode(Node<K, V> node, K k) {
        if (node == null) return null;
        int cmp = k.compareTo(node.key);
        if (cmp < 0) return getNode(node.left, k);
        else if (cmp > 0) return getNode(node.right, k);
        else return node;
    }

    /** Removes the key from the map and returns its value, or null if not present. */
    @Override
    public V remove(K k) {
        Object[] removedValue = new Object[1]; // holder for removed value
        root = remove(root, k, removedValue);
        if (removedValue[0] != null) size--;
        return (V) removedValue[0];
    }

    /** Recursive helper for remove. */
    private Node<K, V> remove(Node<K, V> node, K k, Object[] removedValue) {
        if (node == null) return null;

        int cmp = k.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, k, removedValue);
        } else if (cmp > 0) {
            node.right = remove(node.right, k, removedValue);
        } else {
            removedValue[0] = node.value;
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            // Node with two children: replace with inorder successor
            Node<K, V> successor = min(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node.right = remove(node.right, successor.key, new Object[1]);
        }
        return node;
    }

    /** Returns the node with the minimum key in the subtree. */
    private Node<K, V> min(Node<K, V> node) {
        while (node.left != null) node = node.left;
        return node;
    }

    /** Returns a set of all keys in sorted order. */
    @Override
    public Set<K> keys() {
        Set<K> keySet = new TreeSet<>();
        addKeys(root, keySet);
        return keySet;
    }

    /** Helper method to add keys recursively. */
    private void addKeys(Node<K, V> node, Set<K> set) {
        if (node == null) return;
        addKeys(node.left, set);
        set.add(node.key);
        addKeys(node.right, set);
    }
}
