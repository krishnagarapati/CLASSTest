package com.example.krishna.test;
import android.content.Context;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import android.widget.TextView;
import java.lang.Math;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;




public class MainActivity extends Activity implements SensorEventListener {

    SensorManager senSensorManager;
    Sensor senAccelerometer;
    SensorManager senSensorManager2,senSensorManager3,senSensorManager4;
    Sensor senLight,senProximity,senHumidity;
    private float last_x, last_y, last_z,last_l,last_p,last_h;
    public double Px,Py,Pz,pSgolay,sqrt_sg,Pl,PR,PH;
    private Button button, btnStop, btnPlot;
    private boolean started = false;
    public ArrayList sensorData;
    private LinearLayout layout;
    private View mChart;
    private LineGraphSeries<DataPoint> seriesAccel, seriesLight,seriesProximity,seriesHumidity;

    double t= System.currentTimeMillis();
    public TextView tvX,tvY,tvZ,LI,Pro,Hum;
    boolean enableRecord = true;
    ArrayList sensorDataX = new ArrayList();
    ArrayList sensorDataY = new ArrayList();
    ArrayList sensorDataZ = new ArrayList();

    ArrayList sensorDataSqrt= new ArrayList();


    GraphView graph;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager2 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senLight = senSensorManager2.getDefaultSensor(Sensor.TYPE_LIGHT);
        senSensorManager3=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        senProximity=senSensorManager3.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        senSensorManager4=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        senHumidity=senSensorManager4.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);



        tvX= (TextView)findViewById(R.id.x_axis);
        tvY= (TextView)findViewById(R.id.y_axis);
        tvZ= (TextView)findViewById(R.id.z_axis);
        LI = (TextView)findViewById(R.id.LightIntens);
        Pro=(TextView)findViewById(R.id.Proximity);
        Hum=(TextView)findViewById(R.id.Humidity);

        graph = (GraphView)findViewById(R.id.graph);

        seriesAccel=new LineGraphSeries<DataPoint>();
        seriesAccel.setColor(Color.GREEN);

        seriesLight=new LineGraphSeries<DataPoint>();
        seriesLight.setColor(Color.RED);

        seriesProximity=new LineGraphSeries<DataPoint>();
        seriesProximity.setColor(Color.BLUE);


        seriesHumidity=new LineGraphSeries<DataPoint>();
        seriesHumidity.setColor(Color.CYAN);

        button=(Button)findViewById(R.id.button);





    }

    /** Called when the activity is first created. */
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            last_x=x;
            Px=(double)last_x;


            float y = sensorEvent.values[1];
            last_y=y;
            Py=(double)last_y;


            float z = sensorEvent.values[2];
            last_z=z;
            Pz=(double)last_z;
            double sq=((Px*Px)+(Py*Py)+(Pz*Pz));
            sqrt_sg=Math.sqrt(sq);



            sensorDataSqrt.add(sqrt_sg);
        }
        senSensorManager2.registerListener(this, senLight , SensorManager.SENSOR_DELAY_NORMAL);
        if(mySensor.getType()==Sensor.TYPE_LIGHT){
            float l = sensorEvent.values[0];
            last_l=l;

            sensorDataX.add(last_l);


        }

        senSensorManager3.registerListener(this,senProximity,SensorManager.SENSOR_DELAY_NORMAL);

        if(mySensor.getType()==Sensor.TYPE_PROXIMITY){
            float P = sensorEvent.values[0];
            last_p=P;
            sensorDataY.add(last_p);


        }

        senSensorManager4.registerListener(this,senHumidity,SensorManager.SENSOR_DELAY_NORMAL);

        if(mySensor.getType()==Sensor.TYPE_RELATIVE_HUMIDITY){
            float h = sensorEvent.values[0];
            last_h=h;
            sensorDataZ.add(last_h);


        }



        tvX.setText(Float.toString(last_x));
        tvY.setText(Float.toString(last_y));
        tvZ.setText(Float.toString(last_z));
        LI.setText(Float.toString(last_l));
        Pro.setText(Float.toString(last_p));



    }

    public void plot(View view ){
        int count = sensorDataSqrt.size();
        for(int i = 0; i<count;i++) {
            //double xdata = (double)sensorDataX.get(i);
            seriesAccel.appendData(new DataPoint(i,(double)sensorDataSqrt.get(i)), true, count, false);
            seriesProximity.appendData(new DataPoint(i,(double)sensorDataY.get(i)),true,count,false);
            seriesLight.appendData(new DataPoint(i,(double)sensorDataX.get(i)),true,count,false);
            seriesHumidity.appendData(new DataPoint(i,(double)sensorDataZ.get(i)),true,count,false);
        }
        graph.addSeries(seriesAccel);
        graph.addSeries(seriesProximity);
        graph.addSeries(seriesLight);
        graph.addSeries(seriesHumidity);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }









    }










