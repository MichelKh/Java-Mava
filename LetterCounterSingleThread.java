package com.cutajarjames.multithreading.letterfrequencies;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class LetterCounterSingleThread {
    /**
     * Counts the frequency of each letter in the text at the given URL.
     * Updates the provided frequencyDict in place.
     */
    public void countLetters(URL url, HashMap<Character, Integer> frequencyDict) {
        try {
            // Open a stream to the URL and read all bytes as a string
            var stream = url.openStream();
            var txt = new String(stream.readAllBytes());
            // Iterate over each character in the text
            for (char c : txt.toCharArray()) {
                var letter = Character.toLowerCase(c);
                //so that its case insensitive 
                // If the character is a tracked letter, increment its count
                //if its not a letter do nothing 
                if (frequencyDict.containsKey(letter))
                    frequencyDict.put(letter, frequencyDict.get(letter) + 1);
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws MalformedURLException {
        // Create an instance of the single-threaded letter counter
        var letterCounter = new LetterCounterSingleThread();
        // Initialize the frequency dictionary for a-z
        var frequencyDict = new HashMap<Character, Integer>();
        for (char c : "abcdefghijklmnopqrstuvwxyz".toCharArray())
            frequencyDict.put(c, 0);
        // Start timing
        var start = System.nanoTime();
        // Process RFC 1000 to RFC 1049 sequentially
        for (int i = 1000; i < 1050; i++) {
            var url = new URL("https://www.rfc-editor.org/rfc/rfc%s.txt".formatted(i));
            letterCounter.countLetters(url, frequencyDict);
        }
        // End timing
        var end = System.nanoTime();
        long durationMs = (end - start) / 1_000_000;
        double durationSec = (end - start) / 1_000_000_000.0;
        System.out.printf("Done. Time taken: %d ms (%.2f s)\n", durationMs, durationSec);
        // Print the frequency of each letter
        frequencyDict.forEach((k, v) -> System.out.println(k + ", " + v));
    }
}
