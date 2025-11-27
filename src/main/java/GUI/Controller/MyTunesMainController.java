package GUI.Controller;

import BE.Playlists;
import BE.Song;
import GUI.Model.MyTunesModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class MyTunesMainController implements Initializable {

    @FXML
    private Label lblCurrentlyPlaying;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private Slider volumeSlider;
    @FXML
    private TableView<Playlists> tblPlaylists;
    @FXML
    private TableView<Song> tblSongs;
    @FXML
    private TableColumn<Song, String> colTitles;
    @FXML
    private TableColumn<Song, String> colArtists;
    @FXML
    private TableColumn<Song, String> colCategories;
    @FXML
    private TableColumn<Song, Double> colTime;

    private MyTunesModel songModel;

    public MyTunesMainController()
    {
        try{
            songModel = new MyTunesModel();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }
    @FXML
    private void onClickSearch(ActionEvent actionEvent) {

    }

    // Song Management
    @FXML
    private void onClickNewSong(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickUpdateSong(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickDeleteSong(ActionEvent actionEvent) {
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();

        if(selectedSong != null) {
            try {
                songModel.deleteSongs(selectedSong);
            }
            catch (Exception err) {
                displayError(err);
            }
        }
    }

    @FXML
    private void onClickMoveSongToPlaylist(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickClose(ActionEvent actionEvent) {

    }

    // Playlist
    @FXML
    private void onClickNewPlaylist(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickUpdatePlaylist(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickDeletePlaylist(ActionEvent actionEvent) {

    }

    // Songs on Playlist
    @FXML
    private void onClickScrollUp(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickScrollDown(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickDeleteSongInPlaylist(ActionEvent actionEvent) {

    }

    // Playing music management
    @FXML
    private void onClickNextSong(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickPreviousSong(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickPlayPause(ActionEvent actionEvent) {

    }
}
