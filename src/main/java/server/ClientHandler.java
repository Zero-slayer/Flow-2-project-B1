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

    public String onlineListStatus() {
        StringBuilder result = new StringBuilder("ONLINE#");
        users.getUsers().forEach((user, isOnline) -> {
            if (users.getOnlineUser(user))
                result.append(user).append(",");
        });
        result.deleteCharAt(result.length() -1);
        return String.valueOf(result);
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
                   users.addOnlineUsers(currentUser);
                   server.addToSendQueue(onlineListStatus());
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
                if (users.length == 1) {
                    if (users[0].equals("*")) {
                        server.addToSendQueue("MESSAGE#" + currentUser + "#" + secondArg);
                    }
                    else if (this.users.getOnlineUser(users[0])){
                        String[] currentUserPlusUsers = {currentUser, users[0]};
                        server.addToSendQueue(new toSendUser("MESSAGE#" + currentUser + "#" + secondArg, currentUserPlusUsers));
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
                        if (!result)
                            break;
                    }
                    if (result) {
                        String[] currentUserPlusUsers = new String[users.length + 1];
                        currentUserPlusUsers[0] = currentUser;
                        for (int i = 1; i < currentUserPlusUsers.length ; i++)
                            currentUserPlusUsers[i] = users[i - 1];
                        server.addToSendQueue(new toSendUser("MESSAGE#" + currentUser + "#" + secondArg, currentUserPlusUsers));
                    }
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
        else {
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
        if(!this.currentUser.equals("-1")) {
            users.removeOnlineUser(this.currentUser);
            server.addToSendQueue(onlineListStatus());
        }
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
