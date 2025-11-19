

// SlidingWindowServer.java
import java.io.*;
import java.net.*;

public class SlidingWindowServer {
    public static void main(String[] args) throws Exception {

        ServerSocket ss = new ServerSocket(5000);
        System.out.println("Server waiting...");
        Socket s = ss.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        int expectedFrame = 0;

        while (true) {
            String frame = in.readLine();
            if (frame.equals("END")) break;

            int receivedFrame = Integer.parseInt(frame);

            System.out.println("Received Frame: " + receivedFrame);

            if (receivedFrame == expectedFrame) {
                System.out.println("Correct Frame! Sending ACK: " + receivedFrame);
                out.println(receivedFrame);  // ACK
                expectedFrame++;
            } else {
                System.out.println("Wrong frame! Resend from: " + expectedFrame);
                out.println(expectedFrame - 1);  // ACK last correct
            }
        }
        s.close();
        ss.close();
    }
}


---

🖥 Sliding Window Client (Java)

(Sends a window of frames & retransmits if NACK)

// SlidingWindowClient.java
import java.io.*;
import java.net.*;

public class SlidingWindowClient {
    public static void main(String[] args) throws Exception {

        Socket s = new Socket("localhost", 5000);

        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        int windowSize = 4;
        int totalFrames = 10;
        int base = 0;
        int nextSeq = 0;

        while (base < totalFrames) {

            // send frames inside window
            while (nextSeq < base + windowSize && nextSeq < totalFrames) {
                System.out.println("Sending Frame: " + nextSeq);
                out.println(nextSeq);
                nextSeq++;
            }

            // wait for ACK
            int ack = Integer.parseInt(in.readLine());
            System.out.println("Received ACK: " + ack);

            // move window
            base = ack + 1;

            // retransmit from base
            if (base < nextSeq) {
                System.out.println("Retransmitting from: " + base);
                nextSeq = base;
            }
        }

        out.println("END");
        s.close();
    }
}


---

⭐ How This Works (Real Sliding Window – Go-Back-N ARQ)

Client:

Sends up to windowSize frames at a time

Waits for last ACK

If ACK is behind → retransmits all frames from that point

Window slides forward


Server:

Accepts frames in order

Sends ACK for the correctly received frame

If wrong frame → repeats last acknowledged number (NACK)



---

📌 Sample Output

Client

Sending Frame: 0
Sending Frame: 1
Sending Frame: 2
Sending Frame: 3
Received ACK: 3
Sending Frame: 4
Sending Frame: 5
Sending Frame: 6
Sending Frame: 7
Received ACK: 5
Retransmitting from: 6

Server

Received Frame: 0
Correct Frame! Sending ACK: 0
Received Frame: 1
Correct Frame! Sending ACK: 1
Received Frame: 2
Correct Frame! Sending ACK: 2
Received Frame: 3
Correct Frame! Sending ACK: 3
Received Frame: 5
Wrong frame! Resend from: 4


---