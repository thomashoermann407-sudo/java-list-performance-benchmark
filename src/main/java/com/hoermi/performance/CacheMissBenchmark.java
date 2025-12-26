package com.hoermi.performance;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
public class CacheMissBenchmark {

    @Param({"10000"})
    private int size;

    private List<Integer> contiguousList;
    private List<Integer> scatteredList;
    private List<Integer> arrayList;

    @Setup(Level.Trial)
    public void setup() {
        // 1. ArrayList (Baseline - contiguous memory)
        arrayList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(i);
        }

        // 2. Contiguous LinkedList
        // Allocating nodes sequentially in a tight loop usually places them 
        // adjacent in the TLAB (Thread Local Allocation Buffer).
        contiguousList = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            contiguousList.add(i);
        }

        // 3. Scattered LinkedList (Simulating Heap Fragmentation)
        // We create multiple lists and populate them in a round-robin fashion.
        // This ensures that nodes of 'scatteredList' are separated in memory 
        // by nodes of other lists.
        // A gap of 64 ensures that the next node is likely in a different cache line 
        // (and potentially confuses the prefetcher).
        int gap = 128; 
        List<LinkedList<Integer>> paddingLists = new ArrayList<>(gap);
        for (int i = 0; i < gap; i++) {
            paddingLists.add(new LinkedList<>());
        }
        
        // The list we will actually measure is index 0
        scatteredList = paddingLists.get(0);

        for (int i = 0; i < size; i++) {
            // Add to all lists in round-robin
            for (int j = 0; j < gap; j++) {
                // Use large values to avoid Integer cache and force object allocation
                paddingLists.get(j).add(i + 1000000); 
            }
        }
    }

    @Benchmark
    public int iterateArrayList() {
        int sum = 0;
        for (Integer i : arrayList) {
            sum += i;
        }
        return sum;
    }

    @Benchmark
    public int iterateLinkedListContiguous() {
        int sum = 0;
        for (Integer i : contiguousList) {
            sum += i;
        }
        return sum;
    }

    @Benchmark
    public int iterateLinkedListScattered() {
        int sum = 0;
        for (Integer i : scatteredList) {
            sum += i;
        }
        return sum;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CacheMissBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
