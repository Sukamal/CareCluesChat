package careclues.careclueschat.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import careclues.careclueschat.R;


public class AppDialog {

    private Dialog dialog;
    private ProgressDialog mProgressDialog;
    private DialogListener dialogListener;

    public interface DialogListener {

        /**  On yes key  */
        void OnPositivePress(Object val);

        /** On no key. */
        void OnNegativePress();

    }

    public void dismissProgress(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    public void dismissDialog(Context activity) {
        if (!((Activity) activity).isFinishing() && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void showProgress(Activity activity){
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        mProgressDialog.setMessage("Please wait..");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        mProgressDialog.show();


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = new ProgressBar(activity).getIndeterminateDrawable().mutate();
            drawable.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary),
                    PorterDuff.Mode.SRC_IN);
            mProgressDialog.setIndeterminateDrawable(drawable);
        }else{

        }

        mProgressDialog.setContentView( R.layout.progress_bar_layout);
    }

    public void showErrorDialog(final Activity context, String title, String msg, final DialogListener listener) {
        if (!((Activity) context).isFinishing()) {
//            dialog = new Dialog(context, R.style.ThemeDialogCustom);
            dialog = new Dialog(context);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_error_layout);

            int diveWidth = AppUtil.getDeviceWidthHeight(context).x;
            dialog.getWindow().setLayout((6 * diveWidth)/7, LinearLayout.LayoutParams.WRAP_CONTENT);

            TextView TV_title = (TextView) dialog.findViewById(R.id.TV_title);
            TV_title.setText(title);

            TextView TV_errors = (TextView) dialog.findViewById(R.id.TV_errors);
            TV_errors.setText(msg);

            dialog.show();

            TextView TV_ok = (TextView) dialog.findViewById(R.id.TV_ok);
            TV_ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissDialog(context);
                    if(listener != null){
                        listener.OnPositivePress(null);
                    }

                }
            });

            TextView TV_cancel =  (TextView) dialog.findViewById(R.id.TV_cancel);
            TV_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissDialog(context);
                    if(listener != null){
                        listener.OnNegativePress();
                    }

                }
            });

        }

    }

    public interface PickImageListener {

        /**
         * On Camera .
         */
        void OnCameraPress();

        /**
         * On no Gallery.
         */
        void OnGalleryPress();

        void OnDocumentPress();

    }

    public void showAlertPickImageDialog(final Context context, final PickImageListener dialogListener) {
        if (!((Activity) context).isFinishing()) {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_chose_image_option);
            int diveWidth = AppUtil.getDeviceWidth(context);
            dialog.getWindow().setLayout((6 * diveWidth)/7, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.show();


            TextView TV_camera = (TextView) dialog.findViewById(R.id.TV_camera);
            TV_camera.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissDialog(context);
                    if (dialogListener != null) {
                        dialogListener.OnCameraPress();
                    }

                }
            });

            TextView TV_gallary = (TextView) dialog.findViewById(R.id.TV_gallary);
            TV_gallary.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissDialog(context);
                    if (dialogListener != null) {
                        dialogListener.OnGalleryPress();
                    }
                }
            });

            TextView TV_pdf = (TextView) dialog.findViewById(R.id.TV_pdf);
            TV_pdf.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissDialog(context);
                    if (dialogListener != null) {
                        dialogListener.OnDocumentPress();
                    }
                }
            });



            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });
        }

    }






}
