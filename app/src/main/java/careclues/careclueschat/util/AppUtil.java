package careclues.careclueschat.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class AppUtil {

    public static Point getDeviceWidthHeight(Context mContext) {
        Point size = new Point();
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            display.getSize(size);
        } else {
            size.x = display.getWidth(); // Deprecated
            size.y = display.getHeight(); // Deprecated
        }
        return size;
    }

    public static int getDeviceWidth(Context mContext) {
        return getDeviceWidthHeight(mContext).x;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static void hideSoftKeyBoard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void openSoftKeyBoard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    public static void showToast(Context context, @StringRes int text, boolean isLong) {
        showToast(context, context.getString(text), isLong);
    }

    public static void showToast(final Context context, final String text, final boolean isLong) {
        Toast.makeText(context, text, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public static Snackbar getSnackbarWithAction(View v, int message) {
        Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.RED);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        return snackbar;
    }

    public static String getCurrentUtcTime(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateTime = dateFormat.format(date);
        return dateTime.replace("+0000","Z");
    }

    public static String convertDateFromTs(long timestamp,String pattern){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(tz);
        calendar.setTimeInMillis(timestamp);

        // Quoted "Z" to indicate UTC, no timezone offset
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(tz);

        return df.format(calendar.getTime());
    }

    public static String generateUniquId(){
        String uniqId = UUID.randomUUID().toString();
        return uniqId;
    }

    public static Uri createLocalPath(String folderPath) {

        Uri imgUri = null;
        String imgPath = null;
        try {

//            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
//            File newdir = new File(dir);
//            if (!newdir.exists()) {
//                newdir.mkdir();
//            }
//
//            String file = dir+count+".jpg";
//            File newfile = new File(file);


            File imagesFolder = new File(Environment.getExternalStorageDirectory()+"/CCCHAT/Media/");
////			File imagesFolder = new File(Environment.getExternalStorageDirectory()+"/LaCity/Media", "LaCityImages");
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs();
            }
            File file = new File(imagesFolder, "image" + String.valueOf(System.currentTimeMillis()) + ".jpg");

            imgUri = Uri.fromFile(file);
            imgPath = file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgUri;
    }

    public static boolean checkPermission(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    public static boolean isSDCardAvailable(){
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        return isSDPresent;
    }

    public static String getAbsolutePathFromContentURI(Context context,Uri uri)
    {
        String fileName="unknown";//default fileName
        Uri filePathUri = uri;
        String filePath;
        if (uri.getScheme().toString().compareTo("content")==0)
        {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst())
            {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                filePathUri = Uri.parse(cursor.getString(column_index));
                fileName = filePathUri.getLastPathSegment().toString();
            }
        }
        else if (uri.getScheme().compareTo("file")==0)
        {
            fileName = filePathUri.getLastPathSegment().toString();
        }
        else
        {
            fileName = fileName+"_"+filePathUri.getLastPathSegment().toString();
        }

        filePath = filePathUri.getPath();

        return filePath;
    }

    public static boolean isEmpty(EditText editText){
        if(editText.getText().toString().isEmpty()){
            editText.setError("Please enter some value");
            return true;
        }

        return false;
    }

    public static boolean isValidEmail(EditText editText){
        if(!Patterns.EMAIL_ADDRESS.matcher(editText.getText()).matches()){
            editText.setError("Please enter valid email");
            return false;
        }

        return true;
    }

    public static boolean isValidMobileNumber(EditText editText){

        if(!Patterns.PHONE.matcher(editText.getText()).matches()){
            editText.setError("Please enter valid mobile number");
            return false;
        }

        return true;

    }

}
