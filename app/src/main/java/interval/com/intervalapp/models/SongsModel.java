package interval.com.intervalapp.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by RENT on 2017-05-24.
 */

public class SongsModel {
    @Getter
    @Setter
    private String id;
    private String author;
    private String tittle;
    private final String URI = "android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI";
}
