package server;

import java.util.concurrent.BlockingQueue;

public class SendToClients implements Runnable {

    MessageSender messageSender;
    BlockingQueue<Object> sendQueue;

    public SendToClients(MessageSender messageSender, BlockingQueue<Object> sendQueue) {
        this.messageSender = messageSender;
        this.sendQueue = sendQueue;
    }


    @Override
    public void run() {
        while (true) {
            try {
                Object o = sendQueue.take();
                if (o instanceof String) {
                    String message = (String) o;
                    messageSender.sendMessage(message);
                } else if (o instanceof toSendUser){
                    if (((toSendUser) o).getMulti() != null)
                        messageSender.sendMessage(((toSendUser) o).getMessage(), ((toSendUser) o).getMulti());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
