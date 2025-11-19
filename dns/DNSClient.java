import java.net.*;
import java.io.*;

public class DNSClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("Enter domain: ");
            String domain = br.readLine();

            DatagramPacket dp = new DatagramPacket(
                domain.getBytes(), domain.length(),
                InetAddress.getLocalHost(), 5000
            );

            socket.send(dp);

            byte[] buf = new byte[1024];
            DatagramPacket reply = new DatagramPacket(buf, buf.length);
            socket.receive(reply);

            System.out.println("IP: " +
                new String(reply.getData(), 0, reply.getLength()));
        }
    }
}
