package com.beyond.SearchNearby.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.beyond.SearchNearby.R;
import com.beyond.SearchNearby.util.MyTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 闯儿
 * Date: 13-10-16
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */
public class ThirdlyActivity extends Activity {
    private List<Map<String,Object>> dataList =new ArrayList<Map<String, Object>>() ;
    private SimpleAdapter listViewAdapter;
    private ListView  thirdListView;
    private TextView titleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.thirdly);

        titleTextView = (TextView) findViewById(R.id.thirdly_title_title_TextView);
        thirdListView = (ListView) findViewById(R.id.thirdly_listView);
        listViewAdapter  = new SimpleAdapter(this,dataList,R.layout.third_content_item,new String[]{"text"},new int[]{R.id.third_content_list_textView})
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null)
                {
                    LayoutInflater layoutInflater = getLayoutInflater() ;
                    convertView = layoutInflater.inflate(R.layout.third_content_item,parent,false);
                }
                Map<String,Object> map = (Map<String, Object>) getItem(position);
                TextView textView= (TextView) convertView.findViewById(R.id.third_content_list_textView);
                final  int index = position;
                final  String  title = map.get("text").toString();
                textView.setText(title);

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClick(index,title);
                    }
                });


                return  convertView;
            }
        };
        thirdListView.setAdapter(listViewAdapter);

    }

    private void itemClick(int position,String title) {
        Intent intent = new Intent(this,ListActivity.class);
        intent.putExtra(SecondActivity.dataIndex,position);
        intent.putExtra(SecondActivity.dataText,title);
        startActivity(intent);
    }

    protected void onResume() {

        super.onResume();
        initData();
    }

    private void initData() {
        dataList.clear();
        Intent intent = getIntent();
        int index = intent.getIntExtra(SecondActivity.dataIndex, 1);
        String title = intent.getStringExtra(SecondActivity.dataText);
        titleTextView.setText(title);
        String[] twoStrings;
        List<Integer> list;
        switch (index)
        {

            case 0:
                twoStrings   = new String[]{"中餐厅","综合酒楼","四川菜（川菜）","广东菜（粤菜）","山东菜（鲁菜)","江苏菜","浙江菜","上海菜","湖南菜（湘菜）"};
                 list= new ArrayList<Integer>();
                for (int i = 050100;i<050123;i++)
                {
                    list.add(i);
                }

                dataList = MyTools.initDate(twoStrings, "text", "code",list.toArray(), dataList);
                break;
            case 1:
                twoStrings = new String[]{"商场","便利店","家电电子卖场","超级市场","花鸟鱼虫市场","家居建材市场","综合市场","文化用品店","体育用品店"};
                list = new ArrayList<Integer>();
                for (int i = 050100;i<050123;i++)
                {
                    list.add(i);
                }
                dataList = MyTools.initDate(twoStrings, "text", "code",list.toArray(), dataList);
                break;
            case 2:
                twoStrings = new String[]{"旅行社","信息咨询中心","售票处","邮局","物流速递","电讯营业厅","事务所","人才市场","自来水营业厅"};
                list = new ArrayList<Integer>();
                for (int i = 050100;i<050123;i++)
                {
                    list.add(i);
                }
                dataList = MyTools.initDate(twoStrings, "text", "code",list.toArray(), dataList);
                break;
            case 3:
                twoStrings = new String[]{"商场","便利店","家电电子卖场","超级市场","花鸟鱼虫市场","家居建材市场","综合市场","文化用品店","体育用品店"};
                list = new ArrayList<Integer>();
                for (int i = 050100;i<050123;i++)
                {
                    list.add(i);
                }
                dataList = MyTools.initDate(twoStrings, "text", "code",list.toArray(), dataList);
                break;
            case 4:
                twoStrings = new String[]{"商场","便利店","家电电子卖场","超级市场","花鸟鱼虫市场","家居建材市场","综合市场","文化用品店","体育用品店"};
                list = new ArrayList<Integer>();
                for (int i = 050100;i<050123;i++)
                {
                    list.add(i);
                }
                dataList = MyTools.initDate(twoStrings, "text", "code",list.toArray(), dataList);
                break;
            case 5:
                twoStrings = new String[]{"商场","便利店","家电电子卖场","超级市场","花鸟鱼虫市场","家居建材市场","综合市场","文化用品店","体育用品店"};
                list = new ArrayList<Integer>();
                for (int i = 050100;i<050123;i++)
                {
                    list.add(i);
                }
                dataList = MyTools.initDate(twoStrings, "text", "code",list.toArray(), dataList);
                break;
            case 6:
                twoStrings = new String[]{"商场","便利店","家电电子卖场","超级市场","花鸟鱼虫市场","家居建材市场","综合市场","文化用品店","体育用品店"};
                list = new ArrayList<Integer>();
                for (int i = 050100;i<050123;i++)
                {
                    list.add(i);
                }
                dataList = MyTools.initDate(twoStrings, "text", "code",list.toArray(), dataList);
                break;
            default:
                listViewAdapter.notifyDataSetChanged();
        }
    }

    public void goBackClick(View view){
        this.finish();
    }
}
