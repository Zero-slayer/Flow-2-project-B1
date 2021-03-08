package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private ChatServer server;
    private Socket socket;
    private String myId = "Name: ";
    private String currentUser;

    private Users users;
    private MessageSender sender;

    private static int index = 1;

    public ClientHandler(Socket socket, ChatServer server, Users users) {
        this.socket = socket;
        this.server = server;
        this.users = users;
        this.myId +=index;
        index++;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public String getMyId() {
        return this.myId;
    }

    public String getCurrentUser() {
        return this.currentUser;
    }

    private boolean handleCommand(String msg, PrintWriter pw, Scanner scanner) {
        String[] parts = msg.split("#");
        String token = parts[0];
        String argument;
        String secondArg;

        if (parts.length == 1) {
            if (parts[0].contains("CLOSE")) {
                pw.println("CLOSE#0");
                return false;
            }
        }
        else if (parts.length == 2) {
            argument = parts[1];
            if (token.equals("CONNECT")) {
                if (users.getUsers().contains(argument)) {
                   currentUser = argument;
                   sender = new MessageSender(server, socket, currentUser);
                   StringBuilder result = new StringBuilder("ONLINE#" + currentUser);

                   users.addOnlineUsers(currentUser);
                   users.getOnlineUsers().forEach((user) -> {
                       if (!user.equals(currentUser))
                           result.append(",").append(user);
                   });
                   sender.sendMessage((result));
                }
            }
            else
            {
                pw.println("CLOSE#2");
                return false;
            }
        }
        else if (parts.length == 3) {
            argument = parts[1];
            secondArg = parts[2];
            String[] users = argument.split(",");
            if (token.equals("SEND")) {
                if (users.length == 1) {
                    if (users[0].equals("*")) {
                        //TODO send to all
                        sender.sendMessage(secondArg);
                    }
                    else {
                        //TODO send to one person
                        sender.sendMessage(secondArg, users[0]);
                    }
                }
                else if (users.length > 1) {
                    //TODO send to multiple
                    sender.sendMessage(secondArg, users);
                }
            }
            else
            {
                pw.println("CLOSE#2");
                return false;
            }
        }
        return true;
    }

    private void handleClient(Socket socket) throws IOException {
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(socket.getInputStream());
        pw.println("You are now connected. Send a string to ...");

        try {
            String message = "";
            boolean keepRunning = true;
            while (keepRunning) {
                message = scanner.nextLine();
                keepRunning = handleCommand(message, pw, scanner);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pw.println("Connection is closing");
        socket.close();
    }

    @Override
    public void run() {
        try {
            handleClient(this.socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
