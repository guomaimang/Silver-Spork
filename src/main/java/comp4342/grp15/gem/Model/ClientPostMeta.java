package comp4342.grp15.gem.Model;

public class ClientPostMeta {
    private int id;
    private String posterName;
    private String message;
    private String time;
    private String location;

    public ClientPostMeta(int id, String posterName, String message, String time, String location){
        this.id = id;
        this.posterName = posterName;
        this.message = message;
        this.time = time;
        this.location = location;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPosterName() {
        return posterName;
    }
    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

}
