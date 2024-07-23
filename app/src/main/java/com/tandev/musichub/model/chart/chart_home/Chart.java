package com.tandev.musichub.model.chart.chart_home;

import java.io.Serializable;
import java.util.ArrayList;

public class Chart implements Serializable {
    private ArrayList<TimeChart> times;
    private float minScore;
    private float maxScore;
}
