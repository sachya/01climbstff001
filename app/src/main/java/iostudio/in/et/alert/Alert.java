package iostudio.in.et.alert;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import iostudio.in.et.R;

public class Alert {
    private final static AlertDialog.Builder createAlert(Activity context, String title, String message) {

        AlertDialog.Builder dialog;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            dialog = new AlertDialog.Builder(context);
            //          dialog = new AlertDialog.Builder(context);
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            dialog = new AlertDialog.Builder(context);
        else
            dialog = new AlertDialog.Builder(context);

        //  dialog.setIcon(R.mipmap.ic_launcher);
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.setTitle("");
        dialog.setMessage(message);
        dialog.setCancelable(false);


        return dialog;
    }

    private final static AlertDialog.Builder createCustomAlert(Activity context,
                                                               String title, String message, int layout) {

        AlertDialog.Builder dialog;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            dialog = new AlertDialog.Builder(new ContextThemeWrapper(context,
                    android.R.style.Theme_Material_Light_Dialog_Alert));

        else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            dialog = new AlertDialog.Builder(new ContextThemeWrapper(context,
                    android.R.style.Theme_Holo_Light_Dialog));
        else
            dialog = new AlertDialog.Builder(context);

        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(layout, null);
        dialog.setView(dialogView);
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.setTitle("Information");
        dialog.setMessage(message);
        dialog.setCancelable(false);
        return dialog;
    }

    public static void alert(Context context, String title, String message,
                             String negativeButton, String positiveButton,
                             final Runnable negativeRunnable,
                             final Runnable positiveRunnable) {
        try {
            AlertDialog.Builder dialog = createAlert((Activity) context, title, message);
            if (negativeButton != null) {
                dialog.setNegativeButton(negativeButton,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                if (negativeRunnable != null)
                                    negativeRunnable.run();
                            }
                        });
            }
            if (positiveButton != null) {
                dialog.setPositiveButton(positiveButton,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (positiveRunnable != null)
                                    positiveRunnable.run();
                            }
                        });
            }

            try {
                AlertDialog dialogBuilder = dialog.create();
                dialogBuilder.show();
                // Get the alert dialog buttons reference

                //change the color of alert
                Button positiveBtn = dialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                positiveBtn.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));

                Button negativeBtn = dialogBuilder.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                negativeBtn.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));

                //Button neutralButton = dialogBuilder.getButton(AlertDialog.BUTTON_NEUTRAL);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void alertCustomView(Activity context, String title, String message,
                                       String negativeButton, String positiveButton,
                                       final Runnable negativeRunnable, final Runnable positiveRunnable, int layout) {
        AlertDialog.Builder dialog = createCustomAlert(context, title, message, layout);
        if (negativeButton != null) {
            dialog.setNegativeButton(negativeButton,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (negativeRunnable != null)
                                negativeRunnable.run();
                        }
                    });
        }
        if (positiveButton != null) {
            dialog.setPositiveButton(positiveButton,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (positiveRunnable != null)
                                positiveRunnable.run();
                        }
                    });
        }
        dialog.show();
    }
}
