package GUI.Controller;

import BE.Playlists;
import BE.Song;
import BLL.util.MyTunesSearcher;
import GUI.Model.MyTunesModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MyTunesMainController implements Initializable {

    @FXML
    private Label lblCurrentlyPlaying;
    @FXML
    private Label lblTimer;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private Button btnSearchClear;
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
    @FXML
    private TableColumn<Playlists, String> colPlaylistName;
    @FXML
    private TableColumn<Playlists, Integer> colPlaylistSongs;
    @FXML
    private TableColumn<Playlists, Double> colPlaylistTime;

    private MyTunesModel myTunesModel = new MyTunesModel();
    private ObservableList<Song> allSongs;
    private MyTunesSearcher searcher;
    private boolean isFilterActive = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colTitles.setCellValueFactory(new PropertyValueFactory<>("title"));
        colArtists.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colCategories.setCellValueFactory(new PropertyValueFactory<>("category"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colPlaylistName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPlaylistSongs.setCellValueFactory(new PropertyValueFactory<>("songs"));
        colPlaylistTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        tblSongs.setItems(myTunesModel.getObservableSongs());
        tblPlaylists.setItems(myTunesModel.getObservablePlaylists());
        allSongs = myTunesModel.getObservableSongs();

        tblSongs.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, selectedSong) ->
        {
            if (selectedSong != null) {
                lblCurrentlyPlaying.setText(selectedSong.getTitle() + " - " + selectedSong.getArtist());
            }
        });

        tblPlaylists.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, selectedPlaylist) -> {
            if (selectedPlaylist != null) {
                // Example: load the songs from the playlist
                // Or update a label or list
            }
        });
    }

    public MyTunesMainController() throws Exception {
        try{
            myTunesModel = new MyTunesModel();
            searcher = new MyTunesSearcher();
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
    private void onClickSearchClear(ActionEvent actionEvent) {

        // CLEAR FILTER
        if (isFilterActive) {
            tblSongs.setItems(allSongs);
            txtFieldSearch.clear();
            btnSearchClear.setText("🔍");
            isFilterActive = false;
            return;
        }

        // APPLY FILTER
        String query = txtFieldSearch.getText().trim();
        if (query.isEmpty()) {
            return; // nothing typed
        }

        // Use our searcher
        List<Song> filtered = searcher.search(allSongs, query);

        tblSongs.setItems(FXCollections.observableArrayList(filtered));
        btnSearchClear.setText("❌");
        isFilterActive = true;
    }

    // Song Management
    @FXML
    private void onClickNewSong(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MyTunesSong.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("New Song");
        stage.setScene(scene);
        MyTunesSongController controller = fxmlLoader.getController();
        controller.setModel(myTunesModel);
        stage.initModality(Modality.APPLICATION_MODAL); // Makes only open one new window
        stage.show();
    }

    @FXML
    private void onClickUpdateSong(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MyTunesSong.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        stage.setTitle("Edit Song");
        stage.setScene(scene);
        MyTunesSongController controller = fxmlLoader.getController();
        controller.setModel(myTunesModel);
        stage.initModality(Modality.APPLICATION_MODAL); // Makes only open one new window
        stage.show();
    }

    private void setModel(MyTunesModel myTunesModel) {
        this.myTunesModel = myTunesModel;
    }

    @FXML
    private void onClickDeleteSong(ActionEvent actionEvent) {
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();

        if(selectedSong != null) {
            try {
                myTunesModel.deleteSongs(selectedSong);
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
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    // Playlist
    @FXML
    private void onClickNewPlaylist(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MyTunesPlaylist.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("New Playlist");
        stage.setScene(scene);
        MyTunesPlaylistController controller = fxmlLoader.getController();
        controller.setModel(myTunesModel);
        stage.initModality(Modality.APPLICATION_MODAL); // Makes only open one new window
        stage.show();
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
