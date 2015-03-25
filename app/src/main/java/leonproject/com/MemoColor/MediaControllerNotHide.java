package leonproject.com.MemoColor;

import android.content.Context;
import android.widget.MediaController;

/**
 * Created by fudan on 3/24/15.
 */
public class MediaControllerNotHide extends MediaController {
    public MediaControllerNotHide(Context context) {
        super(context);
    }

    public void hide(){

    }

    public void reallyHide(){
        super.hide();
    }


}
