package com.androidfiletransfer;

import java.nio.charset.Charset;

public class TextEncoding {

    public Charset getCharset(byte payload) {
        if((payload & 0200) == 0){
            return Charset.forName("UTF-8");
        }
        else {
            return Charset.forName("UTF-16");
        }
    }
}
