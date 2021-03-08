package server;

import Client.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageSender implements Message{

    private ChatServer server;
    private Socket socket;
    private String currentUser;
    private String output = "MESSAGE#" + currentUser + "#" + input;

    public MessageSender(ChatServer server,Socket socket, String currentUser) {
       this.socket = socket;
       this.server = server;
       this.currentUser = currentUser;
    }

    @Override
    public void sendMessage(String input) {
       //TODO search through all users and send the message
        server.getAllClientHandlers().forEach((ID, ClientHandler) -> {
            try {
                if (!ClientHandler.getSocket().isClosed())
                    new PrintWriter(ClientHandler.getSocket().getOutputStream(), true)
                            .println(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sendMessage(String input, String user) {
        //TODO print message to sender and receiver
        try {
            new PrintWriter(socket.getOutputStream(), true).println(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.getAllClientHandlers().forEach((ID, ClientHandler) -> {
            try {
                if (!ClientHandler.getSocket().isClosed()) {
                    if (ClientHandler.getCurrentUser().equals(user)) {
                        new PrintWriter(ClientHandler.getSocket().getOutputStream(), true)
                                .println(output);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sendMessage(String input, String[] users) {
       //TODO print message to sender and receivers

        AtomicInteger i = new AtomicInteger(0);

        try {
            new PrintWriter(socket.getOutputStream(), true).println(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.getAllClientHandlers().forEach((ID, ClientHandler) -> {
            try {
                if (!ClientHandler.getSocket().isClosed()) {
                    if (ClientHandler.getCurrentUser().equals(users[i.get()])) {
                        i.incrementAndGet();
                        new PrintWriter(ClientHandler.getSocket().getOutputStream(), true)
                                .println(output);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
