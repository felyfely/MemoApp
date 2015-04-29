package leonproject.com.MemoColor;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.etsy.android.grid.StaggeredGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements PopupMenu.OnMenuItemClickListener, OnMenuItemClickListener, OnMenuItemLongClickListener {
    public static final int REQUEST_ADD = 0;
    public static final int REQUEST_EDIT = 1;
    public static int gridViewColumnNum = 2;

    private Parcelable state = null;


    private com.software.shell.fab.ActionButton text_btn_add;
    private ImageButton settings_btn;

    private StaggeredGridView gridView;
    private Intent i;
    private NotesDB notesDB;
    private SQLiteDatabase dbReader, dbWriter;
    private Cursor cursor;
//    private HashSet<String> delete_list;
    public static DisplayImageOptions options;
    //    public static ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;
    public static double ratio = 1;
    private MemoAdapter adapter;


    private FragmentManager fragmentManager;
    private DialogFragment mMenuDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(4)
                .build();


        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();


        initView();

//        delete_list = new HashSet<>();
//
//
//        gridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        gridView.setMultiChoiceModeListener(new GridView.MultiChoiceModeListener() {
//
//            @Override
//            public void onItemCheckedStateChanged(ActionMode mode, int position,
//                                                  long id, boolean checked) {
//                // Here you can do something when items are selected/de-selected,
//                // such as update the title in the CAB
//                final int checkedCount = gridView.getCheckedItemCount();
//                // Set the CAB title according to total checked items
//                mode.setTitle(checkedCount + " Selected");
//
//                cursor.moveToPosition(position);
//
//                if (checked) {
//
//                    delete_list.add(cursor.getString(cursor.getColumnIndex(NotesDB.ID)));
//                } else {
//                    delete_list.remove(cursor.getString(cursor.getColumnIndex(NotesDB.ID)));
//                }
//
//
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                // Respond to clicks on the actions in the CAB
//                switch (item.getItemId()) {
//                    case R.id.action_delete:
////                        deleteSelectedItems();
//
//                        // continue with delete
//
//                        String[] args = delete_list.toArray(new String[delete_list.size()]);
//                        deleteDB(args);
//
//
////                        TODO
//                        delete_list.clear();
//
//                        mode.finish(); // Action picked, so close the CAB
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                // Inflate the menu for the CAB
//                MenuInflater inflater = mode.getMenuInflater();
//                inflater.inflate(R.menu.menu_main_batch_delete, menu);
//                text_btn_add.setEnabled(false);
//                settings_btn.setEnabled(false);
//                return true;
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//                // Here you can make any necessary updates to the activity when
//                // the CAB is removed. By default, selected items are deselected/unchecked.
//
////                for (int count = 0; count < gridView.getChildCount(); count++) {
////
////                    gridView.setItemChecked(count, false);
////
////                }
//                text_btn_add.setEnabled(true);
//                settings_btn.setEnabled(true);
//                selectDB();
//
//
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                // Here you can perform updates to the CAB due to
//                // an invalidate() request
//                return false;
//            }
//        });


        fragmentManager = getSupportFragmentManager();


        MenuObject close = new MenuObject("Dismiss");
        close.setScaleType(ImageView.ScaleType.FIT_XY);
        close.setBgResource(R.drawable.android_close);

        MenuObject plus = new MenuObject("Add column");
        plus.setScaleType(ImageView.ScaleType.FIT_XY);
        plus.setBgResource(R.drawable.add_column);

        MenuObject minus = new MenuObject("Remove column");
        minus.setScaleType(ImageView.ScaleType.FIT_XY);
        minus.setBgResource(R.drawable.sub_column);

        MenuObject scale = new MenuObject("Change Scale");
        scale.setScaleType(ImageView.ScaleType.FIT_XY);
        scale.setBgResource(R.drawable.scale);

        MenuObject delete = new MenuObject("Delete");
        delete.setScaleType(ImageView.ScaleType.FIT_XY);
        delete.setBgResource(R.drawable.delete_menu);

        MenuObject about = new MenuObject("About");
        about.setScaleType(ImageView.ScaleType.FIT_XY);
        about.setBgResource(R.drawable.about);


        List<MenuObject> menuObjects = new ArrayList<>();
        menuObjects.add(close);
        menuObjects.add(plus);
        menuObjects.add(minus);
        menuObjects.add(scale);
        menuObjects.add(delete);
        menuObjects.add(about);


        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 74, r.getDisplayMetrics());

        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(px, menuObjects);
        addFragment(new MainFragment(), true, R.id.container);
    }


    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }


    public void initView() {

        gridView = (StaggeredGridView) findViewById(R.id.list);
        text_btn_add = (com.software.shell.fab.ActionButton) findViewById(R.id.text_button_add);
        settings_btn = (ImageButton) findViewById(R.id.text_button_Settings);
        text_btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(MainActivity.this, AddMemo.class);
                i.putExtra("request_code", REQUEST_ADD);
                startActivity(i);
            }
        });
        settings_btn.setOnClickListener
                (new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {


                         if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                             mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                         }
                         return;
                     }
                 }

                );
        notesDB = new NotesDB(this);

        dbReader = notesDB.getReadableDatabase();
        dbWriter = notesDB.getWritableDatabase();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                        {

                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view,
                                                                    int position, long id) {
                                                cursor.moveToPosition(position);
                                                Intent i = new Intent(MainActivity.this, AddMemo.class);
                                                i.putExtra(NotesDB.ID,
                                                        cursor.getString(cursor.getColumnIndex(NotesDB.ID)));
                                                i.putExtra(NotesDB.TIME,
                                                        cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                                                i.putExtra(NotesDB.CONTENT,
                                                        cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT)));
                                                i.putExtra(NotesDB.PATH,
                                                        cursor.getString(cursor.getColumnIndex(NotesDB.PATH)));
                                                i.putExtra(NotesDB.AUDIOPATH,
                                                        cursor.getString(cursor.getColumnIndex(NotesDB.AUDIOPATH)));
                                                i.putExtra(NotesDB.ISCHECKED,
                                                        cursor.getInt(cursor.getColumnIndex(NotesDB.ISCHECKED)));
                                                i.putExtra(NotesDB.DETAILS,
                                                        cursor.getString(cursor.getColumnIndex(NotesDB.DETAILS)));

                                                i.putExtra("request_code", REQUEST_EDIT);
                                                startActivity(i);
                                            }
                                        }

        );
    }


    public void selectDB() {


        cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null,
                null, null, null);
        adapter = new MemoAdapter(this, cursor);
