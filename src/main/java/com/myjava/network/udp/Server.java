package com.myjava.network.udp;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

/**
 * @Author ldx
 */
public class Server {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(6666);
        while (true){
            byte[] buffer = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(datagramPacket);
            String string = new String(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength(), StandardCharsets.UTF_8);
            System.out.println("receive: " + string);
            System.out.println(datagramPacket.getAddress());
            byte[] data = "ACK".getBytes(StandardCharsets.UTF_8);
            datagramPacket.setData(data);
            datagramSocket.send(datagramPacket);
        }
    }
}
