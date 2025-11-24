package DAL;

// Project imports
import BE.Tunes;

// Java imports
import java.util.List;

/**
 * Basic CRUD operations on the Movie
 */
public interface IMyTunesDataAccess {

    List<Tunes> getAllTunes() throws Exception;
    Tunes createTunes(Tunes newTunes) throws Exception;
    void updateTunes(Tunes tunes) throws Exception;
    void deleteTunes(Tunes tunes) throws Exception;
}
