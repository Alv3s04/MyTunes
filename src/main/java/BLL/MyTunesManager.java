package BLL;

import BE.Songs;
import DAL.IMyTunesDataAccess;

public class MyTunesManager {
    private IMyTunesDataAccess songDAO;
    public void deleteMovie(Songs selectedSong) throws Exception {
        songDAO.deleteSongs(selectedSong);
    }
}
