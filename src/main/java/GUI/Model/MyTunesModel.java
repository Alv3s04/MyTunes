package GUI.Model;

import BE.Songs;
import BLL.MyTunesManager;

public class MyTunesModel {
    private MyTunesManager songManager;
    public void deleteSong(Songs selectedSong) throws Exception {
        songManager.deleteMovie(selectedSong);
    }
}
