package app.main.kolibri;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    private NotificationManager mNotificationManager;
    private ArrayList<Notification> mNotifications;
    private Notification.Builder mNotificationBuilder;
    private int lastId = 0;


    public NotificationService() {
        Timer timer = new Timer("Check location");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkLocation();
            }
        },0,2*1000*60);
    }
    private void checkLocation() {
        DataSingleton dataSignleton = DataSingleton.getInstance();
        dataSignleton.clearDistancesMap();
        String defaultProvider;
        if (dataSignleton.getGPSStatus()) {
            defaultProvider = LocationManager.GPS_PROVIDER;
        }
        else {
            defaultProvider = LocationManager.NETWORK_PROVIDER;
            Log.w("info", "ya zdes;");
        }
        Location addressLocation = new Location(defaultProvider);
        Map<String,String> fMap = dataSignleton.getAddressMap();
        double latitude;
        double longitude;
        if (dataSignleton.getLastKnownLocation() == null) {
            Log.w("locationError","Локация не определена");
            Log.w(defaultProvider, "defaultProvider");
        }
        else  {
            for (String i:fMap.keySet()) {
                dataSignleton.setDistance(i, dataSignleton.getLastKnownLocation().distanceTo(addressLocation));
                latitude = Double.parseDouble(fMap.get(i).substring(0, fMap.get(i).indexOf(";")));
                longitude = Double.parseDouble(fMap.get(i).substring(fMap.get(i).indexOf(";") + 1));
                addressLocation.setLatitude(latitude);
                addressLocation.setLongitude(longitude);
                if (dataSignleton.getLastKnownLocation().distanceTo(addressLocation) < dataSignleton.getDefaultDistance()) {
                    sendNotification(i);
                }
                else {
                    Log.w("distance" , String.valueOf(dataSignleton.getLastKnownLocation().distanceTo(addressLocation)));
                }
            }
        }
    }

    private void sendNotification(String name) {
        Log.w("Beda", "Notification send");
        if(mNotifications.size() > 10) {
            mNotifications.clear();
        }
        if (mNotificationBuilder == null) {
            mNotificationBuilder = new Notification.Builder(getApplicationContext());
        }
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        String notificationText = name + " - Здесь тебя ждет сюрприз!";
        Intent notificationIntent = new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),0,notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        mNotificationBuilder
                .setAutoCancel(true)
                .setContentTitle("Акция!")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(notificationText)
                .setTicker(notificationText)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_notification);
        Notification notification = mNotificationBuilder.build();
        mNotifications.add(notification);
        mNotificationManager.notify(lastId,notification);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
