package com.example.thecollectivediet.Us_Fragment_Components;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.thecollectivediet.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class UsFragment extends Fragment {
    private LineChart lineChart;
    private TextInputLayout stockTickerTextInputLayout;
    private RadioGroup periodRadioGroup, intervalRadioGroup;
    private CheckBox highCheckBox, lowCheckBox, closeCheckBox;
    private Spinner spinnerDatabaseItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_us, container, false);

        lineChart = v.findViewById(R.id.line_chart);

        stockTickerTextInputLayout = v.findViewById(R.id.activity_main_stockticker);
        periodRadioGroup = v.findViewById(R.id.activity_main_period_radiogroup);
        intervalRadioGroup = v.findViewById(R.id.activity_main_priceinterval);

        highCheckBox = v.findViewById(R.id.activity_main_high);
        lowCheckBox = v.findViewById(R.id.activity_main_low);
        closeCheckBox = v.findViewById(R.id.activity_main_close);

        spinnerDatabaseItems = v.findViewById(R.id.spinner_database);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext() ,R.array.database_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerDatabaseItems.setAdapter(adapter);

        ArrayList<Entry> lineList = new ArrayList<>();
        lineList.add(new Entry(10f, 100f));
        lineList.add(new Entry(20f, 200f));
        lineList.add(new Entry(30f, 300f));
        lineList.add(new Entry(40f, 400f));
        lineList.add(new Entry(50f, 500f));

        LineDataSet lineDataSet = new LineDataSet(lineList, "Count");
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setValueTextColor(Color.BLUE);
        lineDataSet.setValueTextSize(20f);

        configureLineChart();
        return v;
    }

    private void configureLineChart() {
        Description desc = new Description();
        desc.setText("Test Graph");
        desc.setTextSize(28);
        lineChart.setDescription(desc);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        //XAxis xAxis = lineChart.getXAxis();
    }
}