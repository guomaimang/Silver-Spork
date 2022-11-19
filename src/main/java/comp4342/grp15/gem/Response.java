package comp4342.grp15.gem;

import comp4342.grp15.gem.Service.DynamicTextProcessor;
import comp4342.grp15.gem.Service.UploadProcessor;

import java.io.*;
import java.util.Date;
import java.util.Objects;

/**
 * @author Han Jiaming
 * Only supports GET or HEAD of HTTP/1.1
 * For usage, reference and more info, please visit <a href="https://guomaimang.github.io/note/cs/cn/Java-Socket-Programming">...</a>
 * Or check the project report
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
    public void process() throws IOException {

        if (!request.isLegal()){
            response400();
            return;
        }
        if (request.getType().equals("GET") || request.getType().equals("HEAD")){
            processGet();
        }else if(request.getType().equals("POST")){
            processPost();
        }
        else{
            response400();
        }

    }

    private void processGet() throws IOException{
        File file;

        // Pre Process: get file from website root
        if (request.getUrl().endsWith("/")){
            file = new File(HttpServer.serverRoot, request.getUrl() + "index.html");
        }else{
            file = new File(HttpServer.serverRoot,request.getUrl());
        }

        // Response 200 Static
        if (file.exists()){
            if (request.getLastModSince() != null && request.getLastModSince().equals(lastModTime(file))){
                response304();
            } else if(request.getType().equals("GET")){
                response200Static(file);
            }else {
                response200Head(file);
            }
        }
        // Response 200 Dynamic
        else if(isDynamic(request.getUrl())) {
            response200Dynamic(request.getUrl());
        }
        // Response 404
        else {
            response404();
        }

    }

    private void processPost(){
        String file = "";

        switch (request.getUrl()) {
            case "/upload" ->
                    file = UploadProcessor.getProcess(request.getRequestInfoArr()[request.getRequestInfoArr().length - 1]);
            case "/login" -> System.out.println("LOGIN");
            case "/signup" -> System.out.println("SIGNUP");
            default -> file = """
                    {"statue":"Fail", "commit":"Bad Request", "code":"-1",}
                    """;
        }

        PrintWriter responseInfo = new PrintWriter(outputStream);
        responseInfo.println("HTTP/1.1 200 OK");
        responseInfo.println("Server: Silver Spork by Hanjiaming");
        responseInfo.println("Date: " + new Date());
        responseInfo.println("Content-type: " + getContentType("re.json"));
        responseInfo.println("Content-length: " + file.length());
        responseInfo.println("Connection: keep-alive");
        responseInfo.println("Keep-Alive: timeout=10, max=1000");
        responseInfo.println();
        responseInfo.flush();
        status = 200;

        byte[] buf = new byte[1024];
        try (InputStream fis = new ByteArrayInputStream(file.getBytes())) {
            // read file as byte and write file into dos as file.
            int s;
            while ((s = fis.read(buf)) != -1) {
                outputStream.write(buf, 0, s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Response 200
    private void response200Static(File file){
        response200Head(file);

        byte[] buf = new byte[1024];
        try (FileInputStream fis = new FileInputStream(file)) {
            // read file as byte and write file into dos as file.
            int s;
            while ((s = fis.read(buf)) != -1) {
                outputStream.write(buf, 0, s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void response200Head(File file){
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
        status = 200;
    }
    private void response200Dynamic(String url){
        String file = "";
        if (Objects.equals(url, "/trend.json")){
            file = DynamicTextProcessor.trendJson();
        }else if(Objects.equals(url, "/map.html")){
            file = DynamicTextProcessor.map();
        }

        PrintWriter responseInfo = new PrintWriter(outputStream);
        responseInfo.println("HTTP/1.1 200 OK");
        responseInfo.println("Server: Silver Spork by Hanjiaming");
        responseInfo.println("Date: " + new Date());
        responseInfo.println("Content-type: " + getContentType(url));
        responseInfo.println("Content-length: " + file.length());
        responseInfo.println("Connection: keep-alive");
        responseInfo.println("Keep-Alive: timeout=10, max=1000");
        responseInfo.println();
        responseInfo.flush();
        status = 200;

        byte[] buf = new byte[1024];
        try (InputStream fis = new ByteArrayInputStream(file.getBytes())) {
            // read file as byte and write file into dos as file.
            int s;
            while ((s = fis.read(buf)) != -1) {
                outputStream.write(buf, 0, s);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    // For Get, Check if request a dynamic resource
    private boolean isDynamic(String url){
        return Objects.equals(url, "/trend.json") || Objects.equals(url, "/map.html");
    }
}
