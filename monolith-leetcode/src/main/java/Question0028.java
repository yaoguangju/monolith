

public class Question0028 {

    public static int strStr(String haystack, String needle) {
        int hl = haystack.length();
        int nl = needle.length();

        if(nl == 0){
            return 0;
        }
        char ns = needle.charAt(0);
        for (int i = 0; i < hl; i++) {
            char hs = haystack.charAt(i);
            if(hs == ns){
                if(hl - i >= nl){
                    for (int j = 0; j < nl; j++) {
                        if(haystack.charAt(i+j) != needle.charAt(j)){
                            break;
                        }
                        if(j == nl - 1){
                            return i;
                        }
                    }
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {

        String a = "a";
        String b = "a";
        final int i = strStr(a, b);
        System.out.println(i);
    }
}
