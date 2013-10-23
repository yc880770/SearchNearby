package com.beyond.SearchNearby.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.beyond.SearchNearby.R;

/**
 * Created with IntelliJ IDEA.
 * User: 闯儿
 * Date: 13-10-21
 * Time: 上午10:54
 * To change this template use File | Settings | File Templates.
 */
public class ParticularsActivity extends Activity {
    public static final String POILatitude="Latitude";
    public static final String POILongitude="Longitude";
    private int Y = 0;
    private int X = 0;
    private MapView mapView;
    private MapController mapController;
    private TextView odometer;
    private TextView poiName;
    private TextView address;
    private TextView telephone;
    private RadioGroup radioGroup;
    private MKRoute route;
    private int type =1;
    private  TransitOverlay transitOverlay = null;//保存公交路线图层数据的变量，供浏览节点时使用

    private RouteOverlay routeOverlay = null;
    private MKSearch mSearch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        DemoApplication app = (DemoApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            app.mBMapManager.init(DemoApplication.strKey,new DemoApplication.MyGeneralListener()
            );
        }
        setContentView(R.layout.particulars);
        mapView = (MapView) findViewById(R.id.particulars_bmapView);
        odometer = (TextView) findViewById(R.id.particulars_odometer_textView);
        address = (TextView) findViewById(R.id.particulars_address);
        poiName = (TextView) findViewById(R.id.particulars_poiName_textView);
        telephone = (TextView) findViewById(R.id.particulars_telephone_textView);
        mapController =  mapView.getController();
        radioGroup = (RadioGroup) findViewById(R.id.particulars_radioGroup);
//        radioGroup.check(1098615480);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("mSearch","checkedId = "+ checkedId) ;
                Log.d("mSearch","checkedId = "+(checkedId==R.id.particulars_walk_radioButton)) ;
                if (checkedId == R.id.particulars_walk_radioButton)
                {

                    SearchButtonProcess(1);
                }else if (checkedId == R.id.particulars_bus_radioButton)
                {
                    SearchButtonProcess(2);
                }else if (checkedId == R.id.particulars_car_radioButton)
                {
                    SearchButtonProcess(3);
                }

                Log.d("particulars",checkedId+" checkedId ");
                Log.d("particulars",R.id.particulars_walk_radioButton+" getCheckedRadioButtonId ");
            }
        });
        Log.d("particulars", radioGroup.getCheckedRadioButtonId()+"");
        Log.d("particulars", radioGroup.getCheckedRadioButtonId()+"");
        mapView.setBuiltInZoomControls(false);
