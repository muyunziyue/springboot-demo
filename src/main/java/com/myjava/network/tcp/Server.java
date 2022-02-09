package com.myjava.network.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @Author ldx
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("server is running...");
        while (true){
            Socket socket = serverSocket.accept();
            SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
            System.out.println("remoteSocketAddress=" + remoteSocketAddress);

            Server server = new Server();
            Handler handler = server.new Handler();
            handler.setSocket(socket);
            handler.start();
        }
    }
    class Handler extends Thread{
        private Socket socket;

        public void setSocket(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream is = socket.getInputStream(); OutputStream os = socket.getOutputStream()){
                handle(is, os);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("client disconnected...");
        }

        private void handle(InputStream is, OutputStream os) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write("connected...");
            writer.newLine();
            writer.flush();
            while (true){
                String line = reader.readLine();
                writer.write(line);
                writer.newLine();
                writer.flush();
                if ("bye".equals(line)){
                    break;
                }
            }
        }
    }
}
