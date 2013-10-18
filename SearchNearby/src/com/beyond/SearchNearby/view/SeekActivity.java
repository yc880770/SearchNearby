package com.beyond.SearchNearby.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import com.beyond.SearchNearby.R;

/**
 * Created with IntelliJ IDEA.
 * User: 闯儿
 * Date: 13-10-15
 * Time: 下午11:44
 * To change this template use File | Settings | File Templates.
 */
public class SeekActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.seek);
    }

    public  void  backOnClick(View view)
    {
        Intent intent = new Intent(this,IndexActivity.class);
        this.finish();
//        startActivity(intent);
    }
}
