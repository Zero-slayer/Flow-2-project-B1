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
    private String input;
    private String output;

    public MessageSender(ChatServer server,Socket socket, String currentUser) {
       this.socket = socket;
       this.server = server;
       this.currentUser = currentUser;
    }

    @Override
    public void sendMessage(String input) {
       //TODO search through all users and send the message
        this.input = input;
        server.getAllClientHandlers().forEach((ID, ClientHandler) -> {
            try {
                if (!ClientHandler.getSocket().isClosed())
                    new PrintWriter(ClientHandler.getSocket().getOutputStream(), true)
                            .println("MESSAGE#" + currentUser + "#" + input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sendMessage(String input, String user) {
        //TODO print message to sender and receiver
        this.input = input;
        try {
            new PrintWriter(socket.getOutputStream(), true).println("MESSAGE#" + currentUser + "#" + input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.getAllClientHandlers().forEach((ID, ClientHandler) -> {
            try {
                if (!ClientHandler.getSocket().isClosed()) {
                    if (ClientHandler.getCurrentUser().equals(user)) {
                        new PrintWriter(ClientHandler.getSocket().getOutputStream(), true)
                                .println("MESSAGE#" + currentUser + "#" + input);
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
        this.input = input;

        AtomicInteger i = new AtomicInteger(0);

        try {
            new PrintWriter(socket.getOutputStream(), true).println("MESSAGE#" + currentUser + "#" + input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.getAllClientHandlers().forEach((ID, ClientHandler) -> {
            try {
                if (!ClientHandler.getSocket().isClosed()) {
                    if (ClientHandler.getCurrentUser().equals(users[i.get()])) {
                        i.incrementAndGet();
                        new PrintWriter(ClientHandler.getSocket().getOutputStream(), true)
                                .println("MESSAGE#" + currentUser + "#" + input);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendMessage(StringBuilder input) {
        this.input = String.valueOf(input);
        server.getAllClientHandlers().forEach((ID, ClientHandler) -> {
            try {
                if (!ClientHandler.getSocket().isClosed())
                    new PrintWriter(ClientHandler.getSocket().getOutputStream(), true)
                            .println(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
