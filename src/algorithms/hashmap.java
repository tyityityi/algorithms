package algorithms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class hashmap {


    /**
     * LC1. 两数之和
     * https://leetcode-cn.com/problems/two-sum/
     * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 的那 两个 整数，并返回它们的数组下标。
     *
     * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
     *
     * 你可以按任意顺序返回答案。
     *
     *
     *
     * 示例 1：
     *
     * 输入：nums = [2,7,11,15], target = 9
     * 输出：[0,1]
     * 解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
     * 示例 2：
     *
     * 输入：nums = [3,2,4], target = 6
     * 输出：[1,2]
     * 示例 3：
     *
     * 输入：nums = [3,3], target = 6
     * 输出：[0,1]
     *
     *
     * 提示：
     *
     * 2 <= nums.length <= 103
     * -109 <= nums[i] <= 109
     * -109 <= target <= 109
     * 只会存在一个有效答案
     *
     */

    public static void testTwoSum(){
        int[] a = {2,5,9,6,11};
        System.out.println("Array: "+ Arrays.toString(a)+", target: "+"13");
        System.out.println("Result: "+Arrays.toString(twoSum(a, 13)));
    }

    private static int[] twoSum(int[] nums, int target){

        //Key=nums中的数字，value=数字在nums中的index
        //原因：通过key获取value只要只用get方法，但通过value拿key要遍历hashmap
        Map<Integer,Integer> hashMap = new HashMap<Integer, Integer>(nums.length-1);
        for(int i=0; i<nums.length; i++){
            if(hashMap.containsKey(target-nums[i])){
                return new int[]{hashMap.get(target-nums[i]), i};
            }
            hashMap.put(nums[i], i);
        }
        throw new IllegalArgumentException("Unsolvable!");
    }
}
