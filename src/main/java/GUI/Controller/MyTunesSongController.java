package GUI.Controller;

import BE.Song;
import GUI.Model.MyTunesModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MyTunesSongController {
    @FXML
    private ComboBox comboBoxGenre;
    @FXML
    private TextField txtFieldSongTitle;
    @FXML
    private TextField txtFieldTime;
    @FXML
    private TextField txtFieldMP3;
    @FXML
    private TextField txtFieldArtistName;

    private MyTunesModel model;
    private Song editingSong = null;

    // Called by main controller
    public void setModel(MyTunesModel model) {
        this.model = model; //denne metode kommer til interact med songs NÅRR vi har en filepath og ka bruge songs
    }

    // Called when editing an existing song
    public void setEditingSong(Song song) {
        this.editingSong = song;// holder på sangen der bliver edited

        txtFieldSongTitle.setText(song.getTitle());
        txtFieldArtistName.setText(song.getArtist());
        comboBoxGenre.setValue(song.getCategory());
        txtFieldTime.setText(String.valueOf(song.getTime()));
        txtFieldMP3.setText(""); // vores Song class har ik en filepath endnu til vores sange :(
    }

    @FXML
    private void onClickMP3FileChooser(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose MP3 File"); //laver et window til fil vælger

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("MP3 Files (*.mp3)", "*.mp3")
        ); //den limiter valget af filer til kun at være mp3 filer

        File file = fileChooser.showOpenDialog(txtFieldMP3.getScene().getWindow());//hvis dialogen og return den valgte fil eller null

        if (file != null) {
            txtFieldMP3.setText(file.getAbsolutePath()); //hvis vores bruger har valgt en fil, så display dens path
        }
    }

    @FXML
    private void onClickCancelSong(ActionEvent actionEvent) {
        Stage stage = (Stage) txtFieldSongTitle.getScene().getWindow(); //vi "getter" den nuværende stage(window) hvor formen er
        stage.close(); //luk window/stage
    }

    @FXML
    private void onClickSaveSong(ActionEvent actionEvent) {

    }
}
