package server;

public class toSendUser {

    String message;
    String[] oneOrMoreUsers;

    public toSendUser(String message, String[] multi) {
       this.message = message;
       this.oneOrMoreUsers = multi;
    }

    public String getMessage() {
        return message;
    }

    public String[] getOneOrMoreUsers() {
        return oneOrMoreUsers;
    }
}
