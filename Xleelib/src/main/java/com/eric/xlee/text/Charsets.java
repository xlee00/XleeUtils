/**
 * Charsets.java [V 1.0.0]
 * Classes : com.eric.xlee.text.Charsets
 * Xlee Create at 26/11/2015 18:09
 */
package com.eric.xlee.text;

import java.nio.charset.Charset;

/**
 * com.eric.xlee.text.Charsets
 *
 * @author Xlee <br/>
 *         Create at 26/11/2015 18:09
 */
public class Charsets {
    private static final String TAG = Charsets.class.getSimpleName();

    public static final String ASCII = "ASCII";
    public static final String US_ASCII = "US-ASCII";
    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String Unicode = "Unicode";
    public static final String BIG5 = "BIG5";
    public static final String UTF_8 = "UTF-8";
    public static final String UTF_16 = "UTF-16";
    public static final String GB2312 = "GB2312";
    public static final String GBK = "GBK";
    public static final String GB18030 = "GB18030";

    public static Charset getCharset(String charsetName) {
        return Charset.forName(charsetName);
    }
}