//        View.OnClickListener clickListener = new View.OnClickListener(){
//            public void onClick(View v) {
//                //发起搜索
//                SearchButtonProcess();
//            }
//        };

        mSearch = new MKSearch();
        mSearch.init(app.mBMapManager, new MKSearchListener(){
            //开车
            public void onGetDrivingRouteResult(MKDrivingRouteResult res,
                                                int error) {
                //起点或终点有歧义，需要选择具体的城市列表或地址列表
                if (error == MKEvent.ERROR_ROUTE_ADDR){
                    //遍历所有地址
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
                    return;
                }
                // 错误号可参考MKEvent中的定义
                if (error != 0 || res == null) {
                    Toast.makeText(ParticularsActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ParticularsActivity.this,"刷新驾车路线中。。。",Toast.LENGTH_SHORT);
                routeOverlay = new RouteOverlay(ParticularsActivity.this, mapView);
                // 此处仅展示一个方案作为示例
                routeOverlay.setData(res.getPlan(0).getRoute(0));
                //清除其他图层
//                mapView.getOverlays().clear();
                //添加路线图层
                mapView.getOverlays().add(routeOverlay);
                //执行刷新使生效
                mapView.refresh();
                // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
                //移动地图到起点
                mapView.getController().animateTo(res.getStart().pt);
                //将路线数据保存给全局变量
                route = res.getPlan(0).getRoute(0);
                //重置路线节点索引，节点浏览时使用

            }

//公交
            public void onGetTransitRouteResult(MKTransitRouteResult res,
                                                int error) {
                //起点或终点有歧义，需要选择具体的城市列表或地址列表
                if (error == MKEvent.ERROR_ROUTE_ADDR){
                    //遍历所有地址
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
                    return;
                }
                if (error != 0 || res == null) {
                    Toast.makeText(ParticularsActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ParticularsActivity.this,"刷新公交路线中。。。",Toast.LENGTH_SHORT);
                transitOverlay = new TransitOverlay(ParticularsActivity.this, mapView);
                // 此处仅展示一个方案作为示例
                transitOverlay.setData(res.getPlan(0));
                //清除其他图层
//                mapView.getOverlays().clear();
                //添加路线图层
                mapView.getOverlays().add(transitOverlay);
                //执行刷新使生效
                mapView.refresh();
                // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                mapView.getController().zoomToSpan(transitOverlay.getLatSpanE6(), transitOverlay.getLonSpanE6());
                //移动地图到起点

                mapView.getController().animateTo(res.getStart().pt);
                route = res.getPlan(0).getRoute(0);
                Log.d("route",res.getAddrResult().toString());

            }

            public void onGetWalkingRouteResult(MKWalkingRouteResult res,
                                                int error) {
                //起点或终点有歧义，需要选择具体的城市列表或地址列表
                if (error == MKEvent.ERROR_ROUTE_ADDR){
                    //遍历所有地址
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
                    return;
                }
                if (error != 0 || res == null) {
                    Toast.makeText(ParticularsActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }

                 Toast.makeText(ParticularsActivity.this,"刷新步行路线中。。。",Toast.LENGTH_SHORT);
                routeOverlay = new RouteOverlay(ParticularsActivity.this, mapView);
                // 此处仅展示一个方案作为示例
                routeOverlay.setData(res.getPlan(0).getRoute(0));
                //清除其他图层
//                mapView.getOverlays().clear();
                //添加路线图层
                mapView.getOverlays().add(routeOverlay);
                //执行刷新使生效
                mapView.refresh();
                // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
                //移动地图到起点
                mapView.getController().animateTo(res.getStart().pt);
                //将路线数据保存给全局变量
                route = res.getPlan(0).getRoute(0);
                //重置路线节点索引，节点浏览时使用

            }
            public void onGetAddrResult(MKAddrInfo res, int error) {
            }
            public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {
            }
            public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            }

            @Override
            public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
            }

            @Override
            public void onGetPoiDetailSearchResult(int type, int iError) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onGetShareUrlResult(MKShareUrlResult result, int type,
                                            int error) {
                // TODO Auto-generated method stub

            }
        });
        initData();
    }


    @Override
    protected void onResume() {

        mapView.onResume();
        super.onResume();
    }

    private void initData() {
        Intent intent = getIntent();
        Y =  intent.getIntExtra(POILatitude,1);
        X =  intent.getIntExtra(POILongitude,2);
        SearchButtonProcess(1);

    }

    public void goBackClick(View view){
//        mapView.refresh();
        this.finish();
    }

    private  void SearchButtonProcess(int i) {
        //重置浏览节点的路线数据
        route = null;
        if (routeOverlay!=null) mapView.getOverlays().remove(routeOverlay);
        if (transitOverlay!=null) mapView.getOverlays().remove(transitOverlay);
        mapView.refresh();
        routeOverlay = null;
        transitOverlay = null;
        // 处理搜索按钮响应
        // 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
        MKPlanNode stNode = new MKPlanNode();
//        GeoPoint myLocation = new GeoPoint((int) (DemoApplication.locData.latitude * 1E6), (int) (DemoApplication.locData.longitude * 1E6));
//        OverlayItem myOverlayItem = new OverlayItem(myLocation, "", "");
//        myOverlayItem.setMarker(getResources().getDrawable(R.drawable.ic_current_loc));
        GeoPoint startNode = new GeoPoint((int) (DemoApplication.locData.latitude * 1E6), (int) (DemoApplication.locData.longitude * 1E6));
//        mapController.setCenter(myLocation);
        stNode.pt = startNode;
        MKPlanNode enNode = new MKPlanNode();
        GeoPoint endNode = new GeoPoint(Y,X);
        enNode.pt = endNode;

        // 实际使用中请对起点终点城市进行正确的设定
        //驾车
        if (i==3) {
            mSearch.drivingSearch("西安", stNode, "西安", enNode);
            //公交
        } else if (i==2) {
            mSearch.transitSearch("西安", stNode, enNode);
            //步行
        } else if (i==1) {
            mSearch.walkingSearch("西安", stNode, "西安", enNode);
        }


    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        mapView.getOverlays().clear();
//        mapView.refresh();
        mapView.destroy();
        super.onDestroy();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mapView.onRestoreInstanceState(savedInstanceState);
    }


}
