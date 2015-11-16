package algs.days.day12;

import edu.princeton.cs.algs4.*;
import algs.hw3.SequentialSearchST;

public class SeparateChainingHashST<Key, Value> {
	int N;                                // number of key-value pairs
	int M;                                // hash table size
	SequentialSearchST<Key, Value>[] st;  // array of linked-list symbol tables
	int INIT_CAPACITY = 4;                // initial default size
	int AVG_LENGTH = 7;                   // Threshold to determine resizing

	/** Initialize empty symbol table with <tt>M</tt> chains. */
	public SeparateChainingHashST(int M) {
		this.M = M;
		st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[M];
		for (int i = 0; i < M; i++) {
			st[i] = new SequentialSearchST<Key, Value>();
		}
	} 

	/** Choose initial default size. */
	public SeparateChainingHashST() { this(11); } 

	// resize the hash table to have the given number of chains by rehashing all of the keys
	void resize(int chains) {
		SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<Key, Value>(chains);
		for (int i = 0; i < M; i++) {
			for (Key key : st[i]) {                  // NOTE ITERATOR OVER KEYS
				temp.put(key, st[i].get(key));
			}
		}

		M  = temp.M;
		N  = temp.N;
		st = temp.st;
	}

	// hash value between 0 and M-1
	int hash(Key key) {
		return (key.hashCode() & 0x7fffffff) % M;
	} 

	public int size()                { return N; } 
	public boolean isEmpty()         { return size() == 0; }
	public boolean contains(Key key) { return get(key) != null; } 

	public Value get(Key key) {
		int i = hash(key);
		return st[i].get(key);
	} 

	public void put(Key key, Value val) {
		// double table size if average length of list >= AVG_LENGTH
		if (N >= AVG_LENGTH*M) resize(2*M);

		int i = hash(key);
		if (!st[i].contains(key)) N++;
		st[i].put(key, val);
	} 

	public void delete(Key key) {
		int i = hash(key);
		if (st[i].contains(key)) { N--; }
		st[i].delete(key);

		// halve table size if average length of list <= 2
		if (M > INIT_CAPACITY && N <= 2*M) resize(M/2);
	} 

	public static void main(String[] args) {
		SeparateChainingHashST<String, String> table = new SeparateChainingHashST<String, String>();

		System.out.println("Sample Hash Code Values:");
		String s = "Hello";
		System.out.println(s + " gives " + s.hashCode());
		s = "aaaaaa";
		System.out.println(s + " gives " + s.hashCode());
		
		
		In in = new In ("words.english.txt");
		while (!in.isEmpty()) {
			String word = in.readString(); 
			table.put(word, word);      // You can use value as the key
		}

		StdOut.println(table.size() + " words in the hash table.");

		// number of empty bins
		// largest chain length
		int numEmpty = 0;
		int maxChain = 0;    	for (int i = 0; i < table.M; i++) {
			if (table.st[i].isEmpty()) {
				numEmpty++;
			}

			if (table.st[i].size() > maxChain) {
				maxChain = table.st[i].size();
			}
		}

		double percent = 100*(1-(numEmpty*1.0)/table.M);
		StdOut.println("Table has " + table.M + " indices.");
		StdOut.println("  there are " + numEmpty + " empty indices " + percent + "%");
		StdOut.println("  maximum chain is " + maxChain);
	}
}