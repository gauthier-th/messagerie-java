package fr.gauthierth.messageriejava.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ConfigSaver {

    private String filename;

    ConfigSaver(String filename) {
        this.filename = filename;
    }

    public ArrayList<Channel> load() {
        ArrayList<Channel> channels = new ArrayList<>();
        try {
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = br.readLine();
            while (true) {
                if (line == null || line.equalsIgnoreCase("========")) {
                    String uuid = lines.get(0);
                    String name = lines.get(1);
                    String password = lines.get(2);
                    int maxMessageCount = Integer.parseInt(lines.get(3));
                    Channel channel = new Channel(uuid);
                    if (name.length() > 0)
                        channel.setName(name);
                    if (password.length() > 0)
                        channel.setPassword(password);
                    channel.setMaxMessageCount(maxMessageCount);
                    channels.add(channel);
                    lines.clear();
                    if (line == null)
                        break;
                }
                else
                    lines.add(line);
                line = br.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return channels;
    }

    public void save(ArrayList<Channel> channels) {
        try {
            try (PrintWriter out = new PrintWriter(this.filename)) {
                for (int i = 0; i < channels.size(); i++) {
                    if (i > 0)
                        out.println("========");
                    Channel channel = channels.get(i);
                    out.println(channel.getUuid());
                    out.println(channel.getName() == null ? "" : channel.getName());
                    out.println(channel.getPassword() == null ? "" : channel.getPassword());
                    out.println(channel.getMaxMessageCount());
                }
            }
        }
        catch (Exception e) {}
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

}
