package com.example.myapplication;

import android.graphics.PointF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.view.View;
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
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NaverMap.OnCameraChangeListener, NaverMap.OnCameraIdleListener, Overlay.OnClickListener, NaverMap.OnMapClickListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private boolean isCameraAnimated = false;
    private InfoWindow infoWindow;

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
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        CameraPosition cameraPosition = new CameraPosition(new LatLng(33.50646625992274,126.49325628820664),12);
        Xmlparser.mapx = 126.49325628820664; //제주공항 위도, 경도
        Xmlparser.mapy = 33.50646625992274;

        naverMap.setCameraPosition(cameraPosition);
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        naverMap.setOnMapClickListener(this);

        naverMap.addOnCameraChangeListener(this);
        naverMap.addOnCameraIdleListener(this);
        naverMap.setExtent(new LatLngBounds(new LatLng(32.998197262657335 , 125.90787494613036), new LatLng( 33.78626662360304 , 127.22933090668839))); // 제주도 영역제한

        naverMap.setMinZoom(11.0);
        naverMap.setMaxZoom(18.0);

        setUpMap();

        infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(this) {
            @NonNull
            @Override
            protected View getContentView(@NonNull InfoWindow infoWindow) {
                Marker marker = infoWindow.getMarker();
                Touristdestination entity = (Touristdestination) marker.getTag();
                View view = View.inflate(MapActivity.this, R.layout.view_info_window,null);
                ((TextView) view.findViewById(R.id.txttitle)).setText(entity.getTitle());
                ((TextView) view.findViewById(R.id.txtaddr)).setText(entity.getAddr());
                ((TextView) view.findViewById(R.id.txttel)).setText(entity.getTel());
                ImageView imagePoint = (ImageView) view.findViewById(R.id.imagepoint);
                Glide.with(view).load(entity.getFirstimage()).into(imagePoint);



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
            setUpMap();
        }
    }

    @Override
    public  boolean onClick(@NonNull Overlay overlay){
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
    public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        if (infoWindow.getMarker() != null) {
            infoWindow.close();
        }
    }

    private void setUpMap() {

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

