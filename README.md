# Java List Performance Benchmarks

This project benchmarks various Java `List` implementations (`ArrayList`, `LinkedList`) as well as `ArrayDeque` and raw arrays (`int[]`, `Integer[]`) using JMH (Java Microbenchmark Harness).

## 🚀 Key Takeaways

1.  **Primitives are the fastest**: `int[]` is consistently **10x-20x faster** than any collection or wrapper array due to memory locality and lack of boxing overhead.
2.  **ArrayList is the Default**: For almost all general-purpose use cases (iteration, random access, appending), `ArrayList` outperforms `LinkedList`.
3.  **Avoid LinkedList for Random Access**: `LinkedList` is O(N) for `get(i)`, making it disastrously slow for random access compared to `ArrayList`'s O(1).
4.  **The LinkedList Niche**: `LinkedList` is only superior when using a `ListIterator` to insert or remove elements *during* iteration. In this specific scenario, it is **~50x faster** than `ArrayList`.

## 📊 Benchmark Results

### 1. Iteration (Traversing 100,000 elements)
*Comparing different ways to iterate over data.*

| Type | Method | Time (us/op) | Notes |
| :--- | :--- | :--- | :--- |
| **int[]** | `Stream` | **24.37** | Extremely optimized |
| **int[]** | `Loop` | 25.51 | Baseline |
| **ArrayList** | `Iterate` | 144.22 | Standard collection overhead |
| **ArrayDeque** | `Iterate` | 150.92 | Similar to ArrayList |
| **LinkedList** | `Iterate` | 366.55 | Slower due to pointer chasing |

### 2. Random Access (Get Index)
*Accessing an element at a specific index (Size: 100,000).*

| Type | Method | Time (us/op) | Complexity |
| :--- | :--- | :--- | :--- |
| **ArrayList** | `Get` | **~0.001** | O(1) - Instant |
| **LinkedList** | `Get` | 90.16 | O(N) - Linear scan |

### 3. Adding Elements (Append to End)
*Adding elements to the end of the collection (Size: 100,000).*

| Type | Method | Time (us/op) |
| :--- | :--- | :--- |
| **ArrayList** | `Add` | **382.53** |
| **ArrayDeque** | `Add` | 407.33 |
| **LinkedList** | `Add` | 633.73 |

### 4. Queue Operations (Add/Remove First)
*Using the collection as a Queue/Deque (Size: 100,000).*

| Type | Method | Time (us/op) | Notes |
| :--- | :--- | :--- | :--- |
| **ArrayDeque** | `Add First` | **493.91** | Best for Queues |
| **ArrayDeque** | `Remove First` | **496.39** | |
| **LinkedList** | `Add First` | 581.01 | |
| **LinkedList** | `Remove First` | 863.51 | |

### 5. The "Killer App" for LinkedList: ListIterator Modifications
*Inserting or removing elements **while iterating** (Size: 10,000).*
*This is where LinkedList shines.*

| Operation | ArrayList (us/op) | LinkedList (us/op) | Difference |
| :--- | :--- | :--- | :--- |
| **Iterator Add** | 3,355.22 | **73.99** | **~45x Faster** |
| **Iterator Remove** | 2,962.87 | **57.14** | **~52x Faster** |

> **Why?** `LinkedList` only needs to change pointers (O(1)) during iteration. `ArrayList` must shift all subsequent elements in memory (O(N)) for every single operation, leading to O(N²) complexity.

## 🛠️ How to Run

**Run the Benchmarks:**

```bash
# Build the project
mvn package -DskipTests

# Run the General List Benchmark (Fork=0 to avoid classpath issues in some envs)
mvn exec:java "-Dexec.mainClass=performance.ListBenchmark"

# Run the ListIterator Benchmark
mvn exec:java "-Dexec.mainClass=performance.ListIteratorBenchmark"
```
