public class Message {

    private User from;
    private String content;
    private Channel channel;

    Message(User from, String content, Channel channel) {
        this.from = from;
        this.content = content;
        this.channel = channel;
    }

}
