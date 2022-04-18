import java.util.Objects;
/**
 * @author Hann Jiaming
 * For usage and more info, please visit guomaimang.github.io/note/cs/cn/
 */
public class Request{
    private final boolean legal;
    private String type;
    private String url;
    private String lastModSince;

    public Request(String requestInfo){

        // parse info to http attributes
        String[] requestInfoArr = requestInfo.split("\\s+");
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
}
