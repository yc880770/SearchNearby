package com.beyond.SearchNearby.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.beyond.SearchNearby.R;
import com.beyond.SearchNearby.util.MyTools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 闯儿
 * Date: 13-10-16
 * Time: 下午10:36
 * To change this template use File | Settings | File Templates.
 */
public class ListActivity extends Activity {
    public static final  int SUSSES = 0;
    public static final int ERROR_SERVER = 1;
    public static final int ERROR_DATA_FORMAT= 2;
    private TextView titleTextView;
    private ListView contentListView ;
    private SimpleAdapter simpleAdapter ;
    private List<Map<String,Object>> dataList = new ArrayList<Map<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list);
        titleTextView = (TextView) findViewById(R.id.list_title_textView);
        contentListView = (ListView) findViewById(R.id.list_content_listView);
        simpleAdapter = new SimpleAdapter(this,dataList,R.layout.list_conten_item,new String[]{"name","distance","address"},new int[]{R.id.list_content_left_textView,R.id.list_content_item_right_textView,R.id.list_content_item_bottom_textView})
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null)
                {
                    LayoutInflater layoutInflater = getLayoutInflater() ;
                    convertView = layoutInflater.inflate(R.layout.list_conten_item,parent,false);
                }
                Map<String,Object> map = (Map<String, Object>) getItem(position);
                TextView nameView= (TextView) convertView.findViewById(R.id.list_content_left_textView);
                TextView distanceView= (TextView) convertView.findViewById(R.id.list_content_item_right_textView);
                TextView addressView= (TextView) convertView.findViewById(R.id.list_content_item_bottom_textView);
                LinearLayout linearLayout= (LinearLayout) convertView.findViewById(R.id.list_content_item_linearLayout);
                final  int index = position;
                final  String  title = map.get("name").toString();
                nameView.setText(title);
                distanceView.setText(map.get("distance").toString());
                addressView.setText(map.get("address").toString());
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClick(index,title);
                    }
                });

                return  convertView;
            }
        };
        contentListView.setAdapter(simpleAdapter);
    }

    @Override
    protected void onResume() {
        init();

        super.onResume();
    }

    private void init() {
        Intent intent = getIntent();
       String title =  intent.getStringExtra(SecondActivity.dataText);
       int index =  intent.getIntExtra(SecondActivity.dataIndex, 1);
       titleTextView.setText(title);
       initData(index, title);
    }

    private void initData(int position,String text) {
        final int index = position;
        final String title = text;

        final ProgressDialog progressDialog = new ProgressDialog(ListActivity.this);
        progressDialog.setMessage("刷新中。。。。");

        AsyncTask<Integer, Float, Integer> asyncTask = new AsyncTask<Integer, Float, Integer>() {


            @Override
            protected Integer doInBackground(Integer... string) {
                String php ="https://api.weibo.com/2/location/pois/search/by_geo.json";
                String coordinate = DemoApplication.locData.longitude+","+DemoApplication.locData.latitude;
                String centre =title;

                String url_username =php+"?coordinate="+coordinate+"&q="+centre+"&access_token=2.00KZZWLEREyGdC3cba17f3a70wTEoO";
                try {
                    String requestStr  = MyTools.requestServerDate(url_username);
                    Log.d("ListAcitvity",requestStr) ;
                    JSONObject jsonObject = new JSONObject(requestStr);
                    JSONArray  ja = jsonObject.getJSONArray("poilist");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = (JSONObject) ja.get(i);
                         Map<String,Object> map = new HashMap<String, Object>();
                        map.put("name", jo.get("name"));
                        map.put("distance", jo.get("distance")+".00米");
                        map.put("address", jo.get("address"));
                        dataList.add(map);

                    }
                } catch (IOException e) {
                    return ERROR_SERVER;
                } catch (JSONException e) {
                    return ERROR_DATA_FORMAT;
                }


                return SUSSES;
            }

            @Override
            protected void onPreExecute() {
                progressDialog.show();



            }

            @Override
            protected void onPostExecute(Integer request) {
                progressDialog.dismiss();
                if (request == SUSSES) {
                      simpleAdapter.notifyDataSetChanged();

                } else if (request ==ERROR_SERVER)
                {
                    showServerErrorMessage();
                
                } else if (request ==ERROR_DATA_FORMAT)
                {
                    showDataErrorMessage();
                }

            }


        };
        asyncTask.execute(0);
    }

    private void itemClick(int position,String title) {
        Intent intent = new Intent(this,MapListActivity.class);
        intent.putExtra(SecondActivity.dataIndex,position);
        intent.putExtra(SecondActivity.dataText,titleTextView.getText().toString());
        startActivity(intent);
    }


    private void showDataErrorMessage() {
        Toast.makeText(this,"数据格式错误",Toast.LENGTH_LONG).show();
    }

    private void showServerErrorMessage() {
        Toast.makeText(this,"请求数据失败",Toast.LENGTH_LONG).show();
    }
    public void goBackClick(View view){
        this.finish();
    }
}
