package vn.cusc.animalsound;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Nguyen Hoang Thuc on 6/16/2017.
 */

public class AnimalAdapter extends BaseAdapter {

    ArrayList<Animal> lst;
    Context c;

    public AnimalAdapter(ArrayList<Animal> lst, Context c) {
        this.lst = lst;
        this.c = c;
    }

    public AnimalAdapter() {
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int i) {
        return lst.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lst.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Animal_cell cell;
        if (view == null)
        {
            cell = new Animal_cell();
            view = ((Activity)c).getLayoutInflater().inflate(R.layout.item,viewGroup,false);
            cell.textView = (TextView) view.findViewById(R.id.tvTen);
            cell.imageView = (ImageView) view.findViewById(R.id.imgHinh);
            view.setTag(cell);
        }
        else
        {
            cell = (Animal_cell) view.getTag();
        }
        cell.textView.setText(lst.get(i).getName());

        String url = Setting.address +"image/" + lst.get(i).getImg();
        new DownLoad(cell.imageView).execute(url);
        return view;
    }
    class Animal_cell
    {
        TextView textView;
        ImageView imageView;
    }
    class DownLoad extends AsyncTask<String, Object, Bitmap>
    {
        ImageView imageView;

        public DownLoad(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                Bitmap bitmap;
                URL url = new URL(strings[0]);
                URLConnection connection = url.openConnection();
                InputStream i = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(i);
                return bitmap;
            }catch (Exception e)
            {
                Log.d("Loi", e.toString());
                return null;
            }
        }
        @Override
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);
            imageView.setImageBitmap(s);
        }
    }

}
