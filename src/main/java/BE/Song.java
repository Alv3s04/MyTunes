package BE;

public class Song {

    private int id;
    private String title;
    private String artist;
    private String category;
    private double time;
    private String filePath;

    public Song(int id, String title, String artist, String category, double time, String filePath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.time = time;
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getCategory() {
        return category;
    }

    public double getTime() {
        return time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return id + ": " + title + " " + artist + " " + category + " " + " (" + time + ") "+ " " + filePath;
    }

    public int getId() {
        return id;
    }
}