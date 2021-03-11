package server;

public interface Message {

    void sendMessage(String input);
    void sendMessage(String input, String[] users);
}
