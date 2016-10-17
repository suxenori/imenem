package com.menemi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;

import java.sql.Date;
import java.util.Calendar;

public class DatePicker extends DialogFragment
{

    int[] date = {1980,01,01};
    String title ="";

    public void setDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.date[0] = calendar.get(Calendar.YEAR);
        this.date[1] = calendar.get(Calendar.MONTH);
        this.date[2] = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public Date getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(this.date[0],this.date[1],  this.date[2]);

        return new Date(calendar.getTimeInMillis());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    OnOkListener okListener = (Date date)->{
        Log.e("DatePicker", "listener is called but not set");
    };

    public void setOkListener(OnOkListener okListener) {
        this.okListener = okListener;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        Dialog datePicker = new DatePickerDialog(getActivity(),new OnDataSet(),date[0],date[1],date[2]);
        datePicker.setTitle(title);
        return datePicker;
    }

  /*  public void onStart(){
        super.onStart();
        DatePickerDialog dialog = (DatePickerDialog )getDialog();
        Button nButton =  dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText("Готово");
    }*/

class OnDataSet implements DatePickerDialog.OnDateSetListener{

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        date[0] = year;
        date[1] = month;
        date[2] = dayOfMonth;
        okListener.onOkPressed(getDate());
    }
}
    public interface OnOkListener{
        void onOkPressed(Date date);
    }

}
