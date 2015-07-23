package app.main.kolibri;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataSingleton.getInstance().setGPSEnable(true);
        NotifyLocationListener.setUpLocationListener(this);
        startService(new Intent(MainActivity.this, NotificationService.class));
        setContentView(R.layout.activity_main);
        FragmentManager fManager = getFragmentManager();
        MapFragment map = MapFragment.newInstance();
        AddressItemFragment addressItemFragment = AddressItemFragment.newInstance();
        fManager.beginTransaction().add(map,"map");
        fManager.beginTransaction().show(map);
        fManager.beginTransaction().add(addressItemFragment, "addresses");
        fManager.beginTransaction().show(addressItemFragment);
        fManager.beginTransaction().commit();
    }
    @Override
    protected void onPause() {
        super.onPause();
        DataSingleton.getInstance().setGPSEnable(false);
        NotifyLocationListener.disableGPS();
    }
    protected void onResume() {
        super.onResume();
        DataSingleton.getInstance().setGPSEnable(true);
        NotifyLocationListener.enableGPS();
    }
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this,NotificationService.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
