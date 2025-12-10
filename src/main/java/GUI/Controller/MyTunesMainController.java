package GUI.Controller;

import BE.Playlists;
import BE.Song;
import BLL.util.MyTunesSearcher;
import DAL.db.MyTunesDAO_DB;
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
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.print.attribute.standard.MediaPrintableArea;
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
    @FXML private Label lblSongsOnPlaylist;
    @FXML private Label lblCurrentlyPlaying;
    @FXML private Label lblTimer;
    @FXML private TextField txtFieldSearch;
    @FXML private Button btnSearchClear;
    @FXML private Button btnPlayPause;
    @FXML private Button btnMoveSongUp;
    @FXML private Button btnMoveSongDown;
    @FXML private Button btnDeleteSongOnPlaylist;
    @FXML private Button btnMoveSongToPlaylist;
    @FXML private Slider volumeSlider;
    @FXML private Slider timeSlider;
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
    private MyTunesDAO_DB dao;

    private enum SongSource {PLAYLIST, ALL_SONGS} // TODO: Explain
    private SongSource currentSource = null; // TODO: Explain
    Song currentlyPlayingSong = null; // Tracks currently playing song

    /**
     * Runs automatically when FXML is loaded.
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
            hideSongsOnPlaylistControls();

            volumeSlider.setValue(100.0);

        // Listener for playlist (Hides and shows songs on playlist controls)
        tblPlaylists.getSelectionModel().selectedItemProperty().addListener((obs, old, playlist) -> {
            if (playlist != null) {
                try {
                    ObservableList<Song> songsOnPlaylist = model.getSongsOnPlaylist(playlist);
                    lvSongsOnPlaylist.setItems(songsOnPlaylist);
                    lblSongsOnPlaylist.setText("Songs on: " + playlist.getName());
                    showSongsOnPlaylistControls();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                hideSongsOnPlaylistControls();
            }
        });

            // When a song in the playlist is selected, clear selection in all songs table
            lvSongsOnPlaylist.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
                if (selected != null) {
                    tblSongs.getSelectionModel().clearSelection();
                    currentSource = SongSource.PLAYLIST;
                }
            });

            // When a song in the all songs table is selected, clear selection in playlist ListView
            tblSongs.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
                if (selected != null) {
                    lvSongsOnPlaylist.getSelectionModel().clearSelection();
                    currentSource = SongSource.ALL_SONGS;
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
    public void setModel(MyTunesModel model) throws IOException {
        this.model = model;
        this.searcher = new MyTunesSearcher();

        try {
            this.dao = new MyTunesDAO_DB();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        Playlists selectedPlaylist = tblPlaylists.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null && selectedSong != null) {
            try {
                dao.addSongToPlaylist(selectedPlaylist.getId(), selectedSong.getId());

                // Refresh playlist songs
                List<Song> updatedSongs = dao.getSongsOnPlaylist(selectedPlaylist);
                lvSongsOnPlaylist.getItems().setAll(updatedSongs);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    public void onClickDeleteSongOnPlaylist(ActionEvent actionEvent) {
        Song selectedSong = lvSongsOnPlaylist.getSelectionModel().getSelectedItem();
        Playlists selectedPlaylist = tblPlaylists.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            try {
                model.deleteSongOnPlaylist(selectedPlaylist, selectedSong);
                lvSongsOnPlaylist.getItems().remove(selectedSong);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
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

        ObservableList<Song> list = (currentSource == SongSource.PLAYLIST)
                ? lvSongsOnPlaylist.getItems()
                : tblSongs.getItems();

        int index = list.indexOf(currentlyPlayingSong);
        if (index == -1 || index >= list.size() - 1)
            return; // no next song

        Song nextSong = list.get(index + 1);
        play(nextSong, currentSource);
    }


    /**
     * Handles clicking the 'Previous Song' button.
     * Controls selection sync between ListView and TableView and plays the previous song.
     */
    @FXML
    private void onClickPreviousSong(ActionEvent event) {
        if (currentlyPlayingSong == null || currentSource == null)
            return;

        ObservableList<Song> list = (currentSource == SongSource.PLAYLIST)
                ? lvSongsOnPlaylist.getItems()
                : tblSongs.getItems();

        int index = list.indexOf(currentlyPlayingSong);
        if (index <= 0)
            return; // no previous song

        Song prevSong = list.get(index - 1);
        play(prevSong, currentSource);
    }


    /**
     * Plays selected song
     * To avoid duplicate code
     */
    private void play(Song song, SongSource source) {
        currentlyPlayingSong = song;
        currentSource = source;

        // Update UI selection
        if (source == SongSource.PLAYLIST)
            lvSongsOnPlaylist.getSelectionModel().select(song);
        else
            tblSongs.getSelectionModel().select(song);

        lblCurrentlyPlaying.setText(song.getTitle() + " - " + song.getArtist());

        // Load and play the song
        musicPlayer.stop();
        musicPlayer.load(song.getFilePath());

        MediaPlayer mp = musicPlayer.getMediaPlayer();
        if (mp != null) {
            // Set up listeners when media is ready
            mp.setOnReady(() -> {
                Duration total = mp.getTotalDuration();
                timeSlider.setMax(total.toSeconds());
                lblTimer.setText(formatTime(Duration.ZERO, total));
            });

            // Update slider and timer as song plays
            mp.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                if (!timeSlider.isValueChanging()) {
                    timeSlider.setValue(newTime.toSeconds());
                }
                Duration total = mp.getTotalDuration();
                lblTimer.setText(formatTime(newTime, total));
            });

            musicPlayer.play();
            btnPlayPause.setText("⏸");
        }
    }

    /**
     * Handles play/pause logic.
     * Starts a new song if a new selection is made,
     * otherwise pauses/resumes current song.
     */
    @FXML
    private void onClickPlayPause(ActionEvent actionEvent) {
        Song selectedSong = (lvSongsOnPlaylist.getSelectionModel().getSelectedItem() != null)
                ? lvSongsOnPlaylist.getSelectionModel().getSelectedItem()
                : tblSongs.getSelectionModel().getSelectedItem();
        if (selectedSong == null) return;

        SongSource source = (lvSongsOnPlaylist.getSelectionModel().getSelectedItem() != null)
                ? SongSource.PLAYLIST
                : SongSource.ALL_SONGS;

        if (currentlyPlayingSong != selectedSong) {
            play(selectedSong, source);
            return;
        }

        if (musicPlayer.isPlaying()) {
            musicPlayer.pause();
            btnPlayPause.setText("‣");
        } else {
            musicPlayer.play();
            btnPlayPause.setText("⏸");
        }
    }

    /**
     * Hides songs on playlist controls
     */
    private void hideSongsOnPlaylistControls() {
        lblSongsOnPlaylist.setVisible(false);
        lvSongsOnPlaylist.setVisible(false);
        btnMoveSongToPlaylist.setVisible(false);
        btnMoveSongUp.setVisible(false);
        btnMoveSongDown.setVisible(false);
        btnDeleteSongOnPlaylist.setVisible(false);
    }

    /**
     * Shows songs on playlist controls
     */
    private void showSongsOnPlaylistControls() {
        lblSongsOnPlaylist.setVisible(true);
        lvSongsOnPlaylist.setVisible(true);
        btnMoveSongToPlaylist.setVisible(true);
        btnMoveSongUp.setVisible(true);
        btnMoveSongDown.setVisible(true);
        btnDeleteSongOnPlaylist.setVisible(true);
    }
}