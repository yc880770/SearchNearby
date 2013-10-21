package com.beyond.SearchNearby.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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
    private LayoutInflater layoutInflater;
    private View mapPopWindow;
    private PoiOverlay<OverlayItem> itemItemizedOverlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);
        itemItemizedOverlay = new PoiOverlay<OverlayItem>(marker, mapView);
//

        layoutInflater = LayoutInflater.from(this);
        mapPopWindow = layoutInflater.inflate(R.layout.custom_text_view, null);
        mapPopWindow.setVisibility(View.GONE);
        mapView.addView(mapPopWindow);
    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        String title = intent.getStringExtra(SecondActivity.dataText);
        titleTextView.setText(title);
        initData(title);
        super.onResume();
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
                    GeoPoint geoPoint=new GeoPoint((int) (DemoApplication.locData.latitude * 1E6), (int) (DemoApplication.locData.longitude * 1E6));
                    int MaxX=0;
                    int MinY=(int)(DemoApplication.locData.latitude);
                    int MinX=(int)(DemoApplication.locData.longitude);
                    int MaxY=0;
                    OverlayItem myOverlayItem = new OverlayItem(geoPoint, "我的位置", "");
                    myOverlayItem.setMarker(getResources().getDrawable(R.drawable.ic_current_loc));
                    mapController.setCenter(geoPoint);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = (JSONObject) ja.get(i);
                        int y = (int)(jo.getDouble("y")*1E6);
                        int x = (int)(jo.getDouble("x")*1E6);
                        MaxX =  Math.max(MaxX,x);
                        MinX =  Math.max(MinX,x);
                        MaxY =  Math.max(MaxY,y);
                        MinY =  Math.max(MinY,y);
                        GeoPoint point = new GeoPoint(y,x);
                        OverlayItem overlayItem = new OverlayItem(point, jo.getString("name"),  jo.getString("address"));
                        itemItemizedOverlay.addItem(overlayItem);
                    }
                    itemItemizedOverlay.addItem(myOverlayItem);
                    mapController.zoomToSpan(Math.abs(MaxY-MinY),Math.abs(MaxX-MinX));
                    mapView.getOverlays().add(itemItemizedOverlay);
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

    class PoiOverlay<OverlayItem> extends ItemizedOverlay {

        public PoiOverlay(Drawable drawable, MapView mapView) {
            super(drawable, mapView);
        }

        @Override
        protected boolean onTap(int i) {
            Log.d("BaiduMapDemo", "onTap " + i);
            com.baidu.mapapi.map.OverlayItem item = itemItemizedOverlay.getItem(i);
            final GeoPoint point = item.getPoint();
            String title = item.getTitle();
            String content = item.getSnippet();

            TextView titleTextView = (TextView) mapPopWindow.findViewById(R.id.custom_name_textView);
            TextView contentTextView = (TextView) mapPopWindow.findViewById(R.id.custom_text_textView);
            titleTextView.setText(title);
            contentTextView.setText(content);
            contentTextView.setVisibility(View.VISIBLE);

            MapView.LayoutParams layoutParam = new MapView.LayoutParams(
                    //控件宽,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //控件高,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //使控件固定在某个地理位置
                    point,
                    0,
                    -40,
                    //控件对齐方式
                    MapView.LayoutParams.BOTTOM_CENTER);

            mapPopWindow.setVisibility(View.VISIBLE);

            mapPopWindow.setLayoutParams(layoutParam);
            mapPopWindow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapListActivity.this,ParticularsActivity.class);
                    mapView.getOverlays().clear();
                    intent.putExtra(ParticularsActivity.POILatitude,point.getLatitudeE6());
                    intent.putExtra(ParticularsActivity.POILongitude,point.getLongitudeE6());
                    startActivity(intent);
                }
            });

            mapController.animateTo(point);

            return super.onTap(i);
        }

        @Override
        public boolean onTap(GeoPoint geoPoint, MapView mapView) {
            Log.d("BaiduMapDemo", "onTap geoPoint " + geoPoint);

            mapPopWindow.setVisibility(View.GONE);

            return super.onTap(geoPoint, mapView);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
}
