package com.cutajarjames.multithreading.atomic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicLetterCounter {
    /**
     * Counts the frequency of each letter in the text at the given URL.
     * Uses AtomicInteger for thread-safe increments without explicit locking.
     */
    public void countLetters(URL url, HashMap<Character, AtomicInteger> frequencyDict) {
        try {
            // Open a stream to the URL and read all bytes as a string
            var stream = url.openStream();
            var txt = new String(stream.readAllBytes());
            // Iterate over each character in the text
            for (char c : txt.toCharArray()) {
                var letter = Character.toLowerCase(c);
                // If the character is a tracked letter, increment its count atomically
                if (frequencyDict.containsKey(letter))
                    frequencyDict.get(letter).incrementAndGet();
                    /*avoid race condition by using atomic integer*/
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        // Create an instance of the atomic letter counter
        var letterCounter = new AtomicLetterCounter();
        // Initialize the frequency dictionary for a-z, each with an AtomicInteger
        var frequencyDict = new HashMap<Character, AtomicInteger>();
        for (char c : "abcdefghijklmnopqrstuvwxyz".toCharArray())
            frequencyDict.put(c, new AtomicInteger(0));
        // Start timing
        var start = System.nanoTime();
        var workers = new ArrayList<Thread>();
        // Start a new thread for each RFC document
        for (int i = 1000; i < 1050; i++) {
            var url = new URL("https://www.rfc-editor.org/rfc/rfc%s.txt".formatted(i));
            var worker = new Thread(() -> letterCounter.countLetters(url, frequencyDict));
            workers.add(worker);
            worker.start();
        }
        // Wait for all threads to finish
        for (Thread worker: workers)
            worker.join();
        // End timing
        var end = System.nanoTime();
        long durationMs = (end - start) / 1_000_000;
        double durationSec = (end - start) / 1_000_000_000.0;
        System.out.printf("Done. Time taken: %d ms (%.2f s)\n", durationMs, durationSec);
        // Print the frequency of each letter
        frequencyDict.forEach((k, v) -> System.out.println(k + ", " + v));
    }
}
