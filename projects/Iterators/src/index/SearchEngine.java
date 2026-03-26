/*
 * Copyright 2023 Marc Liberatore.
 */

package index;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;

import comparators.TfIdfComparator;
import documents.DocumentId;

/**
 * A simplified document indexer and search engine.
 * 
 * Documents are added to the engine one-by-one, and uniquely identified by a DocumentId.
 *
 * Documents are internally represented as "terms", which are lowercased versions of each word 
 * in the document. 
 * 
 * Queries for terms are also made on the lowercased version of the term. Terms are 
 * therefore case-insensitive.
 * 
 * Lookups for documents can be done by term, and the most relevant document(s) to a specific term 
 * (as computed by tf-idf) can also be retrieved.
 *
 * See:
 * - <https://en.wikipedia.org/wiki/Inverted_index>
 * - <https://en.wikipedia.org/wiki/Search_engine_(computing)> 
 * - <https://en.wikipedia.org/wiki/Tf%E2%80%93idf>
 * 
 * @author Marc Liberatore
 *
 */
public class SearchEngine {
	private final Map<DocumentId, Map<String, Integer>> documents = new HashMap<>();
	
	/**
	 * Inserts a document into the search engine for later analysis and retrieval.
	 * 
	 * The document is uniquely identified by a documentId; attempts to re-insert the same 
	 * document are ignored.
	 * 
	 * The document is supplied as a Reader; this method stores the document contents for 
	 * later analysis and retrieval.
	 * 
	 * @param documentId
	 * @param reader
	 * @throws IOException iff the reader throws an exception 
	 */
	public void addDocument(DocumentId documentId, Reader reader) throws IOException {
		if (documents.containsKey(documentId)) {
            return;
        }

        Map<String, Integer> termCounts = new HashMap<>();
        Scanner scanner = new Scanner(reader);
        scanner.useDelimiter("\\W+"); // split on non-word characters

        while (scanner.hasNext()) {
            String token = scanner.next().toLowerCase(); // case-insensitive
            if (token.isEmpty()) continue;
            termCounts.put(token, termCounts.getOrDefault(token, 0) + 1);
        }

        documents.put(documentId, termCounts);
	}
	
	/**
	 * Returns the set of DocumentIds contained within the search engine that contain a given term.
	 * 
	 * @param term
	 * @return the set of DocumentIds that contain a given term
	 */
	public Set<DocumentId> indexLookup(String term) {
		Set<DocumentId> result = new HashSet<>();
        if (term == null) return result;
        String lower = term.toLowerCase();
        for (var entry : documents.entrySet()) {
            if (entry.getValue().containsKey(lower)) {
                result.add(entry.getKey());
            }
        }
        return result;
	}
	
	/**
	 * Returns the term frequency of a term in a particular document.
	 * 
	 * The term frequency is number of times the term appears in a document.
	 * 
	 * See 
	 * @param documentId
	 * @param term
	 * @return the term frequency of a term in a particular document
	 * @throws IllegalArgumentException if the documentId has not been added to the engine
	 */
	public int termFrequency(DocumentId documentId, String term) throws IllegalArgumentException {
		Map<String, Integer> termMap = documents.get(documentId);
		if (termMap == null) {
			throw new IllegalArgumentException("Document not found in index");
		}
		return termMap.getOrDefault(term.toLowerCase(), 0);
	}
	
	/**
	 * Returns the inverse document frequency of a term across all documents in the index.
	 * 
	 * For our purposes, IDF is defined as log ((1 + N) / (1 + M)) where 
	 * N is the number of documents in total, and M
	 * is the number of documents where the term appears.
	 * 
	 * @param term
	 * @return the inverse document frequency of term 
	 */
	public double inverseDocumentFrequency(String term) {
		int N = documents.size();
		int M = indexLookup(term).size();
		return Math.log((1.0 + N) / (1.0 + M));
	}
	
	
	/**
	 * Returns the tfidf score of a particular term for a particular document.
	 * 
	 * tfidf is the product of term frequency and inverse document frequency for the given term and document.
	 * 
	 * @param documentId
	 * @param term
	 * @return the tfidf of the the term/document
	 * @throws IllegalArgumentException if the documentId has not been added to the engine
	 */
	public double tfIdf(DocumentId documentId, String term) throws IllegalArgumentException {
		return termFrequency(documentId, term) * inverseDocumentFrequency(term);
	}
	
	/**
	 * Returns a sorted list of documents, most relevant to least relevant, for the given term.
	 * 
	 * A document with a larger tfidf score is more relevant than a document with a lower tfidf score.
	 * 
	 * Each document in the returned list must contain the term.
	 * 
	 * @param term
	 * @return a list of documents sorted in descending order by tfidf
	 */
	public List<DocumentId> relevanceLookup(String term) {
		Set<DocumentId> docsWithTerm = indexLookup(term);
		List<DocumentId> result = new ArrayList<>(docsWithTerm);
		result.sort(new TfIdfComparator(this, term)); // descending tf-idf
		return result;
	}
}
