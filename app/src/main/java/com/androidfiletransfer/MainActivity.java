package com.androidfiletransfer;

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

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private RelativeLayout qrCodeLayout;
    private TextView deviceIdText;
    private String myDeviceId;
    private TextView ipAddressText;
    private String myIpAddress;
    private ImageView qrCodeImage;

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
                    hideQrLayout();
                    return true;
                case R.id.navigation_nfc:
                    mTextMessage.setText(R.string.title_nfc);
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

}
