package DAL;

// Project imports
import BE.Playlists;
import BE.Songs;
import DAL.db.DBConnector;
// Java imports
import java.util.List;

public class MyTunesDAO_File implements IMyTunesDataAccess {

    private DBConnector dbConnector;

    @Override
    public List<Songs> getAllSongs() throws Exception {
        // return List.of();
        throw new UnsupportedOperationException();
    }

    @Override
    public Songs createSongs(Songs newSongs) throws Exception {
        return null;
    }

    @Override
    public void updateSongs(Songs songs) throws Exception {

    }

    @Override
    public void deleteSongs(Songs songs) throws Exception {

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