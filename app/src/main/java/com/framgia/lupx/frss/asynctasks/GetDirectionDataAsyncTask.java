package com.framgia.lupx.frss.asynctasks;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by FRAMGIA\pham.xuan.lu on 04/08/2015.
 */
public class GetDirectionDataAsyncTask extends AsyncTask<String, Void, String> {

    public interface OnExecuteCompleteListener {
        void onExecuted(String result);
    }

    private OnExecuteCompleteListener listener;

    public GetDirectionDataAsyncTask(OnExecuteCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String data = "";
        String api = params[0];
        InputStream is = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(api);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null)
                connection.disconnect();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        if (listener != null) {
            listener.onExecuted(s);
        }
    }

}