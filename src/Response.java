import java.io.*;
import java.util.Date;

/**
 * @author Han Jiaming
 * Only supports GET of HTTP/1.1
 */

public class Response {
    Request request;
    OutputStream outputStream;
    int status;

    public Response(Request request, OutputStream outputStream){
        this.request = request;
        this.outputStream = outputStream;
    }

    // send info stream to client via outputStream & Response 200
    public void sendInfo() throws IOException {
        File file;
        byte[] buf = new byte[1024];
        FileInputStream fis = null;

        try {
            if (!request.isLegal()){
                response400();
                return;
            }

            if (request.getType().equals("GET")){

                // get file from website root
                if (request.getUrl().equals("/")){
                    file = new File(HttpServer.serverRoot, "/index.html");
                }else {
                    file = new File(HttpServer.serverRoot,request.getUrl());
                }

                if (file.exists()){
                    if (request.getLastModSince() != null && request.getLastModSince().equals(lastModTime(file))){
                        response304();
                    } else {
                        int fileLength = (int) file.length();
                        PrintWriter responseInfo = new PrintWriter(outputStream);
                        responseInfo.println("HTTP/1.1 200 OK");
                        responseInfo.println("Server: Silver Spork by Hanjiaming");
                        responseInfo.println("Date: " + new Date());
                        responseInfo.println("Content-type: " + getContentType(file.getName()));
                        responseInfo.println("Content-length: " + fileLength);
                        responseInfo.println("Last-Modified: " + lastModTime(file));
                        responseInfo.println("Connection: keep-alive");
                        responseInfo.println("Keep-Alive: timeout=10, max=1000");
                        responseInfo.println("Expires: " + new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 14)));
                        responseInfo.println();
                        responseInfo.flush();
                        // read file as byte and write file into dos as file.
                        fis = new FileInputStream(file);
                        int s;
                        while ((s = fis.read(buf))!=-1){
                            outputStream.write(buf,0,s);
                        }
                        status = 200;
                    }
                }else {
                    response404();
                }
            }else {
                response400();
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (fis != null)
                fis.close();
        }
    }

    // Response 304
    private void response304(){
        PrintWriter responseInfo = new PrintWriter(outputStream);
        responseInfo.println("HTTP/1.1 304 Not Modified");
        responseInfo.println("Server: Silver Spork by Hanjiaming");
        responseInfo.println("Date: " + new Date());
        responseInfo.println("Last-Modified: " + request.getLastModSince());
        responseInfo.println("Expires: " + new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 14)));
        responseInfo.println();
        responseInfo.flush();
        status = 304;
    }

    // Response 404
    private void response404() throws IOException{
        File file = new File(HttpServer.serverRoot,"/404.html");
        byte[] fileBytes = new byte[1024];
        FileInputStream fis = null;

        try {
            int fileLength = (int) file.length();
            PrintWriter responseInfo = new PrintWriter(outputStream);
            responseInfo.println("HTTP/1.1 404 File Not Found");
            responseInfo.println("Server: Silver Spork by Hanjiaming");
            responseInfo.println("Date: " + new Date());
            responseInfo.println("Content-type: " + getContentType(file.getName()));
            responseInfo.println("Content-length: " + fileLength);
            responseInfo.println("Keep-Alive: timeout=10, max=1000");
            responseInfo.println();
            responseInfo.flush();

            // file
            fis = new FileInputStream(file);
            int s = fis.read(fileBytes,0,fileLength);
            while (s != -1){
                outputStream.write(fileBytes,0,s);
                s = fis.read(fileBytes,0,fileLength);
            }
            status = 404;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (fis != null)
                fis.close();
        }

    }

    // Response 400
    private void response400() {
            PrintWriter responseInfo = new PrintWriter(outputStream);
            responseInfo.println("HTTP/1.1 400 Bad Request");
            responseInfo.println("Server: Silver Spork by Hanjiaming");
            responseInfo.println();
            responseInfo.flush();
            status = 400;
    }

    // Retrieve file's Last modify time
    private String lastModTime(File file){
        if (file.exists()){
            long millisec = file.lastModified();
            Date dt = new Date(millisec);
            return dt.toString();
        }else return null;
    }

    // MIME type
    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".html") || fileRequested.endsWith(".htm"))
            return "text/html";
        else if (fileRequested.endsWith(".jpg") || fileRequested.endsWith(".jpeg")){
            return "image/jpeg";
        }else if (fileRequested.endsWith(".png")){
            return "image/png";
        }else if (fileRequested.endsWith(".gif")){
            return "image/gif";
        }else {
            return "text/plain";
        }
    }
}
