package fr.gauthierth.messageriejava.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ConfigSaver {

    private String filename;
    private String host = "localhost";
    private int port = 3000;
    private String username = null;

    ConfigSaver(String filename) {
        this.filename = filename;
    }

    public void load() {
        try {
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
            this.host = lines.get(0);
            this.port = Integer.parseInt(lines.get(1));
            String username = lines.get(2);
            if (username.length() > 0)
                this.username = username;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            try (PrintWriter out = new PrintWriter(this.filename)) {
                out.println(this.host);
                out.println(this.port);
                out.println(this.username == null ? "" : this.username);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
