package com.menemi.customviews;

import android.content.Context;

/**
 * Created by Ui-Developer on 11.10.2016.
 */

public class EditTextDialog extends OCDialog {
    /**
     * @param ctx
     * @param title          use getString() to get string from resources
     * @param message        use getString() to get string from resources
     * @param resIcon        pass Resource id for icon, or OCDialog.NO_ICON if no icon needed
     * @param okListener
     * @param cancelListener
     */
    public EditTextDialog(Context ctx, String title, String hint, int resIcon, OKListener okListener, CANCELListener cancelListener) {
        super(ctx, title, "", resIcon, okListener, cancelListener);
    }
}
