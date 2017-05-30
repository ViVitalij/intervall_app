package interval.com.intervalapp.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by m.losK on 2017-05-30.
 */

@Getter
@Setter
public class RunningMode {
    private String name;
    private List<RunSection> runMode;

    public RunningMode(String name, List<RunSection> runMode) {
        this.name = name;
        this.runMode = runMode;
    }

    public RunningMode() {
    }


}
