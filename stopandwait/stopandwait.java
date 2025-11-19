
import java.io.*;
import java.net.*;

public class StopWaitServer {
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(5000);
        System.out.println("Server started. Waiting for client...");

        Socket s = ss.accept();
        System.out.println("Client connected.");

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        String frame;
        while ((frame = in.readLine()) != null) {

            System.out.println("Received Frame: " + frame);

            // RANDOMLY SEND ACK / NACK (to simulate frame loss)
            if (Math.random() < 0.8) {
                out.println("ACK");
                System.out.println("Sent: ACK");
            } else {
                out.println("NACK");
                System.out.println("Sent: NACK");
            }
        }

        s.close();
        ss.close();
    }
}


---

✅ Stop and Wait CLIENT (TCP) — StopWaitClient.java

import java.io.*;
import java.net.*;

public class StopWaitClient {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 5000);

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        int frame = 1;

        while (frame <= 5) {     // send 5 frames for demo
            System.out.println("\nSending Frame " + frame);
            out.println("Frame " + frame);

            String response = in.readLine();
            System.out.println("Received: " + response);

            if (response.equals("ACK")) {
                frame++;        // go to next frame
            } else {
                System.out.println("Retransmitting Frame " + frame);
            }
        }

        s.close();
    }
}


---

🟦 Sample Output

Client

Sending Frame 1
Received: ACK

Sending Frame 2
Received: NACK
Retransmitting Frame 2
Sending Frame 2
Received: ACK

Sending Frame 3
Received: ACK

Server

Received Frame: Frame 1
Sent: ACK
Received Frame: Frame 2
Sent: NACK
Received Frame: Frame 2
Sent: ACK


---