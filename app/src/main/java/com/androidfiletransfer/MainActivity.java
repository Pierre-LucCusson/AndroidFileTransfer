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

import com.androidfiletransfer.contacts.Contact;
import com.androidfiletransfer.contacts.Contacts;
import com.androidfiletransfer.contacts.ContactsActivity;
import com.androidfiletransfer.contacts.ContactsViewHandler;
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
    private RelativeLayout nfcLayout;
    private RelativeLayout contactsLayout;
    private RelativeLayout filesLayout;

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

    private ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            serverService = ((ServerService.MyBinder) binder).getService();
            server = serverService.getServer();
        }

        public void onServiceDisconnected(ComponentName className) {
            serverService = null;
            server = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (serverService == null) {
            doBindService();
        }

        setContentView(R.layout.activity_main);

        qrCodeLayout = findViewById(R.id.qrCodeLayout);
        deviceIdText = findViewById(R.id.deviceIdText);
        ipAddressText = findViewById(R.id.ipAddressText);
        qrCodeImage = findViewById(R.id.qrCodeImage);

        nfcLayout = findViewById(R.id.nfcLayout);
        initNfcButtonsListener();

        contactsLayout = findViewById(R.id.contactsLayout);
        filesLayout = findViewById(R.id.filesLayout);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent in) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, in);
        if (scanningResult != null) {
            try {
                String contents = in.getStringExtra("SCAN_RESULT");
                Contact.fromJson(contents).save(this);
            }
            catch (NullPointerException e) {

            }
        }
    }

    private void doBindService() {
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
                    openScanQrCodeActivity();
                    return true;
                case R.id.navigation_nfc:
                    showNfcLayout();
                    return true;
                case R.id.navigation_contacts:
                    showContactsLayout();
                    return true;
                case R.id.navigation_files:
                    showFilesLayout();
                    return true;
            }
            return false;
        }
    };

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

    private void openScanQrCodeActivity() {
        hideAllLayouts();
        IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
        scanIntegrator.setDesiredBarcodeFormats(Arrays.asList(BarcodeFormat.QR_CODE.toString()));
        scanIntegrator.setCameraId(0);
        scanIntegrator.setBeepEnabled(true);
        scanIntegrator.setBarcodeImageEnabled(false);
        scanIntegrator.initiateScan();
    }

    private void initNfcButtonsListener() {
        findViewById(R.id.nfcSendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNfcSendActivity();
            }
        });

        findViewById(R.id.nfcReceiveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNfcReceiveActivity();
            }
        });

        findViewById(R.id.contactsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContactsActivity();
            }
        });
    }

    private void showNfcLayout() {
        hideAllLayouts();
        nfcLayout.setVisibility(View.VISIBLE);
    }

    private void showContactsLayout() {
        hideAllLayouts();
        ContactsViewHandler contactsView = new ContactsViewHandler(this);
        contactsLayout.setVisibility(View.VISIBLE);
    }

    private void showFilesLayout() {
        hideAllLayouts();
        FilesViewHandler filesView = new FilesViewHandler(this);
        filesView.setFilesRecyclerViewContent();
        filesLayout.setVisibility(View.VISIBLE);
    }

    private void hideAllLayouts() {
        qrCodeLayout.setVisibility(View.GONE);
        nfcLayout.setVisibility(View.GONE);
        contactsLayout.setVisibility(View.GONE);
        filesLayout.setVisibility(View.GONE);
    }

    private void openNfcSendActivity() {
        Intent intentNfcSend = new Intent(MainActivity.this, NfcSendActivity.class);
        String msgNfcToSend = new Contacts(this).toJson();
        intentNfcSend.putExtra("EXTRA_NFC_CONTACT_TO_SEND", msgNfcToSend);
        startActivity(intentNfcSend);
    }

    private void openNfcReceiveActivity() {
        Intent intentNfcReceive = new Intent(MainActivity.this, NfcReceiveActivity.class);
        startActivity(intentNfcReceive);
    }

    private void openContactsActivity() {
        Intent intentContacts = new Intent(MainActivity.this, ContactsActivity.class);
        intentContacts.putExtra("EXTRA_CONTACTS", new Contacts().toJson());
        startActivity(intentContacts);
    }

}
