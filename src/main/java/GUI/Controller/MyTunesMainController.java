package GUI.Controller;

import BE.Playlists;
import BE.Song;
import BLL.util.MyTunesSearcher;
import GUI.Model.MyTunesModel;
import GUI.util.MusicPlayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Main controller for the MyTunes GUI.
 * Handles interaction between the UI, model, and music player.
 */
public class MyTunesMainController implements Initializable {

    // UI elements
    @FXML private Label lblCurrentlyPlaying;
    @FXML private Label lblTimer;
    @FXML private TextField txtFieldSearch;
    @FXML private Button btnSearchClear;
    @FXML private Button btnPlayPause;
    @FXML private Slider volumeSlider;
    @FXML private ListView<Song> lvSongsOnPlaylist;
    @FXML private TableView<Playlists> tblPlaylists;
    @FXML private TableView<Song> tblSongs;

    // Song table columns
    @FXML private TableColumn<Song, String> colTitles;
    @FXML private TableColumn<Song, String> colArtists;
    @FXML private TableColumn<Song, String> colCategories;
    @FXML private TableColumn<Song, Double> colTime;

    // Playlist table columns
    @FXML private TableColumn<Playlists, String> colPlaylistName;
    @FXML private TableColumn<Playlists, Integer> colPlaylistSongs;
    @FXML private TableColumn<Playlists, Double> colPlaylistTime;

    private MyTunesModel model; // Reference to model layer for DB + logic
    private MusicPlayer musicPlayer = new MusicPlayer(); // Handles audio playback
    private ObservableList<Song> allSongs; // All songs from model
    private MyTunesSearcher searcher; // Search utility
    private boolean isFilterActive = false; // Tracks if search filter is applied
    @FXML
    private Label lblSongsOnPlaylist;

    private enum SongSource {PLAYLIST, ALL_SONGS} // TODO: Explain
    private SongSource currentSource = null; // TODO: Explain
    Song currentlyPlayingSong = null; // Tracks currently playing song

    /**
     * Runs automatically when FXML is loaded.
     * TODO: Load songs from selected playlist
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Connect table columns to Song and Playlist properties
        colTitles.setCellValueFactory(new PropertyValueFactory<>("title"));
        colArtists.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colCategories.setCellValueFactory(new PropertyValueFactory<>("category"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colPlaylistName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPlaylistSongs.setCellValueFactory(new PropertyValueFactory<>("songs"));
        colPlaylistTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        volumeSlider.setValue(100.0);

        // Listener for playlist
        tblPlaylists.getSelectionModel().selectedItemProperty().addListener((obs, old, playlist) -> {
            if (playlist != null) {
                ObservableList<Song> songsOnPlaylist = null;
                try {
                    songsOnPlaylist = model.getSongsOnPlaylist(playlist);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                lvSongsOnPlaylist.setItems(songsOnPlaylist);
            }
        });

        // Listener for volume
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            musicPlayer.setVolume(newValue.doubleValue() / 100);
        });
    }

    /**
     * Injects the model into the controller and initializes the tables.
     */
    public void setModel(MyTunesModel model) {
        this.model = model;
        this.searcher = new MyTunesSearcher();

        // Fill tables with observable data from model
        tblSongs.setItems(model.getObservableSongs());
        tblPlaylists.setItems(model.getObservablePlaylists());
        allSongs = model.getObservableSongs();
    }

    /**
     * Displays an error popup.
     */
    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    /**
     * Handles search button click.
     * Toggles between applying and clearing filter.
     */
    @FXML
    private void onClickSearchClear(ActionEvent actionEvent) {

        // If filter is active, clear it
        if (isFilterActive) {
            tblSongs.setItems(allSongs);
            txtFieldSearch.clear();
            btnSearchClear.setText("🔍");
            isFilterActive = false;
            return;
        }

        // Apply filter
        String query = txtFieldSearch.getText().trim();
        if (query.isEmpty()) return;

        // Perform search
        List<Song> filtered = searcher.search(allSongs, query);

        tblSongs.setItems(FXCollections.observableArrayList(filtered));
        btnSearchClear.setText("❌");
        isFilterActive = true;
    }

