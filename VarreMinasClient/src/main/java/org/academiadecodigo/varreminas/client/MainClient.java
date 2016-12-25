package org.academiadecodigo.varreminas.client;


/**
 * Created by codecadet on 17/11/16.
 */
public class MainClient {

    public static void main(String[] args) {

        Player player = new Player(/*"192.168.1.14"*/"172.16.16.11", 8888);
        player.start();


    }
}
