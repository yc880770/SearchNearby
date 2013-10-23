package com.beyond.SearchNearby.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.beyond.SearchNearby.R;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 闯儿
 * Date: 13-10-22
 * Time: 上午10:48
 * To change this template use File | Settings | File Templates.
 */
public class MySpinnerAdapter<T> extends ArrayAdapter<T> {
    private int dropDownViewResourceId;
    private LayoutInflater inflater;

    public MySpinnerAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        init();
    }

    public MySpinnerAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        init();
    }

    public MySpinnerAdapter(Context context, int textViewResourceId, T[] objects) {
        super(context, textViewResourceId, objects);
        init();
    }

    public MySpinnerAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
        init();
    }

    public MySpinnerAdapter(Context context, int textViewResourceId, List<T> objects) {
        super(context, textViewResourceId, objects);
        init();
    }

    public MySpinnerAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
        init();
    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
        dropDownViewResourceId = resource;
    }

    public void init() {
        inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        Log.d("Spinner", "getDropDownView at position " + position);

        Object item = getItem(position);

        LinearLayout dropDownItemView = (LinearLayout) inflater.inflate(dropDownViewResourceId,
                null);

        TextView text1 = (TextView) dropDownItemView
                .findViewById(R.id.spinner_dropdown_textView);
        text1.setText(item.toString());
        return dropDownItemView;
    }

}
