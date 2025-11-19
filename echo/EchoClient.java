import java.io.*;
import java.net.*;

public class EchoClient {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 5000);
            System.out.println("Connected to server.");

            BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            while (true) {
                System.out.print("Enter message: ");
                String msg = user.readLine();
                out.println(msg);

                if (msg.equalsIgnoreCase("exit"))
                    break;

                String reply = in.readLine();
                System.out.println("Echo from server: " + reply);
            }

            s.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
