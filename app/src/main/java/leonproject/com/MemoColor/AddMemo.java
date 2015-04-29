package leonproject.com.MemoColor;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import at.markushi.ui.CircleButton;


public class AddMemo extends Activity implements View.OnClickListener, MediaController.MediaPlayerControl {
    public static final int REQUEST_ADD = 0;
    public static final int REQUEST_EDIT = 1;


    private static final int REQUEST_PHOTO = 1;
    private static final int PICK_PHOTO = 2;

    private Handler mHandler = new Handler();
    private MediaRecorder mMediaRecorder;
    private MediaControllerNotHide mediaControllerNotHide;
    private MediaPlayer mPlayer = null;
    private ImageView imageView_large;
    private CircularProgressButton audioRecordButton, audioPlayButton;
    private Button  date_btn;
    private CircleButton  imageButtonTakePhoto;
    private com.software.shell.fab.ActionButton delete_btn;
    private EditText edit_title, edit_content;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    private File photoFile, AudioFile;
    private String m_uuid;
    Boolean isChecked = false, isRecording = false;
    private Switch checkBox;
    Uri outputFileUri = null;
    private RelativeLayout add_memo_layout;
    Drawable old_background;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


//        save_btn = (Button) findViewById(R.id.button_save);
        delete_btn = (com.software.shell.fab.ActionButton) findViewById(R.id.button_delete);
        imageButtonTakePhoto = (CircleButton) findViewById(R.id.button_camera);
//        imageButtonTakePhoto = (ImageButton) findViewById(R.id.memo_take_photo_button);
        edit_title = (EditText) findViewById(R.id.memo_title);
        edit_content = (EditText) findViewById(R.id.editText_content);
//        imageVi = (ImageView) findViewById(R.id.memo_imageView);
        imageView_large = (ImageView) findViewById(R.id.image_full_view);
        audioRecordButton = (CircularProgressButton) findViewById(R.id.audio_image_button);
        audioPlayButton = (CircularProgressButton) findViewById(R.id.audio_play_button);
//        audioStopButton = (ImageButton) findViewById(R.id.audio_stop_button);
//        pick_image_button = (ImageButton) findViewById(R.id.add_from_gallery_imageButton);
        add_memo_layout = (RelativeLayout) findViewById(R.id.add_memo);


        date_btn = (Button) findViewById(R.id.memo_date);
        checkBox = (Switch) findViewById(R.id.events_done);
        checkBox.setChecked(isChecked);
        date_btn.setText(getTime());
        old_background = add_memo_layout.getBackground();


        audioPlayButton.setOnClickListener(this);
//        audioStopButton.setOnClickListener(this);
        imageButtonTakePhoto.setOnClickListener(this);
        audioRecordButton.setOnClickListener(this);
//        imageVi.setOnClickListener(this);
//        save_btn.setOnClickListener(this);
        delete_btn.setOnClickListener(this);


