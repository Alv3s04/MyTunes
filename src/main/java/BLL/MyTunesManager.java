package BLL;

import BE.Playlists;
import BE.Song;
import BLL.util.MyTunesSearcher;
import DAL.IMyTunesDataAccess;
import DAL.db.MyTunesDAO_DB;

import java.io.IOException;
import java.util.List;

public class MyTunesManager {

    private IMyTunesDataAccess myTunesDAO;
    private MyTunesSearcher myTunesSearcher = new MyTunesSearcher();

    public MyTunesManager() throws IOException {
        myTunesDAO = new MyTunesDAO_DB();
    }

    // Songs
    public List<Song> getAllSongs() throws Exception {
        return myTunesDAO.getAllSongs();
    }

    public List<Song> searchSongs(String query) throws Exception {
        List<Song> allSongs = getAllSongs();
        List<Song> searchResult = myTunesSearcher.search(allSongs, query);
        return searchResult;
    }

    public Song createSongs(Song newSongs) throws Exception {
        return myTunesDAO.createSongs(newSongs);
    }

    public void updateSongs(Song updatedSong) throws Exception {
        myTunesDAO.updateSongs(updatedSong);
    }

    public void deleteSongs(Song selectedSong) throws Exception {
        myTunesDAO.deleteSongs(selectedSong);
    }

    // Playlist
    public List<Playlists> getAllPlaylists() throws Exception {
        return myTunesDAO.getAllPlaylists();
    }

    public Playlists createPlaylists(Playlists newPlaylists) throws Exception {
        return myTunesDAO.createPlaylists(newPlaylists);
    }

    public void updatePlaylists(Playlists updatedPlaylists) throws Exception {
        myTunesDAO.updatePlaylists(updatedPlaylists);
    }

    public void deletePlaylists(Playlists selectedPlaylists) throws Exception {
        myTunesDAO.deletePlaylists(selectedPlaylists);
    }
}
