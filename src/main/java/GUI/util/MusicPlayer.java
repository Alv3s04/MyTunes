package GUI.util;

import BE.Song;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

import java.io.File;
import javafx.util.Duration;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;
    public MusicPlayer(){
    }

    public void load(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            return;
        }
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
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
