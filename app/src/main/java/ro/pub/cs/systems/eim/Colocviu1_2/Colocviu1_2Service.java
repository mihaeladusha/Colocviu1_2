package ro.pub.cs.systems.eim.Colocviu1_2;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Colocviu1_2Service extends Service {

    public static final String BROADCAST_ACTION = "ro.pub.cs.systems.eim.Colocviu1_2.broadcast";
    public static final String EXTRA_SUM = "sum";
    private static final String TAG = "Colocviu1_2Service";
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        Log.d(TAG, "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int sum = intent.getIntExtra(EXTRA_SUM, 0);
        Log.d(TAG, "Service started with sum: " + sum);
        handler.postDelayed(() -> {
            Intent broadcastIntent = new Intent(BROADCAST_ACTION);
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            broadcastIntent.putExtra("currentTime", currentTime);
            broadcastIntent.putExtra(EXTRA_SUM, sum);
            sendBroadcast(broadcastIntent);
            Log.d(TAG, "Broadcast sent with currentTime: " + currentTime + " and sum: " + sum);
        }, 2000);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        Log.d(TAG, "Service destroyed");
        super.onDestroy();
    }
}