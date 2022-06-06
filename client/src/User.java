import java.util.Date;
import java.util.UUID;

public class User extends Channel {

    private Date loggingDate;

    User(String uuid) {
        super(uuid);
        this.name = "Anonymous " + this.uuid;
        this.loggingDate = new Date();
    }

    public Date getLoggingDate() {
        return loggingDate;
    }
    public void setLoggingDate(Date loggingDate) {
        this.loggingDate = loggingDate;
    }
}
