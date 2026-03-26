
/*
 * Copyright 2025 Marc Liberatore.
 */

package heaps;

public class HeapUtilities {
    /**
     * Returns true iff the subtree of a starting at index i is a max-heap.
     * @param a an array representing a mostly-complete tree, possibly a heap
     * @param i an index into that array representing a subtree rooted at i
     * @return true iff the subtree of a starting at index i is a max-heap
     */
    static boolean isHeap(double[] a, int i) {
        if (a == null) return true;
        if (i >= a.length) return true; // empty subtree
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < a.length) {
            if (a[i] < a[left]) return false;
            if (!isHeap(a, left)) return false;
        }
        if (right < a.length) {
            if (a[i] < a[right]) return false;
            if (!isHeap(a, right)) return false;
        }
        return true;
    }

    /**
     * Perform the heap siftdown operation on index i of the array a. 
     * 
     * This method assumes the subtrees of i are already valid max-heaps.
     * 
     * This operation is bounded by n (exclusive)! In a regular heap, 
     * n = a.length, but in some cases (for example, heapsort), you will 
     * want to stop the sifting at a particular position in the array. 
     * siftDown should stop before n, in other words, it should not 
     * sift down into any index great than (n-1).
     * 
     * @param a the array being sifted
     * @param i the index of the element to sift down
     * @param n the bound on the array (that is, where to stop sifting)
     */
    static void siftDown(double[] a, int i, int n) {
        if (a == null || n <= 0 || i >= n) return;
        int idx = i;
        while (true) {
            int left = 2 * idx + 1;
            int right = 2 * idx + 2;
            int largest = idx;

            if (left < n && a[left] > a[largest]) {
                largest = left;
            }
            if (right < n && a[right] > a[largest]) {
                largest = right;
            }
            if (largest == idx) break;

            double tmp = a[idx];
            a[idx] = a[largest];
            a[largest] = tmp;

            idx = largest;
        }
    }

    /**
     * Heapify the array a in-place in linear time as a max-heap.
     * @param a an array of values
     */
    static void heapify(double[] a) {
        if (a == null) return;
        int n = a.length;
        int start = (n - 2) / 2;
        for (int i = start; i >= 0; i--) {
            siftDown(a, i, n);
        }
    }

    /**
     * Heapsort the array a in-place, resulting in the elements of
     * a being in ascending order.
     * @param a
     */
    static void heapSort(double[] a) {
        if (a == null) return;
        int n = a.length;
        if (n <= 1) return;
        heapify(a);
        for (int bound = n - 1; bound > 0; bound--) {
            double tmp = a[0];
            a[0] = a[bound];
            a[bound] = tmp;
            siftDown(a, 0, bound);
        }
    }
}
