package sorting;

import java.util.Arrays;

public class sortingAlgorithms {

//  源地址：https://zhuanlan.zhihu.com/p/57088609

//  选择排序：时间复杂度：O(n^2)
//           空间复杂度：O(1)
//           非稳定排序
//           原地排序
//  1. 找到数组中最小的那个元素，将它和数组的第一个元素交换位置
//  2. 在剩下的元素中找到最小的元素，将它与数组的第二个元素交换位置
//  3. 以此类推…
    public static int[] selectSort(int[] a) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i+1; j < n; j++) {
                if(a[min] > a[j])
                    min = j;
            }
            //交换
            int temp = a[i];
            a[i] = a[min];
            a[min] = temp;

            System.out.println(Arrays.toString(a));
        }
        return a;
    }

//  插入排序：时间复杂度：O(n^2)
//           空间复杂度：O(1)
//           稳定排序
//           原地排序
//  1. 从数组第2个元素开始抽取元素, 往左比较，直到遇到不比它大的元素，然后插到这个元素的右边。
//  2. 继续选取第3，4，5个元素..
    public static int[] insertSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int temp = a[i];
            int k = i - 1;
            //找小于他的元素
            while(k >= 0 && a[k] > temp)
                k--;
            //腾出位置插进去,要插的位置是 k + 1;
            for(int j = i ; j > k + 1; j--)
                a[j] = a[j-1];
            //插进去
            a[k+1] = temp;
            System.out.println(Arrays.toString(a));
        }
        return a;
    }

//  top2 冒泡排序：时间复杂度：O(n^2)
//           空间复杂度：O(1)
//           稳定排序
//           原地排序
//  1. 把第一个元素与第二个元素比较，如果第一个比第二个大，则交换他们的位置;
//     接着继续比较第二个与第三个元素，如果第二个比第三个大，则交换他们的位置...
//     这样一趟比较交换下来之后，排在最右的元素就会是最大的数。
//  2. 除去最右的元素，我们对剩余的元素做1的工作...
    public static int[] bubbleSort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length - i - 1; j++) {
                if (a[j + 1] < a[j]) {
                    int temp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = temp;
                }
                //System.out.println(Arrays.toString(a));
            }
        }
        return a;
    }
//    希尔排序：时间复杂度：O(nlogn)
//             空间复杂度：O(1)
//             非稳定排序
//             原地排序
//    先让数组中任意间隔为 h 的元素做insertSort，
//    刚开始 h 的大小是 h = n / 2,接着让 h = n / 4，让 h 一直缩小，
//    当 h = 1 时，数组中任意间隔为1的元素有序，此时的数组就是有序的了。
    public static int[] shellSort(int a[]) {
        int j, temp;
        // 对每组间隔为 h的分组进行排序，刚开始 h = n / 2;
        for (int increment = a.length / 2; increment > 0; increment /= 2) {
            //对各个局部分组进行插入排序
            System.out.println("间隔"+increment+"做insertionSort:");
            for (int i = increment; i < a.length; i++) {
                // System.out.println("i:" + i);
                temp = a[i];
                for (j = i - increment; j >= 0; j -= increment) {
                    if (temp < a[j]) {
                        a[j + increment] = a[j];
                    } else {
                        break;
                    }
                }
                a[j + increment] = temp;
                System.out.println(Arrays.toString(a));
            }
        }
        return a;
    }

