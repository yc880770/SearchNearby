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
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.beyond.SearchNearby.R;

import java.util.*;

public class IndexActivity extends Activity {
    private ListView indexListView;

    public  static double lng;
    public  static double lat;
    private List<Map<String,Object>> dateList = new ArrayList<Map<String, Object>>();
    private SimpleAdapter listViewAdapter ;
    private LocationClient locationClient;
    private TextView  bottomTextView;
    public static   String[] strings = new String[]{"餐饮服务","购物服务","生活服务","体育休闲服务","医疗保健服务","住宿服务","科教文化服务","交通设施服务","公共设施服务","公共设施服务","公共设施服务"};


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.index);
        bottomTextView = (TextView) findViewById(R.id.index_bottom_textView);
        indexListView = (ListView) findViewById(R.id.index_listView);
        listViewAdapter  = new SimpleAdapter(this,dateList,R.layout.index_content_item,new String[]{"text"},new int[]{R.id.index_content_list_textView})
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
                final  String title = map.get("text").toString();
                textView.setText(title);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClick(index,title);
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoNextView(index,title);
                    }
                });

                return  convertView;
            }
        };

        indexListView.setAdapter(listViewAdapter);

        getMyLocation();
        locationOnclick(bottomTextView);
    }

    private void itemClick(int position,String title) {
            Intent intent = new Intent(this,ListActivity.class);
            intent.putExtra(SecondActivity.dataIndex,position);
            intent.putExtra(SecondActivity.dataText,title);
            startActivity(intent);
    }

    @Override
    protected void onResume() {
        initDate();


        super.onResume();
    }

    private void initDate() {
        dateList.clear();
        for (int i = 0;i<strings.length;i++){
           Map<String,Object> map = new HashMap<String, Object>();
            map.put("text",strings[i]);
            map.put(SecondActivity.dataIndex,i);
            dateList.add(map);
        }
        listViewAdapter.notifyDataSetChanged();
    }


    public void seekOnClick(View view)
    {
          Intent intent = new Intent(this,SeekActivity.class);
//        this.finish();
        startActivity(intent);
    }

    public void settingOnClick(View view)
    {
          Intent intent = new Intent(this,SettingActivity.class);
//        this.finish();
        startActivity(intent);
    }



    private void gotoNextView(int index,String title) {
        Intent intent = new Intent(IndexActivity.this,SecondActivity.class);
        intent.putExtra(SecondActivity.dataIndex,index);
        intent.putExtra(SecondActivity.dataText,title);
        startActivity(intent);
    }

    private void getMyLocation() {
        Log.d("BaiduMapDemo", "getMyLocation");
        locationClient= new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setAddrType("all");
        option.setOpenGps(true);
        option.setPriority(LocationClientOption.NetWorkFirst);
        option.setScanSpan(5000);




        locationClient.setLocOption(option);

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null)
                    return ;
                Log.d("BaiduMapDemo", "onReceiveLocation address " + bdLocation.getAddrStr());

                Log.d("BaiduMapDemo", "onReceiveLocation Latitude " + bdLocation.getLatitude());
                Log.d("BaiduMapDemo", "onReceiveLocation Longitude " + bdLocation.getLongitude());
                 lng = bdLocation.getLongitude();
                 lat = bdLocation.getLatitude();

                DemoApplication.locData.latitude = lat;
                DemoApplication.locData.longitude = lng;
                if (lng==lat)
                {
                     bottomTextView.setText("定位失败请重新定位");
                }else
                {
                    bottomTextView.setText(bdLocation.getAddrStr());
                }
//                int offset = (int) (new Random().nextFloat() * 10000);
//                GeoPoint point = new GeoPoint((int) (lng * 1E6) + offset, (int) (lat * 1E6) - offset);

//                myLocationOverlay.setMyLocation(point);

//                mapView.refresh();
            }

            @Override
            public void onReceivePoi(BDLocation bdLocation) {
                Log.d("BaiduMapDemo", "onReceivePoi ");
            }
        });





    }
    public void locationOnclick(View view)
    {
        locationClient.start();
        bottomTextView.setText("定位中请稍等");
        Toast.makeText(IndexActivity.this, "正在定位……", Toast.LENGTH_SHORT).show();
        locationClient.requestLocation();
    }
}
