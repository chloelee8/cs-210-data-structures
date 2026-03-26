/*
 * Copyright 2023 Marc Liberatore.
 */

 package comparators;

import java.util.Comparator;

/**
 * A comparator to determine the order of two web pages. The two web pages are
 * compared lexicographically. However, if the CasedURLComparator is created
 * with ignoreCase set to true, then the comparison should be case-insensitive.
 */
public class CasedURLComparator implements Comparator<WebPageRecord> {
    final boolean ignoreCase;

    public CasedURLComparator(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    @Override
    public int compare(WebPageRecord x, WebPageRecord y) {
        int cmp;
        if (ignoreCase) {
            cmp = x.URL.compareToIgnoreCase(y.URL);
        } else {
            cmp = x.URL.compareTo(y.URL);
        }

        // If URLs are equal (cmp == 0), compare by size (ascending)
        if (cmp == 0) {
            return Integer.compare(x.length, y.length);
        }
        return cmp;
    
    }

}
