package fr.gauthierth.messageriejava.server.objects;

import fr.gauthierth.messageriejava.server.socket.SocketRunnable;

import java.util.Date;

/**
 * User class, to store User infos.
 * This class extends Channel to add the ability to send private messages. (not implemented yet)
 */
public class User extends Channel {

    private Date loggingDate;
    private SocketRunnable socketRunnable;

    public User(String uuid, SocketRunnable socketRunnable) {
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
