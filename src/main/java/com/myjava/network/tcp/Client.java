package com.myjava.network.tcp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author ldx
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 9999);
        try (InputStream is = socket.getInputStream(); OutputStream os = socket.getOutputStream()) {
            handle(is, os);
        }
        socket.close();
        System.out.println("disconnected...");
    }

    private static void handle(InputStream is, OutputStream os) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        Scanner scanner = new Scanner(System.in);
        System.out.println("[server] " + reader.readLine());
        while (true) {
            System.out.print(">>> ");
            String next = scanner.nextLine();
            writer.write(next);
            writer.newLine();
            writer.flush();
            String s = reader.readLine();
            System.out.println("<<< " + s);
            if ("bye".equals(s)) {
                break;
            }
        }
    }
}
