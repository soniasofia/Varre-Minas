package org.academiadecodigo.minesweeper;

/**
 * Created by codecadet on 18/11/16.
 */
public class ConversionTests {
    

    public static void main(String[] args) {


        String[][] matrix = new String[9][9];
        int size = matrix.length*matrix.length;
        String[] stringArray = new String[size];

        String[] newArray;

        String message;
        String[][] newMatrix;
        byte[][] byteArray;




        matrix = populateMatrix(matrix);
        stringArray = matrixToArray(matrix);
        byteArray = arrayToBytes(stringArray);

        message = stringArrayToSingleString(stringArray);
        newArray = stringToArray(message);
        newMatrix = arrayToMatrix(newArray);

        System.out.println("matrix");
        printMatrix(matrix);
        System.out.println("array");
        printArray(stringArray);
        System.out.println();
        System.out.println("array to string");
        System.out.println(message);
        System.out.println("array to bytes");

        System.out.println("bytes to array");

        System.out.println("string to array");
        printArray(newArray);
        System.out.println("\narray to matrix");
        System.out.println(newMatrix.length);
        printMatrix(newMatrix);
    }

    public static byte[][] arrayToBytes(String[] array){

        byte[][] byteArray = new byte[array.length][];


        for (int i = 0; i < array.length; i++){
            byteArray[i] = array[i].getBytes();
        }

        return byteArray;
    }

    public static String[] matrixToArray(String[][] matrix){

        String[] array = new String[matrix.length * matrix.length];
        for(int row = 0; row < matrix.length; row++){
            for (int col = 0; col < matrix.length; col++) {
                array[(row * matrix.length) + col] = matrix[row][col];
            }
        }

        return array;
    }

    public static String[][] arrayToMatrix(String[] array){

        int size = (int)Math.sqrt(array.length);
        String[][] matrix = new String[size][size];

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                matrix[i][j] = array[(i*size) + j];
            }
        }

        return matrix;
    }

    public static String[] stringToArray(String message){

        String[] array = new String[message.length()/3];
        int j = 0;

        for (int i = 0; i < message.length(); i = i + 3) {
            array[j] = message.substring(i, i + 3);
            j++;
        }

        return array;
    }

    public static String stringArrayToSingleString(String[] array){

        String message = new String();

        System.out.println(array.length);
        for(int i = 0; i < array.length; i++){
            message += array[i];
        }

        return message;
    }


    public static String[][] populateMatrix(String[][] matrix){

        for(int row = 0; row < matrix.length; row++){
            for (int col = 0; col < matrix.length; col++) {
                matrix[row][col] = "[a]";
            }
        }

        return matrix;
    }

    public static void printMatrix(String[][] matrix){

        for(int row = 0; row < matrix.length; row++){
            for (int col = 0; col < matrix.length; col++)
                System.out.print(matrix[row][col]);

            System.out.println();
        }
    }

    public static void printArray(String[] array){

        System.out.println(array.length);
        for(int i = 0; i < array.length; i++){
            System.out.print(array[i]);
        }
    }
}
