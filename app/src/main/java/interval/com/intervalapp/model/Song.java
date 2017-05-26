package interval.com.intervalapp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

public class Song extends RealmObject  {
    //TODO be aware
    public static final String FAST = "fast";
    public static final String SLOW = "slow";

    @Getter
    @Setter
    @PrimaryKey
    private String id;
    @Setter
    private String tittle;

    private String uri;

    //TODO
    private String type;

    public Song(String tittle, String uri, String type) {
        this.tittle = tittle;
        this.uri = uri;
        this.type = type;
    }

    public Song(String tittle, String uri) {
        this.tittle = tittle;
        this.uri = uri;
        this.type = FAST;
    }

    public Song() {
    }
}
