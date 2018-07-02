package com.androidfiletransfer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.zxing.integration.android.IntentIntegrator.QR_CODE_TYPES;

public class MainActivity extends AppCompatActivity {
    private Contact contact;
    private TextView mTextMessage;

    private RelativeLayout qrCodeLayout;
    private TextView deviceIdText;
    private String myDeviceId;
    private TextView ipAddressText;
    private String myIpAddress;
    private ImageView qrCodeImage;

    SharedPreferences sharedPrefs;
    List<String> contacts;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    showQrLayout();
                    return true;
                case R.id.navigation_qr_scanner:
                    mTextMessage.setText(R.string.title_qr_scanner);

                    IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                    scanIntegrator.initiateScan(QR_CODE_TYPES);

                    hideQrLayout();
                    return true;
                case R.id.navigation_nfc_send:
                    mTextMessage.setText(R.string.title_nfc_send);
                    openNfcSendActivity();
                    hideQrLayout();
                    return true;
                case R.id.navigation_nfc_receive:
                    mTextMessage.setText(R.string.title_nfc_receive);
                    openNfcReceiveActivity();
                    hideQrLayout();
                    return true;
                case R.id.navigation_contacts:
                    mTextMessage.setText("This view should show the list of contacts with the ability to order them by different filter");
                    hideQrLayout();
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

                mTextMessage.setText(contact.toString());
            }
            catch (NullPointerException e){mTextMessage.setText(e.getMessage());}
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);

        qrCodeLayout = findViewById(R.id.qrCodeLayout);
        deviceIdText = findViewById(R.id.deviceIdText);
        ipAddressText = findViewById(R.id.ipAddressText);
        qrCodeImage = findViewById(R.id.qrCodeImage);
        setQrCodeLayoutContent();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        sharedPrefs = getSharedPreferences("Contacts", 0);
        contacts = new ArrayList<>();

        // Load contacts
        contacts = new Gson().fromJson(sharedPrefs.getString("Contacts", ""), List.class);
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
            myIpAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        } catch (Exception e) {
            Toast.makeText(this, R.string.permission_access_network_state_is_required, Toast.LENGTH_LONG).show();
        }

        ipAddressText.setText(getResources().getString(R.string.my_ip) + " " + myIpAddress);
    }

    private void setQrCodeImage() {
        QrCode qrCode = new QrCode( new Contact(myDeviceId, myIpAddress).toJson());
        qrCodeImage.setImageBitmap(qrCode.getImage());
    }

    private void hideQrLayout() {
        qrCodeLayout.setVisibility(View.GONE);
    }

    private void showQrLayout() {
        qrCodeLayout.setVisibility(View.VISIBLE);
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

}
