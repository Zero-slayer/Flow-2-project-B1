package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class MessageSender implements Message{

    private ChatServer server;

    public MessageSender(ChatServer server) {
       this.server = server;
    }

    @Override
    public void sendMessage(String message) {
        server.getAllClientHandlers().forEach((ID, ClientHandler) -> {
            try {
                if (!ClientHandler.getSocket().isClosed())
                    new PrintWriter(ClientHandler.getSocket().getOutputStream(), true)
                            .println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void sendMessage(String message, String[] users) {
        System.out.println(Arrays.toString(users));
        server.getAllClientHandlers().forEach((ID, ClientHandler) -> {
            try {
                if (!ClientHandler.getSocket().isClosed()) {
                    for (int i = 0; i < users.length; i++) {
                       if (ClientHandler.getCurrentUser().equals(users[i])) {
                           System.out.println(ClientHandler.getCurrentUser());
                           new PrintWriter(ClientHandler.getSocket().getOutputStream(), true)
                                   .println(message);
                       }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
