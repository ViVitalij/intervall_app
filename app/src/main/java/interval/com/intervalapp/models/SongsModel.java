package interval.com.intervalapp.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by RENT on 2017-05-24.
 */

public class SongsModel extends RealmObject {
    private  static final String FAST = "fast";
    private static final String SLOW ="slow";

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
}
