package com.eightycats.litterbox.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Simple HTTP GET.
 */
public class HttpGet
{
    public static int get (String url, StringBuilder response)
        throws IOException
    {
        return readResponse(new URL(url).openConnection(), response);
    }

    static int readResponse (URLConnection conn, StringBuilder response)
        throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = in.readLine();
        StringWriter output = new StringWriter();
        PrintWriter out = new PrintWriter(output);
        while (line != null) {
            out.println(line);
            line = in.readLine();
        }
        in.close();
        response.append(output.getBuffer());
        // presumes the url is http
        return ((HttpURLConnection)conn).getResponseCode();
    }

    public static void main (String[] args)
    {
        try {
            StringBuilder response = new StringBuilder();
            int httpResponseCode = get(args[0], response);
            System.out.println("[" + httpResponseCode + "]");
            System.out.println(response);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
