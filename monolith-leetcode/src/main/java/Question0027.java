




public class Question0027 {

    public static int removeElement(int[] nums, int val) {
        if(nums.length == 0){
            return 0;
        }
        int i = 0;
        int j = 0;
        for (int k = 0; k < nums.length; k++) {
            if (nums[j] != val) {
                nums[i] = nums[j];
                i++;
            }
            j++;
        }
        return i;
    }

    public static void main(String[] args) {
        int[] nums = {1,2,2,4,5};
        int val = 2;
        int i = removeElement(nums, val);
        System.out.println(i);

    }
}
