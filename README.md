# Letter Frequencies: Parallel and Sequential Letter Counter in Java

## Prepared by:
Abdallah GHANEM

Elias ISHAK

Michel KHOURY (Aegon I in the recording)


### Drive link of the recoring:

https://drive.google.com/file/d/1l8u-F1qtxxAmVAcCtzjtH6-0Ejx7IFz5/view?usp=drive_link



## Overview
This project demonstrates sequential and parallel approaches to counting letter frequencies in large text files using Java. It is designed as a practical exercise in concurrent and parallel programming, showcasing the performance benefits and challenges of multithreading.

The code fetches multiple RFC documents from the web and counts the frequency of each letter (a-z) across all documents. Three implementations are provided:
- **LetterCounterSingleThread**: Sequential baseline
- **LetterCounter**: Parallel version using raw threads and synchronized blocks
- **AtomicLetterCounter**: Parallel version using atomic variables for thread-safe counting

## Project Structure
- `LetterCounterSingleThread.java`: Sequential implementation
- `LetterCounter.java`: Parallel implementation with synchronized blocks
- `AtomicLetterCounter.java`: Parallel implementation with atomic counters

## How It Works
Each implementation downloads a set of RFC documents (by default, RFC 1000 to RFC 1049) and counts the frequency of each letter (case-insensitive) in the text. The results and execution time are printed to the console.

## Usage
### Prerequisites
- Java 11 or higher
- Internet connection (to fetch RFC documents)

### Compile
Navigate to the `letterfrequencies` directory and compile all Java files:

```bash
javac *.java
```

### Run Sequential Version
```bash
java com.cutajarjames.multithreading.letterfrequencies.LetterCounterSingleThread
```

### Run Parallel Version (Synchronized)
```bash
java com.cutajarjames.multithreading.letterfrequencies.LetterCounter
```

### Run Parallel Version (Atomic)
```bash
java com.cutajarjames.multithreading.atomic.AtomicLetterCounter
```


### Docker
To build and run with docker, run the following bash command:
```bash
docker build -t multi-java-app .
docker run --rm multi-java-app
```


## Performance Testing
- The output includes the total time taken for each run.
- Compare the sequential and parallel versions to observe speed-up.
- For more rigorous benchmarking, run each version multiple times and average the results.

## Customization
- To change the range of RFCs processed, modify the loop bounds in the `main` method of each class.
- To process local files, adapt the URL handling logic accordingly.

## Notes
- The parallel versions demonstrate different synchronization strategies: explicit locks vs. atomic variables.
- Network I/O may affect timing; for pure CPU-bound tests, use local files.

## License
This project is for educational purposes.

## Comparison of Implementations

This project provides three different approaches to counting letter frequencies:

| File                       | Parallelism | Data Structure                    | Synchronization         | Expected Speed |
|----------------------------|-------------|-----------------------------------|-------------------------|---------------|
| LetterCounterSingleThread   | No          | HashMap<Character, Integer>       | None                    | Slowest       |
| LetterCounter               | Yes         | HashMap<Character, Integer>       | synchronized blocks     | Faster        |
| AtomicLetterCounter         | Yes         | HashMap<Character, AtomicInteger> | Atomic operations       | Fastest       |

### Details
- **LetterCounterSingleThread.java**: Processes files one after another in a single thread. No synchronization is needed. This is the slowest but simplest approach.
- **LetterCounter.java**: Spawns a thread for each file. All threads share a single map, so updates are protected with `synchronized` blocks. This is faster than the sequential version, but synchronization can become a bottleneck.
- **AtomicLetterCounter.java**: Spawns a thread for each file, but uses `AtomicInteger` for each letter's count. This allows thread-safe increments without locking the whole map, resulting in the best scalability and performance.

## Motivation: Which English Letters Are Most Frequent?

This project investigates which English letters appear most frequently in a set of large text documents (RFCs). It demonstrates three approaches to solving this problem:

1. **Single-threaded:** Processes all files one after another, providing a baseline for performance and correctness.
2. **Shared Memory with Synchronization:** Uses multiple threads that share a single frequency map, with updates protected by `synchronized (this)` blocks to avoid race conditions.
3. **Atomic Variables:** Uses multiple threads and a frequency map of `AtomicInteger` values, allowing safe concurrent updates without explicit locking.

By comparing these approaches, you can observe both the frequency distribution of English letters and the impact of different concurrency strategies on performance and scalability. 
