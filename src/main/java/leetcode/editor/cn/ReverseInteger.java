package leetcode.editor.cn;
//给你一个 32 位的有符号整数 x ，返回将 x 中的数字部分反转后的结果。
//
// 如果反转后整数超过 32 位的有符号整数的范围 [−231, 231 − 1] ，就返回 0。 
//假设环境不允许存储 64 位整数（有符号或无符号）。
//
// 
//
// 示例 1： 
//
// 
//输入：x = 123
//输出：321
// 
//
// 示例 2： 
//
// 
//输入：x = -123
//输出：-321
// 
//
// 示例 3： 
//
// 
//输入：x = 120
//输出：21
// 
//
// 示例 4： 
//
// 
//输入：x = 0
//输出：0
// 
//
// 
//
// 提示： 
//
// 
// -231 <= x <= 231 - 1 
// 
// Related Topics 数学 
// 👍 2939 👎 0


public class ReverseInteger {
    public static void main(String[] args) {
        Solution solution = new ReverseInteger().new Solution();
        System.out.println(solution.reverse(1534236469));
    }

    //7-整数反转
    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int reverse(int x) {
            boolean positive = true;
            if (x < 0) {
                positive = false;
            }
            x = Math.abs(x);
            char[] chars = String.valueOf(x).toCharArray();
            char tmp;
            for (int i = 0; i < chars.length / 2; i++) {
                tmp = chars[i];
                chars[i] = chars[chars.length - 1 - i];
                chars[chars.length - 1 - i] = tmp;
            }
            int i;
            try {
                i = Integer.parseInt(String.copyValueOf(chars));
            } catch (NumberFormatException e) {
                i = 0;
            }

            if (positive) {
                return i;
            } else {
                return -i;
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
