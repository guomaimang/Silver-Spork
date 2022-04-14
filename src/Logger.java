import java.io.*;

public class Logger {

    private final String file;
    private String text;

    public Logger(String fileName){
        this.file = fileName;
        add("-------------------\n");
    }

    public synchronized void add(String content){

        text = content;
        if(text == null || file == null){
            return;
        }

        BufferedWriter bwo = null;
        try {
            bwo = new BufferedWriter(new OutputStreamWriter (new FileOutputStream(file, true)));
            bwo.write(text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bwo.close();
            } catch (IOException e) {
            }finally {
                this.notifyAll();
            }
        }
    }
}
