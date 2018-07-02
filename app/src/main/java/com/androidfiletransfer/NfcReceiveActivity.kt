package com.androidfiletransfer

import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import android.app.PendingIntent
import android.content.IntentFilter
import android.content.Intent
import android.widget.Toast
import android.nfc.tech.NfcF
import android.nfc.NdefRecord
import android.nfc.NdefMessage
import android.nfc.Tag
import java.util.*
import kotlin.experimental.and


//source: https://www.developer.com/ws/android/nfc-programming-in-android.html
class NfcReceiveActivity : AppCompatActivity() {

    private var nfcAdapter : NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private var intentFilters: Array<IntentFilter>? = null
    private var mNFCTechLists: Array<Array<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_receive)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        pendingIntent = PendingIntent.getActivity(this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)

        val ndefIntent = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            ndefIntent.addDataType("*/*")
            intentFilters = arrayOf(ndefIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "messageNFC Exception", Toast.LENGTH_LONG).show()
        }

        mNFCTechLists = arrayOf(arrayOf(NfcF::class.java.name))

    }

    public override fun onNewIntent(intent: Intent) {
        val action = intent.action
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)

        var s = ""

        // parse through all NDEF messages and their records and pick text type only
        val data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        if (data != null) {
            try {
                for (i in data.indices) {
                    val recs = (data[i] as NdefMessage).records
                    for (j in recs.indices) {
                        if (recs[j].tnf == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(recs[j].type, NdefRecord.RTD_TEXT)) {
                            val payload = recs[j].payload

                            val langCodeLen = payload[0] and 63

                            s += String(payload, langCodeLen + 1, payload.size - langCodeLen - 1, TextEncoding().getCharset(payload[0]))
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, "messageNFC Exception", Toast.LENGTH_LONG).show()
            }

        }

        Toast.makeText(this, "The NFC message received is: $s", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFilters, mNFCTechLists)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

}
