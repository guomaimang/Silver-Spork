import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    public static final String serverRoot = System.getProperty("user.dir") + "/public"; // Static file root
    public static final int port = 8082;              // server port

    public static void main(String[] args) throws IOException{

        // create thread pool for timer exec
        ExecutorService timerExec = Executors.newCachedThreadPool();

        // create server and as server
        int clientID = 0;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.printf("Website root dic: %s.\n", serverRoot);
            System.out.printf("Server start with listening port: %d.\n", port);
            while (true) {
                Socket socket = serverSocket.accept(); // wait for new client and accept
                socket.setKeepAlive(true);
                Worker worker = new Worker(socket, ++clientID);
                Thread wt = new Thread(worker);
                wt.start();
                Clock clock = new Clock(wt,socket,10000);
                timerExec.execute(clock);
            }
        } finally {
            System.out.println("Server stop running!");
            timerExec.shutdown();
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
    public void run(){
        try {
            while (true){

                // set IO
                DataInputStream dis = new DataInputStream(
                        new BufferedInputStream(socket.getInputStream()));

                DataOutputStream dos = new DataOutputStream(
                        new BufferedOutputStream(socket.getOutputStream()));

                // process request and response
                Request request = new Request(parse(dis));
                Response response = new Response(request,dos);
                response.sendInfo();

                // dos flush
                dos.flush();
                System.out.printf("Socket: %d,\tIP: %s,\tType: %s,\tUrl: %s,\tServer response: %d\n",serverTimes,clientIP,request.getType(),request.getUrl(),response.status);

            }

        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String parse(InputStream dis){
        // read info from socket
        StringBuilder requestInfo = new StringBuilder(4096);
        byte[] buffer = new byte[4096];

        // parse to string
        int infoLength = -1;
        try {
            infoLength = dis.read(buffer);
        } catch (IOException e){
            e.printStackTrace();
        }
        for (int i = 0; i < infoLength; i++) {
            requestInfo.append((char) buffer[i]);
        }
        return requestInfo.toString();
    }
}

class Clock implements Runnable{
    private final Thread thread;
    private final int millisecond;
    private final Socket socket;

    public void run(){
        try {
            Thread.sleep(millisecond);
            thread.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Clock(Thread thread, Socket socket, int millisecond){
        this.millisecond = millisecond;
        this.thread = thread;
        this.socket = socket;
    }
}