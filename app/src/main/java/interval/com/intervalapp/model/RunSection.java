package interval.com.intervalapp.model;

import io.realm.RealmObject;
import lombok.Getter;

/**
 * Created by m.losK on 2017-05-28.
 */

@Getter
public class RunSection extends RealmObject {

    public static final String LOW = "low";
    public static final String MEDIUM = "medium";
    public static final String HIGH = "high";

    private String intensity;
    private Long duration;

    public RunSection(String intensity, Long duration) {
        this.intensity = intensity;
        this.duration = duration;
    }

    public RunSection() {
    }
}