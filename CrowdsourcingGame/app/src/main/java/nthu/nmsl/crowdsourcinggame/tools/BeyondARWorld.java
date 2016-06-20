package nthu.nmsl.crowdsourcinggame.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import nthu.nmsl.crowdsourcinggame.R;

/**
 *  Created by inin6 on 2015/10/30.
 */
@SuppressLint("SdCardPath")
public class BeyondARWorld  {
    private World sharedWorld;
    GeoObject user;
    public BeyondARWorld(Context context, double Lat, double Lon, boolean displayUser) {
        sharedWorld = new World(context);
        sharedWorld.setDefaultImage(R.drawable.ic_launcher);
        sharedWorld.setGeoPosition(Lat, Lon);
        if (displayUser) {
            user = new GeoObject(0);
            user.setGeoPosition(Lat, Lon);
            user.setImageResource(R.drawable.user);
            user.setName("User");
            sharedWorld.addBeyondarObject(user);
        }
    }

    public World getWorld() {
        return sharedWorld;
    }

    public void addObject(long id, double Lat, double Lon, int resource, String name) {
        GeoObject temp = new GeoObject(id);
        temp.setGeoPosition(Lat, Lon);
        temp.setImageResource(resource);
        temp.setName(name);
        sharedWorld.addBeyondarObject(temp);
    }

    public void setGeoPosition(double Lat, double Lon) {
        sharedWorld.setGeoPosition(Lat, Lon);
    }
}
