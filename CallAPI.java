package com.api;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class CallAPI {
    private final static int CONNECTION_TIME_OUT = 20 * 1000;
    // TODO: Change your service URL.
    private static final String strURL = "url";
    private static final String TAG = "CallAPI";


    private static String getResponseText(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        if (is != null) {
            int line;
            try {
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.read()) != -1) {
                    sb.append((char) line);
                }
            } catch (Exception e) {
                StringBuilder error = new StringBuilder();
                error.append("Message = ").append(e.getMessage()).append("Cause = ").append(e.getCause());
                sb = error;
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        StringBuilder error = new StringBuilder();
                        error.append("Message = ").append(e.getMessage()).append("Cause = ").append(e.getCause());
                        sb = error;
                    }
                }
            }
        }
        return sb.toString();
    }

    public static void callGetAsync(final String methodName, final String token, final ResultCallBack resultCallBack) {

        new AsyncTask<Void, Void, APIResponse>() {
            @Override
            protected APIResponse doInBackground(Void... params) {
                APIResponse apiResponse = new APIResponse(0, "");
                try {
                    URL url = new URL(strURL + methodName);
                    Log.d(TAG, "doInBackground: URL: " + url.toString());
//                    URL url = new URL(strURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(CONNECTION_TIME_OUT);
                    if (token != null)
                        urlConnection.setRequestProperty("authorization", "Basic " + token);   //token string
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Content-Language", "en-US");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestMethod("GET");
                    int responseCode = urlConnection.getResponseCode();
                    String response;
                    if (responseCode == 200) {
                        response = getResponseText(urlConnection.getInputStream());
                    } else {
                        response = getResponseText(urlConnection.getErrorStream());
                        resultCallBack.onFailure(responseCode, response);
                    }
                    urlConnection.disconnect();
                    apiResponse = new APIResponse(responseCode, response);
                    return apiResponse;
                } catch (SocketTimeoutException e) {
                    return apiResponse;
                } catch (Exception e) {
                    e.printStackTrace();
                    return apiResponse;
                }
            }

            @Override
            protected void onPostExecute(APIResponse result) {
//                try {
                if (result.getResponseCode() == 200) {
                    resultCallBack.onSuccess(result.getResponseCode(), result.getResponseMessage());
                } else {
                    resultCallBack.onFailure(result.getResponseCode(), result.getResponseMessage());
                }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    resultCallBack.onFailure(result.getResponseCode(), result.getResponseMessage());
//                }
            }
        }.execute();
    }

    public static void callPostAsync(final String methodName, final String rawData, final String token, final ResultCallBack resultCallBack) {

        new AsyncTask<Void, Void, APIResponse>() {
            @Override
            protected APIResponse doInBackground(Void... params) {
                APIResponse apiResponse = new APIResponse(0, "");
                try {
                    URL url = new URL(strURL + methodName);
                    Log.d(TAG, "doInBackground: URL: " + url.toString());
//                    URL url = new URL(strURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(CONNECTION_TIME_OUT);
                    if (token != null)
                        urlConnection.setRequestProperty("authorization", "Basic " + token);   //token string
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Content-Language", "en-US");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestMethod("POST");

                    OutputStream outputStream = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    writer.write(rawData);
                    writer.flush();
                    writer.close();
                    outputStream.close();

                    int responseCode = urlConnection.getResponseCode();
                    String response;
                    if (responseCode == 200) {
                        response = getResponseText(urlConnection.getInputStream());
                    } else {
                        response = getResponseText(urlConnection.getErrorStream());
                        resultCallBack.onFailure(responseCode, response);
                    }
                    urlConnection.disconnect();
                    apiResponse = new APIResponse(responseCode, response);
                    return apiResponse;
                } catch (java.net.SocketTimeoutException e) {
                    return apiResponse;
                } catch (Exception e) {
                    e.printStackTrace();
                    return apiResponse;
                }
            }

            @Override
            protected void onPostExecute(APIResponse result) {
                try {
                    if (result.getResponseCode() == 200) {
                        resultCallBack.onSuccess(result.getResponseCode(), result.getResponseMessage());
                    } else {
                        resultCallBack.onFailure(result.getResponseCode(), result.getResponseMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resultCallBack.onFailure(result.getResponseCode(), result.getResponseMessage());
                }
            }
        }.execute();
    }

    public interface ResultCallBack {
        void onSuccess(int responseCode, String strResponse);

        void onFailure(int responseCode, String strResponse);
    }
}