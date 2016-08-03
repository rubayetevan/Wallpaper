package com.bdjobs.wallpaper;


import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Main2Activity extends AppCompatActivity {
    ImageView imageView;
    String link,imgTitle,imgDescription,imgSource,imgRating,imgCategory,thumb;
    Bitmap theBitmap;
    String loc;
    String fname = "";
    boolean state;
    private ProgressDialog progress;
    Button button,button2;
    View backBTN;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private ProgressDialog pDialog;
    private ProgressBar mProgress;
    RatingBar imgRatingBar;
    Dialog dialog;
    private FirebaseAnalytics mFirebaseAnalytics;
    TextView imgTitleTV,imgDescriptionTV,imgSourceTV;

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    public static final int dialog_bar_type = 1;

    final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        state = false;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        LayoutInflater factory = LayoutInflater.from(Main2Activity.this);
        View DialogView = factory.inflate(R.layout.layout, null);
        /*MobileAds.initialize(getApplicationContext(),"ca-app-pub-4958954259926855~1561957723");
        NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(request);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                FirebaseCrash.log("Ad Load Failed.Error Code: "+String.valueOf(i));
                //Toast.makeText(Main2Activity.this,"Failed: "+String.valueOf(i),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                FirebaseCrash.log("Ad Loaded Successfully");
            }
        });
*/
        //mProgress = (ProgressBar) DialogView.findViewById(R.id.progressBar);

        imgTitleTV = (TextView) findViewById(R.id.imgTitleTV);
        imgDescriptionTV = (TextView) findViewById(R.id.imgDescriptionTV);
        imgSourceTV = (TextView) findViewById(R.id.imgSourceTV);
        imgRatingBar = (RatingBar) findViewById(R.id.imgRatingBar);

        imageView = (ImageView) findViewById(R.id.imgD);
        button = (Button) findViewById(R.id.downloadBTN);
        button2 = (Button) findViewById(R.id.wallpaperBTN);
        backBTN = (View) findViewById(R.id.backBTN);
        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        thumb=intent.getStringExtra("thumb");
        link = intent.getStringExtra("link");
        imgTitle = intent.getStringExtra("title");
        imgDescription = intent.getStringExtra("description");
        imgSource = intent.getStringExtra("source");
        imgRating = intent.getStringExtra("rating");
        imgCategory = intent.getStringExtra("category");




        imgSourceTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(imgSource));
                startActivity(i);
            }
        });


        Glide.with(this).load(thumb).into(imageView);

        Rqpr();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, FullscreenActivity.class);
                intent.putExtra("link", link);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(Main2Activity.this, imageView, imageView.getTransitionName()).toBundle();
                    startActivity(intent, bundle);
                } else {

                    startActivity(intent);
                }

            }
        });

        imgTitleTV.setText(imgTitle);
        imgDescriptionTV.setText(imgDescription);
        imgSourceTV.setText(imgSource);
        imgRatingBar.setRating(Float.valueOf(imgRating));


        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String permission = sharedPref.getString("permission", "0");

        if (permission.matches("permission_denied"))
        {
            button.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
        }
        else if (permission.matches("permission_granted"))
        {
            button.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.show();
                //pDialog.setContentView(R.layout.layout);
                return pDialog;
            case dialog_bar_type:
                dialog = new Dialog(Main2Activity.this);
                dialog.setContentView(R.layout.layout);
                dialog.setTitle("Title...");
                //mProgress.setMax(100);
                //mProgress.setProgress(0);
                dialog.setCancelable(false);
                dialog.show();
                return dialog;
            default:
                return null;
        }
    }

    private void Rqpr() {
        if (ContextCompat.checkSelfPermission(Main2Activity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Main2Activity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }
    }

    public void OnClickDownload(View view) {

        Rqpr();

        if (state == true) {
            Toast.makeText(Main2Activity.this, "This Wallpaper is already downloaded!", Toast.LENGTH_SHORT).show();
        } else if (state == false) {

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, imgCategory);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, link);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            new DownloadFileFromURL().execute(link);

        }

    }

    public void OnClickSetWallpaper(View view) {

        Rqpr();
        if (state == true) {
            String path = "/sdcard/downloadedfile.jpg";
            Intent wall_intent = new Intent(Intent.ACTION_ATTACH_DATA);
            wall_intent.setDataAndType(getImageContentUri(Main2Activity.this, path), "image/*");
            wall_intent.putExtra("mimeType", "image/*");
            Intent chooserIntent = Intent.createChooser(wall_intent, "Set As");
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(chooserIntent);
        } else if (state == false) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, imgDescription);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, link);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            new DownloadFileFromURL2().execute(link);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("permission", "permission_granted");
                    editor.commit();
                    button.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);

                } else {
                    FirebaseCrash.log("permission_denied");
                    SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("permission", "permission_denied");
                    editor.commit();
                    button.setVisibility(View.INVISIBLE);
                    button2.setVisibility(View.INVISIBLE);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void SaveImage(Bitmap finalBitmap) {
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        fname = "Image-" + n;

        System.out.print("file name:" + fname);

       /* File path = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "wallpaper");*/
        loc = Environment.getExternalStorageDirectory().toString()
                + "/Evan";
        File path = new File(loc);
        path.mkdirs();

        if (!path.mkdirs()) {
            System.out.println("Directory not created");
        }


        File file = new File(path, fname + ".jpg");

        if (file.exists()) file.delete();
        try {
            path.mkdirs();

            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);


        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                System.out.println("lenghtOfFile: " + lenghtOfFile);

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream("/sdcard/downloadedfile.jpg");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));


                    // writing data to file
                    output.write(data, 0, count);

                    System.out.println("total: " + total);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());

                FirebaseCrash.report(new Exception(e.getMessage()));
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        @Override
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
           pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            //dismissDialog(progress_bar_type);
            //button.setText("Set as Wallpaper");
            dismissDialog(progress_bar_type);
            Toast.makeText(Main2Activity.this, "Downloaded", Toast.LENGTH_SHORT).show();
            state = true;

        }

    }

    class DownloadFileFromURL2 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                System.out.println("lenghtOfFile: " + lenghtOfFile);

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream("/sdcard/downloadedfile.jpg");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);

                    System.out.println("total: " + total);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                FirebaseCrash.report(new Exception(e.getMessage()));
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            //button.setText("Set as Wallpaper");
            Toast.makeText(Main2Activity.this, "Downloaded", Toast.LENGTH_SHORT).show();
            state = true;
            String path = "/sdcard/downloadedfile.jpg";
            Intent wall_intent = new Intent(Intent.ACTION_ATTACH_DATA);
            wall_intent.setDataAndType(getImageContentUri(Main2Activity.this, path), "image/*");
            wall_intent.putExtra("mimeType", "image/*");
            Intent chooserIntent = Intent.createChooser(wall_intent, "Set As");
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(chooserIntent);

        }

    }

    public static Uri getImageContentUri(Context context, String absPath) {
        Log.v("URI", "getImageContentUri: " + absPath);

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[]{MediaStore.Images.Media._ID}
                , MediaStore.Images.Media.DATA + "=? "
                , new String[]{absPath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(id));

        } else if (!absPath.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, absPath);
            return context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return null;
        }
    }


}
