package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private ChatServer server;
    private Socket socket;
    private String myId = "Name: ";
    private String currentUser = "-1";

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
            if (parts[0].equals("CLOSE")) {
                pw.println("CLOSE#0");
            } else {
                pw.println("CLOSE#1");
            }
            return false;
        }
        else if (parts.length == 2) {
            argument = parts[1];
            if (token.equals("CONNECT")) {
                if (!users.getOnlineUser(argument)) {
                   currentUser = argument;
                   sender = new MessageSender(server, socket, currentUser);
                   StringBuilder result = new StringBuilder("ONLINE#" + currentUser);

                   users.addOnlineUsers(currentUser);
                   users.getUsers().forEach((user, isOnline) -> {
                       if (!user.equals(currentUser) && (users.getOnlineUser(user) ))
                           result.append(",").append(user);
                   });
                   sender.sendMessage((result));
                } else {
                    pw.println("CLOSE#2");
                    return false;
                }
            } else {
                pw.println("CLOSE#1");
                return false;
            }
        }
        else if (parts.length == 3) {
            argument = parts[1];
            secondArg = parts[2];
            String[] users = argument.split(",");
            if (token.equals("SEND")) {
                System.out.println(users.length);
                if (users.length == 1) {
                    if (users[0].equals("*")) {
                        sender.sendMessage(secondArg);
                    }
                    else if (this.users.getOnlineUser(users[0])){
                        sender.sendMessage(secondArg, users[0]);
                    }
                    else {
                        pw.println("CLOSE#2");
                        return false;
                    }
                }
                else if (users.length > 1) {
                    boolean result = false;
                    for (String user: users) {
                        result = this.users.getOnlineUser(user);
                    }
                    if (result)
                        sender.sendMessage(secondArg, users);
                    else {
                    pw.println("CLOSE#2");
                    return false;
                    }
                }
            } else {
                pw.println("CLOSE#1");
                return false;
            }
        }
        else if (parts.length > 3) {
            pw.println("CLOSE#1");
            return false;
        }
        return true;
    }

    private void handleClient() throws IOException {
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(socket.getInputStream());
//        pw.println("You are now connected. Send a string to ...");
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
//        System.out.println("Connection is closing: " + myId);
        if(!this.currentUser.equals("-1"))
        users.removeOnlineUser(this.currentUser);
        socket.close();
    }

    @Override
    public void run() {
        try {
            handleClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
