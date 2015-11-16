package com.mycompany.app.rawio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RawIOEchoServer {

    public void serve(int port) throws Exception {
        ServerSocket ss = new ServerSocket(port);
        while (true) {
            System.out.println(Thread.currentThread() + " is starting to serve.");
            final Socket socket = ss.accept();
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            BufferedInputStream in = new BufferedInputStream(
                                    socket.getInputStream());
                            byte[] buf = new byte[1024];
                            int len = in.read(buf); // read message from client
                            if (len>0){

                                String message = new String(buf, 0, len);
                                message = "Boss heard: "+message;
                                BufferedOutputStream out = new BufferedOutputStream(
                                        socket.getOutputStream());
                                out.write(message.getBytes()); // echo to client
                                out.flush();
                                System.out.println(socket+"\t"+message);
                            }else {
                                socket.close();
                                System.out.println(Thread.currentThread() + " is dying.");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                socket.close();

                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            System.out.println(Thread.currentThread() + " is dying after a possible exception");
                            break;
                        }
                    }

                }
            }).start();
        }
    }
    public static void main(String[] args) throws Exception {
        new RawIOEchoServer().serve(6888);
    }
}
