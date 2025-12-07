package BLL.util;

import BE.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for searching songs by title or artist.
 */
public class MyTunesSearcher {

    // Main search method: takes a list of songs and a search query, then returns all songs where the title or artist matches the query.
    public List<Song> search(List<Song> searchBase, String query) {
        List<Song> searchResult = new ArrayList<>();

        // Loop through every song in the provided list
        for (Song song : searchBase) {

            // If the query matches either the song title or the artist name, add the song to the result list
            if (compareToSongTitle(query, song) || compareToSongArtist(query, song)) {
                searchResult.add(song);
            }
        }
        // Return all found matches
        return searchResult;
    }

    // Checks if the query matches part of the artist's name (case-insensitive)
    private boolean compareToSongArtist(String query, Song song) {
        return song.getArtist().toLowerCase().contains(query.toLowerCase());
    }

    // Checks if the query matches part of the song title (case-insensitive)
    private boolean compareToSongTitle(String query, Song song) {
        return song.getTitle().toLowerCase().contains(query.toLowerCase());
    }
}