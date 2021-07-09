# Sorting 排序

------

- ### 快速排序

    1. 选择第一个数作为pivot，比pivot小的放左边，比pivot大的放右边。
    2. 在左边的数里取第一个作为pivot..
       在右边的数里取第一个作为pivot..

<img src="./imgs/quicksort.gif" style="zoom:50%;" />

```java
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
        // 向右找到第一个大于 pivot 的元素位置
        while (i <= j && arr[i] <= pivot) i++;
        // 向左找到第一个小于 pivot 的元素位置
        while (i <= j && arr[j] >= pivot ) j--;
      	if (i >= j)
        		break;
      	//交换两个元素的位置, 使得左边的元素不大于pivot, 右边的元素不小于pivot
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    arr[left] = arr[j];
    // 使中轴元素处于有序的位置
    arr[j] = pivot;
    return j;
}
```

时间复杂度：O(nlogn)
空间复杂度：O(logn)
非稳定排序
原地排序

- ### 归并排序

1. 通过递归的方式将大的数组一直分割，直到数组的大小为1，
2. 之后再把两个数组大小为1的合并成一个大小为2的，再把两个大小为2的合并成4的.....
    直到全部小的数组合并起来。

递归实现

```java
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
```

迭代实现

```java
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

```

时间复杂度：O(nlogn)

空间复杂度：O(1)

非稳定排序

原地排序

## 