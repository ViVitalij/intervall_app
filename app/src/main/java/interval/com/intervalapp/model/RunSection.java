package interval.com.intervalapp.model;


import org.joda.time.Duration;

import io.realm.RealmObject;
import lombok.Getter;

/**
 * Created by m.losK on 2017-05-28.
 */

@Getter
public class RunSection extends RealmObject{

    public enum Intensity {
        LOW("low"),
        MEDIUM("medium"),
        HIGH("high");

        Intensity(String intensity) {
        }

        Intensity() {
        }
    }

    private Intensity intensity;
    private Duration duration;

    public RunSection(Intensity intensity, Duration duration) {
        this.intensity = intensity;
        this.duration = duration;
    }

    public RunSection() {
    }
}