//  top3 归并排序：时间复杂度：O(nlogn)
//           空间复杂度：O(1)
//           非稳定排序
//           原地排序
//  通过递归的方式将大的数组一直分割，直到数组的大小为1，
//  之后再把两个数组大小为1的合并成一个大小为2的，再把两个大小为2的合并成4的.....
//  直到全部小的数组合并起来。

    //递归
    public static int[] recurMergeSort(int[] arr, int left, int right){

        if(left >= right)
            return arr;

        int mid = (right+left)/2;
        arr = recurMergeSort(arr, left, mid);
        arr = recurMergeSort(arr, mid+1, right);
        recurMerge(arr, left, mid, right);

        return arr;
    }
    public static void recurMerge(int[] arr, int left, int mid, int right){
        //+1是因为？？
        int[] temp = new int[right-left+1];
        int array1start = left;
        int array2start = mid+1;
        int i = 0;
        while(array1start<=mid && array2start<=right){
            if(arr[array1start]<arr[array2start]){
                //i++和array1start++：先赋值，再+1
                temp[i++] = arr[array1start++];
            } else{
                temp[i++] = arr[array2start++];
            }
        }
        while(array1start<=mid) temp[i++]=arr[array1start++];
        while(array2start<=mid) temp[i++]=arr[array2start++];

        for(int a=0; a<i; a++){
            arr[left++] = temp[a];
        }
    }


    // 非递归式的归并排序
    public static int[] iterMergeSort(int[] a) {
        // 子数组的大小分别为1，2，4，8...
        // 刚开始合并的数组大小是1，接着是2，接着4....
        for (int i = 1; i < a.length; i += i) {
            //进行数组进行划分
            int left = 0;
            int mid = left + i - 1;
            int right = mid + i;
            //进行合并，对数组大小为 i 的数组进行两两合并
            while (right < a.length) {
                // 合并函数和递归式的合并函数一样
                iterMerge(a, left, mid, right);
                left = right + 1;
                mid = left + i - 1;
                right = mid + i;
            }
            // 还有一些被遗漏的数组没合并，千万别忘了
            // 因为不可能每个字数组的大小都刚好为 i
            if (left < a.length && mid < a.length) {
                iterMerge(a, left, mid, a.length - 1);
            }
            System.out.println(Arrays.toString(a));

        }
        return a;
    }

    // 合并函数，把两个有序的数组合并起来
    // arr[left..mif]表示一个数组，arr[mid+1 .. right]表示一个数组
    private static void iterMerge(int[] a, int left, int mid, int right) {
        //先用一个临时数组把他们合并汇总起来
        int[] temp = new int[right - left + 1];
        int i = left;
        int j = mid + 1;
        int k = 0;
        while (i <= mid && j <= right) {
            if (a[i] < a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
            }
        }
        while(i <= mid)
            temp[k++] = a[i++];
        while(j <= right)
            temp[k++] = a[j++];
        // 把临时数组复制到原数组
        for (i = 0; i < k; i++) {
            a[left++] = temp[i];
        }
    }

//  top1 快速排序：时间复杂度：O(nlogn)
//           空间复杂度：O(logn)
//           非稳定排序
//           原地排序
//  1. 选择第一个数作为pivot，比pivot小的放左边，比pivot大的放右边。
//  2. 在左边的数里取第一个作为pivot..
//     在右边的数里取第一个作为pivot..
    public static int[] quickSort(int[] arr, int left, int right) {
        if (left < right) {
            //获取中轴元素所处的位置
//            System.out.println("Pivot:"+arr[left]);
            int mid = partition(arr, left, right);
//            System.out.println(Arrays.toString(arr));
            //进行分割
            arr = quickSort(arr, left, mid - 1);
            arr = quickSort(arr, mid + 1, right);
        }
        return arr;
    }

    private static int partition(int[] arr, int left, int right) {
        //选取中轴元素
        int pivot = arr[left];
        int i = left + 1;
        int j = right;
        while (true) {
            // 向右找到第一个小于等于 pivot 的元素位置
            while (i <= j && arr[i] <= pivot) i++;
            // 向左找到第一个大于等于 pivot 的元素位置
            while(i <= j && arr[j] >= pivot ) j--;
            if(i >= j)
                break;
            //交换两个元素的位置，使得左边的元素不大于pivot,右边的不小于pivot
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        arr[left] = arr[j];
        // 使中轴元素处于有序的位置
        arr[j] = pivot;
        return j;
    }




}
