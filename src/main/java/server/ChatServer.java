package server;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

    private ServerSocket serverSocket;
    private Map<String, ClientHandler> allClientHandlers = new ConcurrentHashMap<>();
    private BlockingQueue<String> sendQueue = new ArrayBlockingQueue<>(8);
    private Users users = new Users();

    public Map<String, ClientHandler> getAllClientHandlers() {
        return allClientHandlers;
    }

    private void startserver(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started, listening on " +port);

        while (true) {
            System.out.println("Waiting for a client");
            Socket socket = serverSocket.accept(); //blocking call
            System.out.println("New client connected");

            ClientHandler clientHandler = new ClientHandler(socket, this, users);

        }
    }

    //Call server with arguments like this: 0.0.0.0 8088 logfile.log
    public static void main(String[] args) throws IOException {
        String ip ="localhost";
        int port = 8088;
        String logFile = "log.txt";  //Do we need this

        try {
            if (args.length == 3) {
                ip = args[0];
                port = Integer.parseInt(args[1]);
                logFile = args[2];
            }
            else {
                throw new IllegalArgumentException("Server not provided with the right arguments");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Illegal inputs provided when starting the server!");
            return;
        }

        ChatServer server = new ChatServer();
        server.startserver(port);

    }


}
