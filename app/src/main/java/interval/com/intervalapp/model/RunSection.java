package interval.com.intervalapp.model;


import lombok.Getter;

/**
 * Created by m.losK on 2017-05-28.
 */

@Getter
public class RunSection {

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
    private int duration;

    public RunSection(Intensity intensity, int duration) {
        this.intensity = intensity;
        this.duration = duration;
    }
}
