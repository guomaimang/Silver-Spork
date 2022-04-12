import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    public static void main(String[] args) throws IOException{
        int port = 8080; // server port
        ServerSocket serverSocket = new ServerSocket(port); // create server socket

        // create thread pool
        ExecutorService exec = Executors.newCachedThreadPool();

        

    }
}
