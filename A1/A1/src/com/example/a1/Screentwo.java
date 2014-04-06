package com.example.a1;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;

public class Screentwo extends Activity 
 { 
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    Intent intent = createIntent();
    startActivity(intent);
}


 public Intent createIntent() 

 {

String[] titles = new String[] { "Barbara Buono", "Chris Christie" };
List<double[]> values = new ArrayList<double[]>();
values.add(new double[] { 29, 83, 47, 33 });
values.add(new double[] {});

int[] colors = new int[] {Color.BLUE,Color.YELLOW};

XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
renderer.setOrientation(Orientation.HORIZONTAL);

setChartSettings(renderer, "Twitter Analysis", " ", "Number of Responses", 0,200, 0,200, Color.WHITE, Color.WHITE);

renderer.setXLabels(1);
renderer.setYLabels(10);

renderer.addXTextLabel(10.75, "positive");
renderer.addXTextLabel(20.75, "negative");
renderer.addXTextLabel(30.75, "mixed");

int length = renderer.getSeriesRendererCount();
for (int i = 0; i < length; i++) 
{
  SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
  seriesRenderer.setDisplayChartValues(true);
  }

return ChartFactory.getBarChartIntent(this, buildBarDataset(titles, values), renderer,Type.DEFAULT);
}

protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) 
{

// creates a SeriesRenderer and initializes it with useful default values as well as colors

    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

    renderer.setAxisTitleTextSize(16);

    renderer.setChartTitleTextSize(20);

    renderer.setLabelsTextSize(15);

    renderer.setLegendTextSize(15);

    int length = colors.length;

    for (int i = 0; i < length; i++) 
    {
      SimpleSeriesRenderer r = new SimpleSeriesRenderer();
      r.setColor(colors[i]);
      renderer.addSeriesRenderer(r);
    }
    return renderer;
  }

   protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
   String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,int labelsColor) 
  {

  // sets lots of default values for this renderer

   renderer.setChartTitle(title);

   renderer.setXTitle(xTitle);
   renderer.setYTitle(yTitle);

   renderer.setXAxisMin(xMin);
   renderer.setXAxisMax(xMax);

   renderer.setYAxisMin(yMin);
   renderer.setYAxisMax(yMax);

   renderer.setAxesColor(axesColor);
   renderer.setLabelsColor(labelsColor);

   renderer.setApplyBackgroundColor(true);
   renderer.setBackgroundColor(Color.BLACK);

}

protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) 
{

    // adds the axis titles and values into the dataset

    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    int length = titles.length;

    for (int i = 0; i < length; i++) 
    {
      CategorySeries series = new CategorySeries(titles[i]);
      double[] v = values.get(i);
      int seriesLength = v.length;
      for (int k = 0; k < seriesLength; k++) 
      {
        series.add(v[k]);
      }

      dataset.addSeries(series.toXYSeries());
     }
    return dataset;
    }
  }