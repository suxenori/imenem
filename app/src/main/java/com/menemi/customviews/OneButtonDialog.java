package com.menemi.customviews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Ui-Developer on 02.08.2016.
 */
public class OneButtonDialog extends AlertDialog.Builder{
    public static final int NO_ICON = -1;
    public static final String HAS_EDIT_TEXT = "EditTextFieldEnabled::";
    static OneButtonDialog lastDialog;
    public interface OKListener {
        void onOKpressed();
    }


    /**
     *
     * @param ctx
     * @param title use getString() to get string from resources
     * @param message use getString() to get string from resources
     * @param resIcon pass Resource id for icon, or OCDialog.NO_ICON if no icon needed
     * @param okListener
     */
    public OneButtonDialog(final Context ctx, String title, String message, int resIcon, final OKListener okListener) {
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

}
