package com.example.myapplication;

import android.content.Intent;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;



public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NaverMap.OnCameraChangeListener, NaverMap.OnCameraIdleListener, Overlay.OnClickListener, NaverMap.OnMapClickListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private boolean isCameraAnimated = false;
    private InfoWindow infoWindow;
    private ImageButton back;
    private ImageButton categori;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapactivity_main);




        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        back = findViewById(R.id.back);
        categori = findViewById(R.id.moveCategori);
        //뒤로가기 버튼
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //목록으로 보기 이동 버튼
        categori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, Categori.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) { //지도가 준비된 후
        this.naverMap = naverMap;
        CameraPosition cameraPosition = new CameraPosition(new LatLng(33.50646625992274,126.49325628820664),12); // 지도의 시작좌표, 줌레벨
        Xmlparser.mapx = 126.49325628820664; //제주공항 위도, 경도
        Xmlparser.mapy = 33.50646625992274;

        naverMap.setCameraPosition(cameraPosition); // 시작좌표 지정
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true); // 현재위치 좌표 이동 버튼 활성화
        naverMap.setOnMapClickListener(this);

        naverMap.addOnCameraChangeListener(this);
        naverMap.addOnCameraIdleListener(this);
        naverMap.setExtent(new LatLngBounds(new LatLng(32.998197262657335 , 125.90787494613036), new LatLng( 33.78626662360304 , 127.22933090668839))); // 제주도 영역제한

        naverMap.setMinZoom(11.0); //지도의 최소, 최대줌 조정
        naverMap.setMaxZoom(18.0);

        setUpMap();

        infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(this) {
            @NonNull
            @Override
            protected View getContentView(@NonNull InfoWindow infoWindow) {
                Marker marker = infoWindow.getMarker();
                Touristdestination entity = (Touristdestination) marker.getTag(); // 마커의 Tag값을 얻어와 해당하는 정보창을 띄우기 위함
                View view = View.inflate(MapActivity.this, R.layout.view_info_window,null);
                ((TextView) view.findViewById(R.id.txttitle)).setText(entity.getTitle()); //관광지 이름을 표시
                ((TextView) view.findViewById(R.id.txtaddr)).setText(entity.getAddr()); //관광지 주소를 표시
                ImageView imagePoint = (ImageView) view.findViewById(R.id.imagepoint);
                if(entity.getFirstimage() == null){
                    imagePoint.setImageResource(R.drawable.no_image);
                }else{
                    Glide.with(view).load(entity.getFirstimage()).into(imagePoint);// 관광지 이미지를 표시. Glide 라이브러리 사용
                }





                infoWindow.setOnClickListener(new Overlay.OnClickListener()
                {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay)
                    {
                        Intent intent = new Intent(getApplicationContext(),test1.class);
                        intent.putExtra("conId",entity.getcontentid());
                        startActivity(intent);

                        return false;
                    }
                });


                return view;
            }
        });
    }

    @Override
    public void onCameraChange(int reason, boolean animated) {
        isCameraAnimated = animated;
    }

    @Override
    public void onCameraIdle() {
        if (isCameraAnimated) {
            LatLng mapCenter = naverMap.getCameraPosition().target;
            Xmlparser.mapx = mapCenter.longitude;
            Xmlparser.mapy = mapCenter.latitude;
            setUpMap(); // 지도의 움직임이 멈추면 지도의 중심좌표를 얻어와 마커를 생성
        }
    }

    @Override
    public  boolean onClick(@NonNull Overlay overlay){ // 마커를 클릭하여 정보창을 출력하고 이미 출력되어있을시, 정보창을 닫음
        if (overlay instanceof Marker) {
            Marker marker = (Marker) overlay;
            if (marker.getInfoWindow() != null) {
                infoWindow.close();
            } else {
                infoWindow.open(marker);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) { // 지도의 빈 곳을 클릭하여도 정보창을 닫음
        if (infoWindow.getMarker() != null) {
            infoWindow.close();
        }
    }

    private void setUpMap() { // 마커를 생성하기 위함

        Xmlparser parser = new Xmlparser();
        ArrayList<Touristdestination> list = new ArrayList<Touristdestination>();
        try {
            list = parser.apiParserSearch();
        } catch (Exception e){
            e.printStackTrace();
        }

        for (int i = 0; i < list.size(); i++)
        {
            for(Touristdestination entity : list){
                Marker marker = new Marker();
                marker.setTag(entity);
                marker.setPosition(new LatLng(entity.getMapy(),entity.getMapx()));
                marker.setMap(naverMap);
                marker.setAnchor(new PointF(0.5f,1.0f));
                marker.setWidth(50);
                marker.setHeight(80);
                marker.setCaptionText(entity.getTitle());
                marker.setOnClickListener(this);
            }
        }
    }



}

