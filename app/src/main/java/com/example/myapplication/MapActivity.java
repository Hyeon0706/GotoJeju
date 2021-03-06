package com.example.myapplication;

import android.content.Intent;
import android.graphics.PointF;
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
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.ArrayList;



public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NaverMap.OnCameraChangeListener, NaverMap.OnCameraIdleListener, Overlay.OnClickListener, NaverMap.OnMapClickListener {
    private NaverMap naverMap;
    private boolean isCameraAnimated = false;
    private InfoWindow infoWindow; // 마커 인포윈도우
    private ImageButton back; //뒤로 이동 버튼
    private ImageButton categori; // 목록으로보기로 이동 버튼

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapactivity_main);

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
    public void onMapReady(@NonNull NaverMap naverMap) { //지도가 준비된 후
        this.naverMap = naverMap;
        CameraPosition cameraPosition = new CameraPosition(new LatLng(33.50646625992274,126.49325628820664),12); // 지도의 시작좌표, 줌레벨
        Xmlparser.mapx = 126.49325628820664; //제주공항 위도, 경도
        Xmlparser.mapy = 33.50646625992274;

        naverMap.setCameraPosition(cameraPosition); // 시작좌표 지정
        naverMap.setOnMapClickListener(this);

        naverMap.addOnCameraChangeListener(this);
        naverMap.addOnCameraIdleListener(this);
        naverMap.setExtent(new LatLngBounds(new LatLng(32.998197262657335 , 125.90787494613036), new LatLng( 33.78626662360304 , 127.22933090668839))); // 제주도 영역제한

        naverMap.setMinZoom(11.0); //지도의 최소, 최대줌 조정
        naverMap.setMaxZoom(18.0);

        setUpMap(); //마커 생성

        infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(this) {
            @NonNull
            @Override
            protected View getContentView(@NonNull InfoWindow infoWindow) { // 인포윈도우
                Marker marker = infoWindow.getMarker();
                Touristdestination entity = (Touristdestination) marker.getTag(); // 마커의 Tag값을 얻어와 해당하는 정보창을 띄우기 위함
                View view = View.inflate(MapActivity.this, R.layout.view_info_window,null);
                ((TextView) view.findViewById(R.id.txttitle)).setText(entity.getTitle()); // 관광지 이름을 표시
                ((TextView) view.findViewById(R.id.txtaddr)).setText(entity.getAddr()); // 관광지 주소를 표시
                ImageView imagePoint = (ImageView) view.findViewById(R.id.imagepoint);
                if(entity.getFirstimage() == null){ //데이터에 이미지가 없다면
                    imagePoint.setImageResource(R.drawable.no_image); // 노 이미지를 출력
                }else{
                    Glide.with(view).load(entity.getFirstimage()).into(imagePoint);// 관광지 이미지를 표시 Glide 라이브러리 사용
                }

                infoWindow.setOnClickListener(new Overlay.OnClickListener() // 인포위도우 클릭이벤트
                {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay)
                    {
                        Intent intent = new Intent(getApplicationContext(), InfoActivity.class); // 마커에 해당하는 컨테츠아이디를 가져와 관광지정보화면으로 이동
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
                marker.setTag(entity); // 태그값 설정
                marker.setPosition(new LatLng(entity.getMapy(),entity.getMapx())); // 마커의 위치 설정
                marker.setMap(naverMap);
                marker.setAnchor(new PointF(0.5f,1.0f)); // 마커와 좌표간의 이격을 없애기 위함
                marker.setWidth(80); // 마커 너비 설정
                marker.setHeight(80); // 마커 높이 설정
                marker.setCaptionText(entity.getTitle()); // 마커 밑 관광지이름 출력을 위함
                marker.setOnClickListener(this);
                marker.setIcon(OverlayImage.fromResource(R.drawable.main_image)); // 마커이미지 변경
            }
        }
    }



}

