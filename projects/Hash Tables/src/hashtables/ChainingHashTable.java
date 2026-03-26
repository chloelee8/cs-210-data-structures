package hashtables;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
/**
 * An implementation of HashTable.
 * 
 * This implementation uses chaining to resolve collisions. Chaining means 
 * the underlying array stores references to growable structures (like 
 * LinkedLists) that we expect to remain small in size. When there is a 
 * collision, the element is added to the end of the growable structure. It
 * must search the entire growable structure whenever checking membership
 * or removing elements.
 * 
 * This implementation maintains a capacity equal to 2^n - 1 for some positive
 * integer n. When the load factor exceeds 0.75, the next add() triggers a
 * resize by incrementing n (by one). For example, when n=3, then capacity=7.
 * When size=6, then load factor ~=0.86. The addition of the seventh item would
 * trigger a resize, increasing the capacity of the array to 15.
 */
    public class ChainingHashTable<E> implements HashTable<E> {
        private LinkedList<E>[] table;
        private int size;
        
    /**
     * Instantiate a new hash table. The initial capacity should be 7.
     */
    public ChainingHashTable() {
        this(7); 
    }
    @SuppressWarnings("unchecked")
    public ChainingHashTable(int capacity) {
        int cap = chooseCapacity(capacity);
        table = (LinkedList<E>[]) new LinkedList[cap];
        size = 0;
    }
    private int chooseCapacity(int requested) {
        if (requested <= 0) return 3;
        int k = 1;
        while ((Math.pow(2, k) - 1) < requested) {
            k++;
        }
        return (int)(Math.pow(2, k) - 1);
    }

    private int indexFor(Object o, int cap) {
        int h = (o == null) ? 0 : o.hashCode();
        return (h & 0x7fffffff) % cap;
    }

    @Override
    public int capacity() {
        return table.length;
    }

    @Override
    public int size() {
        return size;
    }

/** Returns the current load factor (size / capacity). */
    @Override
    public double loadFactor() {
        return size / (double) capacity();
    }   

/** Enlarges the table when load factor exceeds 0.75. */
    @SuppressWarnings("unchecked")
    private void enlarge() {
        int oldCap = capacity();
    // compute next capacity of form 2^(k+1) - 1
        int k = 1;
        while ((Math.pow(2, k) - 1) <= oldCap) {
            k++;
        }
        int newCap = (int)(Math.pow(2, k) - 1);

        LinkedList<E>[] old = table;
        table = (LinkedList<E>[]) new LinkedList[newCap];
        size = 0;

        for (LinkedList<E> bucket : old) {
            if (bucket != null) {
                for (E e : bucket) {
                    add(e);
                }
            }
        }
    }

    /**
     * Instantiate a new hash table. The initial capacity should be 
     * at least sufficient to hold n elements, but must be one less
     * than a power of two.
     */
    

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException("Null elements not supported");

        // enlarge before adding if load factor > 0.75
        if (loadFactor() > 0.75) {
            enlarge();
        }

        int idx = indexFor(e, capacity());
        if (table[idx] == null) table[idx] = new LinkedList<>();

        LinkedList<E> bucket = table[idx];
        int i = 0;
        for (E existing : bucket) {
            if (existing.equals(e)) {
                bucket.set(i, e); // replace
                return false;
            }
            i++;
        }

        bucket.add(e);
        size++;
        return true;
    }

    @Override
    public boolean contains(E e) {
        if (e == null) return false;
        int idx = indexFor(e, capacity());
        LinkedList<E> bucket = table[idx];
        if (bucket == null) return false;
        for (E existing : bucket) {
            if (existing.equals(e)) return true;
        }
        return false;
    }

    @Override
    public E get(E e) {
        if (e == null) return null;
        int idx = indexFor(e, capacity());
        LinkedList<E> bucket = table[idx];
        if (bucket == null) return null;
        for (E existing : bucket) {
            if (existing.equals(e)) return existing;
        }
        return null;
    }

    @Override
    public boolean remove(E e) {
        if (e == null) return false;
        int idx = indexFor(e, capacity());
        LinkedList<E> bucket = table[idx];
        if (bucket == null) return false;

        int i = 0;
        for (E existing : bucket) {
            if (existing.equals(e)) {
                bucket.remove(i);
                size--;
                return true;
            }
            i++;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int bucketIdx = 0;
            Iterator<E> it = (table.length > 0 && table[0] != null) ? table[0].iterator() : null;

            private void advanceToNext() {
                while ((it == null || !it.hasNext()) && bucketIdx < table.length - 1) {
                    bucketIdx++;
                    if (table[bucketIdx] != null) it = table[bucketIdx].iterator();
                    else it = null;
                }
            }

            @Override
            public boolean hasNext() {
                advanceToNext();
                return it != null && it.hasNext();
            }

            @Override
            public E next() {
                advanceToNext();
                if (it == null) throw new NoSuchElementException();
                return it.next();
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (LinkedList<E> bucket : table) {
            if (bucket != null) {
                for (E e : bucket) {
                    if (!first) sb.append(", ");
                    sb.append(e);
                    first = false;
                }
            }
        }
        sb.append("}");
        return sb.toString();
    }
}

