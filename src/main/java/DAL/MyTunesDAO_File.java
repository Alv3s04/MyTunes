package DAL;

// Project imports
import BE.Playlists;
import BE.Song;
import DAL.db.DBConnector;
// Java imports
import java.util.List;

public class MyTunesDAO_File implements IMyTunesDataAccess {

    private DBConnector dbConnector;

    @Override
    public List<Song> getAllSongs() throws Exception {
        // return List.of();
        throw new UnsupportedOperationException();
    }

    @Override
    public Song createSongs(Song newSong) throws Exception {
return null;
    }

    @Override
    public void updateSongs(Song song) throws Exception {

    }

    @Override
    public void deleteSongs(Song song) throws Exception {

    }

    @Override
    public List<Playlists> getAllPlaylists() throws Exception {
        // return List.of();
        throw new UnsupportedOperationException();
    }

    @Override
    public Playlists createPlaylists(Playlists newPlaylist) throws Exception {
        return null;
    }

    @Override
    public void updateSongs(Playlists playlists) throws Exception {

    }

    @Override
    public void deleteSongs(Playlists playlists) throws Exception {

    }
}