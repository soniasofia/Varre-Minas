package org.academiadecodigo.varreminas.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by codecadet on 17/11/16.
 */

/**
 * Class that handles the player.
 */
public class Player {
    private Socket clientSocket;
    private DataOutputStream output;
    private String hostName;
    private int portNumber;
    private Scanner keyboardInput;
    private String playerId = "";
    private boolean myTurn;

    public Player(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    /**
     * Establishes the connection between player and client mirror.
     * If it's this player's turn, calls move() method and the received input is sent to client mirror.
     * Initiates the client reader thread.
     */
    public void start() {
        keyboardInput = new Scanner(System.in);
        String playerMessage;

        try {
            clientSocket = new Socket(hostName, portNumber);
            output = new DataOutputStream(clientSocket.getOutputStream());
            Thread thread = new Thread(new Reader());
            thread.start();

            while (!clientSocket.isClosed()) {

                if (myTurn) {
                    playerMessage = move();
                    output.write(playerMessage.getBytes());
                    output.write("\n".getBytes());
                    output.flush();
                    myTurn = false;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the message from the Keyboard input
     */
    public String move() {
        int row, col;
        System.out.println("It's your turn my unstoppable friend! Be wise!");
        System.out.println("Insert row position:");
        row = keyboardInput.nextInt();
        System.out.println("Insert col position:");
        col = keyboardInput.nextInt();
        String messageToServer = "" + row + "." + col;
        System.out.println("Processing...");

        return messageToServer;
    }

    /**
     * Function that prints the matrix that was received in order for the player to see the game map each turn with it's given conditions.
     *
     * @param printableMatrix Receives a matrix that is already prepared to be printed to the screen with the cells expanded according to the game phase.
     */
    public void printMatrix(String[][] printableMatrix) {

        //prints the first line with the coordinates
        System.out.print("  ");
        for (int i = 1; i <= printableMatrix.length; i++)
            System.out.print(" " + i + " ");
        System.out.println();

        for (int i = 0; i < printableMatrix.length; i++) {
            //prints the number ahead of each line
            System.out.print(i + 1);
            System.out.print(" ");

            //prints the actual matrix
            for (int j = 0; j < printableMatrix.length; j++) {
                System.out.print(printableMatrix[i][j]);
            }

            //prints the number at the end of each line
            System.out.print(" ");
            System.out.print(i + 1);
            System.out.println();
        }

        //prints the final line at the bottom of the matrix
        System.out.print("  ");
        for (int i = 1; i <= printableMatrix.length; i++) {
            System.out.print(" " + i + " ");
        }
        System.out.println();
    }

    /**
     * Conversion from an array of strings to a matrix.
     *
     * @param array
     * @return
     */
    public static String[][] arrayToMatrix(String[] array) {

        int size = (int) Math.sqrt(array.length);
        String[][] matrix = new String[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = array[(i * size) + j];
            }
        }
        return matrix;
    }

    /**
     * String to string array.
     *
     * @param message
     * @return
     */
    public static String[] stringToArray(String message) {

        String[] array = new String[message.length() / 3];
        int j = 0;
        for (int i = 0; i < message.length(); i = i + 3) {
            array[j] = message.substring(i, i + 3);
            j++;
        }
        return array;
    }

    /**
     * Inner class that implements runnable.
     * It's the players input stream.
     */
    private class Reader implements Runnable {

        private String playerMessage2;
        private BufferedReader input;

        public Reader() {
            try {
                this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * method that processes the different kind of information that server sends.
         */
        public void run() {

            while (!clientSocket.isClosed()) {
                try {
                    playerMessage2 = input.readLine();
                    if (playerMessage2.length() == 81 * 3) {
                        String[] array = stringToArray(playerMessage2);
                        printMatrix(arrayToMatrix(array));
                    } else if (playerMessage2.length() == 3) {
                        if (playerId.equals("")) {
                            playerId = playerMessage2;
                            System.out.println("Defined my playerId as " + playerId);
                        } else if (playerMessage2.equals(playerId)) {
                            myTurn = true;
                        }
                    } else
                        System.out.println(playerMessage2);

                    if (playerMessage2 == null) {
                        clientSocket.close();
                        return;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}



