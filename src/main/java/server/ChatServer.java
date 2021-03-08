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

    private void startServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started, listening on " +port);

        while (true) {
            System.out.println("Waiting for a client");
            Socket socket = serverSocket.accept(); //blocking call
            System.out.println("New client connected");

            ClientHandler clientHandler = new ClientHandler(socket, this, users);
            allClientHandlers.put(clientHandler.getMyId(), clientHandler);

//            new Thread(new SendToClients(this, ))

            new Thread(clientHandler).start();
        }
    }

    //Call server with arguments like this: 0.0.0.0 8088 logfile.log
    public static void main(String[] args) throws IOException {
        int port = 8088;

            if (args.length == 1) {
                try {
                    port = Integer.parseInt(args[0]);
                } catch (NumberFormatException ne) {
                    System.out.println("Illegal inputs provided when starting the server!");
                    return;
                }
            }

        ChatServer server = new ChatServer();
        server.startServer(port);

    }


}
