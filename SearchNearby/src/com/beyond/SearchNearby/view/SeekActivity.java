package com.beyond.SearchNearby.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import com.beyond.SearchNearby.R;

/**
 * Created with IntelliJ IDEA.
 * User: 闯儿
 * Date: 13-10-15
 * Time: 下午11:44
 * To change this template use File | Settings | File Templates.
 */
public class SeekActivity extends Activity{
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.seek);
        editText = (EditText) findViewById(R.id.seek_title_input_EditText);
    }

    public  void  backOnClick(View view)
    {
        this.finish();
    }

    public void goNextView(View view)
    {
        if (checkName())
        {
            Intent intent = new Intent(this,ListActivity.class);
            intent.putExtra(SecondActivity.dataText,editText.getText().toString());
            startActivity(intent);
        }

    }

    private boolean checkName() {
        Log.d("checkName",editText.getText().toString());
        Log.d("checkName",editText.getText().equals("")+"");
        if (editText.getText().toString()!=null||!editText.getText().toString().equals(" "))return true;
        return false;
    }
}
