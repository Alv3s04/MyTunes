package GUI.Controller;

import BE.Playlists;
import BE.Songs;
import GUI.Model.MyTunesModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MyTunesMainController {

    @FXML
    private Label lblCurrentlyPlaying;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private Slider sliderVolume;
    @FXML
    private TableView<Playlists> tblPlaylists;
    @FXML
    private TableView<Songs> tblSongs;
    @FXML
    private TableColumn<Songs, String> colTitles;
    @FXML
    private TableColumn<Songs, String> colArtists;
    @FXML
    private TableColumn<Songs, String> colCategories;
    @FXML
    private TableColumn<Songs, Double> colTime;

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
    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }
    @FXML
    private void onClickSearch(ActionEvent actionEvent) {

    }

    /**
     * Playing music management
     * @param actionEvent
     */
    @FXML
    private void onClickNextSong(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickPreviousSong(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickPlayPause(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickScrollUp(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickScrollDown(ActionEvent actionEvent) {

    }

    /**
     * Song Controls
     * @param actionEvent
     */
    @FXML
    private void onClickUpdateSong(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickNewSong(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickDeleteSong(ActionEvent actionEvent) {
Songs selectedSong = tblSongs.getSelectionModel().getSelectedItem();

if(selectedSong != null){
    try{
        songModel.deleteSong(selectedSong);
    }
    catch (Exception err){
        displayError(err);
    }
}
    }

    @FXML
    private void onClickClose(ActionEvent actionEvent) {

    }

    /**
     * Playlist Controls
     * @param actionEvent
     */
    @FXML
    private void onClickNewPlaylist(ActionEvent actionEvent) {
    }

    @FXML
    private void onClickUpdatePlaylist(ActionEvent actionEvent) {
    }

    @FXML
    private void onClickDeletePlaylist(ActionEvent actionEvent) {
    }

    @FXML
    private void onClickDeleteSongInPlaylist(ActionEvent actionEvent) {
    }

    @FXML
    private void onClickMoveSongToPlaylist(ActionEvent actionEvent) {
    }
}
