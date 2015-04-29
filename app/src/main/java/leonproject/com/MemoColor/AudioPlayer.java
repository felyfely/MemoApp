package leonproject.com.MemoColor;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;

import java.io.File;

/**
 * Created by fudan on 3/19/15.
 */
public class AudioPlayer {

    private final static String TAG = "AudioPlayer";
    private static MediaPlayer mMediaPlayer;
    private Context mContext;
    private int length;
    private Button audioPlayButton;
    public Boolean isFinishedPlaying = false;


    public AudioPlayer(Context context) {
        mContext = context;
    }

    public void playMicFile(File file) {
        if (file != null && file.exists()) {
            Uri uri = Uri.fromFile(file);
            mMediaPlayer = MediaPlayer.create(mContext, uri);
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    //TODO:finish
                    isFinishedPlaying = true;

                    Log.i(TAG, "Finish");
                }
            });
        }
    }

    public void pausePlayer() {
        mMediaPlayer.pause();
        length = mMediaPlayer.getCurrentPosition();

    }

    public void resumePlayer() {
        mMediaPlayer.seekTo(length);
        mMediaPlayer.start();
    }

    public void stopPlayer() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer=null;
            isFinishedPlaying = true;

        }
    }
}
