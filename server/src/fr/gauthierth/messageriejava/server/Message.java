package fr.gauthierth.messageriejava.server;

public class Message {

    private User author;
    private String content;
    private Channel channel;

    Message(User author, String content, Channel channel) {
        this.author = author;
        this.content = content;
        this.channel = channel;
    }

    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Channel getChannel() {
        return channel;
    }
    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
