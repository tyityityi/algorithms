package algorithms;

public class array {

    /**
     * [LC704. 二分查找](https://leetcode-cn.com/problems/binary-search/)
     */
    public int binarySearch(int[] nums, int target) {
        int left = 0, right = nums.length-1;
        while(left<=right){//终止条件是 left == right + 1
            //计算 mid 时需要防止溢出，代码中 left + (right - left) / 2 就和 (left + right) / 2 的结果相同
            int mid = left + (right-left)/2;
            if(nums[mid]<target){
                left = mid + 1;
            }else if(nums[mid]>target){
                right = mid - 1;
            }else if(nums[mid]==target){
                return mid;
            }
        }
        return -1;
    }
    /**
     * [LC34. 在排序数组中查找元素的第一个和最后一个位置](https://leetcode-cn.com/problems/find-first-and-last-position-of-element-in-sorted-array/)
     */
    public int leftBound(int[] nums, int target){
        int left = 0, right = nums.length-1;
        while(left<=right){
            int mid = left + (right-left)/2;
            if(nums[mid]<target)
                left = mid + 1;
            else if(nums[mid]>target)
                right = mid -1;
            else if(nums[mid]==target)
                // 别返回，锁定左侧边界
                right = mid - 1;
            //如果此时mid为左边界，left会一直加1，直到left==right时，left+1 = 此时的mid
        }
        // 最后要检查 left 越界的情况
        if(left>=nums.length || nums[left]!=target)
            return -1;
        return left;
    }
    public int rightBound(int[] nums, int target){
        int left = 0, right = nums.length-1;
        while(left<=right){
            int mid = left + (right-left)/2;
            if(nums[mid]<target)
                left = mid + 1;
            else if(nums[mid]>target)
                right = mid -1;
            else if(nums[mid]==target)
                // 别返回，锁定右侧边界
                left = mid + 1;
            //如果此时mid为右边界，right会一直减1，直到right==left时，right-1 = 此时的mid
        }
        // 最后要检查 right 越界的情况
        if(right<0 || nums[right]!=target)
            return -1;
        return right;
    }
    public int[] binarySearchRange(int[] nums, int target) {
        int leftBound = leftBound(nums, target);
        int rightBound = rightBound(nums, target);
        return new int[]{leftBound, rightBound};
    }

    /**
     * [LC875. 爱吃香蕉的珂珂](https://leetcode-cn.com/problems/koko-eating-bananas/)
     */
    //f = timeForEatingOut
    // 定义：速度为 k 时，需要 f(x) 小时吃完所有香蕉
    // f(k) 随着 k 的增加单调递 减
    public int timeForEatingOut(int[] piles, int k){
        int hrs = 0;
        for(int i=0; i<piles.length; i++){
            hrs += piles[i]/k;
            if(piles[i]%k!=0)
                hrs+=1;
        }
        return hrs;
    }
    public int minEatingSpeed(int[] piles, int h) {
        int left = 1;
        int right = 1000000000;//也可遍历piles找到最大值

        while(left<=right){
            int mid = left + (right-left)/2;
            //实际吃完的时间小于规定时间，说明k太大（吃的太快），k的值需要减小（右边界减小）
            if(timeForEatingOut(piles, mid)<h)
                right = mid - 1;
                //实际吃完的时间大于规定时间，说明k太小（吃的太慢），k的值需要增大（左边界加大）
            else if(timeForEatingOut(piles, mid)>h)
                left = mid + 1;
                //实际吃完的时间刚刚好，k的值可以继续减小（收缩右边界来搜索左侧边界）
            else if(timeForEatingOut(piles, mid)==h)
                right = mid -1;
        }
        return left;
    }

    /**
     * [LC1011. 在 D 天内送达包裹的能力](https://leetcode-cn.com/problems/capacity-to-ship-packages-within-d-days/)
     */
    //x为船的运载能力, 返回运送所有货物所需时间
    public int daysToShipAll(int[] weights, int x){
        int totalDays = 1, count = 0;
        for(int i=0; i<weights.length; i++){
            count += weights[i];
            if(count>x){
                //今天的运力不足，放到明天运
                count = weights[i];
                totalDays += 1;
            }
        }
        return totalDays;
    }
    public int shipWithinDays(int[] weights, int days) {
        int left = -1;
        int right = 0;
        for(int weight: weights){
            //船的最小载重应该是 weights 数组中元素的最大值，因为每次至少得装一件货物走，不能说装不下嘛。
            left = Math.max(left, weight);
            //最大载重显然就是weights 数组所有元素之和，也就是一次把所有货物都装走。
            right += weight;
        }

        while(left<=right){
            int mid = left + (right-left)/2;
            // <时船的运力过大，要减小
            // =时可以继续减小船的运力
            if(daysToShipAll(weights, mid)<=days)
                right = mid - 1;
                // >时船的运力不够，要增加
            else
                left = mid + 1;
        }
        return left;
    }
    /**
     * [LC410. 分割数组的最大值](https://leetcode-cn.com/problems/split-array-largest-sum/)
     */
    //限制一个最大子数组和x，f(x)返回最大子数组和为 x 时，可以将 nums分割成几个子数组。
    //f(x)会是关于x的单调递减函数。
    public int numOfSplits(int[] nums, int x){
        int totalSplits = 1;
        int count = 0;
        for(int i=0; i<nums.length; i++){
            count += nums[i];
            if(count>x){
                totalSplits += 1;
                count = nums[i];
            }
        }
        return totalSplits;
    }
    public int splitArray(int[] nums, int m) {
        //left（最小值）为数组中最大元素的值，right（最大值）为数组中所有元素和
        int left = -1, right = -1;
        for(int i=0; i<nums.length; i++){
            left = Math.max(left, nums[i]);
            right += nums[i];
        }

        while(left<=right){
            int mid = left + (right-left)/2;
            // 如果最大子数组和是 max，
            // 至少可以把 nums 分割成 n 个子数组
            int fx = numOfSplits(nums, mid);
            if(fx<m)
                // fx小了，说明x大了，减小一些
                right = mid - 1;
            else if(fx>m)
                // fx大了，说明x小了，增大一些
                left = mid + 1;
            else if(fx==m)
                // 收缩右边界，达到搜索左边界的目的
                right = mid - 1;
        }
        return left;
    }








}
