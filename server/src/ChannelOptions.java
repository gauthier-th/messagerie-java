public class ChannelOptions {

    private String name;
    private String password = null;
    private int maxMessageCount = 100;

    ChannelOptions(String name) {
        this.name = name;
    }

    public String getName() {
        return password;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxMessageCount() {
        return maxMessageCount;
    }
    public void setMaxMessageCount(int maxMessageCount) {
        this.maxMessageCount = maxMessageCount;
    }

}
