

public class Question0053 {

    public static int maxSubArray(int[] nums) {
        int pre = 0;
        int max = nums[0];
        for (int num : nums) {
            pre = Math.max(pre+num,num);
            max = Math.max(max,pre);
        }
        return max;
    }

    public static void main(String[] args) {
        int[] nums = {-2};
        System.out.println(maxSubArray(nums));
    }
}
