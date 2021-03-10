package server;

public class toSendUser {

    String message, user;
    String[] multi;

    public toSendUser(String message, String user){
       this.message = message;
       this.user = user;
    }

    public toSendUser(String message, String[] multi) {
       this.message = message;
       this.multi = multi;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public String[] getMulti() {
        return multi;
    }
}
