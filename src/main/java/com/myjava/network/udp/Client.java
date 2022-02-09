package com.myjava.network.udp;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * @Author ldx
 */
public class Client {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.setSoTimeout(1000);
        datagramSocket.connect(InetAddress.getByName("localhost"), 6666);

        byte[] bytes = "Hello".getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
        datagramSocket.send(datagramPacket);
        byte[] buffer = new byte[1024];
        datagramPacket = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(datagramPacket);
        String out = new String(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength(), StandardCharsets.UTF_8);
        System.out.println(out);
        datagramSocket.disconnect();
    }

}
