public class Message {

    private User user;
    private String content;
    private Channel channel;

    Message(User user, String content, Channel channel) {
        this.user = user;
        this.content = content;
        this.channel = channel;
    }

}
