package vn.cusc.animalsound;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ViewAnimal extends AppCompatActivity {

    Animal animal;
    Intent intent;
    String url;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animal);

        intent = getIntent();
        animal = new Animal(intent.getIntExtra("id",0), intent.getStringExtra("name"),
                intent.getStringExtra("image"), intent.getStringExtra("voice"));

        ((TextView)findViewById(R.id.tvName)).setText(animal.getName());
        url = Setting.address + animal.getImg();

        new Download((ImageView)findViewById(R.id.imageView3)).execute(url);

        (findViewById(R.id.button4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = Setting.address +"voice/"+ animal.getVoice();
                Uri u = Uri.parse(url);
                if (player != null && player.isPlaying())
                    player.stop();
                player = new MediaPlayer();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    player.setDataSource(ViewAnimal.this, u);
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    class Download extends AsyncTask<String, Integer, Bitmap>
    {
        ImageView image;
        public Download(ImageView image) {
            this.image = image;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                Bitmap b;
                URL u = new URL(strings[0]);
                URLConnection conn = u.openConnection();
                InputStream i = conn.getInputStream();
                b = BitmapFactory.decodeStream(i);
                return b;
            }catch(Exception ex)
            {
                Log.d("Loi hinh:", ex.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            image.setImageBitmap(bitmap);
        }
    }
}
