import java.util.Date;
import java.util.UUID;

public class User {

    private String uuid;
    private String username;
    private Date loggingDate;

    User(String username) {
        this.uuid = UUID.randomUUID().toString();
        this.username = username;
        this.loggingDate = new Date();
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLoggingDate() {
        return loggingDate;
    }
    public void setLoggingDate(Date loggingDate) {
        this.loggingDate = loggingDate;
    }

}
