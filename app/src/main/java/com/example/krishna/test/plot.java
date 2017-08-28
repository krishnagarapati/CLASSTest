package com.example.krishna.test;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class plot extends AppCompatActivity implements SensorEventListener{
    SensorManager senSensorManager;
    Sensor senAccelerometer;
    SensorManager senSensorManager2,senSensorManager3,senSensorManager4;
    Sensor senLight,senProximity,senHumidity;
    private double last_x, last_y, last_z,last_l,last_p,last_h;
    public double Px,Py,Pz,pSgolay,sqrt_sg,Pl,PR,PH;

    private boolean started = false;
    public ArrayList sensorData;
    private LinearLayout layout;
    private View mChart;


    double t= System.currentTimeMillis();
    public TextView tvX,tvY,tvZ,LI,Pro,Hum;
    boolean enableRecord = true;
    ArrayList sensorDataX = new ArrayList();
    ArrayList sensorDataY = new ArrayList();
    ArrayList sensorDataZ = new ArrayList();

    ArrayList sensorDataSqrt= new ArrayList();



    public LineGraphSeries<DataPoint> seriesAccel, seriesLight,seriesProximity,seriesHumidity;
    GraphView graph_1,graph_2,graph_3,graph_4;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        graph_1 = (GraphView)findViewById(R.id.graph);
        graph_2 = (GraphView)findViewById(R.id.graph1);
        graph_3 = (GraphView)findViewById(R.id.graph2);
        graph_4 = (GraphView)findViewById(R.id.graph3);

        seriesAccel=new LineGraphSeries<DataPoint>();
        seriesAccel.setColor(Color.GREEN);

        seriesLight=new LineGraphSeries<DataPoint>();
        seriesLight.setColor(Color.RED);

        seriesProximity=new LineGraphSeries<DataPoint>();
        seriesProximity.setColor(Color.BLUE);


        seriesHumidity=new LineGraphSeries<DataPoint>();
        seriesHumidity.setColor(Color.CYAN);


        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager2 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senLight = senSensorManager2.getDefaultSensor(Sensor.TYPE_LIGHT);
        senSensorManager3=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        senProximity=senSensorManager3.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        senSensorManager4=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        senHumidity=senSensorManager4.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager2.registerListener(this, senLight , SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager3.registerListener(this,senProximity,SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager4.registerListener(this,senHumidity,SensorManager.SENSOR_DELAY_NORMAL);

    }

public void draw( int type)

    {
        int count;
        if(type == 1){
            graph_1.removeAllSeries();
            count = sensorDataSqrt.size();
            seriesAccel.resetData(new DataPoint[]{new DataPoint(0,0)});
            for (int i = 1; i < count; i++) {
                seriesAccel.appendData(new DataPoint(i, (double) sensorDataSqrt.get(i)), true, count, false);
            }
            graph_1.addSeries(seriesAccel);
        }

        if(type == 2){
            graph_2.removeAllSeries();
            count = sensorDataY.size();
            seriesProximity.resetData(new DataPoint[]{new DataPoint(0,0)});
            for (int i = 1; i < count; i++) {
                seriesProximity.appendData(new DataPoint(i, (double) sensorDataY.get(i)), true, count, false);
            }
            graph_2.addSeries(seriesProximity);
        }
        if(type ==3){
            graph_3.removeAllSeries();
            count = sensorDataX.size();
            seriesLight.resetData(new DataPoint[]{new DataPoint(0,0)});
            for (int i = 1; i < count; i++) {
                seriesLight.appendData(new DataPoint(i, (double) sensorDataX.get(i)), true, count, false);
            }
            graph_3.addSeries(seriesLight);
        }

        if(type == 4){
            graph_4.removeAllSeries();
            count = sensorDataZ.size();
            seriesHumidity.resetData(new DataPoint[]{new DataPoint(0,0)});
            for (int i = 1; i < count; i++) {
                seriesHumidity.appendData(new DataPoint(i, (double) sensorDataZ.get(i)), true, count, false);
            }
            graph_4.addSeries(seriesHumidity);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double x = sensorEvent.values[0];
            last_x=x;
            Px=(double)last_x;


            double y = sensorEvent.values[1];
            last_y=y;
            Py=(double)last_y;


            double z = sensorEvent.values[2];
            last_z=z;
            Pz=(double)last_z;
            double sq=((Px*Px)+(Py*Py)+(Pz*Pz));
            sqrt_sg=Math.sqrt(sq);



            sensorDataSqrt.add(sqrt_sg);
            draw(1);
            return;
        }

        if(mySensor.getType()==Sensor.TYPE_LIGHT){
            double l = sensorEvent.values[0];
            last_l=l;

            sensorDataX.add(last_l);
            draw(3);
            return;
        }


        if(mySensor.getType()==Sensor.TYPE_PROXIMITY){
            double P = sensorEvent.values[0];
            last_p=P;
            sensorDataY.add(last_p);

            draw(2);
            return;
        }


        if(mySensor.getType()==Sensor.TYPE_RELATIVE_HUMIDITY){
            double h = sensorEvent.values[0];
            last_h=h;
            sensorDataZ.add(last_h);

            draw(4);
            return;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
