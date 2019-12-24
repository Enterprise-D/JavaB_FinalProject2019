import com.opencsv.CSVReader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;

public class BubbleChartGraphStockData extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] stockCode;
    private String beginDate;
    private String endDate;
    JPanel jpanel;

    public BubbleChartGraphStockData(String[] stockCode, String beginDate, String endDate) throws IOException {
        this.stockCode = stockCode;
        this.beginDate = beginDate;
        this.endDate = endDate;
        jpanel = createDemoPanel();
        //jpanel.setPreferredSize(new Dimension(500, 400));
        add(jpanel);
    }

    private static JFreeChart createChart(XYZDataset xyzdataset) {
        JFreeChart jfreechart = ChartFactory.createBubbleChart(
                "reward vs risk",
                "risk(%)",
                "reward(%)",
                xyzdataset,
                PlotOrientation.HORIZONTAL,
                true, true, false);

        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        xyplot.setForegroundAlpha(0.65F);
        XYItemRenderer xyitemrenderer = xyplot.getRenderer();
        xyitemrenderer.setSeriesPaint(0, Color.blue);
        NumberAxis numberaxis = (NumberAxis) xyplot.getDomainAxis();
        numberaxis.setLowerMargin(0.2);
        numberaxis.setUpperMargin(0.5);
        NumberAxis numberaxis1 = (NumberAxis) xyplot.getRangeAxis();
        numberaxis1.setLowerMargin(0.8);
        numberaxis1.setUpperMargin(0.9);
        return jfreechart;
    }

    public XYZDataset createDataset() throws IOException {
        double[] amount = new double[stockCode.length];
        for (int i = 0; i < stockCode.length; i++) {
            @SuppressWarnings("resource")
			CSVReader reader = new CSVReader(new FileReader("data/stock_" + stockCode[i] + ".csv"));
            reader.readNext();
            amount[i] = Double.parseDouble(reader.readNext()[7]);

        }

        CalculateStockStatistics stockData = new CalculateStockStatistics(stockCode, beginDate, endDate);
        DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset();
        double[] ad = stockData.getV();
        double[] ad1 = stockData.getM();
        String[] str = stockCode;
        //System.out.print(getRadiusFactor(ad,ad1));
        while(getArrayMaxAndMin(amount)[0]>getRadiusFactor(ad,ad1)/10){
            for(int i = 0;i<amount.length;i++){
                amount[i] = amount[i]/2;
               // System.out.println(amount[i]);
            }
        }
        for (int i = 0; i < str.length; i++) {
            defaultxyzdataset.addSeries(str[i], new double[][]{{ad[i]}, {ad1[i]}, {amount[i]}});
        }
        return defaultxyzdataset;
    }

    public JPanel createDemoPanel() throws IOException {
        JFreeChart jfreechart = createChart(createDataset());
        return new ChartPanel(jfreechart);
    }

    public void setGraphicSize(int width, int height) {
        jpanel.setPreferredSize(new Dimension(width, height));
    }

    public static double getRadiusFactor(double[] x, double[] y) {
        double[] xi = getArrayMaxAndMin(x);
        double[] yi = getArrayMaxAndMin(y);
        return Math.max(xi[0] - xi[1], yi[0] - yi[1]);
    }

    public static double[] getArrayMaxAndMin(double[] arr) {
        double max = arr[0];
        double min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            max = Math.max(max, arr[i]);
            min = Math.min(min, arr[i]);
        }
        return new double[]{max, min};
    }
}
