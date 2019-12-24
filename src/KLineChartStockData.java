
import com.opencsv.CSVReader;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import assss.publicboolean;

class KLineChartStockData extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ChartPanel chartPanel;

    public KLineChartStockData(String stockCode, String beginDate, String endDate) throws IOException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        double highValue = Double.MIN_VALUE;//  Set the maximum value of K data
        double minValue = Double.MAX_VALUE;// Set the minimum value of  K data
        double high2Value = Double.MIN_VALUE;// Set the maximum value of volume
        double min2Value = Double.MAX_VALUE;// set the minimum value of volume
        CSVReader reader;
        CSVReader reader2;
        // Stock K-line chart
        // High-opening and low-receiving data series,
        //the four data of the stock K-line chart, in turn, open, high, low, close
        OHLCSeries series = new OHLCSeries("");
        reader = new CSVReader(new FileReader("data/stock_" + stockCode + ".csv"));
        String[] nextLine;
        reader.readNext();
        while ((nextLine = reader.readNext()) != null) {
            String[] date = nextLine[0].split("-");
            if (Double.parseDouble(nextLine[7]) != 0) {
                series.add(new Day(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0])),
                        Double.parseDouble(nextLine[3]), Double.parseDouble(nextLine[4]), Double.parseDouble(nextLine[5]), Double.parseDouble(nextLine[6]));
            }
        }
        final OHLCSeriesCollection seriesCollection = new OHLCSeriesCollection();
        seriesCollection.addSeries(series);
        TimeSeries series2 = new TimeSeries("");// Corresponding volume data
        reader2 = new CSVReader(new FileReader("data/stock_" + stockCode + ".csv"));
        reader2.readNext();
        while ((nextLine = reader2.readNext()) != null) {
            String[] date = nextLine[0].split("-");
            if (Double.parseDouble(nextLine[7]) != 0) {
                series2.add(new Day(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0])), Double.parseDouble(nextLine[7]) / 100);
            }
        }
        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        timeSeriesCollection.addSeries(series2);

        // Get the highest and lowest values ????of the K-line data
        int seriesCount = seriesCollection.getSeriesCount();
        for (int i = 0; i < seriesCount; i++) {
            int itemCount = seriesCollection.getItemCount(i);
            for (int j = 0; j < itemCount; j++) {
                if (highValue < seriesCollection.getHighValue(i, j)) {
                    highValue = seriesCollection.getHighValue(i, j);
                }
                if (minValue > seriesCollection.getLowValue(i, j)) {
                    minValue = seriesCollection.getLowValue(i, j);
                }
            }

        }
        // Get the highest and lowest values of volume data
        int seriesCount2 = timeSeriesCollection.getSeriesCount();
        for (int i = 0; i < seriesCount2; i++) {
            int itemCount = timeSeriesCollection.getItemCount(i);
            for (int j = 0; j < itemCount; j++) {
                if (high2Value < timeSeriesCollection.getYValue(i, j)) {
                    high2Value = timeSeriesCollection.getYValue(i, j);
                }
                if (min2Value > timeSeriesCollection.getYValue(i, j)) {
                    min2Value = timeSeriesCollection.getYValue(i, j);
                }
            }

        }
        final CandlestickRenderer candlestickRender = new CandlestickRenderer();
        candlestickRender.setUseOutlinePaint(true);
        //Set how to set the width of the K-line graph
        candlestickRender.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_AVERAGE);
        // Set the interval between each K-line diagram
        candlestickRender.setAutoWidthGap(0.001);
        // Set the K-line color of the stock rise
        candlestickRender.setUpPaint(Color.RED);
        // Set the K-line color of the stock fall
        candlestickRender.setDownPaint(Color.GREEN);
        //Set the x axis, which is the timeline
        DateAxis x1Axis = new DateAxis();
        x1Axis.setAutoRange(false);
        try {
            // Set the time range, pay attention to the maximum time is one day more than the existing maximum time
            x1Axis.setRange(dateFormat.parse(beginDate), dateFormat.parse(endDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Date> map = new HashMap<String, Date>();
        for (int index = 0; index < series.getItemCount(); index++) {
            RegularTimePeriod period = series.getPeriod(index);

            String ss = new SimpleDateFormat("yyyy-MM-dd").format(period.getStart());

            map.put(ss, period.getStart());
        }
        SegmentedTimeline timeline = new SegmentedTimeline(SegmentedTimeline.DAY_SEGMENT_SIZE, 5, 2) {
            // calendar.setTime(Datadata);
            public boolean containsDomainValue(Date date) {

                String Date = date.toString();

                System.out.println(date);

                String key = new SimpleDateFormat("yyyy-MM-dd").format(date);

                Date d = map.get(key);

                if (d == null) {

                    this.addException(date);


                }

                return super.containsDomainValue(date);


            }

        };

        timeline.setStartTime(SegmentedTimeline.FIRST_MONDAY_AFTER_1900);
        x1Axis.setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());
        x1Axis.setAutoTickUnitSelection(false);
        x1Axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        x1Axis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());
        x1Axis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 30));// Set the interval of the time scale, usually in weeks
        x1Axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));// Set the format of the display time
        NumberAxis y1Axis = new NumberAxis();
        //x1Axis.setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());
        y1Axis.setAutoRange(false);
        //// Set the range of y-axis values,
        //which is lower than the lowest value and larger than the maximum value, so the graphics look beautiful.
        //y1Axis.setRange(minValue * 0.9, highValue * 1.1);
        y1Axis.setRange(minValue * 0.9, highValue * 1.1);
        y1Axis.setTickUnit(new NumberTickUnit((highValue * 1.1 - minValue * 0.9) / 10));
        XYPlot plot1 = new XYPlot(seriesCollection, x1Axis, y1Axis, candlestickRender);
        //CombinedDomainXYPlot combineddomainxyplot =new CombinedDomainXYPlot(x1Axis);//???????????????????????x?????????
        //combineddomainxyplot.add(plot1);
        XYBarRenderer xyBarRender = new XYBarRenderer() {
            private static final long serialVersionUID = 1L;

            public Paint getItemPaint(int i, int j) {
                if (seriesCollection.getCloseValue(i, j) > seriesCollection.getOpenValue(i, j)) {
                    return candlestickRender.getUpPaint();
                } else {
                    return candlestickRender.getDownPaint();
                }
            }
        };
        xyBarRender.setMargin(0.1);// Set the spacing between histograms
        NumberAxis y2Axis = new NumberAxis();
        y2Axis.setAutoRange(true);
        //y2Axis.setRange(min2Value * 0.9, high2Value * 1.1);
        y2Axis.setTickUnit(new NumberTickUnit((high2Value * 1.1 - min2Value * 0.9) / 4));
        XYPlot plot2 = new XYPlot(timeSeriesCollection, null, y2Axis, xyBarRender);
        CombinedDomainXYPlot combineddomainxyplot = new CombinedDomainXYPlot(x1Axis);
        combineddomainxyplot.add(plot1, 2);// Add a graphic area object, the number behind it is to calculate how much area  should occupy. 2/3
        combineddomainxyplot.add(plot2, 1);

        combineddomainxyplot.setGap(10);//Set the space between two graphics areas
        JFreeChart chart = new JFreeChart(stockCode, JFreeChart.DEFAULT_TITLE_FONT, combineddomainxyplot, false);
        // Convert to panel
        chartPanel = new ChartPanel(chart);
        add(chartPanel);
    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    public void setGraphicSize(int width, int height){
        chartPanel.setPreferredSize(new Dimension(width, height));
    }
}