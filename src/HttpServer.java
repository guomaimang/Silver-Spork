import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    public static void main(String[] args) throws IOException{
        int port = 8080; // server port
        int clientId = 0;

        // create thread pool
        ExecutorService exec = Executors.newCachedThreadPool();

        // create server and as server
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.printf("Server start with listening port %d. Press Ctrl + C to quit.\n", port);
            while (true) {
                Socket socket = serverSocket.accept(); // wait for new client and accept
                exec.execute(new Worker(socket, ++clientId));
            }
        } finally {
            System.out.println("Server stop running!");
        }
    }
}

class Worker implements Runnable{
    private final Socket socket;
    private final int clientId;
    public Worker(Socket socket, int clientId){
        this.socket = socket;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));



        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
