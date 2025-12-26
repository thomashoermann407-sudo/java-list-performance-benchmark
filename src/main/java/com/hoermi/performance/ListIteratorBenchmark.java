package com.hoermi.performance;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
@Fork(0)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 2, time = 1)
public class ListIteratorBenchmark {

    @Param({"100", "1000", "10000"})
    private int size;

    private List<Integer> arrayListTemplate;
    private List<Integer> linkedListTemplate;

    @Setup(Level.Trial)
    public void setup() {
        arrayListTemplate = new ArrayList<>();
        linkedListTemplate = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            arrayListTemplate.add(i);
            linkedListTemplate.add(i);
        }
    }

    @Benchmark
    public void arrayListListIteratorRemove() {
        List<Integer> list = new ArrayList<>(arrayListTemplate);
        ListIterator<Integer> it = list.listIterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    @Benchmark
    public void linkedListListIteratorRemove() {
        List<Integer> list = new LinkedList<>(linkedListTemplate);
        ListIterator<Integer> it = list.listIterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    @Benchmark
    public void arrayListListIteratorAdd() {
        List<Integer> list = new ArrayList<>(arrayListTemplate);
        ListIterator<Integer> it = list.listIterator();
        while (it.hasNext()) {
            it.next();
            it.add(0);
        }
    }

    @Benchmark
    public void linkedListListIteratorAdd() {
        List<Integer> list = new LinkedList<>(linkedListTemplate);
        ListIterator<Integer> it = list.listIterator();
        while (it.hasNext()) {
            it.next();
            it.add(0);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ListIteratorBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
