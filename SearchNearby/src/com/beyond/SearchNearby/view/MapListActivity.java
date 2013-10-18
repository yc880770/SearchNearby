package com.beyond.SearchNearby.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.beyond.SearchNearby.R;
import com.beyond.SearchNearby.util.MyTools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 闯儿
 * Date: 13-10-17
 * Time: 下午9:59
 * To change this template use File | Settings | File Templates.
 */
public class MapListActivity  extends Activity {
    private MapView mapView;
    private MapController mapController;
    private TextView titleTextView;
    private View  viewCache;
    private TextView popNameText;
    private PopupOverlay pop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoApplication app = (DemoApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            app.mBMapManager.init(DemoApplication.strKey,new DemoApplication.MyGeneralListener()
            );
        }
        setContentView(R.layout.map);
        titleTextView = (TextView) findViewById(R.id.map_title_textView);
        mapView = (MapView) findViewById(R.id.BDMap);
//             获取地图控制
        mapController = mapView.getController();
        mapController.setZoom(14);
        mapView.getController().enableClick(true);
        mapView.setBuiltInZoomControls(true);
//
        GeoPoint geoPoint=new GeoPoint((int) (DemoApplication.locData.latitude * 1E6), (int) (DemoApplication.locData.longitude * 1E6));
        Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);
        mapController.setCenter(geoPoint);
    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        String title = intent.getStringExtra(SecondActivity.dataText);
        titleTextView.setText(title);
        initData(title);
        super.onResume();
    }

    public void createPaopao(){
        viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
        popNameText =(TextView) viewCache.findViewById(R.id.custom_name_textView);
        //泡泡点击响应回调
        PopupClickListener popListener = new PopupClickListener(){
            @Override
            public void onClickedPopup(int index) {
//                Log.v("click", "clickapoapo");
            }
        };
        pop = new PopupOverlay(mapView,popListener);
        MyLocationMapView.pop = pop;
    }

    private void initData(String text) {

        final String title = text;

        final ProgressDialog progressDialog = new ProgressDialog(MapListActivity.this);
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
                    Log.d("ListAcitvity", requestStr) ;
                    JSONObject jsonObject = new JSONObject(requestStr);
                    JSONArray ja = jsonObject.getJSONArray("poilist");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = (JSONObject) ja.get(i);
                        Map<String,Object> map = new HashMap<String, Object>();
                        map.put("name", jo.get("name"));
                        map.put("distance", jo.get("distance")+".00米");
                        map.put("address", jo.get("address"));
                    }
                } catch (IOException e) {
                    return ListActivity.ERROR_SERVER;
                } catch (JSONException e) {
                    return ListActivity.ERROR_DATA_FORMAT;
                }


                return ListActivity.SUSSES;
            }

            @Override
            protected void onPreExecute() {
                progressDialog.show();



            }

            @Override
            protected void onPostExecute(Integer request) {
                progressDialog.dismiss();
                if (request == ListActivity.SUSSES) {
                   mapView.refresh();

                } else if (request ==ListActivity.ERROR_SERVER)
                {
                    showServerErrorMessage();

                } else if (request ==ListActivity.ERROR_DATA_FORMAT)
                {
                    showDataErrorMessage();
                }
            }
        };
        asyncTask.execute(0);
    }

    private void showDataErrorMessage() {
        Toast.makeText(this, "数据格式错误", Toast.LENGTH_LONG).show();
    }

    private void showServerErrorMessage() {
        Toast.makeText(this,"请求数据失败",Toast.LENGTH_LONG).show();
    }

    public void goBackClick(View view){
        this.finish();
    }


}
