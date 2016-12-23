package com.qianhua.yxgsd.net.mzw;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class ReqTask extends AsyncTask<Void, Void, String>
{
    private static final String TAG = ReqTask.class.getSimpleName();
    
    private Delegate delegate = null;
    
    private String reqParams = null;
    
    private String reqUrl = null;
    
    public ReqTask(Delegate deg, String params, String url)
    {
        delegate = deg;
        reqParams = params;
        reqUrl = url;
    }
    
    @Override
    protected String doInBackground(Void... params)
    {
        String result = null;
        try
        {
            Thread.sleep(1000);
            result = "result";
            result = sendPost(reqUrl, reqParams);
            //result = NetTool.sendPostRequest(reqUrl, reqParams, "utf-8");
        }
        catch (Exception e)
        {
        }
        return result;
    }
    
    @Override
    protected void onPostExecute(String result)
    {
        delegate.execute(result);
    }
    
    public interface Delegate
    {
        public void execute(String result);
    }
    
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("���� POST ��������쳣��"+e);
            e.printStackTrace();
        }
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
    
}