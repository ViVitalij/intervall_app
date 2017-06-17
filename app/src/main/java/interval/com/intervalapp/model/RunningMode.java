package interval.com.intervalapp.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;

/**
 * Created by m.losK on 2017-05-30.
 */

@Data
public class RunningMode extends RealmObject {

    @PrimaryKey
    private String name;
    private RealmList<RunSection> runMode;

    public RunningMode(String name, RealmList<RunSection> runMode) {
        this.name = name;
        this.runMode = runMode;
    }

    public RunningMode() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RunningMode that = (RunningMode) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return runMode != null ? runMode.equals(that.runMode) : that.runMode == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (runMode != null ? runMode.hashCode() : 0);
        return result;
    }

    public RealmList<RunSection> getRunMode() {
        return runMode;
    }
}
