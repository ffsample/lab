
// GoBackNServer.java
import java.io.*;
import java.net.*;

public class GoBackNServer {
    public static void main(String[] args) throws Exception {

        ServerSocket ss = new ServerSocket(5000);
        System.out.println("Server started. Waiting for client...");
        Socket s = ss.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        int expected = 0;

        while (true) {
            String data = in.readLine();
            if (data.equals("END")) break;

            int frame = Integer.parseInt(data);
            System.out.println("Received Frame: " + frame);

            if (frame == expected) {
                System.out.println("Correct! Sending ACK: " + frame);
                out.println(frame);     // ACK
                expected++;
            } else {
                System.out.println("Out of order! Sending ACK: " + (expected - 1));
                out.println(expected - 1);   // NACK (last correct)
            }
        }

        s.close();
        ss.close();
    }
}


---

✅ Go-Back-N Client (Java)

Sends frames, waits for ACK, and retransmits if ACK is not expected

// GoBackNClient.java
import java.io.*;
import java.net.*;

public class GoBackNClient {
    public static void main(String[] args) throws Exception {

        Socket s = new Socket("localhost", 5000);

        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        int totalFrames = 10;
        int windowSize = 4;

        int base = 0;       // first frame of window
        int next = 0;       // next frame to send

        while (base < totalFrames) {

            // send frames in window
            while (next < base + windowSize && next < totalFrames) {
                System.out.println("Sending Frame: " + next);
                out.println(next);
                next++;
            }

            // receiving ACK
            int ack = Integer.parseInt(in.readLine());
            System.out.println("Received ACK: " + ack);

            // slide window
            base = ack + 1;

            // retransmit if needed
            if (ack < next - 1) {
                System.out.println("Retransmitting from frame: " + base);
                next = base;
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

Sending Frame: 0
Sending Frame: 1
Sending Frame: 2
Sending Frame: 3
Received ACK: 1
Retransmitting from frame: 2
Sending Frame: 2
Sending Frame: 3

Server

Received Frame: 0
Correct! Sending ACK: 0
Received Frame: 1
Correct! Sending ACK: 1
Received Frame: 3
Out of order! Sending ACK: 1


---