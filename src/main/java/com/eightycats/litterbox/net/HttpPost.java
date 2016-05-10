/**
 * Copyright 2016 Matthew A Jensen <eightycats@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.eightycats.litterbox.net;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Simple HTTP POST.
 */
public class HttpPost
{
    public static int post (String url, Map<String, String> params, StringBuilder response)
        throws IOException
    {
        // the request content
        StringBuffer data = new StringBuffer();
        try {
            boolean delimit = false;
            for (Entry<String, String> param : params.entrySet()) {
                if (delimit) {
                    data.append("&");
                }
                data.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8.name()));
                data.append("=");
                data.append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8.name()));
                delimit = true;
            }
        } catch (UnsupportedEncodingException ex) {
            // Why would UTF-8 not be supported?
            ex.printStackTrace();
        }

        URL urlObject = new URL(url);
        URLConnection conn = urlObject.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(data.toString());
        writer.flush();

        return HttpGet.readResponse(conn, response);
    }

    public static int post (String httpsUrl, Map<String, String> params, StringBuilder response,
        String keystorePath, String keystorePassword)
        throws IOException
    {
        System.setProperty("javax.net.ssl.trustStore", keystorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", keystorePassword);
        return post(httpsUrl, params, response);
    }

    public static void main (String[] args)
    {
        try {
            StringBuilder response = new StringBuilder();
            Map<String, String> params = new HashMap<String, String>();
            for (int i = 1; i < args.length; i++) {
                String[] keyValue = args[i].split("=");
                if (keyValue.length > 1) {
                    params.put(keyValue[0], keyValue[1]);
                } else {
                    params.put(args[i], "");
                }
            }
            int httpResponseCode = post(args[0], params, response);
            System.out.println("[" + httpResponseCode + "]");
            System.out.println(response);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
