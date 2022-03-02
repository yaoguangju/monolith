import java.util.Arrays;

/**
 * 1.两数之和
 */
public class Question0001 {
    public static int[] twoSum(int[] nums, int target) {
        for (int i = 0; i <= nums.length - 1; i++) {
            for (int j = i + 1; j <= nums.length; j++) {
                if(nums[i] + nums[j] == target){
                    return new int[]{i,j};
                }
            }
        }
        return new int[]{};
    }

    public static void main(String[] args) {
        int[] ints = twoSum(new int[]{2,7,11,15}, 9);
        System.out.println(Arrays.toString(ints));
    }
}

