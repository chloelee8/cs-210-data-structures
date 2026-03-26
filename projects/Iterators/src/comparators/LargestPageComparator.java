/*
 * Copyright 2023 Marc Liberatore.
 */

package comparators;

import java.util.Comparator;

/**
 * A comparator to determine which of two WebPageRecords represents a longer web
 * page. The page with the larger `length` attribute is comes before the other.
 * If there is a tie, break it using the length of the first line -- again,
 * larger comes first.
 * If there is still a tie, break it by comparing which URL comes
 * lexicographically first.
 * Any remaining ties mean the two WebPageRecords should be considered equal.
 */
public class LargestPageComparator implements Comparator<WebPageRecord> {
    @Override
    public int compare(WebPageRecord x, WebPageRecord y) {
        int cmp = Integer.compare(y.length, x.length);
        if (cmp != 0) {
            return cmp;
        }

        // 2️⃣ If tie, compare by first line length (descending)
        int xFirstLineLen = (x.firstLine == null) ? 0 : x.firstLine.length();
        int yFirstLineLen = (y.firstLine == null) ? 0 : y.firstLine.length();
        cmp = Integer.compare(yFirstLineLen, xFirstLineLen);
        if (cmp != 0) {
            return cmp;
        }

        // 3️⃣ If still tied, compare by URL (ascending)
        return x.URL.compareTo(y.URL);
    
    }
}
