package GUI.util;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.util.Duration;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;
    public MusicPlayer(){
    }

    public void load(String filePath) {
        Path path = Paths.get("data", filePath).toAbsolutePath();
        File file = path.toFile();
        if (!file.exists()) {
                System.out.println("File not found: " + path);
                return;
            }
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void play(){
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void pause(){
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            }
    }

    public void stop(){
        if (mediaPlayer != null) {
        mediaPlayer.stop();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public void setVolume(double volume){
        if (mediaPlayer != null) {
        mediaPlayer.setVolume(volume);
        }
    }

    public Duration getCurrentTime() {
        return mediaPlayer != null ? mediaPlayer.getCurrentTime() : Duration.ZERO;
    }
}