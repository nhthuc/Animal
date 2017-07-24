package vn.cusc.animalsound;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Girdview extends AppCompatActivity {
    String url = Setting.address + "list.php";

    ArrayList<Animal> list;
    AnimalAdapter adp;
    GridView grd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gird_view);

        list = new ArrayList<>();

        new Download().execute(url);

        grd = (GridView)findViewById(R.id.gridView);
        adp = new AnimalAdapter(list,Girdview.this);
        grd.setAdapter(adp);

        grd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Girdview.this,ViewAnimal.class);
                intent.putExtra("id",((Animal)adp.getItem(i)).getId());
                intent.putExtra("name",((Animal)adp.getItem(i)).getName());
                intent.putExtra("image",((Animal)adp.getItem(i)).getImg());
                intent.putExtra("voice",((Animal)adp.getItem(i)).getVoice());
                startActivity(intent);
            }
        });
    }

    class Download extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL u = new URL(strings[0]);
                URLConnection conn = u.openConnection();
                InputStream i = conn.getInputStream();

                StringBuilder temp = new StringBuilder();

                String line = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(i));
                while ((line = br.readLine()) != null) {
                    temp.append(line);
                    temp.append("\n");
                }
                br.close();
                return temp.toString();
            }catch(Exception ex)
            {
                Log.d("Loi",ex.toString());
                return "";
            }
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            try
            {
                JSONArray arr = new JSONArray(str);
                for (int i = 0; i< arr.length(); i++)
                {
                    Animal a = new Animal();

                    a.setId(Integer.parseInt(arr.getJSONObject(i).getString("id")));
                    a.setName(arr.getJSONObject(i).getString("name"));
                    a.setImg(arr.getJSONObject(i).getString("image"));
                    a.setVoice(arr.getJSONObject(i).getString("voice"));
                    list.add(a);
                }

            }catch(Exception e)
            {
                Log.d("Loi",e.toString());
            }
        }
    }
}
