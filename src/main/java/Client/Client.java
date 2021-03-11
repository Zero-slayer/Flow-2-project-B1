package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    Socket socket;
    PrintWriter pw;

    public void connect(String address, int port) throws IOException {
        socket = new Socket(address, port);
        pw = new PrintWriter(socket.getOutputStream(), true);

        new Thread (new ServerReader(socket.getInputStream())).start();

        Scanner keyboard = new Scanner(System.in);
        boolean keepRunning = true;
        while (keepRunning) {
            String msgToSend = keyboard.nextLine(); //Blocking call
            pw.println(msgToSend);
            if (msgToSend.equals("CLOSE#")) {
                keepRunning = false;
            }
        }
        socket.close();
    }

    public static void main(String[] args) throws IOException {

        int DEFAULT_PORT =  8080;
        String DEFAULT_SERVER_IP = " 206.81.26.43";
        int port = DEFAULT_PORT;
        String ip = DEFAULT_SERVER_IP;

        if (args.length == 2) {
            try {
                ip = args[0];
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e){
                System.out.println("Invalid port or ip, using default ports");
            }
        }

        new Client().connect(ip, port);

    }
}
