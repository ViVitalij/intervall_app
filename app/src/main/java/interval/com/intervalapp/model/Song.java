package interval.com.intervalapp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

public class Song extends RealmObject  {
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

    public Song(String author, String tittle, String uri, String type) {
        this.author = author;
        this.tittle = tittle;
        this.uri = uri;
        this.type = type;
    }

    public Song() {
    }
}
