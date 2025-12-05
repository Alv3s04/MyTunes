package GUI.Model;

import BE.Playlists;
import BE.Song;
import BLL.MyTunesManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.List;

public class MyTunesModel {

    private MyTunesManager myTunesManager;
    private ObservableList<Song> songs;
    private ObservableList<Playlists> playlists;
    private FilteredList<Song> filteredList;

    public MyTunesModel() throws Exception {
        myTunesManager = new MyTunesManager();
        // Insert Songs
        songs = FXCollections.observableArrayList();
        songs.addAll(myTunesManager.getAllSongs());
        filteredList = new FilteredList<>(songs);
        // Insert Playlists
        playlists = FXCollections.observableArrayList();
        playlists.addAll(myTunesManager.getAllPlaylists());
    }

    // Songs
    // create
    public Song createSongs(Song newSong) throws Exception {
        Song songCreated = myTunesManager.createSongs(newSong);
        songs.add(songCreated);
        return songCreated;
    }

    public void searchSongs(String query) throws Exception{
        List<Song> searchResults = myTunesManager.searchSongs(query);
        songs.clear();
        songs.addAll(searchResults);
    }

    // read
    public ObservableList<Song> getObservableSongs() {
        return songs;
    }

    // update - TODO: Check if working right
    public void updateSongs(Song updatedSong) throws Exception {
        // update song in DAL layer (through the layers)
        myTunesManager.updateSongs(updatedSong);

        // update observable list (and UI)
        int index = songs.indexOf(updatedSong);
        if (index != -1) {
            songs.set(index, updatedSong);}
    }

    // delete
    public void deleteSongs(Song selectedSong) throws Exception {
        // delete song in DAL layer (through the layers)
        myTunesManager.deleteSongs(selectedSong);

        // remove from observable list (and UI)
        songs.remove(selectedSong);
    }

    // Playlist
    // create
    public Playlists createPlaylists(Playlists newPlaylist) throws Exception {
        System.out.println("Trying to create playlist: " + newPlaylist.getName());
        Playlists playlistCreated = myTunesManager.createPlaylists(newPlaylist);
        if (playlistCreated == null) {
            System.out.println("MyTunesManager returned null!");
            throw new Exception("Failed to create playlist in DB");
        }
        playlists.add(playlistCreated);
        return playlistCreated;
    }


    // read
    public ObservableList<Playlists> getObservablePlaylists() {
        return playlists;
    }

    // update - TODO: Check if working right
    public void updatePlaylists(Playlists updatedPlaylist) throws Exception {
        // update playlist in DAL layer (through the layers)
        myTunesManager.updatePlaylists(updatedPlaylist);

        // update observable list (and UI)
        int index = playlists.indexOf(updatedPlaylist);
        if (index >= 0) {
            playlists.set(index, updatedPlaylist);
        }
    }

    // delete
    public void deletePlaylists(Playlists selectedPlaylist) throws Exception {
        // delete song in DAL layer (through the layers)
        myTunesManager.deletePlaylists(selectedPlaylist);

        // remove from observable list (and UI)
        playlists.remove(selectedPlaylist);
    }
}