        if (getIntent().getIntExtra("request_code", 0) == 1) {
            m_uuid = getIntent().getStringExtra(NotesDB.ID);
            edit_title.setText(getIntent().getStringExtra(NotesDB.CONTENT));
            edit_content.setText(getIntent().getStringExtra(NotesDB.DETAILS));
            checkBox.setChecked(getIntent().getIntExtra(NotesDB.ISCHECKED, 0) > 0);
            if (getIntent().getIntExtra(NotesDB.ISCHECKED, 0) > 0) {
                Random random = new Random();
                int r = random.nextInt(40) + 200;
                int g = random.nextInt(40) + 200;
                int b = random.nextInt(40) + 200;
                int randColor = Color.rgb(r, g, b);
                Log.d("AddMemo", String.valueOf(randColor));
                add_memo_layout.setBackgroundColor(randColor);
            }

            date_btn.setText(getIntent().getStringExtra(NotesDB.TIME));

            if (!getIntent().getStringExtra(NotesDB.PATH).equals("null")) {

                Bitmap bmp = decodeFile(new File(getIntent().getStringExtra(NotesDB.PATH)), 500);
                if (bmp != null) {
                    imageView_large.setImageBitmap(bmp);
                    imageView_large.setVisibility(View.VISIBLE);
                    date_btn.setTop(imageView_large.getHeight());

                }

            }
            if (!getIntent().getStringExtra(NotesDB.AUDIOPATH).equals("null")) {
                AudioFile = new File(getIntent().getStringExtra(NotesDB.AUDIOPATH));
                audioRecordButton.setProgress(100);
                audioRecordButton.setProgress(0);
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Do something after 2000ms
//                        audioRecordButton.setProgress(0);
//                    }
//                }, 1000);
            }


        } else {
            m_uuid = UUID.randomUUID().toString();
        }


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Random random = new Random();
                    int r = random.nextInt(40) + 200;
                    int g = random.nextInt(40) + 200;
                    int b = random.nextInt(40) + 200;
                    int randColor = Color.rgb(r, g, b);
                    Log.d("AddMemo", String.valueOf(randColor));
                    add_memo_layout.setBackgroundColor(randColor);
                } else {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        add_memo_layout.setBackgroundDrawable(old_background);
                    } else {
                        add_memo_layout.setBackground(old_background);
                    }
                }
            }
        });


        audioRecordButton.setIndeterminateProgressMode(true);
        audioPlayButton.setIndeterminateProgressMode(true);

        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();



        setController();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.audio_image_button:

                if(AudioFile!=null&&!isRecording){new AlertDialog.Builder(AddMemo.this)
                        .setTitle("File Overwrite !")
                        .setMessage("A voice memo already exist, Are you sure you want to OVERWRITE?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                if (mPlayer != null) {
                                    try {
                                        mPlayer.stop();
                                        mPlayer.release();
                                    } catch (Exception e) {

                                    }
                                }


                                if (audioRecordButton.getProgress() == 0) {

                                    recordAudio();

                                    isRecording = true;
                                    audioRecordButton.setProgress(1);
                                    audioPlayButton.setEnabled(false);


                                } else if (audioRecordButton.getProgress() == 1) {

                                    stopRecorder();
                                    isRecording = false;
                                    audioPlayButton.setEnabled(true);
                                    audioRecordButton.setProgress(100);
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            //Do something after 2000ms
//                            audioRecordButton.setProgress(0);
//                        }
//                    }, 1000);
                                    audioRecordButton.setProgress(0);

                                } else if (audioRecordButton.getProgress() == 100) {
                                    audioRecordButton.setProgress(0);
                                }
                                dialog.cancel();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.cancel();
                            }
                        })
                        .show();}
                else{
                    if (mPlayer != null) {
                        try {
                            mPlayer.stop();
                            mPlayer.release();
                        } catch (Exception e) {

                        }
                    }


                    if (audioRecordButton.getProgress() == 0) {

                        recordAudio();

                        isRecording = true;
                        audioRecordButton.setProgress(1);
                        audioPlayButton.setEnabled(false);


                    } else if (audioRecordButton.getProgress() == 1) {

                        stopRecorder();
                        isRecording = false;
                        audioPlayButton.setEnabled(true);
                        audioRecordButton.setProgress(100);
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            //Do something after 2000ms
//                            audioRecordButton.setProgress(0);
//                        }
//                    }, 1000);
                        audioRecordButton.setProgress(0);

                    } else if (audioRecordButton.getProgress() == 100) {
                        audioRecordButton.setProgress(0);
                    }
                }



                break;




            case R.id.button_delete:

                new AlertDialog.Builder(AddMemo.this)
                        .setTitle("Delete Memo !")
                        .setMessage("Are you sure you want to delete this memo?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                pause();

                                stopRecorder();

                                if (getIntent().getIntExtra("request_code", 0) == REQUEST_EDIT) {
                                    deleteDB();
                                }
                                finish();
                                dialog.cancel();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.cancel();
                            }
                        })
                        .show();

                break;

            case R.id.button_camera:
                PopupMenu popup = new PopupMenu(this, imageButtonTakePhoto);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu_add_photo, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(
                        new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                    case R.id.take_photo_camera:
                                        takePhoto();

                                        return true;
                                    case R.id.pick_photo_gallery:
                                        pickPhotos();

                                        return true;


                                    default:
                                        return false;
                                }
                            }
                        }

                );

                popup.show();


                break;

            case R.id.audio_play_button:


                if (AudioFile == null) {
                    audioPlayButton.setProgress(-1);

                    Toast.makeText(this, "Record a memo first", Toast.LENGTH_SHORT).show();
                    audioPlayButton.setProgress(0);
                    break;

                }

                if (mPlayer == null) {


                    try {
                        mPlayer = new MediaPlayer();
                        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        start();

                                        audioPlayButton.setProgress(10);
                                        audioRecordButton.setProgress(0);
                                        audioRecordButton.setEnabled(false);
                                        mediaControllerNotHide.show();
                                    }
                                });

                            }
                        });
                        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mPlayer.release();
                                mPlayer = null;
                                audioPlayButton.setProgress(100);
                                mediaControllerNotHide.reallyHide();
                                audioRecordButton.setEnabled(true);
