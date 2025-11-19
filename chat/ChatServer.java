import java.io.*;
import java.net.*;

public class ChatServer {
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(5000);
        System.out.println("Server started. Waiting for client...");

        Socket s = ss.accept();
        System.out.println("Client connected.");

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));

        String msg, reply;
        while (true) {
            msg = in.readLine();
            System.out.println("Client: " + msg);

            System.out.print("Server: ");
            reply = kb.readLine();
            out.println(reply);

            if (reply.equalsIgnoreCase("exit"))
                break;
        }

        s.close();
        ss.close();
    }
}
