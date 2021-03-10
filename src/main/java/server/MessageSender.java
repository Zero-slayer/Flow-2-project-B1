package server;

import Client.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageSender implements Message{

    private ChatServer server;
    private Socket socket;
    private String input;
    private String output;

    public MessageSender(ChatServer server,Socket socket) {
       this.socket = socket;
       this.server = server;
    }

    @Override
    public void sendMessage(String input) {
       //TODO search through all users and send the message
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

    @Override
    public void sendMessage(String input, String user) {
        //TODO print message to sender and receiver
        try {
            new PrintWriter(socket.getOutputStream(), true).println(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.getAllClientHandlers().forEach((ID, ClientHandler) -> {
            try {
                if (!ClientHandler.getSocket().isClosed()) {
                    if (ClientHandler.getCurrentUser().equals(user)) {
                        new PrintWriter(ClientHandler.getSocket().getOutputStream(), true)
                                .println(input);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public synchronized void sendMessage(String input, String[] users) {
       //TODO print message to sender and receivers


        try {
            if (!socket.isClosed())
            new PrintWriter(socket.getOutputStream(), true).println(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.getAllClientHandlers().forEach((ID, ClientHandler) -> {
            try {
                if (!ClientHandler.getSocket().isClosed()) {
                    for (int i = 0; i < users.length - 1; i++) {
                       if (ClientHandler.getCurrentUser().equals(users[i]))
                           new PrintWriter(ClientHandler.getSocket().getOutputStream(), true)
                                   .println(input);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendMessage(StringBuilder input) {
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
