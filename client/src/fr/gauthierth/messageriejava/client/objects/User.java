package fr.gauthierth.messageriejava.client.objects;

import java.awt.*;
import java.util.Date;

/**
 * User class, to store User infos.
 * This class extends Channel to add the ability to send private messages. (not implemented yet)
 */
public class User extends Channel {

    private Date loggingDate;

    public User(String uuid) {
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

    public String getHexColor() {
        int num = 0;
        for (int i = 0; i < this.uuid.length(); i++) {
            num += Integer.parseInt(String.valueOf(this.uuid.charAt(i)), 16);
        }
        int hue = (num * 4) % 360;
        Color color = Color.getHSBColor(hue / 360f, 1f, 1f);
        return Integer.toHexString(color.getRGB()).substring(2);
    }
}
