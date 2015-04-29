package leonproject.com.MemoColor;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Random;

/**
 * Created by fudan on 3/19/15.
 */
public class MemoAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    //    private Activity activity;
    private int columnNum;
    //    private float textSize;
ViewHolder holder;


    public MemoAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
//        this.activity = activity;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }



    public View getView(int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (MainActivity.gridViewColumnNum) {
            case 1:
                convertView = inflater.inflate(R.layout.list_item, null);
                break;
            case 2:
                convertView = inflater.inflate(R.layout.list_item_2, null);
                break;
            case 3:
                convertView = inflater.inflate(R.layout.list_item_3, null);
                break;
            default:
                convertView = inflater.inflate(R.layout.list_item_2, null);

        }


//        TextView content_text_view = (TextView) layout.findViewById(R.id.list_item_titleTextView);
//        TextView time_text_view = (TextView) layout.findViewById(R.id.list_item_dateTextView);
//        ImageView image_view = (ImageView) layout.findViewById(R.id.list_item_image_preview);


        holder.icon = (SquareImageView) convertView.findViewById(R.id.list_item_image_preview);
        holder.text = (TextView) convertView.findViewById(R.id.list_item_titleTextView);
        holder.details = (TextView) convertView.findViewById(R.id.detail_text_view);
        holder.timestamp = (TextView) convertView.findViewById(R.id.list_item_dateTextView);
        holder.checkox = (CheckBox) convertView.findViewById(R.id.checkbox);
        holder.textlayout = (LinearLayout) convertView.findViewById(R.id.text_layout);
        holder.pin = (ImageView) convertView.findViewById(R.id.pinView);


//        holder.bigLayout = (DynamicLayout) convertView.findViewById(R.id.biglayout);


        convertView.setTag(holder);


        holder.icon.setHeightRatio(MainActivity.ratio);



        if(MainActivity.gridViewColumnNum==1){
            holder.details.setHeight(holder.icon.getHeight()-200);
        }

//        holder.bigLayout.setHeightRatio(MainActivity.ratio);


        cursor.moveToPosition(position);
        String content = cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT));
        String details = cursor.getString(cursor.getColumnIndex(NotesDB.DETAILS));
        String time = cursor.getString(cursor.getColumnIndex(NotesDB.TIME));
        String url = cursor.getString(cursor.getColumnIndex(NotesDB.PATH));

        Boolean isChecked = (cursor.getInt(cursor.getColumnIndex("isChecked")) > 0);

        holder.text.setText(content);

        holder.timestamp.setText(time);
        if (isChecked) {
            Random random = new Random();
            int r = random.nextInt(70) + 160;
            int g = random.nextInt(70) + 160;
            int b = random.nextInt(70) + 160;


            convertView.setBackgroundColor(Color.rgb(r, g, b));

            //pin generator
            if (Math.max(r, Math.max(g, b)) == r)
                holder.pin.setImageResource(R.drawable.pin_pink);

            else if (Math.max(r, Math.max(g, b)) == g)
                holder.pin.setImageResource(R.drawable.pin_green);

           else if (Math.max(r, Math.max(g, b)) == b)
                holder.pin.setImageResource(R.drawable.pin_blue);


            holder.pin.setVisibility(View.VISIBLE);

        }
        if (!url.equals("null")) {
//            loadBitmap(url, holder.icon, 20, Thread.MAX_PRIORITY, null);

//            loadBitmap(url, holder.icon, 200, Thread.MAX_PRIORITY, null);
//            ImageSize targetSize = new ImageSize(200, 200);
            if (convertView.getId() == R.id.list_item_2) {
                holder.textlayout.setBackgroundColor(Color.argb(100, 0, 0, 0));

            }
            ImageLoader imageloader = ImageLoader.getInstance();
            imageloader.displayImage("file:///" + url, holder.icon, MainActivity.options);
            holder.icon.setVisibility(View.VISIBLE);

        } else {




            holder.details.setText(details);
            holder.details.setVisibility(View.VISIBLE);
            holder.text.setTextAppearance(this.context, android.R.style.TextAppearance_Large);
            holder.timestamp.setTextAppearance(this.context, android.R.style.TextAppearance_Small);

        }

        return convertView;
    }


