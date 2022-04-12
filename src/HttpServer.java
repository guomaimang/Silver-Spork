import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    public static final String serverRoot = System.getProperty("user.dir") + "/public"; // Static file root
    public static final int port = 8080;              // server port

    public static void main(String[] args) throws IOException{

        // create thread pool
        ExecutorService exec = Executors.newCachedThreadPool();

        // create server and as server
        int serverTimes = 0;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.printf("Website root dic: %s.\n", serverRoot);
            System.out.printf("Server start with listening port: %d.\n", port);
            while (true) {
                Socket socket = serverSocket.accept(); // wait for new client and accept
                exec.execute(new Worker(socket, ++serverTimes));
            }
        } finally {
            System.out.println("Server stop running!");
        }
    }
}

class Worker implements Runnable{
    private final Socket socket;
    private final int serverTimes;
    private final String clientIP;

    public Worker(Socket socket, int serverTimes){
        this.socket = socket;
        this.serverTimes = serverTimes;
        this.clientIP = socket.getRemoteSocketAddress().toString();
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            Request request = new Request(dis);
            Response response = new Response(request,dos);
            response.sendInfo();
            dos.flush();

            System.out.printf("%d \tIP: %s, Type: %s, Url: %s, Server response: %d\n",serverTimes,clientIP,request.getType(),request.getUrl(),response.status);

            dis.close();
            dos.close();
            socket.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
