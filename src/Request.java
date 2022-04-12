import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Request{
    private final boolean legal;
    private String type;
    private String url;
    private String lastModSince;

    public Request(InputStream inputStream){

        // parse to string
        int infoLength = -1;
        try {
            infoLength = inputStream.read(buffer);
        } catch (IOException e){
            e.printStackTrace();
        }
        for (int i = 0; i < infoLength; i++) {
            requestInfo.append((char) buffer[i]);
        }
        // parse info to http attributes
        String[] requestInfoArr = requestInfo.toString().split("\\s+");
        if (requestInfoArr.length >= 9){
            legal = true;
            type = requestInfoArr[0];
            url = requestInfoArr[1];

            for (int i = 0; i < requestInfoArr.length; i++) {
                if (Objects.equals(requestInfoArr[i], "If-Modified-Since:") && i+6 < requestInfoArr.length){
                    lastModSince = requestInfoArr[i+1] + " " + requestInfoArr[i+2] + " " + requestInfoArr[i+3] + " " +
                                    requestInfoArr[i+4] + " " + requestInfoArr[i+5] + " " + requestInfoArr[i+6];
                    break;
                }
            }
        }else {
            legal = false;
        }

    }

    // read info from socket
    StringBuffer requestInfo = new StringBuffer(4096);
    byte[] buffer = new byte[4096];

    // bean methods
    public String getType() {
        return type;
    }
    public String getUrl() {
        return url;
    }
    public boolean isLegal() {
        return legal;
    }
    public String getLastModSince() {
        return lastModSince;
    }
}
