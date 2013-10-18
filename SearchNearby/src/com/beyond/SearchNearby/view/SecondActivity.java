package com.beyond.SearchNearby.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.beyond.SearchNearby.R;
import com.beyond.SearchNearby.util.MyTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 闯儿
 * Date: 13-10-16
 * Time: 下午2:40
 * To change this template use File | Settings | File Templates.
 */
public class SecondActivity extends Activity {
    public static final String dataIndex = "index";
    public static final String dataText = "title";
    private List<Map<String,Object>> dataList =new ArrayList<Map<String, Object>>() ;
    private SimpleAdapter listViewAdapter;
    private ListView secondListView;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.second);

        titleTextView = (TextView) findViewById(R.id.second_title_title_TextView);
        secondListView = (ListView) findViewById(R.id.second_listView);
        listViewAdapter  = new SimpleAdapter(this,dataList,R.layout.index_content_item,new String[]{"text"},new int[]{R.id.index_content_list_textView})
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null)
                {
                    LayoutInflater layoutInflater = getLayoutInflater() ;
                    convertView = layoutInflater.inflate(R.layout.index_content_item,parent,false);
                }
                Map<String,Object> map = (Map<String, Object>) getItem(position);
                TextView textView= (TextView) convertView.findViewById(R.id.index_content_list_textView);
                Button button= (Button) convertView.findViewById(R.id.index_content_next_button);

                final  int index = position;
                final  String  title = map.get("text").toString();
                textView.setText(title);
                if (map.get("id").equals(false))
                {
                    button.setVisibility(View.INVISIBLE);
                }else
                {

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoNextView(index,title);
                    }
                });
                }
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClick(index,title);
                    }
                });

                return  convertView;
            }
        };
        secondListView.setAdapter(listViewAdapter);

    }


    @Override
    protected void onResume() {

        super.onResume();
        initData();
    }

    private void itemClick(int position,String title) {
        Intent intent = new Intent(this,ListActivity.class);
        intent.putExtra(SecondActivity.dataIndex,position);
        intent.putExtra(SecondActivity.dataText,title);
        startActivity(intent);
    }

    private void gotoNextView(int index,String title) {
        Intent intent = new Intent(SecondActivity.this,ThirdlyActivity.class);
        intent.putExtra(SecondActivity.dataIndex,index);
        intent.putExtra(SecondActivity.dataText,title);
        startActivity(intent);
    }

    private void initData() {
        dataList.clear();
        Intent intent = getIntent();
        int index = intent.getIntExtra(dataIndex, 1);
        String title = intent.getStringExtra(dataText);
        titleTextView.setText(title);
        String[] twoStrings;
        boolean[] flag1;
        switch (index)
        {

            case 0:
                twoStrings   = new String[]{"中餐厅","外国餐厅","快餐厅","休闲餐饮场所","咖啡厅","茶艺馆","冷饮店","糕饼店","甜品店"};
                flag1 = new boolean[]{true,true,true,false,true,false,false,false,false};
                dataList = MyTools.initDate(twoStrings,"text","id",flag1,dataList);
                break;
            case 1:
                twoStrings = new String[]{"商场","便利店","家电电子卖场","超级市场","花鸟鱼虫市场","家居建材市场","综合市场","文化用品店","体育用品店"};
                flag1 = new boolean[]{true,true,true,false,true,false,false,false,false};
                dataList = MyTools.initDate(twoStrings,"text","id",flag1,dataList);
                break;
            case 2:
                twoStrings = new String[]{"旅行社","信息咨询中心","售票处","邮局","物流速递","电讯营业厅","事务所","人才市场","自来水营业厅"};
                flag1 = new boolean[]{true,true,true,false,true,false,false,false,false};
                dataList = MyTools.initDate(twoStrings,"text","id",flag1,dataList);
                break;
            case 3:
                twoStrings = new String[]{"商场","便利店","家电电子卖场","超级市场","花鸟鱼虫市场","家居建材市场","综合市场","文化用品店","体育用品店"};
                flag1 = new boolean[]{true,true,true,false,true,false,false,false,false};
                dataList = MyTools.initDate(twoStrings,"text","id",flag1,dataList);
                break;
            case 4:
                twoStrings = new String[]{"商场","便利店","家电电子卖场","超级市场","花鸟鱼虫市场","家居建材市场","综合市场","文化用品店","体育用品店"};
                flag1 = new boolean[]{true,true,true,false,true,false,false,false,false};
                dataList = MyTools.initDate(twoStrings,"text","id",flag1,dataList);
                break;
            case 5:
                twoStrings = new String[]{"商场","便利店","家电电子卖场","超级市场","花鸟鱼虫市场","家居建材市场","综合市场","文化用品店","体育用品店"};
                flag1 = new boolean[]{true,true,true,false,true,false,false,false,false};
                dataList = MyTools.initDate(twoStrings,"text","id",flag1,dataList);
                break;
            case 6:
                twoStrings = new String[]{"商场","便利店","家电电子卖场","超级市场","花鸟鱼虫市场","家居建材市场","综合市场","文化用品店","体育用品店"};
                flag1 = new boolean[]{true,true,true,false,true,false,false,false,false};
                dataList = MyTools.initDate(twoStrings,"text","id",flag1,dataList);
                break;
            default:
                listViewAdapter.notifyDataSetChanged();
        }






    }


    public void goBackClick(View view){
        this.finish();
    }
}

