import java.util.HashMap;
import java.util.Map;

public class Question0013 {

    public static int romanToInt(String s) {
        Map<Character, Integer> map = new HashMap<>();
        map.put('I',1);
        map.put('V',5);
        map.put('X',10);
        map.put('L',50);
        map.put('C',100);
        map.put('D',500);
        map.put('M',1000);

        int n = s.length();
        if(n == 1){
            return map.get(s.charAt(0));
        }
        int res = 0;
        for (int i = 0; i < n; i++) {
            int value = map.get(s.charAt(i));
            if(i<n-1 && map.get(s.charAt(i+1)) >value){
                value = -value;
            }
            res = res + value;
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(romanToInt("MCMXCIV"));
    }
}
