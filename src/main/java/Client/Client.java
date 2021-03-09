package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    Socket socket;
    PrintWriter pw;
    Scanner scanner;

    public void connect(String adresse, int port) throws IOException {
        socket = new Socket(adresse,port);
        pw = new PrintWriter(socket.getOutputStream(),true);
        scanner = new Scanner(socket.getInputStream());

        Scanner keyBoard = new Scanner(System.in);
        Boolean keepRunning = true;
        while (keepRunning){
            String msgToSend = keyBoard.nextLine();
            pw.println(msgToSend);
            System.out.println(scanner.nextLine());
            if (msgToSend.equals("CLOSE#")) {
                keepRunning = false;
            }
        }
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        new Client().connect("localhost", 8088);
    }
}
