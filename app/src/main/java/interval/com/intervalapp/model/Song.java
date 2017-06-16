package interval.com.intervalapp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Song extends RealmObject {

    public static final String FAST = "fast";

    public static final String SLOW = "slow";

    @PrimaryKey
    private String path;

    @Setter
    private String title;

    @Setter
    private String type;


    public Song(String title, String path, String type) {
        this.title = title;
        this.path = path;
        this.type = type;
    }

    public Song() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;

        Song song = (Song) o;

        if (getTitle() != null ? !getTitle().equals(song.getTitle()) : song.getTitle() != null)
            return false;
        if (getPath() != null ? !getPath().equals(song.getPath()) : song.getPath() != null)
            return false;
        return getType() != null ? getType().equals(song.getType()) : song.getType() == null;

    }

    @Override
    public int hashCode() {
        int result = getTitle() != null ? getTitle().hashCode() : 0;
        result = 31 * result + (getPath() != null ? getPath().hashCode() : 0);
        return result;
    }
}
