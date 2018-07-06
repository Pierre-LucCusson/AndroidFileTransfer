package com.androidfiletransfer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfiletransfer.files.FilesViewHandler;
import com.androidfiletransfer.connection.Server;
import com.androidfiletransfer.connection.ServerService;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private Contact contact;

    private RelativeLayout qrCodeLayout;
    private RecyclerView filesRecyclerView;

    private TextView deviceIdText;
    private String myDeviceId;
    private TextView ipAddressText;
    private String myIpAddress;
    private ImageView qrCodeImage;

    SharedPreferences sharedPrefs;
    List<String> contacts;

    private Server server;
    private ServerService serverService;

    private Tracker tracker;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            Toast.makeText(getApplicationContext(), "Lat: " + location.getLatitude() + " - Long: " + location.getLongitude(), Toast.LENGTH_LONG);
            System.err.println(location.getAccuracy() + " - " + location.getLongitude() + " - " + location.getLatitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            serverService = ((ServerService.MyBinder) binder).getService();
            server = serverService.getServer();
        }

        public void onServiceDisconnected(ComponentName className) {
            serverService = null;
            server = null;
        }
    };

    public void doBindService() {
        Intent intent = new Intent(this, ServerService.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showQrLayout();
                    return true;
                case R.id.navigation_qr_scanner:
                    hideAllLayouts();

                    /*
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.setPackage("com.androidfiletransfer");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 0);
                    */

                    IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                    scanIntegrator.setDesiredBarcodeFormats(Arrays.asList(BarcodeFormat.QR_CODE.toString()));
                    scanIntegrator.setCameraId(0);
                    scanIntegrator.setBeepEnabled(true);
                    scanIntegrator.setBarcodeImageEnabled(false);
                    scanIntegrator.initiateScan();


                    return true;
                case R.id.navigation_nfc_send:
                    openNfcSendActivity();
                    hideAllLayouts();
                    return true;
//                    we can not have more then 4 navigation
//                case R.id.navigation_nfc_receive:
//                    mTextMessage.setText(R.string.title_nfc_receive);
//                    openNfcReceiveActivity();
//                    hideAllLayouts();
//                    return true;
                case R.id.navigation_contacts:
                    openContactsActivity();
                    hideAllLayouts();
                    return true;
                case R.id.navigation_files:
                    showFilesLayout();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent in) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, in);
        if (scanningResult != null) {
            try {
                String contents = in.getStringExtra("SCAN_RESULT");
//                String format = in.getStringExtra("SCAN_RESULT_FORMAT");
                Contact contact = Contact.fromJson(contents);
            }
            catch (NullPointerException e) {

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (serverService == null) {
            doBindService();
        }

        setContentView(R.layout.activity_main);

        qrCodeLayout = findViewById(R.id.qrCodeLayout);
        filesRecyclerView = findViewById(R.id.filesRecyclerView);

        deviceIdText = findViewById(R.id.deviceIdText);
        ipAddressText = findViewById(R.id.ipAddressText);
        qrCodeImage = findViewById(R.id.qrCodeImage);

        showQrLayout();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        sharedPrefs = getSharedPreferences("Contacts", 0);
        contacts = new ArrayList<>();

        // Load contacts
        contacts = new Gson().fromJson(sharedPrefs.getString("Contacts", ""), List.class);

        tracker = new Tracker(this);
        tracker.startTracking(locationListener);
    }

    private void setQrCodeLayoutContent() {
        setDeviceId();
        setIdAddress();
        setQrCodeImage();

    }

    private void setDeviceId() {
        myDeviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceIdText.setText(getResources().getString(R.string.my_id) + " " + myDeviceId);
    }

    private void setIdAddress() {
        try {

            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            myIpAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()) + ":" + Server.PORT;
        } catch (Exception e) {
            Toast.makeText(this, R.string.permission_access_network_state_is_required, Toast.LENGTH_LONG).show();
        }

        ipAddressText.setText(getResources().getString(R.string.my_ip) + " " + myIpAddress);
    }

    private void setQrCodeImage() {
        QrCode qrCode = new QrCode( new Contact(myDeviceId, myIpAddress).toJson());
        qrCodeImage.setImageBitmap(qrCode.getImage());
    }

    private void showQrLayout() {
        setQrCodeLayoutContent();
        hideAllLayouts();
        qrCodeLayout.setVisibility(View.VISIBLE);
    }

    private void hideAllLayouts() {
        qrCodeLayout.setVisibility(View.GONE);
        filesRecyclerView.setVisibility(View.GONE);
    }

    private void showFilesLayout() {
        hideAllLayouts();
        FilesViewHandler filesView = new FilesViewHandler(this);
        filesView.setFilesRecyclerViewContent();
        filesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void openNfcSendActivity() {
        Intent intentNfcSend = new Intent(MainActivity.this, NfcSendActivity.class);
        String msgNfcToSend = new Contact("deviceTest", "192.188.0.1").toJson();    //TODO replace With the contact that need to be transfer
        intentNfcSend.putExtra("EXTRA_NFC_CONTACT_TO_SEND", msgNfcToSend);
        startActivity(intentNfcSend);
    }

    private void openNfcReceiveActivity() {
        Intent intentNfcReceive = new Intent(MainActivity.this, NfcReceiveActivity.class);
        startActivity(intentNfcReceive);
    }

    private void openContactsActivity() {
        Intent intentContacts = new Intent(MainActivity.this, ContactsActivity.class);
        startActivity(intentContacts);
    }

}
