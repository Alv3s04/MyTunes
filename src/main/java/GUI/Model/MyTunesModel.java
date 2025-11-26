package GUI.Model;

import BE.Song;
import BLL.MyTunesManager;

public class MyTunesModel {
    private MyTunesManager songManager;
    public void deleteSong(Song selectedSong) throws Exception {
        songManager.deleteSongs(selectedSong);
    }
}
