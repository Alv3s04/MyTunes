package DAL.db;

import BE.Playlists;
import BE.Song;
import DAL.ISongDataAccess;
import DAL.IPlaylistDataAccess;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyTunesDAO_DB implements ISongDataAccess, IPlaylistDataAccess {

    private DBConnector databaseConnector = new DBConnector();

    public MyTunesDAO_DB() throws IOException {
    }

    // SONGS
    // create
    @Override
    public Song createSongs(Song newSong) throws Exception {
        String sql = "INSERT INTO dbo.Song (Title, Artist, Category, Time, FilePath) VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Bind parameters
            stmt.setString(1, newSong.getTitle());
            stmt.setString(2, newSong.getArtist());
            stmt.setString(3, newSong.getCategory());
            stmt.setDouble(4, newSong.getTime());
            stmt.setString(5, newSong.getFilePath());

            // Execute the insert
            stmt.executeUpdate();

            // Retrieve the auto-generated ID from the database
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }

            // Return a new Song object with the correct ID
            return new Song(id, newSong.getTitle(), newSong.getArtist(), newSong.getCategory(), newSong.getTime(), newSong.getFilePath());
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create song", ex);
        }
    }

    // read
    @Override
    public List<Song> getAllSongs() throws Exception {

        ArrayList<Song> allSongs = new ArrayList<>();

        // try-with-resources
        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM dbo.Song;";
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {
                //Map DB row to Song object
                int id = rs.getInt("Song_ID");
                String title = rs.getString("Title");
                String artist = rs.getString("Artist");
                String category = rs.getString("Category");
                double time = rs.getDouble("Time");
                String filePath = rs.getString("FilePath");

                Song song = new Song(id, title, artist, category, time, filePath);
                allSongs.add(song);
            }
            return allSongs;
        }

        catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get songs from database", ex);
        }
    }

    // update
    @Override
    public void updateSongs(Song song) throws Exception {
        String sql = "UPDATE dbo.Song SET Title = ?, Artist = ?, Category = ?, Time = ? WHERE Song_ID = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, song.getTitle());
            stmt.setString(2, song.getArtist());
            stmt.setString(3, song.getCategory());
            stmt.setDouble(4, song.getTime());
            stmt.setInt(5, song.getId()); // her identificere vi hvilken row der ska opdateres.

            stmt.executeUpdate();
        }
    }

    // delete
    @Override
    public void deleteSongs(Song song) throws Exception {
        String sql = "DELETE FROM dbo.Song WHERE Song_ID = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1,song.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get songs from database.",ex);
        }
    }

    // PLAYLIST
    // create
    @Override
    public Playlists createPlaylists(Playlists newPlaylist) throws Exception {
        String sql = "INSERT INTO dbo.Playlist (Name, Songs, Time) VALUES (?, ?, ?);";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Bind parameters
            stmt.setString(1, newPlaylist.getName());
            stmt.setInt(2, newPlaylist.getSongs());
            stmt.setDouble(3, newPlaylist.getTime());

            // Execute the insert
            stmt.executeUpdate();

            // Retrieve the auto-generated ID from the database
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }

            // Return a new Playlists object with the correct ID
            return new Playlists(id, newPlaylist.getName(), newPlaylist.getSongs(), newPlaylist.getTime());

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create playlist", ex);
        }
    }

    // read
    @Override
    public List<Playlists> getAllPlaylists() throws Exception {

        ArrayList<Playlists> allPlaylists = new ArrayList<>();

        // try-with-resources
        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM dbo.Playlist;";
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {
                //Map DB row to Song object
                int id = rs.getInt("Playlist_ID");
                String name = rs.getString("Name");
                int songs = rs.getInt("Songs");
                double time = rs.getDouble("Time");
                Playlists playlist = new Playlists(id, name, songs, time);
                allPlaylists.add(playlist);
            }
            return allPlaylists;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get playlists from database", ex);
        }
    }

    // update
    @Override
    public void updatePlaylists(Playlists playlist) throws Exception {
        String sql = "UPDATE dbo.Playlist SET Name = ?, Songs = ?, Time = ? WHERE Playlist_ID = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Bind parameters
            stmt.setString(1, playlist.getName());
            stmt.setInt(2, playlist.getSongs());
            stmt.setDouble(3, playlist.getTime());
            stmt.setInt(4, playlist.getId()); // Identify which row to update

            // Execute update
            stmt.executeUpdate();
        }
    }

    // delete
    @Override
    public void deletePlaylists(Playlists playlist) throws Exception {
        String sql = "DELETE FROM dbo.Playlist WHERE Playlist_ID = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlist.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not delete playlist", ex);
        }
    }
}