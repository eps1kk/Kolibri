package app.main.kolibri;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.api.VKError;

/**
 * Created by epS on 31.05.2015.
 */
public class kolibriApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataSingleton.initInstance();
//        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        VKSdk.initialize(new VKSdkListener() {
            @Override
            public void onCaptchaError(VKError captchaError) {

            }

            @Override
            public void onTokenExpired(VKAccessToken expiredToken) {
                String[] scopes = new String[]{"notify", "photos", "stats"};
                VKSdk.authorize(scopes);
            }

            @Override
            public void onAccessDenied(VKError authorizationError) {
                onTokenExpired(null);
            }

            @Override
            public void onReceiveNewToken(VKAccessToken newToken) {
                super.onReceiveNewToken(newToken);
                DataSingleton.getInstance().setAccessToken(newToken);
                startActivity(new Intent(kolibriApplication.this, MainActivity.class));
            }
        }, String.valueOf(R.string.appId), DataSingleton.getInstance().getAccessToken());
        VKSdk.authorize("notify,photos,stats");
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        DataSingleton.getInstance().setLastKnownLocation(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER));
        DataSingleton.getInstance().setGPSEnable(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        Intent intent = new Intent(kolibriApplication.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
