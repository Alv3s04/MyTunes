package DAL;

import BE.Tunes;

import java.util.List;

public class MyTunesDAO implements IMyTunesDataAccess {

    @Override
    public List<Tunes> getAllTunes() throws Exception {
        //return List.of();
        throw new UnsupportedOperationException();
    }

    @Override
    public Tunes createTunes(Tunes newTunes) throws Exception {
        return null;
    }

    @Override
    public void updateTunes(Tunes tunes) throws Exception {

    }

    @Override
    public void deleteTunes(Tunes tunes) throws Exception {

    }
}
