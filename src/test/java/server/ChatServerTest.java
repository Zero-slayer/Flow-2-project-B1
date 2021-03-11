package server;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static org.junit.Assert.*;

public class ChatServerTest {
/*
    static Thread thread;
    public static void stopServer() {
        thread.stop();
    }
    @BeforeClass
    static public void startServer(){
        ChatServer server = new ChatServer();
        thread = new Thread(() -> {
            try {
                server.startServer(8088);
            } catch (Exception e) {
             e.printStackTrace();
            }
        });
    }

    @Test
    public void serverTest() throws IOException {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Socket socket = new Socket("localhost", 8088);
        PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
        Scanner scanner = new Scanner(socket.getInputStream());

        pw.println("CONNECT#Lars");
        assertEquals("ONLINE#Lars", scanner.nextLine());
        pw.println("SEND#*#Hello");
        assertEquals("MESSAGE#Lars#Hello", scanner.nextLine());
        pw.println("SEND#Doyle#Hello");
        assertEquals("CLOSE#2", scanner.nextLine());
        stopServer();
    }

*/
}