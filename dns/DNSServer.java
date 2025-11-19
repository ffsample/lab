import java.net.*;

public class DNSServer {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(5000);

        // DNS Table using simple arrays (NO HASHMAP)
        String[] domains = {"google.com", "youtube.com"};
        String[] ips     = {"142.250.190.14", "142.250.187.206"};

        byte[] buf = new byte[1024];
        System.out.println("DNS Server Running...");

        while (true) {
            DatagramPacket request = new DatagramPacket(buf, buf.length);
            socket.receive(request);

            String domain = new String(request.getData(), 0, request.getLength());

            // Linear search
            String ip = "Not Found";
            for (int i = 0; i < domains.length; i++) {
                if (domain.equalsIgnoreCase(domains[i])) {
                    ip = ips[i];
                    break;
                }
            }

            DatagramPacket response = new DatagramPacket(
                ip.getBytes(), ip.length(),
                request.getAddress(), request.getPort()
            );

            socket.send(response);
        }
    }
}
