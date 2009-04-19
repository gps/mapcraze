package edu.umich.gopalkri.mapcraze;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;

public class MapCraze extends Activity
{
    public static final int SELECT_POINTS = 1;

    private TextView mTextDisplay;
    private Intent mLaunchSelectPoints;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTextDisplay = (TextView) findViewById(R.id.text_display);

        Button start = (Button) findViewById(R.id.click_to_select);
        mLaunchSelectPoints = new Intent(this, SelectPoints.class);
        start.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                startActivityForResult(mLaunchSelectPoints, SELECT_POINTS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = data.getExtras();

        switch (requestCode)
        {
        case SELECT_POINTS:
            String locationStr = bundle.getString(SelectPoints.LOCATIONS_STRING);
            if (locationStr == null)
            {
                mTextDisplay.setText("Unable to recieve locations - no location string found.");
                return;
            }
            ArrayList<GeoPoint> tmp = SelectPoints.getGeoPointsFromLocationString(locationStr);
            Integer numPoints = tmp.size();
            String msg = "Total of " + numPoints.toString() + " points were selected. Points are: ";
            msg += locationStr;
            mTextDisplay.setText(msg);
            return;
        }
    }
}