//    private Bitmap decodeFile(File f, int requiredSize) {
//        try {
//            //Decode image size
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
//
//            //The new size we want to scale to
//            final int REQUIRED_SIZE = requiredSize;
//
//            //Find the correct scale value. It should be the power of 2.
//            int scale = 1;
//            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
//                scale *= 2;
//
//            //Decode with inSampleSize
//            BitmapFactory.Options o2 = new BitmapFactory.Options();
//            o2.inSampleSize = scale;
//            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
//        } catch (FileNotFoundException e) {
//            return null;
//        }
//
//    }

//    public Bitmap cropBitMap(Bitmap bmp) {
//        if (bmp != null) {
//            Bitmap dstBmp;
//            if (bmp.getWidth() >= bmp.getHeight()) {
//
//                dstBmp = Bitmap.createBitmap(
//                        bmp,
//                        bmp.getWidth() / 2 - bmp.getHeight() / 2,
//                        0,
//                        bmp.getHeight(),
//                        bmp.getHeight()
//                );
//
//            } else {
//
//                dstBmp = Bitmap.createBitmap(
//                        bmp,
//                        0,
//                        bmp.getHeight() / 2 - bmp.getWidth() / 2,
//                        bmp.getWidth(),
//                        bmp.getWidth()
//                );
//            }
//
//            return dstBmp;
//
//        }
//
//        return null;
static class ViewHolder {
        TextView text;
        TextView details;
        TextView timestamp;
        SquareImageView icon;
        ImageView pin;
        CheckBox checkox;
        LinearLayout textlayout;
    DynamicLayout bigLayout;

//        RelativeLayout relativeLayout;
//        int position;
    }


//    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
//        private final WeakReference<ImageView> imageViewReference;
//        private int size;
//        private String data = "";
//        private int priority = Thread.NORM_PRIORITY;
//        private Activity activity;
//
//        public BitmapWorkerTask(ImageView imageView, Integer size, Integer priority, Activity activity) {
//            // Use a WeakReference to ensure the ImageView can be garbage collected
//            imageViewReference = new WeakReference<ImageView>(imageView);
//            this.size = size;
//            this.priority = priority;
//            this.activity = activity;
//        }
//
//        // Decode image in background.
//        @Override
//        protected Bitmap doInBackground(String... param) {
//            Thread.currentThread().setPriority(priority);
//            data = param[0];
//            return cropBitMap(decodeFile(new File(data), size));
//
//
//        }
//
//        // Once complete, see if ImageView is still around and set bitmap.
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            if (isCancelled()) {
//                bitmap = null;
//            }
//            if (imageViewReference != null && bitmap != null) {
//                final ImageView imageView = imageViewReference.get();
//                if (imageView != null) {
//                    imageView.setImageBitmap(bitmap);
//                    imageView.setVisibility(View.VISIBLE);
//
//                }
//            }
//        }
//
//    }

//    public void loadBitmap(String url, ImageView imageView, Integer size, Integer priority, Bitmap mPlaceHolderBitmap) {
//        if (cancelPotentialWork(url, imageView)) {
//            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, size, priority, this.activity);
//            final AsyncDrawable asyncDrawable =
//                    new AsyncDrawable(this.activity.getResources(), mPlaceHolderBitmap, task);
//            imageView.setImageDrawable(asyncDrawable);
//            task.execute(url);
//        }
//        BitmapWorkerTask task = new BitmapWorkerTask(imageView, size, priority, this.activity);
//        task.execute(url);
//    }

//    static class AsyncDrawable extends BitmapDrawable {
//        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;
//
//        public AsyncDrawable(Resources res, Bitmap bitmap, AsyncTask bitmapWorkerTask) {
//            super(res, bitmap);
//            bitmapWorkerTaskReference = new WeakReference(bitmapWorkerTask);
//        }
//
//        public BitmapWorkerTask getBitmapWorkerTask() {
//            return bitmapWorkerTaskReference.get();
//        }
//    }


//    public static boolean cancelPotentialWork(String data, ImageView imageView) {
//        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
//
//        if (bitmapWorkerTask != null) {
//            final String bitmapData = bitmapWorkerTask.data;
//            if (bitmapData.equals("") || !bitmapData.equals(data)) {
//                // Cancel previous task
//                bitmapWorkerTask.cancel(true);
//            } else {
//                // The same work is already in progress
//                return false;
//            }
//        }
//        // No task associated with the ImageView, or an existing task was cancelled
//        return true;
//    }

//    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
//        if (imageView != null) {
//            final Drawable drawable = imageView.getDrawable();
//            if (drawable instanceof AsyncDrawable) {
//                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
//                return asyncDrawable.getBitmapWorkerTask();
//            }
//        }
//        return null;
//    }

}
