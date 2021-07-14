import algorithms.hashmap;
import algorithms.lookup;
import sorting.sortingAlgorithms;
import algorithms.recursive;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class placeToRun {

    private static int[] origin;

    public static void initOriginArrayForSorting(){
        origin = new int[5];
        origin[0] = 4;
        origin[1] = 3;
        origin[2] = 5;
        origin[3] = 1;
        origin[4] = 2;
        System.out.println("Origin:");
        System.out.println(Arrays.toString(origin));
    }

    public static void runSortingAlgorithms(){
        initOriginArrayForSorting();
        //需测试的排序算法
        int[] result = sortingAlgorithms.quickSort(origin,0, origin.length-1);
        printArray("Result:", result);
    }

    private static void printArray(String pre,int[] a) {
        System.out.print(pre+"\n");
        for(int i=0;i<a.length;i++)
            System.out.print(a[i]+"\t");
        System.out.println();
    }

    public static void main(String[] args) {
        //runSortingAlgorithms();
        //lookup.testSetZeros();
        //lookup.testMaxArea();
        //lookup.testLongestConsecutive();
        //lookup.testExist();
        //lookup.testConvertZtoString();
        //hashmap.testTwoSum();
        //recursive.testFactorized(5);
        //recursive.testFib(7);
//        int[] a = {26, 5, 98, 108, 28, 99, 100, 56, 34, 1 };
//        printArray("排序前：",a);
//        int[] arr = sortingAlgorithms.recurMergeSort(a, 0, a.length-1);
//        printArray("排序后：",arr);

//        Map<String, Integer> nodesFreq = new HashMap<>();
//        nodesFreq.put("s",0);
//        nodesFreq.put("s",1);
//
//        int freq = nodesFreq.get("a");
//        System.out.println(freq);

        int[][] memo = new int[5][5];
        System.out.println(memo[5][5]);



    }
}
