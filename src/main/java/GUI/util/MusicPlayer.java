package GUI.util;

import BE.Song;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

import java.io.File;
import java.time.Duration;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;
    public MusicPlayer(){

    }

    public void playSong(Song song) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        File songFile = new File(song.getFilePath());
        Media media = new Media(songFile.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
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

    public Duration getCurrentTime(){
        return mediaPlayer != null ? Duration.ofMillis((long) mediaPlayer.getCurrentTime().toMillis()) : null;
    }
}
