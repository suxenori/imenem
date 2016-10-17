package com.menemi.customviews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Ui-Developer on 02.08.2016.
 */
public class OCDialog extends AlertDialog.Builder{
    public static final int NO_ICON = -1;
    public static final String HAS_EDIT_TEXT = "EditTextFieldEnabled::";
    static OCDialog lastDialog;
    public interface OKListener {
        void onOKpressed();
    }
    public interface OKListenerDataEdit {
        void onOKpressed(String data);
    }
    public interface CANCELListener {
        void onCANCELpressed();
    }

    /**
     *
     * @param ctx
     * @param title use getString() to get string from resources
     * @param message use getString() to get string from resources
     * @param resIcon pass Resource id for icon, or OCDialog.NO_ICON if no icon needed
     * @param okListener
     * @param cancelListener
     */
    public OCDialog(final Context ctx, String title, String message, int resIcon,  final OKListener okListener, final CANCELListener cancelListener) {
        super(ctx);
        if(lastDialog != null){
            return;
        }
        else {
            lastDialog = this;
        }

        create();
        setTitle(title);

        if(message != null && !message.equals("")) {
            if(message.contains(HAS_EDIT_TEXT)){
                message.replace(HAS_EDIT_TEXT, "");
                final EditText input = new EditText(ctx);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setText(message);
                setView(input);
            } else{
                setMessage(message);
            }

        }


        // Setting Icon to Dialog
        if (resIcon != NO_ICON) {
           setIcon(resIcon);
        }

       setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                lastDialog=null;

                cancelListener.onCANCELpressed();
                dialog.dismiss();
            }
        });
        // Setting OK Button
        setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                lastDialog=null;

                okListener.onOKpressed();
                dialog.dismiss();

            }
        });
        setCancelable(false);
        show();

    }

    /**
     *
     * @param ctx
     * @param title use getString() to get string from resources
     * @param message use to get dialog with edit text, the message will be set as text inside edit text, use getString() to get string from resources.
     * @param resIcon pass Resource id for icon, or OCDialog.NO_ICON if no icon needed
     * @param okListener
     * @param cancelListener
     */
    public OCDialog(final Context ctx, String title, String message, int resIcon,  final OKListenerDataEdit okListener, final CANCELListener cancelListener) {
        super(ctx);
        if(lastDialog != null){
            return;
        }
        else {
            lastDialog = this;
        }

        create();
        setTitle(title);
        final EditText input = new EditText(ctx);
        if(message != null && !message.equals("")) {

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
            input.setGravity(Gravity.CENTER_HORIZONTAL);
                input.setLayoutParams(lp);
                input.setText(message);

                setView(input);
        }


        // Setting Icon to Dialog
        if (resIcon != NO_ICON) {
            setIcon(resIcon);
        }

        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                lastDialog=null;
                cancelListener.onCANCELpressed();
                dialog.dismiss();
            }
        });
        // Setting OK Button
        setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                lastDialog=null;

                okListener.onOKpressed(input.getText().toString());
                dialog.dismiss();

            }
        });
        setCancelable(false);
        show();

    }

}
