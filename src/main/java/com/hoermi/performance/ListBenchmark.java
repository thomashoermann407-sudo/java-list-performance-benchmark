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
@Fork(1)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 2, time = 1)
public class ListBenchmark {

    @Param({"100", "1000", "10000", "100000"})
    private int size;

    private List<Integer> arrayList;
    private List<Integer> linkedList;
    private Deque<Integer> arrayDeque;
    private int[] array;
    private Integer[] integerArray;
    private int accessIndex;

    @Setup(Level.Trial)
    public void setup() {
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
        arrayDeque = new ArrayDeque<>();
        array = new int[size];
        integerArray = new Integer[size];
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            int val = r.nextInt();
            arrayList.add(val);
            linkedList.add(val);
            arrayDeque.add(val);
            array[i] = val;
            integerArray[i] = val;
        }
        accessIndex = size/2;
    }

    @Benchmark
    public int arrayListGet() {
        return arrayList.get(accessIndex);
    }

    @Benchmark
    public int linkedListGet() {
        return linkedList.get(accessIndex);
    }

    @Benchmark
    public int arrayListIterate() {
        int sum = 0;
        for (Integer i : arrayList) {
            sum += i;
        }
        return sum;
    }

    @Benchmark
    public int linkedListIterate() {
        int sum = 0;
        for (Integer i : linkedList) {
            sum += i;
        }
        return sum;
    }

    @Benchmark
    public int arrayListClassicalFor() {
        int sum = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            sum += arrayList.get(i);
        }
        return sum;
    }

    @Benchmark
    public int linkedListClassicalFor() {
        int sum = 0;
        for (int i = 0; i < linkedList.size(); i++) {
            sum += linkedList.get(i);
        }
        return sum;
    }

    @Benchmark
    public int arrayListStream() {
        return arrayList.stream().mapToInt(i -> i).sum();
    }

    @Benchmark
    public int linkedListStream() {
        return linkedList.stream().mapToInt(i -> i).sum();
    }

    @Benchmark
    public List<Integer> arrayListAdd() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

    @Benchmark
    public List<Integer> linkedListAdd() {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

    @Benchmark
    public List<Integer> arrayListAddFirst() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(0, i);
        }
        return list;
    }

    @Benchmark
    public List<Integer> linkedListAddFirst() {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            list.add(0, i);
        }
        return list;
    }

    @Benchmark
    public List<Integer> arrayListRemoveFirst() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        while (!list.isEmpty()) {
            list.remove(0);
        }
        return list;
    }

    @Benchmark
    public List<Integer> linkedListRemoveFirst() {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        while (!list.isEmpty()) {
            list.remove(0);
        }
        return list;
    }

    @Benchmark
    public int arrayGet() {
        return array[accessIndex];
    }

    @Benchmark
    public int arrayLoop() {
        int sum = 0;
        for (int i : array) {
            sum += i;
        }
        return sum;
    }

    @Benchmark
    public int arrayClassicalLoop() {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }

    @Benchmark
    public int arrayStream() {
        return Arrays.stream(array).sum();
    }

    @Benchmark
    public int[] arrayFill() {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
        return arr;
    }

    @Benchmark
    public List<Integer> arrayListSubList() {
        return arrayList.subList(0, size / 2);
    }

    @Benchmark
    public int[] arraySubList() {
        return Arrays.copyOfRange(array, 0, size / 2);
    }

    @Benchmark
    public int integerArrayGet() {
        return integerArray[accessIndex];
    }

    @Benchmark
    public int integerArrayLoop() {
        int sum = 0;
        for (Integer i : integerArray) {
            sum += i;
        }
        return sum;
    }

    @Benchmark
    public int integerArrayClassicalLoop() {
        int sum = 0;
        for (int i = 0; i < integerArray.length; i++) {
            sum += integerArray[i];
        }
        return sum;
    }

    @Benchmark
    public int integerArrayStream() {
        return Arrays.stream(integerArray).mapToInt(i -> i).sum();
    }

    @Benchmark
    public Integer[] integerArrayFill() {
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
        return arr;
    }

    @Benchmark
    public Integer[] integerArraySubList() {
        return Arrays.copyOfRange(integerArray, 0, size / 2);
    }

    @Benchmark
    public int arrayDequeIterate() {
        int sum = 0;
        for (Integer i : arrayDeque) {
            sum += i;
        }
        return sum;
    }

    @Benchmark
    public int arrayDequeStream() {
        return arrayDeque.stream().mapToInt(i -> i).sum();
    }

    @Benchmark
    public Deque<Integer> arrayDequeAdd() {
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < size; i++) {
            deque.add(i);
        }
        return deque;
    }

    @Benchmark
    public Deque<Integer> arrayDequeAddFirst() {
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < size; i++) {
            deque.addFirst(i);
        }
        return deque;
    }

    @Benchmark
    public Deque<Integer> arrayDequeRemoveFirst() {
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < size; i++) {
            deque.add(i);
        }
        while (!deque.isEmpty()) {
            deque.removeFirst();
        }
        return deque;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ListBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
