package nickolaill.staniec.runeak.amagicalplace.Utilities;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class InternetUtils {

    //https://stackoverflow.com/a/27312494
    public static boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
            } catch (Exception e) {
            return false;
        }
    }
}
