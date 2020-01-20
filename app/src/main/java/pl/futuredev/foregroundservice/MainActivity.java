package pl.futuredev.foregroundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Button btnStartService, btnStopService;
    IAdditionService service;
    AdditionServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartService = findViewById(R.id.buttonStartService);
        btnStopService = findViewById(R.id.buttonStopService);
        btnStartService.setOnClickListener(v -> {
            startService();
            initService();
        });
        btnStopService.setOnClickListener(v -> {
            stopService();
            releaseService();
        });

        initService();
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
        Log.d(TAG, "startForegroundService");
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
        Log.d(TAG, "stopService");
    }

    /**
     * Binds this activity to the service.
     */
    private void initService() {
        connection = new AdditionServiceConnection();
        Intent i = new Intent();
        i.setClassName("pl.futuredev.foregroundservice", AdditionService.class.getName());
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "initService() bound with " + ret);
    }

    /**
     * Unbinds this activity from the service.
     */
    private void releaseService() {
        unbindService(connection);
        connection = null;
        Log.d(TAG, "releaseService() unbound.");
    }

    /**
     * Called when the activity is about to be destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseService();
    }

    /**
     * This class represents the actual service connection. It casts the bound
     * stub implementation of the service to the AIDL interface.
     */
    class AdditionServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IAdditionService.Stub.asInterface(boundService);
            Log.d(TAG, "onServiceConnected() connected");
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Log.d(TAG, "onServiceDisconnected() disconnected");
        }
    }

}