//                                final Handler handler = new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        //Do something after 2000ms
//                                        audioPlayButton.setProgress(0);
//                                    }
//                                }, 2000);
                                audioPlayButton.setProgress(0);

                            }
                        });
                        mPlayer.setDataSource(AudioFile.getPath());
                        mPlayer.prepare();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    mPlayer.release();
                    mPlayer = null;
                    audioPlayButton.setProgress(0);
                    mediaControllerNotHide.reallyHide();
                    audioRecordButton.setEnabled(true);
//                    if (!isPlaying())
//                        try {
//                            audioPlayButton.setProgress(10);
//                            start();
//                            mediaControllerNotHide.show();
//                            audioRecordButton.setEnabled(false);
//                            audioRecordButton.setProgress(0);
//
//                        } catch (Exception e) {
//
//                        }
//                    else {
//                        audioPlayButton.setProgress(0);
//                        pause();
//                        mediaControllerNotHide.reallyHide();
//                        audioRecordButton.setEnabled(true);
//
//                    }

                }


                break;
//
//


        }
    }


    public void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.ID, m_uuid);
        cv.put(NotesDB.CONTENT, edit_title.getText().toString());
        cv.put(NotesDB.DETAILS, edit_content.getText().toString());
        cv.put(NotesDB.TIME, getTime());
        cv.put(NotesDB.ISCHECKED, checkBox.isChecked() ? 1 : 0);
        if (AudioFile != null) {
            cv.put(NotesDB.AUDIOPATH, AudioFile.getAbsolutePath());
        } else {
            cv.put(NotesDB.AUDIOPATH, "null");
        }
        if (outputFileUri != null) {
            cv.put(NotesDB.PATH, outputFileUri.getPath());
        } else {
            cv.put(NotesDB.PATH, "null");
        }
        dbWriter.insert(NotesDB.TABLE_NAME, null, cv);
    }


    public void deleteDB() {
        String whereClause = "_id=?";
        String[] whereArgs = {m_uuid};
        dbWriter.delete("notes", whereClause, whereArgs);
    }

    public void updateDB() {

        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT, edit_title.getText().toString());
        cv.put(NotesDB.DETAILS, edit_content.getText().toString());
        cv.put(NotesDB.TIME, date_btn.getText().toString());
        cv.put(NotesDB.ISCHECKED, checkBox.isChecked() ? 1 : 0);
        if (outputFileUri != null) {
            cv.put(NotesDB.PATH, outputFileUri.getPath());
        }
        if (AudioFile != null) {
            cv.put(NotesDB.AUDIOPATH, AudioFile.getAbsolutePath());
        }

        String whereClause = "_id=?";
        String[] whereArgs = {m_uuid};
        dbWriter.update("notes", cv, whereClause, whereArgs);
    }


    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss ");
        Date curDate = new Date();
        return format.format(curDate);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_PHOTO:
                if (resultCode == RESULT_OK) {
                    try{
                    File f = new File(outputFileUri.getPath());
                    Bitmap bmp = decodeFile(f, 500);
                    if (bmp != null) {
                        imageView_large.setImageBitmap((bmp));
                        imageView_large.setVisibility(View.VISIBLE);
                    }}
                    catch(Exception e){

                    }
                }
                break;
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        File file = new File(String.valueOf(outputFileUri));
                        if (file.exists())
                            file.delete();
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        Log.d("AddMemo", picturePath);
                        outputFileUri = Uri.parse(picturePath);
                        File f = new File(picturePath);
                        Bitmap bmp = decodeFile(f, 500);

                        if (bmp != null) {
                            imageView_large.setImageBitmap(bmp);
                            imageView_large.setVisibility(View.VISIBLE);
                        }
                    }
                    break;

                }

        }

    }


    public void takePhoto() {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        String imageFileName = "JPEG_" + m_uuid + "_.jpg";


        File folder = new File(Environment.getExternalStorageDirectory() + "/DCIM/CAMERA/");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        photoFile = new File(
                Environment.getExternalStorageDirectory() + "/DCIM/CAMERA/",
                imageFileName);

        if (photoFile.exists()) {
            photoFile.delete();
        }

        outputFileUri = Uri.fromFile(photoFile);


        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(intent, REQUEST_PHOTO);

    }

    public void pickPhotos() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO);
    }

    private void recordAudio() {
        mMediaRecorder = new MediaRecorder();
        String audioFileName = "RECORD_" + m_uuid + ".pcm";
        AudioFile = new File(
                Environment.getExternalStorageDirectory() + "/DCIM/",
                audioFileName);


        if (AudioFile.exists()) {
            AudioFile.delete();
        }
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setOutputFile(AudioFile.getAbsolutePath());
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mMediaRecorder.start();
        } catch (Exception e) {

        }
    }

    private void setController() {
        mediaControllerNotHide = new MediaControllerNotHide(this);
        mediaControllerNotHide.setMediaPlayer(this);
        mediaControllerNotHide.setAnchorView(findViewById(R.id.anchor));
        mediaControllerNotHide.setEnabled(true);
    }

    private void stopRecorder() {
        if (mMediaRecorder != null) {
            try {
                mMediaRecorder.stop();
                mMediaRecorder.release();
                mMediaRecorder = null;
            } catch (Exception e) {

            }
        }
    }

    private Bitmap decodeFile(File f, int requiredSize) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //The new size we want to scale to
            final int REQUIRED_SIZE = requiredSize;

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            Log.d("AddMemo", f.getAbsolutePath());
            Log.i("AddMemo", "FileNotFound");
