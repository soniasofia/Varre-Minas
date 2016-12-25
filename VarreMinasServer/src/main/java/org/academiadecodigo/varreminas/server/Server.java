package org.academiadecodigo.varreminas.server;

import org.academiadecodigo.varreminas.game.GameEngineMultiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by codecadet on 14/11/16.
 */
public class Server {

    private Vector<ClientMirror> clientMirrorList;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int portNumber;
    private String username;
    private GameEngineMultiplayer gameEngineMultiplayer;
    private String[][] sendableMatrix;


    /**
     * @param portNumber
     */
    public Server(int portNumber) {
        clientMirrorList = new Vector<>();
        this.portNumber = portNumber;
        gameEngineMultiplayer = new GameEngineMultiplayer();
    }

    /**
     * Method that initiates the server, waits for clients connection and keeps the loop game.
     */
    public void start() {

        Thread thread;
        ClientMirror clientMirror;

        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Waiting for clients...");

            //Connections loop, so the server stays open.

            while (true) {

                clientSocket = serverSocket.accept();
                clientMirror = new ClientMirror(this, clientSocket);
                thread = new Thread(clientMirror);

                clientMirrorList.add(clientMirror);
                System.out.println("added client to list position " + (clientMirrorList.size() - 1));

                System.out.println(clientMirrorList.size());
                thread.setName("player" + clientMirrorList.size());
                thread.start();
                System.out.println("Started thread");

                // Game initiates only when two clients are connected.

                if (clientMirrorList.size() == 2) {

                    //If the condition is filled the game initiates (method init in GameEngineMultiplayer class).
                    gameEngineMultiplayer.init();


                    clientMirrorList.get(0).sendMessage("000");
                    System.out.println("Sent playerId = 000 to player" + clientMirrorList.get(0));
                    clientMirrorList.get(1).sendMessage("001");
                    System.out.println("Sent playerId = 001 to player" + clientMirrorList.get(1));

                    // Game loop.
                    while (true) {
                        System.out.println("about to send matrix");

                        sendableMatrix = gameEngineMultiplayer.getPrintableMatrix();
                        int playerTurn = gameEngineMultiplayer.getPlayerTurn();

                        //Information about the current score.
                        String score = "Player 0 score: " + gameEngineMultiplayer.getScore()[0] + "\n" +
                                "Player 1 score: " + gameEngineMultiplayer.getScore()[1];

                        //Broadcast of the current matrix, score and which player is playing.
                        broadcast(matrixToString(sendableMatrix), score, "00" + Integer.toString(playerTurn));

                        //depending on player turn executes the playerTurn Method.
                        if (playerTurn == 0) {

                            if (!playerTurn(0))
                                continue;
                        } else {
                            if (!playerTurn(1)) {
                                continue;
                            }
                        }

                        //If a bomb hasn't been blown changes the player.
                        if (!gameEngineMultiplayer.checkGameConditions()) {
                            gameEngineMultiplayer.changePlayer();
                        }

                        //Checks if all the bombs have been blown.
                        if (gameEngineMultiplayer.checkForGameOver()) {
                            System.out.println("Player 0 score: " + gameEngineMultiplayer.getScore()[0]);
                            System.out.println("Player 1 score: " + gameEngineMultiplayer.getScore()[1]);

                            //compares the number of bombs hit by each player.
                            if (gameEngineMultiplayer.getScore()[0] > gameEngineMultiplayer.getScore()[1]) {
                                broadcast("Player 0 won", "Game Over", "");
                            } else {
                                broadcast("Player 1 won", "Game Over", "");
                            }
                            System.out.println("Game Over");
                            break;
                        }
                        //boolean that resets bombBlown boolean to false
                        gameEngineMultiplayer.setBombBlown(false);
                    }
                    clientMirrorList.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that verifies if the players moves are valid. If they are valid the game engine will compute those move and act accordingly.
     * @param playerMove method receives which player is playing aka playerMove.
     * @return true if move is valid, return false if its not valid.
     * @throws IOException
     */
    public boolean playerTurn(int playerMove) throws IOException {

        System.out.println("waiting for Player " + playerMove + " input");

        String playermove = clientMirrorList.get(playerMove).getInput().readLine();

        System.out.println("player" + playermove + "input: " + playerMove);

        int row = Integer.parseInt(playermove.split("\\.")[0]);
        int col = Integer.parseInt(playermove.split("\\.")[1]);

        if (gameEngineMultiplayer.checkMoveViability(row, col) && gameEngineMultiplayer.checkMoveAlreadyMade(row, col)) {
            gameEngineMultiplayer.checkMove(row, col, playerMove);

        } else {

            if (gameEngineMultiplayer.checkMoveViability(row, col) == false) {
                clientMirrorList.get(playerMove).sendMessage("\n" + "-->     WARNING: Out of boundaries! Please pick numbers between 1 and 9!     <--" + "\n");
                return false;
            }

            if (gameEngineMultiplayer.checkMoveAlreadyMade(row, col) == false) {
                clientMirrorList.get(playerMove).sendMessage("\n" + "-->     WARNING: Hey YOU, keep your head in the game! That move has already been made!     <--" + "\n");
                return false;
            }

        }
        return true;
    }

    /**
     * Method that sends to all the clients the current matrix, score and informs which player should make a move.
     * @param matrix
     * @param score
     * @param playerTurnFlag
     */
    public synchronized void broadcast(String matrix, String score, String playerTurnFlag) {

        for (int i = 0; i < clientMirrorList.size(); i++) {
            clientMirrorList.get(i).sendMessage(matrix);
            clientMirrorList.get(i).sendMessage(score);
            clientMirrorList.get(i).sendMessage(playerTurnFlag);
        }
    }

    //TODO when player writes "exit" is removed from list and the games ends;
//    public synchronized void exitGame(String username) {
////        String exitMessage = username + " exit the conversation!";
////
////        for (int i = 0; i < clientMirrorList.size(); i++) {
////
////            if (clientMirrorList.get(i).getUsername().equals(username)) ;
////            clientMirrorList.remove(i);
////            broadcast(exitMessage, "", "");
////            return;
////        }
//    }

    /**
     * conversion of matrix to a string (one line)
     * @param matrix
     * @returns the resultant "matrix"
     */
    public String matrixToString(String[][] matrix) {

        String message = "";

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                message += matrix[i][j];
            }
        }
        return message;
    }
}
