package Client;

import java.io.InputStream;
import java.util.Scanner;

class ServerReader implements Runnable {

    Scanner scanner;
    ServerReader(InputStream inputStream) {
        scanner = new Scanner(inputStream);
    }

    @Override
    public void run() {
        while (true) {
            String message = scanner.nextLine();
            System.out.println(message);
        }
    }
}
