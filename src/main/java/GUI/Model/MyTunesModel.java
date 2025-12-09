package GUI.Model;

import BE.Playlists;
import BE.Song;
import BLL.MyTunesManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.util.List;

/**
 * The model class for MyTunes.
 * Acts as an intermediary between the UI controllers and the business logic layer (MyTunesManager).
 * Maintains observable lists of songs and playlists for automatic UI updates.
 */
public class MyTunesModel {

    private MyTunesManager myTunesManager; // Reference to business logic manager
    private ObservableList<Song> songs; // Observable list of all songs
    private ObservableList<Playlists> playlists; // Observable list of all playlists
    private FilteredList<Song> filteredList; // Filtered view of songs for search/filtering

    /**
     * Constructor initializes the model and loads all songs and playlists from the manager.
     * @throws Exception if data retrieval from MyTunesManager fails.
     */
    public MyTunesModel() throws Exception {
        myTunesManager = new MyTunesManager();

        // Load all songs from manager
        songs = FXCollections.observableArrayList();
        songs.addAll(myTunesManager.getAllSongs());
        filteredList = new FilteredList<>(songs);

        // Load all playlists from manager
        playlists = FXCollections.observableArrayList();
        playlists.addAll(myTunesManager.getAllPlaylists());
    }

    // SONGS
    /**
     * Creates a new song and adds it to the observable list.
     * @return The newly created Song object.
     * @throws Exception if creation in MyTunesManager fails.
     */
    public Song createSongs(Song newSong) throws Exception {
        Song songCreated = myTunesManager.createSongs(newSong);
        songs.add(songCreated); // Add to observable list for UI
        return songCreated;
    }

    /**
     * Searches songs based on a query and updates the observable list.
     * @throws Exception if search in MyTunesManager fails.
     */
    public void searchSongs(String query) throws Exception {
        List<Song> searchResults = myTunesManager.searchSongs(query);
        songs.clear();
        songs.addAll(searchResults); // Update observable list
    }

    /**
     * Returns the observable list of songs for UI binding.
     * @return ObservableList<Song>
     */
    public ObservableList<Song> getObservableSongs() {
        return songs;
    }

    /**
     * Updates an existing song in both the business layer and observable list.
     * @throws Exception if update in MyTunesManager fails.
     */
    public void updateSongs(Song updatedSong) throws Exception {
        myTunesManager.updateSongs(updatedSong); // Update in business layer

        // Update in observable list for UI refresh
        int index = songs.indexOf(updatedSong);
        if (index != -1) {
            songs.set(index, updatedSong);
        }
    }

    /**
     * Deletes a song from both the business layer and observable list.
     * @throws Exception if deletion in MyTunesManager fails.
     */
    public void deleteSongs(Song selectedSong) throws Exception {
        myTunesManager.deleteSongs(selectedSong); // Delete in business layer
        songs.remove(selectedSong); // Remove from observable list for UI
    }

    // PLAYLISTS
    /**
     * Creates a new playlist and adds it to the observable list.
     * @return The newly created Playlists object.
     * @throws Exception if creation fails in MyTunesManager.
     */
    public Playlists createPlaylists(Playlists newPlaylist) throws Exception {
        System.out.println("Trying to create playlist: " + newPlaylist.getName());
        Playlists playlistCreated = myTunesManager.createPlaylists(newPlaylist);
        if (playlistCreated == null) {
            System.out.println("MyTunesManager returned null!");
            throw new Exception("Failed to create playlist in DB");
        }
        playlists.add(playlistCreated); // Add to observable list for UI
        return playlistCreated;
    }

    /**
     * Returns the observable list of playlists for UI binding.
     * @return ObservableList<Playlists>
     */
    public ObservableList<Playlists> getObservablePlaylists() {
        return playlists;
    }

    /**
     * Updates an existing playlist in both the business layer and observable list.
     * @throws Exception if update in MyTunesManager fails.
     */
    public void updatePlaylists(Playlists updatedPlaylist) throws Exception {
        myTunesManager.updatePlaylists(updatedPlaylist); // Update in business layer

        // Update observable list for UI refresh
        int index = playlists.indexOf(updatedPlaylist);
        if (index >= 0) {
            playlists.set(index, updatedPlaylist);
        }
    }

    /**
     * Deletes a playlist from both the business layer and observable list.
     * @throws Exception if deletion in MyTunesManager fails.
     */
    public void deletePlaylists(Playlists selectedPlaylist) throws Exception {
        myTunesManager.deletePlaylists(selectedPlaylist); // Delete in business layer
        playlists.remove(selectedPlaylist); // Remove from observable list for UI
    }

    /**
     * Returns the observable list of songs in a playlist.
     * @return ObservableList<Song>
     */
    public ObservableList<Song> getSongsOnPlaylist(Playlists songsInPlaylist) throws Exception {
        return FXCollections.observableArrayList(
                myTunesManager.getSongsOnPlaylist(songsInPlaylist)
        );
    }
}