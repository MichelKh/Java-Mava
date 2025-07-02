package com.cutajarjames.multithreading.letterfrequencies;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LetterCounter {

    int finishedCount = 0; // Tracks how many threads have finished processing

    /**
     * Counts the frequency of each letter in the text at the given URL.
     * Uses synchronized blocks to avoid race conditions when updating the shared map.
     */
    public void countLetters(URL url, HashMap<Character, Integer> frequencyDict) {
        try {
            // Open a stream to the URL and read all bytes as a string
            var stream = url.openStream();
            var txt = new String(stream.readAllBytes());
            // Iterate over each character in the text
            for (char c : txt.toCharArray()) {
                var letter = Character.toLowerCase(c);
                // Synchronize on 'this' to avoid race conditions when updating the map
                /*avoid race condition by synchronizing on this*/
                synchronized (this) {
                    if (frequencyDict.containsKey(letter))
                        frequencyDict.put(letter, frequencyDict.get(letter) + 1);
                }
            }
            stream.close();
            // Mark this thread as finished
            synchronized (this) {
                finishedCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        // Create an instance of the parallel letter counter
        var letterCounter = new LetterCounter();
        // Initialize the frequency dictionary for a-z
        var frequencyDict = new HashMap<Character, Integer>();
        for (char c : "abcdefghijklmnopqrstuvwxyz".toCharArray())
            frequencyDict.put(c, 0);
        // Start timing
        var start = System.nanoTime();
        // Start a new thread for each RFC document
        for (int i = 1000; i < 1050; i++) {
            var url = new URL("https://www.rfc-editor.org/rfc/rfc%s.txt".formatted(i));
            new Thread(() -> letterCounter.countLetters(url, frequencyDict)).start();
        }
        // Wait until all threads have finished
        while (letterCounter.finishedCount < 50)
            TimeUnit.MILLISECONDS.sleep(500);
        // End timing
        var end = System.nanoTime();
        long durationMs = (end - start) / 1_000_000;
        double durationSec = (end - start) / 1_000_000_000.0;
        System.out.printf("Done. Time taken: %d ms (%.2f s)\n", durationMs, durationSec);
        // Print the frequency of each letter
        frequencyDict.forEach((k, v) -> System.out.println(k + ", " + v));
    }
}
