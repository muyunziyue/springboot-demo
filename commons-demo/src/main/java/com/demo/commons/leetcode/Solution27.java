package com.demo.commons.leetcode;

/**
 * @author ldx
 * @date 2022/7/20
 */
public class Solution27 {
    public int removeElement(int[] nums, int val) {
        /**
         * 1. 记录数组总长度 y
         * 2. 记录数组需要去除的元素个数x
         * 3. 遍历数组
         * 4. 如果元素值相同则迁移x 位
         * 5. 返回y-x
         */
        int y = nums.length;
        int x = 0;
        for (int i = 0; i < nums.length; i++) {
            if(nums[i] == val){
                ++x;
                continue;
            }
            nums[i-x] = nums[i];
        }

        return y -x;
    }

    public static void main(String[] args) {
        int[] test = {3,2,2,3};
        int val =3;
        int i = new Solution27().removeElement(test, val);
    }
}
