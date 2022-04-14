import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    public static final String serverRoot = System.getProperty("user.dir") + "/public"; // Static file root
    public static final int port = 8082;              // server port
    public static Logger logger =  new Logger(System.getProperty("user.dir") + "/log.txt");

    public static void main(String[] args) throws IOException{

        // create thread pool for timer exec
        ExecutorService workerExec = Executors.newCachedThreadPool();

        // create server and as server
        int clientID = 0;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.printf("Website root dic: %s.\n", serverRoot);
            System.out.printf("Server start with listening port: %d.\n", port);
            while (true) {
                Socket socket = serverSocket.accept(); // wait for new client and accept
                socket.setKeepAlive(true);
                socket.setSoTimeout(10000);
                workerExec.execute(new Worker(socket, ++clientID,logger));
            }
        } finally {
            System.out.println("Server stop running!");
            workerExec.shutdown();
        }
    }
}

class Worker implements Runnable{
    private final Socket socket;
    private final int serverTimes;
    private final String clientIP;
    private final Logger logger;

    public Worker(Socket socket, int serverTimes,Logger logger){
        this.socket = socket;
        this.serverTimes = serverTimes;
        this.clientIP = socket.getRemoteSocketAddress().toString();
        this.logger = logger;
    }

    @Override
    public void run(){
        System.out.printf("Socket: %d,\tDate: %s, IP: %s, comes in.\n",serverTimes,new Date(),clientIP);
        logger.add(String.format("Socket: %d,\tDate: %s, IP: %s, comes in.\n",serverTimes,new Date(),clientIP));
        try {
            while (true){

                DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                if (socket.isClosed()){
                    break;
                }

                // process request and response
                Request request = new Request(parse(dis));
                Response response = new Response(request,dos);
                response.sendInfo();

                // dos flush
                dos.flush();
                System.out.printf("Socket: %d,\tDate: %s, IP: %s, Type: %s,\tUrl: %s,\tServer response: %d\n",serverTimes,new Date(),clientIP,request.getType(),request.getUrl(),response.status);
                logger.add(String.format("Socket: %d,\tDate: %s, IP: %s, Type: %s,\tUrl: %s,\tServer response: %d\n",serverTimes,new Date(),clientIP,request.getType(),request.getUrl(),response.status));
            }
        }
        catch (Exception e){
        }finally {
            try {
                socket.close();
            } catch (Exception e) {
            }finally {
                System.out.printf("Socket: %d,\tDate: %s,\tIP: %s, close connection.\n",serverTimes,new Date(),clientIP);
                logger.add(String.format("Socket: %d,\tDate: %s,\tIP: %s, close connection.\n",serverTimes,new Date(),clientIP));
            }
        }
    }

    public String parse(DataInputStream dis) throws IOException{
        // read info from socket
        StringBuilder requestInfo = new StringBuilder(4096);
        byte[] buffer = new byte[4096];

        // parse to string
        int infoLength = dis.read(buffer);
        for (int i = 0; i < infoLength; i++) {
            requestInfo.append((char) buffer[i]);
        }
        return requestInfo.toString();
    }
}
