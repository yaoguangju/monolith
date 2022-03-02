import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']'的字符串 s ，判断字符串是否有效。
 *
 * 有效字符串需满足：
 *
 * 左括号必须用相同类型的右括号闭合。
 * 左括号必须以正确的顺序闭合。
 *
 */
public class Question0020 {
    public static boolean isValid(String s) {
        int n = s.length();
        if(n % 2 == 1){
            return false;
        }
        Map<Character,Integer> map = new HashMap<>();
        map.put('(',1);
        map.put(')',-1);
        map.put('{',2);
        map.put('}',-2);
        map.put('[',3);
        map.put(']',-3);
        LinkedList<Character> stack = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char a = s.charAt(i);
            if(map.get(a) > 0){
                stack.addLast(a);
                continue;
            }
            if(stack.isEmpty()){
                return false;
            }
            char b = stack.removeLast();
            if(map.get(a) + map.get(b) != 0){
                return false;
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        System.out.println(isValid("()[]{}"));
    }
}
