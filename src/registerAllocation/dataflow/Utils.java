package registerAllocation.dataflow;

import java.util.Arrays;

public class Utils {

    /**
     * Helper function to print a matrix.
     * @param mat Matrix.
     */
    public static void printMatrix(Object[][] mat) {
        System.out.print("[");
        for (int i = 0; i < mat.length; i++) {
            System.out.print("[ ");
            for (int j = 0; j < mat[i].length; j++)
                System.out.print(mat[i][j] + " ");
            System.out.print("]");
        }
        System.out.print("]");
    }

    /**
     * Helper function to print the variables defined in a method.
     */
    public static void printArray(Object[] array){
        System.out.print("[ ");
        for (int i = 0 ; i < array.length; i++){
            System.out.print(array[i] + " ");
        }
        System.out.println("]");
    }

    public static String[][] deepCopyMatrix(String[][] matrix){
        if (matrix == null){
            return null;
        }

        String[][] result = matrix.clone();
        for (int i = 0 ; i  < matrix.length; i++){
            if (matrix[i] == null)  result[i] = new String[]{};
            else result[i] = matrix[i].clone();
        }
        return result;
    }

    public static Boolean compareMatrix(Object[][] arr1, Object[][] arr2){
        if (arr1.length != arr2.length)
            return false;
        for (int i = 0; i < arr1.length; i++){
            if (!Arrays.equals(arr1[i], arr2[i]))
                return false;
        }
        return true;
    }
}
