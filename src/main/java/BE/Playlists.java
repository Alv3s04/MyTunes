package BE;

public class Playlists {

    private int id;
    private String name;
    private int songs;
    private double time;

    public Playlists(int id, String name, int songs, double time) {
        this.id = id;
        this.name = name;
        this.songs = songs;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public int getSongs() {
        return songs;
    }

    public double getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSongs(int songs) {
        this.songs = songs;
    }


    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return id + ": " + name + " " + songs + " (" + time + ")";
    }

    public int getId() {
        return id;
    }
}