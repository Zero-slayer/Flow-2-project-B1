package Client;

import java.io.InputStream;
import java.util.Scanner;

class ServerReader implements Runnable {

    boolean servReader = true;
    Scanner scanner;
    String message;
    Client client;

    ServerReader(InputStream inputStream, Client client) {
        scanner = new Scanner(inputStream);
        this.client = client;
    }

    @Override
    public void run() {
        while (servReader && scanner.hasNext()) {
            message = scanner.nextLine();

            if (message.equals("CLOSE#0") || message.equals("CLOSE#1") || message.equals("CLOSE#2")) {
                servReader = false;
                client.setKeepRunning(false);
            }
            System.out.println(message);
        }
    }
}
