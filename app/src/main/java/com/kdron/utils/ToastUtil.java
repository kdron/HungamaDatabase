package com.kdron.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kdron.hungamadatabase.R;


public class ToastUtil {

    public static void toastCenterShort(Context context, String message){
        Toast toast = Toast.makeText(context,message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.getBackground().setColorFilter(context.getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(context.getResources().getColor(R.color.white));
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    
}




