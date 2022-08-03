package com.demo.commons.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ldx
 * @date 2022/7/20
 */
public class Solution {
    public List<List<Integer>> shiftGrid(int[][] grid, int k) {
        int length = grid.length * grid[0].length;

        LinkedList<Integer> list = new LinkedList<>();
        for (int[] ints : grid) {
            for (int anInt : ints) {
                list.add(anInt);
            }
        }
        for (int i = 0; i < k; i++) {
            moveOneStep(list);
        }

        List<List<Integer>> resultList = new ArrayList<>(length);
        for (int i = 0; i < grid.length; i++) {
            ArrayList<Integer> secondDimList = new ArrayList<>(grid[i].length);
            for (int i1 = 0; i1 < grid[i].length; i1++) {
                secondDimList.add(list.removeFirst());
            }
            resultList.add(secondDimList);
        }

        return resultList;
    }

    private void moveOneStep(LinkedList<Integer> list) {
        Integer integer = list.removeLast();
        list.addFirst(integer);
    }


    public static void main(String[] args) {
        int[][] test = {{1,2,3},{4,5,6},{7,8,9}};
        int k = 1;
        Solution solution = new Solution();
        solution.shiftGrid(test, k);
    }
}
