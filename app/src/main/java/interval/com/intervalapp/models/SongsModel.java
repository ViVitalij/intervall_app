package interval.com.intervalapp.models;

import org.parceler.Parcel;

import java.io.Serializable;

import interval.com.intervalapp.databases.RealmSongsList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by RENT on 2017-05-25.
 */

public class SongsModel extends RealmObject  {
    public static final String FAST = "fast";


    public static final String SLOW = "slow";


    @Getter
    @Setter
    @PrimaryKey
    private String id;
    @Setter
    private String author;
    @Setter
    private String tittle;

    private String uri;

    private String type;

    public SongsModel(String author, String tittle, String uri, String type) {
        this.author = author;
        this.tittle = tittle;
        this.uri = uri;
        this.type = type;
    }

    public SongsModel() {
    }

}
