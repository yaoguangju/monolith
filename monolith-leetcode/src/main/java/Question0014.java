public class Question0014 {
    public static String longestCommonPrefix(String[] strs) {
        if(strs.length == 0){
            return "";
        }
        if(strs.length == 1){
            return strs[0];
        }

        StringBuilder res = new StringBuilder();

        int min = strs[0].length();
        for (String str : strs) {
            if(str.length()<min){
                min = str.length();
            }
        }
        for (int i = 0; i < min; i++) {
            char temp = strs[0].charAt(i);
            for (String str : strs) {
                if(str.charAt(i)!= temp){
                    return str.substring(0,i);
                }
            }
            res.append(temp);
        }
        return String.valueOf(res);
    }

    public static void main(String[] args) {

        String[] strs = {"ab", "a"};
        String s = longestCommonPrefix(strs);
        System.out.println(s);

    }
}
