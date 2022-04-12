import java.io.File;
import java.util.Date;

public class exam {
    public static void main(String[] args) {

        File f = new File(System.getProperty("user.dir") + "/public" + "/index.html");

        boolean bool = f.exists();

        // if path exists
        if (bool) {
            // returns the time file was last modified
            long millisec = f.lastModified();
            Date dt = new Date(millisec);
            System.out.print(dt);
        }

    }


}
