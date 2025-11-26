package BLL;

import BE.Song;
import BLL.util.MyTunesSearcher;
import DAL.IMyTunesDataAccess;
import DAL.db.MyTunesDAO_DB;

import java.io.IOException;
import java.util.List;

public class MyTunesManager {

    private IMyTunesDataAccess songDAO;
    private MyTunesSearcher myTunesSearcher = new MyTunesSearcher();

    public MyTunesManager() throws IOException {
        songDAO = new MyTunesDAO_DB();
    }

    public List<Song> getAllSongs() throws Exception {
        return songDAO.getAllSongs();
    }

    public List<Song> searchSongs(String query) throws Exception {
        List<Song> allSongs = getAllSongs();
        List<Song> searchResult = myTunesSearcher.search(allSongs, query);
        return searchResult;
    }

    public Song createSongs(Song newSongs) throws Exception {
        return songDAO.createSongs(newSongs);
    }

    public void updateSongs(Song updatedSong) throws Exception {
        songDAO.updateSongs(updatedSong);
    }

    public void deleteSongs(Song selectedSong) throws Exception {
        songDAO.deleteSongs(selectedSong);
    }
}
