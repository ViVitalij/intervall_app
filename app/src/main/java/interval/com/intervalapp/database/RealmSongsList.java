package interval.com.intervalapp.database;

import java.util.List;

import interval.com.intervalapp.model.Song;
import io.realm.Realm;

public class RealmSongsList {
    private Realm realm = Realm.getDefaultInstance();

    public void saveSongs(final List<Song> allSongs) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(allSongs);

            }
        });
    }
}

