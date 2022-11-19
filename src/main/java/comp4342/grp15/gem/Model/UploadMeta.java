package comp4342.grp15.gem.Model;

public class UploadMeta {
    private String user;
    private String identifier;
    private String message;
    private String location;

    private String picture;

    public UploadMeta(String user, String identifier, String message, String location, String picture) {
        this.user = user;
        this.identifier = identifier;
        this.message = message;
        this.location = location;
        this.picture = picture;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
