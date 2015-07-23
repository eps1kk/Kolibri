package app.main.kolibri;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    private MapView mMap;
    private MapController mConroller;
    private DataSingleton mDataSingleton;
    private OverlayManager mOverlayManager;
    private Overlay mOverlay;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MapFragment.
     */
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataSingleton = DataSingleton.getInstance();
        Timer timer = new Timer("repaintMapTimer");
        timer.schedule(repaintTask, 1000*30, 30 * 60 * 1000);
    }
    private TimerTask repaintTask = new TimerTask() {
        @Override
        public void run() {
            repaintMapObjects();
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMap = (MapView) view.findViewById(R.id.map_);
        mConroller = mMap.getMapController();
        GeoPoint userLocation;
        if (mDataSingleton.getLastKnownLocation() != null) {
            userLocation = new GeoPoint(mDataSingleton.getLastKnownLocation().getLatitude(),mDataSingleton.getLastKnownLocation().getLongitude());
            mConroller.setPositionAnimationTo(userLocation);
        }
        mOverlayManager = mConroller.getOverlayManager();
        mOverlay = new Overlay(mConroller);
        repaintMapObjects();
        return view;
    }
    private void repaintMapObjects() {
        Map<String,String>fMap = mDataSingleton.getAddressMap();
        OverlayItem[] overlayItems = new OverlayItem[fMap.size()];
        int j = 0;
        for (String i:fMap.keySet()) {
            String cd = fMap.get(i);
            double latitude = Double.parseDouble(cd.substring(0, cd.indexOf(";")));
            double longitude = Double.parseDouble(cd.substring(cd.indexOf(";") + 1,cd.length()));
            BitmapDrawable bitmap = new BitmapDrawable(getResources(),BitmapFactory.decodeResource(getResources(),R.mipmap.ic_place));
            GeoPoint gp = new GeoPoint(latitude,longitude);
            overlayItems[j] = new OverlayItem(gp,bitmap);
            BalloonItem item = new BalloonItem(mConroller.getContext(),gp);
            item.setText(i);
            overlayItems[j].setBalloonItem(item);
            j++;
        }
        mOverlay.clearOverlayItems();
        for (int i = 0; i < overlayItems.length; i++) {
            mOverlay.addOverlayItem(overlayItems[i]);
        }
        mOverlayManager.addOverlay(mOverlay);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
