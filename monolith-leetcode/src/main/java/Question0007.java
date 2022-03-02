
/**
 * 7. 整数反转
 */
public class Question0007 {
    public static int reverse(int x) {
        int res = 0;
        while (x != 0){
            if (res < Integer.MIN_VALUE / 10 || res > Integer.MAX_VALUE / 10) {
                return 0;
            }
            int temp = x % 10;
            res = res * 10 + temp;
            x = x/10;
        }
        return res;
    }

    public static void main(String[] args) {
        int reverse = reverse(-123);
        System.out.println(reverse);
    }
}
