import algorithms.DFS;
import algorithms.hashmap;
import algorithms.lookup;
import domain.ListNode;
import sorting.sortingAlgorithms;
import algorithms.recursive;

import javax.sound.midi.Soundbank;
import java.util.*;

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

//        int[][] memo = new int[5][5];
//        System.out.println(memo[5][5]);

//        LinkedList<Integer> nodes = new LinkedList<>();
//        nodes.addLast(1);
//        System.out.println(nodes.removeLast());
//        System.out.println(nodes.removeLast());//报错，NoSuchElementException

//        List<List<Integer>> res = new LinkedList<>();
//        LinkedList<Integer> track = new LinkedList<>();
//        track.add(1);
//        track.add(2);
//        res.add(track);
//        track.add(3);
//        System.out.println(res);

//        int[][] a = new int[3][5];
//        System.out.println(a.length);
//        List<List<String>> results = new LinkedList<>();
//        char[][] board = new char[5][5];
//        for (int i = 0; i < 5; i++) {
//            char[] row = new char[5];
//            Arrays.fill(row, '.');
//            board[i] = row;
//        }
//
//
//        List<String> result = new LinkedList<>();
//        for(int row=0; row<5; row++){
//            String resRow = String.valueOf(board[0]);
//            result.add(resRow);
//        }
//        results.add(result);
//        System.out.println(results);
//        int a = (5/3)*3;
//        System.out.println(a);


        Map<Character, Integer> target = new HashMap<>(), window = new HashMap<>();
//        int start = -1;
//        int end = Integer.MAX_VALUE;
//        System.out.println(end);
//        System.out.println(start);
//        System.out.println(end - start);
//        System.out.println(Integer.MAX_VALUE-0);
//        Integer i3 = 400;
//        Integer i4 = 400;
//        System.out.println(i3==i4);
//        System.out.println(i3.equals(i4));
//        ArrayList<Integer> a = new ArrayList<>();
//        Map<Integer, Integer> hm = new HashMap<>();
//        PriorityQueue<int[]> minHeap = new PriorityQueue<>((o1, o2) -> o2[0]-o1[0]);
//        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
//        minHeap.offer(2);
//        minHeap.offer(2);
//        //System.out.println(minHeap.poll());
//        System.out.println(minHeap.poll());
//        String s = "aab";
//        char[] chars = s.toCharArray();
//        for(int i=0; i<chars.length; i++)
//            System.out.println(chars[i]);
//        List<Long> list = new ArrayList<>();
//        list.add(1l);
//        list.add(2l);
//        list.remove(0);
//        list.add(5, 5l);
//        System.out.println(list.get(5));
        ListNode l1 = new ListNode(2);
        l1.next = new ListNode(4);
        l1.next.next = new ListNode(3);
        ListNode l2 = new ListNode(5);
        l2.next = new ListNode(6);
        l2.next.next = new ListNode(4);
        ListNode res = addTwoNumbers(l1,l2);
        while(res!=null){
            System.out.println(res.val);
            res = res.next;
        }



    }
    private static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int jinweiFlag = 0;
        ListNode dummy = new ListNode();
        ListNode curr = new ListNode();
        dummy.next = curr;
        while(l1!=null && l2!=null){
            if(l1.val+l2.val<10){
                curr.val = l1.val+l2.val + jinweiFlag==0?0:1;
                curr.next = new ListNode();;
                curr = curr.next;
                jinweiFlag = 0;
            } else {
                curr.val = l1.val+l2.val - 10 + jinweiFlag==0?0:1;
                curr.next = new ListNode();;
                curr = curr.next;
                jinweiFlag = 1;
            }
        }
        if(l1!=null){
            while(l1!=null){
                curr.val = l1.val;
                l1 = l1.next;
            }
        }
        if(l2!=null){
            while(l2!=null){
                curr.val = l1.val;
                l2 = l2.next;
            }
        }
        return dummy.next;
    }
}
