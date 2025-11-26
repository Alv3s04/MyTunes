package BLL;

import BE.Song;
import DAL.IMyTunesDataAccess;

public class MyTunesManager {
    private IMyTunesDataAccess songDAO;
    public void deleteSongs(Song selectedSong) throws Exception {
        songDAO.deleteSongs(selectedSong);
    }
}
