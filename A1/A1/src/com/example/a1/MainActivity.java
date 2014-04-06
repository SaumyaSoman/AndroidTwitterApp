package com.example.a1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity {
	private RadioGroup rdg;
	private RadioButton option1;
	private RadioButton option2;
	private RadioButton option3;
	private boolean op2=false;
	private boolean op1=false;
	private boolean op3=false;
	private boolean finish=false;
	ArrayList<AnalyzedData> dataList=new ArrayList<AnalyzedData>();
	ArrayList<String> names=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rdg = (RadioGroup) findViewById(R.id.radioGroup1);
		option1 = (RadioButton) findViewById(R.id.radioButton2);
		option2 = (RadioButton) findViewById(R.id.RadioButton01);
		option3 = (RadioButton) findViewById(R.id.radioButton1);
		rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				if(i==R.id.radioButton2){

				}else if(i==R.id.RadioButton01){
					op1=true;
				}else if(i==R.id.radioButton1){
					op2=true;
				} else{
					op3=true;
				}
			}
		});

		final Button getServerData = (Button) findViewById(R.id.screen2); 
		getServerData.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// WebServer Request URL
				String serverURL="";


				if(op1){
					serverURL = "http://192.168.43.76:8080/TwitterDataAnalytics/AnalyzedDataService?searchId1=Cole%20Vosbury&searchId2=Tessanne%20Chin";

				}else if(op2){
					serverURL = "http://192.168.43.76:8080/TwitterDataAnalytics/AnalyzedDataService?searchId1=Barbara%20Buono&searchId2=Chris%20Christie";
				}else if(op3){
					serverURL = "http://192.168.43.76:8080/TwitterDataAnalytics/AnalyzedDataService?searchId1=Samsung&searchId2=Apple";
				}else{
					serverURL = "http://192.168.43.76:8080/TwitterDataAnalytics/AnalyzedDataService?searchId1=Nike&searchId2=Adidas";
				}


				// Use AsyncTask execute Method To Prevent ANR Problem
				new LongOperation().execute(serverURL);
				if(finish){
					startActivity(new Intent("android.intent.action.SCREENTWO"));
				}


			}
		});
	}
	// Class with extends AsyncTask class

	private class LongOperation  extends AsyncTask<String, Void, Void> {

		// Required initialization         
		private String Content;
		private String Error = null;
		private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);


		protected void onPreExecute() {
			// NOTE: You can call UI Element here.

			//Start Progress Dialog (Message)

			Dialog.setMessage("Please wait..");
			Dialog.show();


		}

		// Call after onPreExecute method
		protected Void doInBackground(String... urls) {

			HttpClient httpclient = new DefaultHttpClient();
			Log.d("url",urls[0]);
			HttpGet request = new HttpGet(urls[0]);
			ResponseHandler<String> handler = new BasicResponseHandler();
			try {
				Content = httpclient.execute(request, handler);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			httpclient.getConnectionManager().shutdown();
			Log.i("aaaa", Content);
			return null;

		}

		protected void onPostExecute(Void unused) {
			// NOTE: You can call UI Element here.

			// Close progress dialog
			Dialog.dismiss();

			if (Error != null) {
				Log.d("ERROR", Error);

				//    uiUpdate.setText("Output : "+Error);

			} else {
				Log.d("gt", "yayyyy");
				JSONObject jsonResponse;                       
				try {
					jsonResponse = new JSONObject(Content);

					/***** Returns the value mapped by name if it exists and is a JSONArray. ***/
					/*******  Returns null otherwise.  *******/
					JSONArray jsonMainNode = jsonResponse.optJSONArray("response");

					/*********** Process each JSON Node ************/

					int lengthJsonArr = jsonMainNode.length(); 

					for(int i=0; i < lengthJsonArr; i++)
					{
						/****** Get Object for each JSON node.***********/
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
						AnalyzedData data=new AnalyzedData();
						/******* Fetch node values **********/
						data.setPositive(Integer.parseInt(jsonChildNode.optString("positive")));
						data.setNegative(Integer.parseInt(jsonChildNode.optString("negative")));
						data.setMixed(Integer.parseInt(jsonChildNode.optString("mixed")));
						data.setName(jsonChildNode.optString("name"));
						data.setScore(jsonChildNode.optDouble("score"));
						Log.d("data",data.toString());
						names.add(jsonChildNode.optString("name"));
						dataList.add(data);
					}
					finish=true;
					Intent intent= createIntent();
					startActivity(intent);
				} catch (JSONException e) {

					e.printStackTrace();
				}


			}
		}

	}



	public Intent createIntent() 

	{
		XYMultipleSeriesRenderer renderer = getTruitonBarRenderer();
        myChartSettings(renderer);
		return ChartFactory.getBarChartIntent(this, getTruitonBarDataset(), renderer,Type.DEFAULT);
	}


    private XYMultipleSeriesDataset getTruitonBarDataset() {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        Random r = new Random();
        ArrayList<String> legendTitles = new ArrayList<String>();
        legendTitles.add(dataList.get(0).getName());
        legendTitles.add(dataList.get(1).getName());
        for (int i = 0; i < dataList.size(); i++) {
            CategorySeries series = new CategorySeries(legendTitles.get(i));
                series.add(dataList.get(i).getPositive());
                series.add(dataList.get(i).getNegative());
                series.add(dataList.get(i).getMixed());
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }
 
    public XYMultipleSeriesRenderer getTruitonBarRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setMargins(new int[] { 30, 40, 15, 0 });
        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        r.setColor(Color.BLUE);
        renderer.addSeriesRenderer(r);
        r = new SimpleSeriesRenderer();
        r.setColor(Color.RED);
        renderer.addSeriesRenderer(r);
        return renderer;
    }
 
    private void myChartSettings(XYMultipleSeriesRenderer renderer) {
        renderer.setChartTitle("Twitter Analysis");
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(10.5);
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(210);
        renderer.addXTextLabel(1, "positive");
        renderer.addXTextLabel(2, "negative");
        renderer.addXTextLabel(3, "mixed");
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setBarSpacing(0.5);
        renderer.setXTitle("");
        renderer.setYTitle("NUmber of Tweets");
        renderer.setShowGrid(true);
        renderer.setGridColor(Color.GRAY);
        renderer.setXLabels(0); // sets the number of integer labels to appear
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
