package dyachenko.androidbeginnernotes;

import java.io.Serializable;

public interface DialogYesNoResponse extends Serializable {
    void answered(boolean yes);
}
