import com.opencsv.CSVReader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class LinechartGraphStockData extends JPanel {

    private static final long serialVersionUID = 1L;
    private ChartPanel chartPanel;

    public LinechartGraphStockData(String stockCode) throws IOException {
        LinechartGraphStockDataGen(stockCode);
    }

    public static double[] CalculateMA5(String stockCode) {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader("data/stock_" + stockCode + ".csv"));
            String[] nextLine;
            reader.readNext();
            Stack<Double> stack = new Stack<>();
            while ((nextLine = reader.readNext()) != null) {
                if (Double.parseDouble(nextLine[6]) == 0) {
                    continue;
                }
                stack.push(Double.parseDouble(nextLine[6]));
            }
            int size = stack.size();
            double[] ma5 = new double[size];
            double[] pool = new double[5];
            for (int i = 0; i < size; i++) {
                System.arraycopy(pool, 0, pool, 1, 4);
                pool[0] = stack.pop();
                double sum = 0;
                int zeros = 0;
                for (int j = 0; j < 5; j++) {
                    sum = sum + pool[j];
                    if (pool[j] == 0) {
                        zeros++;
                    }
                }
                if (zeros != 5) {
                    ma5[size - i - 1] = sum / (5 - zeros);
                } else {
                    ma5[size - i - 1] = ma5[size - i - 2];
                }
            }
            return ma5;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public double[] CalculateMA20(String stockCode) throws IOException {
        CSVReader reader;
        reader = new CSVReader(new FileReader("data/stock_" + stockCode + ".csv"));
        String[] nextLine;
        reader.readNext();
        reader.readNext();
        Stack<Double> stack = new Stack<>();
        while ((nextLine = reader.readNext()) != null) {
            if (Double.parseDouble(nextLine[6]) == 0) {
                continue;
            }
            stack.push(Double.parseDouble(nextLine[6]));
        }
        int size = stack.size();
        double[] ma20 = new double[size];
        double[] pool = new double[20];
        for (int i = 0; i < size; i++) {
            System.arraycopy(pool, 0, pool, 1, 19);
            pool[0] = stack.pop();
            double sum = 0;
            int zeros = 0;
            for (int j = 0; j < 20; j++) {
                sum = sum + pool[j];
                if (pool[j] == 0) {
                    zeros++;
                }
            }
            if (zeros != 20) {
                ma20[size - i - 1] = sum / (20 - zeros);
            } else {
                ma20[size - i - 1] = ma20[size - i - 2];
            }
        }
        return ma20;
    }

    public double[] CalculateMA30(String stockCode) throws IOException {
        CSVReader reader;
        reader = new CSVReader(new FileReader("data/stock_" + stockCode + ".csv"));
        String[] nextLine;
        reader.readNext();
        reader.readNext();
        Stack<Double> stack = new Stack<>();
        while ((nextLine = reader.readNext()) != null) {
            if (Double.parseDouble(nextLine[6]) == 0) {
                continue;
            }
            stack.push(Double.parseDouble(nextLine[6]));
        }
        int size = stack.size();
        double[] ma30 = new double[size];
        double[] pool = new double[30];
        for (int i = 0; i < size; i++) {
            System.arraycopy(pool, 0, pool, 1, 29);
            pool[0] = stack.pop();
            double sum = 0;
            int zeros = 0;
            for (int j = 0; j < 30; j++) {
                sum = sum + pool[j];
                if (pool[j] == 0) {
                    zeros++;
                }
            }
            if (zeros != 30) {
                ma30[size - i - 1] = sum / (30 - zeros);
            } else {
                ma30[size - i - 1] = ma30[size - i - 2];
            }
        }
        return ma30;
    }

    public void LinechartGraphStockDataGen(String stockCode) throws IOException {
        double[] ma5 = CalculateMA5(stockCode);
        double[] ma20 = CalculateMA20(stockCode);
        double[] ma30 = CalculateMA30(stockCode);
        CSVReader reader_ma;
        TimeSeries timeseries5 = new TimeSeries("MA5", org.jfree.data.time.Day.class);
        TimeSeries timeseries20 = new TimeSeries("MA20", org.jfree.data.time.Day.class);
        TimeSeries timeseries30 = new TimeSeries("MA30", org.jfree.data.time.Day.class);
        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
        reader_ma = new CSVReader(new FileReader("data/stock_" + stockCode + ".csv"));
        String[] nextLine;
        reader_ma.readNext();

        for (int i = 0; i < ma20.length; i++) {
            nextLine = reader_ma.readNext();
            String[] date = nextLine[0].split("-");
            assert ma5 != null;
            timeseries5.add(new Day(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0])), ma5[i+1]);
            timeseries20.add(new Day(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0])), ma20[i]);
            timeseries30.add(new Day(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0])), ma30[i]);
        }

        timeseriescollection.addSeries(timeseries5);
        timeseriescollection.addSeries(timeseries20);
        timeseriescollection.addSeries(timeseries30);
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(stockCode + " MA", "", "", timeseriescollection, true, true, true);
        chartPanel = new ChartPanel(jfreechart, true);
        //chartPanel.setPreferredSize(new java.awt.Dimension(500, 400));
        add(chartPanel);
    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    public void setGraphicSize(int width, int height){
        chartPanel.setPreferredSize(new Dimension(width, height));
    }
}

