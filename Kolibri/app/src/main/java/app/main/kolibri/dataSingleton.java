package app.main.kolibri;

import android.location.Location;
import android.util.Log;

import com.vk.sdk.VKAccessToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by epS on 31.05.2015.
 */
class DataSingleton {

    private static DataSingleton mInstance;
    private VKAccessToken accessToken;
    private Location lastKnownLocation;
    private boolean isGPSEnabled = false;
    private Timer timer;
    private int defaultDistance = 150;
    private Map<String,String> addresses = new HashMap<String,String>();
    private Map<String,Float> distances = new HashMap<String,Float>();
    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            addresses.clear();
            addresses.putAll(checkUpdates());
        }
    };
    private DataSingleton() {
        // Create singleton
        Log.w("Singleton", "Singleton Created!");
        addresses = new HashMap<String,String>();
        timer = new Timer("Update Timer");
        timer.schedule(updateTask,0,30*60*1000);
    }

//    Init Instance of singleton
    public static void initInstance() {
        if (mInstance == null) {
            mInstance = new DataSingleton();
        }
    }
    // Return instance of singleton
    public static DataSingleton getInstance() {
        return mInstance;
    }
    public Map<String,Float> getDistances() {
        return distances;
    }
    public void setDistance(String name,float distance) {
        distances.put(name, distance);
    }
    public void clearDistancesMap() {
        distances.clear();
    }
//    get AccessToken for VK
    public VKAccessToken getAccessToken() {
        return accessToken;
    }
//    Set AccessToken for VK
    public void setAccessToken(VKAccessToken token) {
        accessToken = token;
    }
//    Return dataMap
    public Map<String,String> getAddressMap() {
        return addresses;
    }
//    Set LastKnownLocation
    public void setLastKnownLocation(Location location) {
        lastKnownLocation = location;
    }
//    Get LastKnownLocation
    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }
//    set GPSEnable
    public void setGPSEnable(boolean enable) {
        isGPSEnabled = enable;
    }
//    get GPS status
    public boolean getGPSStatus() {
        return isGPSEnabled;
    }
//   return the defaultDistance
    public int getDefaultDistance() {
        return defaultDistance;
    }
//    set the defaultDistance
    public void setDefaultDistance(int distance) {
        defaultDistance = distance;
    }
//    Return a new data from server
    private Map<String,String> checkUpdates() {
        Map<String,String>coordMap = new HashMap<String,String>();
       double[][] coords = new double[][]{
                new double[]{56.836219, 60.594063},
                new double[]{56.833413, 60.596294},
                new double[]{56.829639, 60.598687},
                new double[]{56.825906, 60.604995},
                new double[]{56.840186, 60.610645},
                new double[]{56.840250, 60.616170},
                new double[]{56.842457, 60.651747},
                new double[]{56.843930, 60.653549},
                new double[]{56.819466, 60.624219},
                new double[]{56.824750, 60.626601}
        };
       String[]  names = new String[]{
               "Успенский",
               "Бургер Кинг на Вайнера",
               "Гринвич",
               "Цирк",
               "Базар",
               "УрГУ на Ленина",
               "Физ-тех",
               "ГУК",
               "Мой дом",
               "Остановка Декабристов"
        };
        for (int i = 0; i < names.length; i++) {
            coordMap.put(names[i],String.valueOf(coords[i][0]) + ";" + String.valueOf(coords[i][1]));
        }
        for (String i:coordMap.keySet()) {
            System.out.println(coordMap.get(i) + "  " + i);
        }
        return coordMap;
    }
}