//        adapter.setColumnNum(gridView.getNumColumns());
//        gridView.getColumnWidth()
        gridView.setAdapter(adapter);


        if (state != null) {
            gridView.requestFocus();
            gridView.onRestoreInstanceState(state);
        }


    }

    public void deleteDB(String[] id) {
        String whereClause = "_id IN (" + new String(new char[id.length - 1]).replace("\0", "?,") + "?)";
        String[] whereArgs = id;

        dbWriter.delete(notesDB.TABLE_NAME, whereClause, whereArgs);
    }

    public void deleteDB(String id) {
        String whereClause = "_id=?";
        String[] whereArgs = {id};

        dbWriter.delete(notesDB.TABLE_NAME, whereClause, whereArgs);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    protected void addFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        invalidateOptionsMenu();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(containerId, fragment, backStackName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }


    @Override
    public void onMenuItemClick(View view, int i) {

        switch (i) {
            case 0:
                settings_btn.setVisibility(View.VISIBLE);

                break;


            case 1:
                if (gridViewColumnNum <= 2) {
                    gridViewColumnNum++;
//                    adapter.setColumnNum(gridViewColumnNum);
                    gridView.setColumnCount(gridViewColumnNum);
                }


                break;
            case 2:
                if (gridViewColumnNum >= 2) {
                    gridViewColumnNum--;
//                    adapter.setColumnNum(gridViewColumnNum);
                    gridView.setColumnCount(gridViewColumnNum);
                }

                break;

            case 3:

//if(gridViewColumnNum==2){
                if (ratio == 0) {
                    ratio = 1;
                } else {
                    ratio = 0;
                }


//                if (scaleType == ImageView.ScaleType.CENTER_CROP) {
//                    scaleType = ImageView.ScaleType.FIT_CENTER;
//                } else {
//                    scaleType = ImageView.ScaleType.CENTER_CROP;
//                }
//                settings_btn.setVisibility(View.VISIBLE);

                selectDB();

                break;

            case 4:


                final ActionBar actionBar = getActionBar();
                if (actionBar != null)
                    actionBar.show();
                actionBar.setTitle("Click On Items to Delete");


                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        cursor.moveToPosition(position);
                        deleteDB(cursor.getString(cursor.getColumnIndex(NotesDB.ID)));
                        state = gridView.onSaveInstanceState();
                        selectDB();
                    }
                });


                break;

            case 5:

                                new AlertDialog.Builder(this)
                        .setTitle("Acknowledgement")
                        .setMessage(R.string.about)
                        .setPositiveButton("I see, Thanks.", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                settings_btn.setVisibility(View.VISIBLE);

                                dialog.cancel();
                            }
                        })
                        .show();

                break;

            default:
                break;
        }

    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    public void onMenuItemLongClick(View view, int i) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_single_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        //only one item right now
        switch (item.getItemId()) {
            case R.id.action_cancel_delete:

                final ActionBar actionBar = getActionBar();
                if (actionBar != null)
                    actionBar.hide();
                initView();

                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onPause() {
        state = gridView.onSaveInstanceState();
        super.onPause();
    }

    @Override
    public void onBackPressed() {


        if (getActionBar().isShowing()) {
            getActionBar().hide();
            initView();
        }


        super.onBackPressed();
    }
}

