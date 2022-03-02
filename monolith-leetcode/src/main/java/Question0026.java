public class Question0026 {
    public static int removeDuplicates(int[] nums) {

        if(nums.length == 0){
            return 0;
        }
        int a = 1;
        for (int i = 1; i < nums.length; i++) {
            if(nums[i] != nums[i-1]){
                nums[a] = nums[i];
                a++;
            }
        }
        return a;
    }

    public static void main(String[] args) {
        int[] arr = {1, 1,1,2,3, 3};
        System.out.println(removeDuplicates(arr));

    }
}
