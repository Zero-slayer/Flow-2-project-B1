package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    Socket socket;
    PrintWriter pw;
    private boolean keepRunning = true;

    public void connect(String address, int port) throws IOException {
        socket = new Socket(address, port);
        pw = new PrintWriter(socket.getOutputStream(), true);

        new Thread (new ServerReader(socket.getInputStream(), this)).start();

        Scanner keyboard = new Scanner(System.in);
        while (keepRunning) {
            String msgToSend = keyboard.nextLine(); //Blocking call
            pw.println(msgToSend);
            if (msgToSend.equals("CLOSE#")) {
                keepRunning = false;
            }
        }
        socket.close();
    }

    public void setKeepRunning(boolean input) {
        this.keepRunning = input;
    }

    public static void main(String[] args) throws IOException {

        int DEFAULT_PORT =  5555;
        String DEFAULT_SERVER_IP = "138.68.93.150";
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
