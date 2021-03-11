package server;

public class toSendUser {

    String message;
    String[] multi;

    public toSendUser(String message, String[] multi) {
       this.message = message;
       this.multi = multi;
    }

    public String getMessage() {
        return message;
    }

    public String[] getMulti() {
        return multi;
    }
}
