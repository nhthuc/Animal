package vn.cusc.animalsound;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Upload extends AppCompatActivity {

    ImageView img;
    TextView tv;
    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        img = (ImageView)findViewById(R.id.imageView2);
        tv = (TextView)findViewById(R.id.textView);
        etName = (EditText)findViewById(R.id.editText);
        findViewById(R.id.btnReList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Upload.this,Girdview.class));
                finish();
            }
        });
    }

    public void click(View v)
    {
        if(v.getId() == R.id.imageView2)
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            startActivityForResult(intent, 113);
        }

        if(v.getId() == R.id.btnCreate)
        {
            Toast.makeText(Upload.this, "Bat dau upload",Toast.LENGTH_LONG).show();
            new TaiLen().execute(duongdan, etName.getText().toString());
        }
    }

    String duongdan="";
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 113) {
            Uri filePath = data.getData();
            duongdan = layDuongDanThucTe(filePath);
            tv.setText(duongdan);
            File f = new File(duongdan);
            Bitmap hinh = BitmapFactory.decodeFile(duongdan);

            img.setImageBitmap(hinh);
        }
    }
    private String layDuongDanThucTe(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    class TaiLen extends AsyncTask<String, Integer, Integer> {
        String fileName;
        String fullname;
        @Override
        protected Integer doInBackground(String... params) {
            fileName = params[0];
            fullname = params[1];

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 512;
            File sourceFile = new File(fileName);

            try {
                // tham chieu toi file vua chon
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                // ket noi voi web server
                URL url = new URL(Setting.address+"upload.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("fileDuocChon", fileName);
                conn.setRequestProperty("fullname", fullname);

                dos = new DataOutputStream(conn.getOutputStream());

                // cung cap thong tin ve hinh
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"fileDuocChon\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // doc noi dung vao bo nho tap buffer
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                publishProgress(bytesRead);
                while (bytesRead > 0) {
                // upload noi dung vua doc duoc len server
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    // doc noi dung vao bo nho tap buffer
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    publishProgress(bytesRead);
                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                // kem du lieu text
                dos.writeBytes("Content-Disposition: form-data; name=\"fullname\""+lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(fullname+lineEnd);

                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                int serverResponseCode = conn.getResponseCode();
                int serverResponseMessage = conn.getResponseCode();

                return serverResponseMessage;
            } catch (Exception ex) {
                Log.d("Loi:", ex.toString());
                return 500;
            }
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), "Dang tao thanh cong",Toast.LENGTH_LONG).show();
        }
    }
}