//            imageVi.setBackgroundResource(android.R.drawable.ic_menu_report_image);

            Toast.makeText(AddMemo.this, "Oops! the file must be corrupted", Toast.LENGTH_SHORT).show();

            return null;
        }

    }



    @Override
    protected void onDestroy() {
        if (mPlayer != null) {
            mPlayer.release();
        }
        if (mPlayer != null) {
            mPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void start() {
        mPlayer.start();
    }

    @Override
    public void pause() {
        if (isPlaying())
            mPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        if (mPlayer != null)
            return mPlayer.getCurrentPosition();
        else
            return 0;
    }

    @Override
    public void seekTo(int pos) {
        mPlayer.seekTo(pos);

    }

    @Override
    public boolean isPlaying() {
        if (mPlayer != null)
            return mPlayer.isPlaying();
        else
            return false;
    }

    @Override
    public int getBufferPercentage() {
        if (mPlayer != null)
            return (mPlayer.getCurrentPosition() * 100) / (mPlayer.getDuration() > 0 ? mPlayer.getDuration() : 0);
        else
            return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        if (!isPlaying()) {
//            Log.i("touch", "I really want to hide it");
//            mediaControllerNotHide.reallyHide();
//        }


        return false;
    }

    @Override
    public void onBackPressed() {
        try {
            pause();
        } catch (Exception e) {

        }
        if (getIntent().getIntExtra("request_code", 0) == REQUEST_EDIT) {
            updateDB();
        } else {
            addDB();
        }
        super.onBackPressed();
    }



}


