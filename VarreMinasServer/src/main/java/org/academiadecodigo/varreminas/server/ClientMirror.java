package org.academiadecodigo.varreminas.server;

import java.io.*;
import java.net.Socket;


/**
 * Created by codecadet on 14/11/16.
 */

/**
 * Class that works as an extension of server and establishes communications with the client.
 */
public class ClientMirror implements Runnable {

    private Server server;
    private Socket clientSocket;
    private DataOutputStream output;
    private BufferedReader input;

    public ClientMirror(Server server, Socket clientSocket) throws IOException {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try {
            output = new DataOutputStream(clientSocket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            gameTitle();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * That's fucking amazing!!! And it's the most important method in our game!
     * or
     * Opening message when clients establish connection with server.
     */
    public void gameTitle() {

        String title = "____   ____                                _____  .__                         \n" +
                "\\   \\ /   /____ ______________   ____     /     \\ |__| ____ _____    ______   \n" +
                " \\   Y   /\\__  \\\\_  __ \\_  __ \\_/ __ \\   /  \\ /  \\|  |/    \\\\__  \\  /  ___/   \n" +
                "  \\     /  / __ \\|  | \\/|  | \\/\\  ___/  /    Y    \\  |   |  \\/ __ \\_\\___ \\    \n" +
                "   \\___/  (____  /__|   |__|    \\___  > \\____|__  /__|___|  (____  /____  >   \n" +
                "               \\/                   \\/          \\/        \\/     \\/     \\/    \n" +
                "                                      __                                      \n" +
                "                              _____  |  | _______                             \n" +
                "                              \\__  \\ |  |/ /\\__  \\                            \n" +
                "                               / __ \\|    <  / __ \\_                          \n" +
                "                              (____  /__|_ \\(____  /                          \n" +
                "                                   \\/     \\/     \\/                           \n" +
                "   _____  .__                                                                 \n" +
                "  /     \\ |__| ____   ____   ________  _  __ ____   ____ ______   ___________ \n" +
                " /  \\ /  \\|  |/    \\_/ __ \\ /  ___/\\ \\/ \\/ // __ \\_/ __ \\\\____ \\_/ __ \\_  __ \\\n" +
                "/    Y    \\  |   |  \\  ___/ \\___ \\  \\     /\\  ___/\\  ___/|  |_> >  ___/|  | \\/\n" +
                "\\____|__  /__|___|  /\\___  >____  >  \\/\\_/  \\___  >\\___  >   __/ \\___  >__|   \n" +
                "        \\/        \\/     \\/     \\/              \\/     \\/|__|        \\/     ";
        try {
            output.write(title.getBytes());
            output.write("\n \n \n".getBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that forward the server output to the client connected to this mirror.
     *
     * @param message
     */
    public void sendMessage(String message) {

        try {
            output.write(message.getBytes());
            output.write("\n".getBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedReader getInput() {
        return input;
    }
}
