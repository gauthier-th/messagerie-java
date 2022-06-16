package fr.gauthierth.messageriejava.server;

import java.util.Date;

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

    public SocketRunnable getSocketRunnable() {
        return socketRunnable;
    }
}
