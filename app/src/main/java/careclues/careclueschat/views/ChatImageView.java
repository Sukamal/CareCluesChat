package careclues.careclueschat.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.executor.ThreadsExecutor;
import careclues.careclueschat.feature.chat.AnswerSelectionListner;
import careclues.careclueschat.storage.database.entity.MessageEntity;
import careclues.careclueschat.storage.database.entity.RoomMemberEntity;
import careclues.careclueschat.util.AppConstant;

public class ChatImageView extends RelativeLayout implements View.OnClickListener{


    private static  final int CHECK_LOCALPATH = 101;
    private View view;
    private Context context;
    private String localImagePath;
    private String imageUrl;
    private String msgId;
    private Activity activity;
    private Handler handler;
    private String fileType;

    @BindView(R.id.ivDocImageage)
    ImageView ivDocImageage;
    @BindView(R.id.pbProgress)
    ProgressBar pbProgress;

    public ChatImageView(Context context) {
        super(context);
        initView(context);
    }

    public ChatImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ChatImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        this.context = context;
        view = inflate(context, R.layout.view_chat_image,this);
        ButterKnife.bind(this, view);
        ivDocImageage.setOnClickListener(this);
        handleMessage();

    }

    public void setImage(Activity activity, String msgId, String imageUrl,String fileType){
        this.activity = activity;
        this.msgId = msgId;
        this.imageUrl = imageUrl;
        this.fileType = fileType;
        getLocalPath(msgId);
    }

    private void showImage(){
        Picasso.with(context)
                .load(imageUrl)
                .into(ivDocImageage);
    }

    private void downloadImage(){
        new DownloadFileFromURL().execute(imageUrl);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivDocImageage:
                openFile();
                break;
        }
    }

    private void showDialog(){
        pbProgress.setIndeterminate(false);
        pbProgress.setMax(100);
        pbProgress.setVisibility(VISIBLE);

    }

    private void dismissDialog(){
        pbProgress.setVisibility(GONE);
    }

    private void saveLocalPath(final String localpath){
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                ((CareCluesChatApplication) activity.getApplication()).getChatDatabase().messageDao().updateLocalPath(msgId,localpath);
                localImagePath = localpath;
                handler.sendEmptyMessage(CHECK_LOCALPATH);

            }
        });
    }

    private void getLocalPath(final String messageId){
        ThreadsExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                MessageEntity messageEntity = ((CareCluesChatApplication) activity.getApplication()).getChatDatabase().messageDao().findById(messageId);
                localImagePath = ((CareCluesChatApplication) activity.getApplication()).getChatDatabase().messageDao().getLocalPath(messageId);
                Log.d("downlod", "LINK "+localImagePath);
                handler.sendEmptyMessage(CHECK_LOCALPATH);

            }
        });
    }

    private void handleMessage() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case CHECK_LOCALPATH:
                        if(localImagePath != null && localImagePath.length()>0){
                            pbProgress.setVisibility(GONE);
                            if(fileType.equals("pdf")){
                                ivDocImageage.setImageResource(R.drawable.ic_pdf);

                            }else{
                                ivDocImageage.setImageDrawable(Drawable.createFromPath(localImagePath));

                            }
                        }else{
                            downloadImage();
                        }
                        break;
                }
            }
        };
    }


    private void  openFile(){
        File file = new File(localImagePath);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null)
            type = "*/*";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(file);

        intent.setDataAndType(data, type);


        activity.startActivity(intent);
    }


    public class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }

        @Override
        protected String doInBackground(String... f_url) {

            Log.d("downlod", "downloading.....");
            File directory = new File(Environment.getExternalStorageDirectory(), "ccchat/download");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fname = f_url[0].substring(f_url[0].lastIndexOf("/") + 1);
//            String fname = "Pic_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
            File newFile = new File(directory, fname);


            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
//                OutputStream output = new FileOutputStream("/sdcard/downloadedfile.jpg");
                OutputStream output = new FileOutputStream(newFile);


                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return newFile.getAbsolutePath();
        }

        protected void onProgressUpdate(String... progress) {
            pbProgress.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog();
            saveLocalPath(file_url);
        }

    }
}