    // SONG MANAGEMENT
    /**
     * Opens window (MyTunesSong.fxml) to create a new song.
     */
    @FXML
    private void onClickNewSong(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MyTunesSong.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        stage.setTitle("New Song");
        stage.setScene(scene);

        // Pass model to the new controller
        MyTunesSongController controller = fxmlLoader.getController();
        controller.setModel(model);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * Opens window (MyTunesSong.fxml) to edit the selected song.
     */
    @FXML
    private void onClickUpdateSong(ActionEvent actionEvent) throws IOException {
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
        if (selectedSong == null) return;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MyTunesSong.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        stage.setTitle("Edit Song");
        stage.setScene(scene);

        MyTunesSongController controller = fxmlLoader.getController();
        controller.setModel(model);
        controller.setEditingSong(selectedSong);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * Deletes the selected song.
     */
    @FXML
    private void onClickDeleteSong(ActionEvent actionEvent) {
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();

        if (selectedSong != null) {
            try {
                model.deleteSongs(selectedSong);
            } catch (Exception err) {
                displayError(err);
            }
        }
    }

    /**
     * Adds a selected song to the playlist ListView (not database).
     */
    @FXML
    private void onClickMoveSongToPlaylist(ActionEvent actionEvent) {
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
        if (selectedSong == null) return;

        lvSongsOnPlaylist.getItems().add(selectedSong);
        lvSongsOnPlaylist.getSelectionModel().select(selectedSong);
    }

    /**
     * Closes the app.
     */
    @FXML
    private void onClickClose(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    // PLAYLIST MANAGEMENT
    /**
     * Opens window (MyTunesPlaylist.fxml) to create a new playlist.
     */
    @FXML
    private void onClickNewPlaylist(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MyTunesPlaylist.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        stage.setTitle("New Playlist");
        stage.setScene(scene);

        MyTunesPlaylistController controller = fxmlLoader.getController();
        controller.setModel(model);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * Opens window (MyTunesPlaylist.fxml) to rename/update a playlist.
     */
    @FXML
    private void onClickUpdatePlaylist(ActionEvent actionEvent) throws IOException {
        Playlists selectedPlaylist = tblPlaylists.getSelectionModel().getSelectedItem();
        if (selectedPlaylist == null) return;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MyTunesPlaylist.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        stage.setTitle("Rename Playlist");
        stage.setScene(scene);

        MyTunesPlaylistController controller = fxmlLoader.getController();
        controller.setModel(model);
        controller.setEditingPlaylist(selectedPlaylist);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * Deletes selected playlist.
     */
    @FXML
    private void onClickDeletePlaylist(ActionEvent actionEvent) {
        Playlists selectedPlaylists = tblPlaylists.getSelectionModel().getSelectedItem();

        if (selectedPlaylists != null) {
            try {
                model.deletePlaylists(selectedPlaylists);
            } catch (Exception err) {
                displayError(err);
            }
        }
    }

    // SONGS ON PLAYLIST
    /**
     * Moves song up in the playlist ListView.
     */
    @FXML
    private void onClickMoveUp(ActionEvent actionEvent) {
        int index = lvSongsOnPlaylist.getSelectionModel().getSelectedIndex();
        if (index <= 0) return;

        Collections.swap(lvSongsOnPlaylist.getItems(), index, index - 1);
        lvSongsOnPlaylist.getSelectionModel().select(index - 1);
    }

    /**
     * Moves song down.
     */
    @FXML
    private void onClickMoveDown(ActionEvent actionEvent) {
        ObservableList<Song> items = lvSongsOnPlaylist.getItems();
        int index = lvSongsOnPlaylist.getSelectionModel().getSelectedIndex();

        if (index == -1 || index >= items.size() - 1) return;

        Collections.swap(items, index, index + 1);
        lvSongsOnPlaylist.getSelectionModel().select(index + 1);
    }

    /**
     * Removes song from playlist.
     */
    @FXML
    private void onClickDeleteSongOnPlaylist(ActionEvent actionEvent) {
        Song selectedSong = lvSongsOnPlaylist.getSelectionModel().getSelectedItem();
        if (selectedSong == null) return;

        lvSongsOnPlaylist.getItems().remove(selectedSong);
    }

    // MUSIC PLAYER
    /**
     * Handles clicking the 'Next Song' button.
     * Controls selection sync between ListView and TableView and plays the next song.
     */
    @FXML
    private void onClickNextSong(ActionEvent event) {

        if (currentlyPlayingSong == null || currentSource == null)
            return;

        ObservableList<Song> list;
        int index;

        if (currentSource == SongSource.PLAYLIST) {
            list = lvSongsOnPlaylist.getItems();
            index = list.indexOf(currentlyPlayingSong);

            if (index == -1 || index >= list.size() - 1)
                return; // no next
            Song next = list.get(index + 1);

            lvSongsOnPlaylist.getSelectionModel().select(index + 1);
            play(next);

        } else { // ALL_SONGS
            list = tblSongs.getItems();
            index = list.indexOf(currentlyPlayingSong);

            if (index == -1 || index >= list.size() - 1)
                return;
            Song next = list.get(index + 1);

            tblSongs.getSelectionModel().select(index + 1);
            play(next);
        }
    }

    /**
     * Handles clicking the 'Previous Song' button.
     * Controls selection sync between ListView and TableView and plays the previous song.
     */
    @FXML
    private void onClickPreviousSong(ActionEvent event) {

        if (currentlyPlayingSong == null || currentSource == null)
            return;

        ObservableList<Song> list;
        int index;

        if (currentSource == SongSource.PLAYLIST) {
            list = lvSongsOnPlaylist.getItems();
            index = list.indexOf(currentlyPlayingSong);

            if (index <= 0)
                return;
            Song prev = list.get(index - 1);

            lvSongsOnPlaylist.getSelectionModel().select(index - 1);
            play(prev);

        } else { // ALL_SONGS
            list = tblSongs.getItems();
            index = list.indexOf(currentlyPlayingSong);

            if (index <= 0)
                return;
            Song prev = list.get(index - 1);

            tblSongs.getSelectionModel().select(index - 1);
            play(prev);
        }
    }

    private void play(Song song) {
        musicPlayer.stop();
        musicPlayer.load(song.getFilePath());
        musicPlayer.play();
        currentlyPlayingSong = song;
        // Detect where it was selected from
        if (lvSongsOnPlaylist.getSelectionModel().getSelectedItem() == song) {
            currentSource = SongSource.PLAYLIST;
        } else if (tblSongs.getSelectionModel().getSelectedItem() == song) {
            currentSource = SongSource.ALL_SONGS;
        }
        lblCurrentlyPlaying.setText(song.getTitle() + " - " + song.getArtist());
    }

    /**
     * Handles play/pause logic.
     * Starts a new song if a new selection is made,
     * otherwise pauses/resumes current song.
     */
    @FXML
    private void onClickPlayPause(ActionEvent actionEvent) {
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
        if (selectedSong == null) return;

        // New song selected, load and play it
        if (currentlyPlayingSong != selectedSong) {
            musicPlayer.stop();
            musicPlayer.load(selectedSong.getFilePath());
            musicPlayer.play();

            currentlyPlayingSong = selectedSong;
            lblCurrentlyPlaying.setText(selectedSong.getTitle() + " - " + selectedSong.getArtist());
            btnPlayPause.setText("⏸");
            return;
        }
        // Same song, toggle pause/play
        if (musicPlayer.isPlaying()) {
            musicPlayer.pause();
            btnPlayPause.setText("‣");
        } else {
            musicPlayer.play();
            btnPlayPause.setText("⏸");
        }
    }
}