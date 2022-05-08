/*
 * MIT License
 *
 * Copyright (c) 2022 Negative
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package games.negative.framework.util;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * HasteBin Utility
 *
 * @author Fals3
 * <p>
 * The ability to post text to HasteBin.
 * <p>
 * *CREDIT TO FALS3*
 */

public class HasteBin {
    String binURL = "https://bin.hypews.com/";
    boolean useSSL = true;

    /**
     * Create a new HasteBin instance with your custom HasteBin (optional)
     *
     * @param binURL The exact URL to your pastebin - for example https://bin.seailz.com/
     * @author Seailz
     */
    public HasteBin(@NotNull String binURL, boolean SSL) {
        if (!binURL.endsWith("/")) binURL = binURL + "/";
        if (!binURL.contains("https://")) binURL = "https://" + binURL;

        this.binURL = binURL;
        useSSL = SSL;
    }

    public HasteBin() {
    }

    public String post(@NotNull String text, boolean raw) throws IOException {
        byte[] postData = text.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        URL url = new URL(binURL + "documents");
        if (!useSSL) disableSslVerification();
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", "Hastebin Java Api");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);
        String response = null;

        try {
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = reader.readLine();
        } catch (IOException var11) {
            var11.printStackTrace();
        }

        assert response != null;

        if (response.contains("\"key\"")) {
            response = response.substring(response.indexOf(":") + 2, response.length() - 2);
            String postURL = raw ? binURL + "raw/" : binURL;
            response = postURL + response;
        }

        return response;
    }

    /**
     * disable SSL
     * @author WolfgangFahl
     */
    private void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs,
                                                       String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs,
                                                       String authType) {
                        }
                    }};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
}