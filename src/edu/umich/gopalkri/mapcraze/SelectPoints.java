package edu.umich.gopalkri.mapcraze;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class SelectPoints extends MapActivity
{
    public static final String LOCATIONS_STRING = "LOCATIONS_STRING";

    private static final String LOCATION_STRING_SEPARATOR = "Z";

    private static final int MENU_DONE = Menu.FIRST;

    private MyLocationOverlay mMyLocationOverlay;

    @Override
    protected void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.select_points);

        String msg = "Tap the screen to create markers. Tap on a marker to erase it. " + "" +
        		"When you are done, hit Menu->Done";
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();

        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setSatellite(true);

        LinearLayout zoomView = (LinearLayout) findViewById(R.id.zoomview);
        zoomView.addView(mapView.getZoomControls());

        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc == null)
        {
            loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (loc != null)
        {
            mapView.getController().setCenter(getGeoPointFromLocation(loc));
            mapView.getController().setZoom(15);
        }

        Drawable marker = getResources().getDrawable(R.drawable.map_pin_32);
        marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
        SelectPointsOverlay spo = new SelectPointsOverlay(marker, true);
        mapView.getOverlays().add(spo);

        mMyLocationOverlay = new MyLocationOverlay(this, mapView);
        mMyLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(mMyLocationOverlay);
    }

    @Override
    protected void onPause()
    {
        mMyLocationOverlay.disableMyLocation();
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        mMyLocationOverlay.enableMyLocation();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_DONE, Menu.NONE, R.string.done);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        switch (item.getItemId())
        {
        case MENU_DONE:
            ArrayList<GeoPoint> points = SelectPointsOverlay.getAllLocations();
            StringBuilder sb = new StringBuilder();
            for (GeoPoint point : points)
            {
                sb.append(point.getLatitudeE6());
                sb.append(LOCATION_STRING_SEPARATOR);
                sb.append(point.getLongitudeE6());
                sb.append(LOCATION_STRING_SEPARATOR);
            }
            Intent i = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(LOCATIONS_STRING, sb.toString());
            i.putExtras(bundle);
            setResult(RESULT_OK, i);
            finish();
            break;
        }

        return true;
    }

    public static ArrayList<GeoPoint> getGeoPointsFromLocationString(String locationStr)
    {
        ArrayList<GeoPoint> ret = new ArrayList<GeoPoint>();
        String[] split = locationStr.split(LOCATION_STRING_SEPARATOR);
        for (int i = 0; i < split.length - 1; ++i)
        {
            int lat = Integer.parseInt(split[i]);
            int lon = Integer.parseInt(split[i+1]);
            ret.add(new GeoPoint(lat, lon));
        }
        return ret;
    }

    @Override
    protected boolean isRouteDisplayed()
    {
        return false;
    }

    private static GeoPoint getGeoPointFromLocation(Location location)
    {
        Double lat = location.getLatitude() * 1E6;
        Double lon = location.getLongitude() * 1E6;
        return new GeoPoint(lat.intValue(), lon.intValue());
    }
}
