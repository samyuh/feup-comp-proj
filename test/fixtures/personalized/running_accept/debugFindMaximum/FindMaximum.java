package fixtures.personalized.running_accept.debugFindMaximum;

public class FindMaximum {
    int[] test_arr;

    public int build_test_arr() {
        test_arr = new int[5];
        test_arr[0] = 14;
        test_arr[1] = 28;
        test_arr[2] = 0;
        test_arr[3] = 0-5; // No unary minus in Java--
        test_arr[4] = 12;

        return 0;
    }


    public static void main(String[] args) {
        FindMaximum fm;
        int max;
        fm = new FindMaximum();
        fm.build_test_arr();
    }
}
