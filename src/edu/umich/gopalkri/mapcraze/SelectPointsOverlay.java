package edu.umich.gopalkri.mapcraze;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class SelectPointsOverlay extends ItemizedOverlay<OverlayItem>
{
    private Drawable mMarker;
    private static ArrayList<OverlayItem> mLocationList = new ArrayList<OverlayItem>();

    public SelectPointsOverlay(Drawable defaultMarker, boolean reset)
    {
        super(defaultMarker);
        mMarker = defaultMarker;
        if (reset)
        {
            mLocationList.clear();
        }
        populate();
    }

    public void draw(Canvas canvas, MapView mapView, boolean shadow)
    {
        super.draw(canvas, mapView, shadow);
        boundCenterBottom(mMarker);
    }

    @Override
    public boolean onTap(GeoPoint p, MapView mapView)
    {
        super.onTap(p, mapView);

        OverlayItem overlayItem = new OverlayItem(p, "", "");
        mLocationList.add(overlayItem);
        populate();

        return true;
    }

    @Override
    protected boolean onTap(int index)
    {
        super.onTap(index);

        mLocationList.remove(index);
        populate();

        return true;
    }

    @Override
    protected OverlayItem createItem(int index)
    {
        return mLocationList.get(index);
    }

    @Override
    public int size()
    {
        return mLocationList.size();
    }

    public static ArrayList<GeoPoint> getAllLocations()
    {
        ArrayList<GeoPoint> ret = new ArrayList<GeoPoint>();
        for (OverlayItem oi : mLocationList)
        {
            ret.add(oi.getPoint());
        }
        return ret;
    }
}
