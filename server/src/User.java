import java.util.Date;
import java.util.UUID;

public class User extends Channel {

    private Date loggingDate;
    private SocketRunnable socketRunnable;

    User(String uuid, SocketRunnable socketRunnable) {
        super(uuid);
        this.name = "Anonymous " + this.uuid;
        this.loggingDate = new Date();
        this.socketRunnable = socketRunnable;
    }

    public Date getLoggingDate() {
        return loggingDate;
    }
    public void setLoggingDate(Date loggingDate) {
        this.loggingDate = loggingDate;
    }

}
