package vodiolabs.androidinterview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.LinearLayout;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.List;

/**
 * Created by idan on 1/13/15.
 */
public class MainListAdapter extends BaseAdapter {
    View container;
    long counter = 0;
    private List<List<String>> mItems;
    private Context mContext;

    public MainListAdapter(Context ctx, List<List<String>> items) {
        mItems = items;
        mContext = ctx;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public List<String> getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        The method to implement
         */
        container = parent;

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        ViewGroup myLayout = (ViewGroup) view.findViewById(R.id.mylayout);

        List<String> item = mItems.get(position);

        for (int i = 0; i < item.size(); i++) {
            String url = item.get(i);

            createSubItem(url, myLayout);

        }

        return view;
    }

    public void createSubItem(String url, ViewGroup parent) {

        ImageView imageView = new ImageView(mContext);
        imageView.setTag(String.valueOf(counter));

        parent.addView(imageView);

        DownloadImage di = new DownloadImage();
        di.execute(url, String.valueOf(counter));
        counter++;
        imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        String tag;

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            tag = params[1];
            try {
                URL urlObj = new URL(url);
                InputStream is = urlObj.openStream();

                BitmapFactory.Options options = new BitmapFactory.Options();
            // Decode image size
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, null, options);

                final int REQUIRED_SIZE=200;
                // Find the correct scale value. It should be the power of 2.
                int scale = 1;
                while(options.outWidth / scale / 2 >= REQUIRED_SIZE &&
                        options.outHeight / scale / 2 >= REQUIRED_SIZE) {
                    scale *= 2;
                }

                // Decode with inSampleSize
                BitmapFactory.Options options2 = new BitmapFactory.Options();
                options2.inSampleSize = scale;

                is = urlObj.openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, options2);

                return bitmap;

            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (bitmap == null || container == null) {
                return;
            }

           ImageView im = (ImageView)container.findViewWithTag(tag);
            if(im != null){
                im.setImageBitmap(bitmap);

            }

        }
    }
}
