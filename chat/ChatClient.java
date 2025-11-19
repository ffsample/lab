import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 5000);
        System.out.println("Connected to server.");

        BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        String msg, reply;
        while (true) {
            System.out.print("Client: ");
            msg = kb.readLine();
            out.println(msg);

            reply = in.readLine();
            System.out.println("Server: " + reply);

            if (msg.equalsIgnoreCase("exit"))
                break;
        }

        s.close();
    }
}
