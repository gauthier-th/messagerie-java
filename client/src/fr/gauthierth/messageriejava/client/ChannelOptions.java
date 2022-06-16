package fr.gauthierth.messageriejava.client;

public class ChannelOptions {

    private String name = null;
    private String password = null;
    private int maxMessageCount = 100;

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxMessageCount() {
        return this.maxMessageCount;
    }
    public void setMaxMessageCount(int maxMessageCount) {
        this.maxMessageCount = maxMessageCount;
    }

}
