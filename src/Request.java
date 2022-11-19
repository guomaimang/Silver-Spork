import java.util.Objects;

/**
 * @author Han Jiaming
 * Only supports GET or HEAD of HTTP/1.1
 * For usage, reference and more info, please visit <a href="https://guomaimang.github.io/note/cs/cn/Java-Socket-Programming">...</a>
 * Or check the project report
 */

public class Request{
    private final boolean legal;
    private String type;
    private String url;
    private String lastModSince;

    private final String[] requestInfoArr;

    public Request(String requestInfo){

        // parse info to http attributes
        requestInfoArr = requestInfo.split("\\s+");
        // parse legal type url
        if (requestInfoArr.length >= 3){
            legal = true;
            type = requestInfoArr[0];
            url = requestInfoArr[1];
        }else {
            legal = false;
        }
        // parse last mod
        if (requestInfoArr.length >= 9){
            for (int i = 0; i < requestInfoArr.length; i++) {
                if (Objects.equals(requestInfoArr[i], "If-Modified-Since:") && i+6 < requestInfoArr.length){
                    lastModSince = requestInfoArr[i+1] + " " + requestInfoArr[i+2] + " " + requestInfoArr[i+3] + " " +
                                    requestInfoArr[i+4] + " " + requestInfoArr[i+5] + " " + requestInfoArr[i+6];
                    break;
                }
            }
        }
    }

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
    public String[] getRequestInfoArr() {
        return requestInfoArr;
    }
}
