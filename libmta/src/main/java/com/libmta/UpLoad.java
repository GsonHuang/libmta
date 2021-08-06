package com.libmta;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Gson
 * @date 2021/8/2 16:01
 */
public class UpLoad {

    private static final String TAG = UpLoad.class.getName();

    public static UpLoad upLoad = new UpLoad();

    public static LinkedBlockingQueue<MtaEvent> mtaEvents = new LinkedBlockingQueue<>();

    public void up() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    MtaEvent mtaEvent = null;
                    try {
                        if ((mtaEvent = mtaEvents.take()) != null) {

                            uploadAction(mtaEvent);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    private void uploadAction(MtaEvent mtaEvent) {
        String actionContent = mtaEvent.getActionContent();
        if (actionContent==null){
            return;
        }
        Log.e(TAG, mtaEvent.getActionContent());
        HttpURLConnection httpURLConnection = null;
        OutputStream outputStream = null;
        try {
            URL url = new URL(mtaEvent.getBaseUrl()+mtaEvent.getApi());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(actionContent.getBytes());
            outputStream.flush();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
    }
}
