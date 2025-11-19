

// SelectiveRepeatServer.java
import java.io.*;
import java.net.*;
import java.util.Random;

public class SelectiveRepeatServer {
    public static void main(String[] args) throws Exception {

        ServerSocket ss = new ServerSocket(5000);
        System.out.println("Server waiting...");
        Socket s = ss.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        Random rand = new Random();

        while (true) {
            String data = in.readLine();
            if (data.equals("END")) break;

            int frame = Integer.parseInt(data);
            System.out.println("Received Frame: " + frame);

            // Randomly drop ACK for demonstration
            boolean drop = rand.nextInt(4) == 0; // 25% drop chance

            if (drop) {
                System.out.println("Dropping ACK for frame: " + frame);
                // Do not send ACK    ← Selective Repeat: ACK lost
                continue;
            }

            System.out.println("Sending ACK: " + frame);
            out.println(frame);
        }

        s.close();
        ss.close();
    }
}


---

✅ Selective Repeat Client (Very SIMPLE Program)

// SelectiveRepeatClient.java
import java.io.*;
import java.net.*;

public class SelectiveRepeatClient {
    public static void main(String[] args) throws Exception {

        Socket s = new Socket("localhost", 5000);

        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        int totalFrames = 10;
        boolean acked[] = new boolean[totalFrames];

        for (int f = 0; f < totalFrames; f++) {

            // Send frame
            System.out.println("Sending Frame: " + f);
            out.println(f);

            // Wait for ACK (with timeout simulation)
            long start = System.currentTimeMillis();
            boolean gotAck = false;

            while (System.currentTimeMillis() - start < 500) { // 0.5 sec wait
                if (in.ready()) {
                    int ack = Integer.parseInt(in.readLine());
                    if (ack == f) {
                        System.out.println("Received ACK: " + ack);
                        acked[f] = true;
                        gotAck = true;
                        break;
                    }
                }
            }

            // If no ACK → retransmit later (Selective Repeat)
            if (!gotAck) {
                System.out.println("ACK not received for frame " + f + ". Retransmitting...");
                f--;  // send this frame again
            }
        }

        out.println("END");
        s.close();
    }
}


---




---

📌 Sample Output

Client

Sending Frame: 3
ACK not received for frame 3. Retransmitting...
Sending Frame: 3
Received ACK: 3

Server

Received Frame: 3
Dropping ACK for frame: 3
Received Frame: 3
Sending ACK: 3


---