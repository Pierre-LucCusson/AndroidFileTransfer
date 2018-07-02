package com.androidfiletransfer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_nfc_send.*
import java.nio.charset.Charset
import java.util.*

//Source: https://www.developer.com/ws/android/nfc-programming-in-android.html
class NfcSendActivity : AppCompatActivity() {

    private var nfcAdapter : NfcAdapter? = null
    private var ndefMessage : NdefMessage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_send)
        setSupportActionBar(toolbar)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        val msgNfcToSend = intent.getStringExtra("EXTRA_NFC_CONTACT_TO_SEND")
        Toast.makeText(this, msgNfcToSend, Toast.LENGTH_LONG).show()

        ndefMessage = NdefMessage(
                arrayOf(getNdefRecord(msgNfcToSend, Locale.ENGLISH, true)))

//        nfcAdapter?.setNdefPushMessage(ndefMessage, this)
    }

    private fun getNdefRecord(ndefMessage: String, locale: Locale, encodeInUtf8: Boolean): NdefRecord {
        val langBytes = locale.getLanguage().toByteArray(Charset.forName("US-ASCII"))

        val utfEncoding = if (encodeInUtf8) Charset.forName("UTF-8") else Charset.forName("UTF-16")
        val textBytes = ndefMessage.toByteArray(utfEncoding)

        val utfBit = if (encodeInUtf8) 0 else 1 shl 7
        val status = (utfBit + langBytes.size).toChar()

        val data = ByteArray(1 + langBytes.size + textBytes.size)
        data[0] = status.toByte()
        System.arraycopy(langBytes, 0, data, 1, langBytes.size)
        System.arraycopy(textBytes, 0, data, 1 + langBytes.size, textBytes.size)

        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), data)
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundNdefPush(this, ndefMessage)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundNdefPush(this)
    }

}
