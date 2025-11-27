package GUI.Model;

import BE.Playlists;
import BE.Song;
import BLL.MyTunesManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MyTunesModel {
    private MyTunesManager myTunesManager;
    private ObservableList<Song> songs;
    private ObservableList<Playlists> playlists;

    public MyTunesModel() throws Exception {
        myTunesManager = new MyTunesManager();
        // Insert Songs
        songs = FXCollections.observableArrayList();
        songs.addAll(myTunesManager.getAllSongs());
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

    // read
    public ObservableList<Song> getObservableSongs() {
        return songs;
    }

    // update
    public void updateSongs(Song updatedSong) throws Exception {
        // update song in DAL layer (through the layers)
        myTunesManager.updateSongs(updatedSong);

        // update observable list (and UI)
        Song s = songs.get(songs.indexOf(updatedSong));
        s.setTitle(updatedSong.getTitle());
        s.setArtist(updatedSong.getArtist());
        s.setCategory(updatedSong.getCategory());
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
        Playlists playlistCreated = myTunesManager.createPlaylists(newPlaylist);
        playlists.add(playlistCreated);
        return playlistCreated;
    }

    // read
    public ObservableList<Playlists> getObservablePlaylists() {
        return playlists;
    }

    // update
    public void updatePlaylists(Playlists updatedPlaylist) throws Exception {
        // update playlist in DAL layer (through the layers)
        myTunesManager.updatePlaylists(updatedPlaylist);

        // update observable list (and UI)
        Playlists p = playlists.get(playlists.indexOf(updatedPlaylist));
        // TODO: Make Playlists in BE
        // p.setTitle(updatedPlaylist.getTitle());
    }

    // delete
    public void deletePlaylists(Playlists selectedPlaylist) throws Exception {
        // delete song in DAL layer (through the layers)
        myTunesManager.deletePlaylists(selectedPlaylist);

        // remove from observable list (and UI)
        playlists.remove(selectedPlaylist);
    }
}
