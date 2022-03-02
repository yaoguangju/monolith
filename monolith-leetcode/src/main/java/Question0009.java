public class Question0009 {

    public static boolean isPalindrome(int x) {
        int t = x;
        if(x<0){
            return false;
        }
        if(x>0){
            int y = 0;
            while (x != 0){
                int item = x % 10;
                y = y * 10 + item;
                x = x / 10;
            }
            return t == y;
        }
        return true;
    }

    public static void main(String[] args) {
        boolean palindrome = isPalindrome(121221);
        System.out.println(palindrome);

    }
}
