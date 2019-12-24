import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;


	public class MainExecute extends JFrame{
	    /**
		 * 
		 */

		private static final long serialVersionUID = 1L;	      
		public MainExecute() {
        JFrame menuFrame = new JFrame("Menu -Stock Price History Query");
        JButton klc = new JButton("K-Line Chart");
        JButton lc = new JButton("Line Chart");
        JButton bc = new JButton("Bubble Chart");
        klc.setFont(new Font("StockAnalysis",Font.BOLD,50));
        lc.setFont(new Font("StockAnalysis",Font.BOLD,50));
        bc.setFont(new Font("StockAnalysis",Font.BOLD,50));
        Dimension preferredSize = new Dimension(350,100);
        klc.setPreferredSize(preferredSize );
        lc.setPreferredSize(preferredSize );
        bc.setPreferredSize(preferredSize );
        Color buttoncolor = new Color(1,119,215);
        klc.setForeground(Color.white);
        bc.setForeground(Color.white);
        lc.setForeground(Color.white);
        klc.setBackground(buttoncolor);
        lc.setBackground(buttoncolor);
        bc.setBackground(buttoncolor);

        klc.addActionListener(actionEvent -> ShowControl(0, true));
        lc.addActionListener(actionEvent -> ShowControl(1, true));
        bc.addActionListener(actionEvent -> ShowControl(2, false));
        
        klc.setSize(45,20);
        lc.setSize(45,20);
        bc.setSize(45,20);
        JPanel panelContainer = new JPanel();
        panelContainer.add(klc);
		klc.setLayout(new GridLayout(1, 3));
        panelContainer.add(lc);
		lc.setLayout(new GridLayout(1, 3));
        panelContainer.add(bc);
		bc.setLayout(new GridLayout(1, 3));
        menuFrame.add(panelContainer);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(20,55);
        menuFrame.setBounds(0, 0, 370, 360);
        menuFrame.setResizable(false);
        menuFrame.setVisible(true);
		pack();
		setLocationRelativeTo(null);
	}

	public void ShowControl(int queryId, boolean isSingle) {
        String str = "";
        switch (queryId) {
            case 0:
                str = "K-Line Chart";
                break;
            case 1:
                str = "Line Chart";
                break;
            case 2:
                str = "Bubble Chart";
                break;
            default:
                break;
        }
        JFrame queryFrame = new JFrame("Control -" + str);
        JPanel panelContainer = new JPanel();
        JButton queryButton = new JButton("query");
        queryButton.setFont(new Font("query",Font.BOLD,20));
        Dimension preferredSize = new Dimension(150,30);
        queryButton.setPreferredSize(preferredSize);
        Color buttoncolor = new Color(1,119,215);
        queryButton.setBackground(buttoncolor);
        queryButton.setForeground(Color.white);
        JTextField codeInput = new JTextField(20);
        JTextField BDInput = new JTextField(20);
        JTextField EDInput = new JTextField(20);
        if (isSingle) {
        	JLabel l1 = new JLabel("Stock Code:");
        	l1.setFont(new Font("Segoe UI", Font.BOLD, 20));
//            Color buttoncolor1 = new Color(1,119,215);
//            l1.setForeground(buttoncolor1);
            panelContainer.add(l1);
        } else {
        	JLabel l1 = new JLabel("Stock Codes:");
        	l1.setFont(new Font("Segoe UI", Font.BOLD, 20));
//            Color buttoncolor1 = new Color(1,119,215);
//            l1.setForeground(buttoncolor1);
            panelContainer.add(l1);
        }
        panelContainer.add(codeInput);
    	JLabel l2 = new JLabel("Begin Date (yyyy-mm-dd):");
    	l2.setFont(new Font("Segoe UI", Font.BOLD, 20));
//        Color buttoncolor2 = new Color(1,119,215);
//        l2.setForeground(buttoncolor2);
        panelContainer.add(l2);
        panelContainer.add(BDInput);
    	JLabel l3 = new JLabel("End Date (yyyy-mm-dd):");
    	l3.setFont(new Font("Segoe UI", Font.BOLD, 20));
//        Color buttoncolor3 = new Color(1,119,215);
//        l3.setForeground(buttoncolor3);
        panelContainer.add(l3);
        panelContainer.add(EDInput);
        panelContainer.add(queryButton);
        queryFrame.add(panelContainer);
        queryFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        queryFrame.setBounds(0, 0, 300, 250);
        queryFrame.setResizable(false);
        queryFrame.setVisible(true);
		setBackground(Color.black);

        queryButton.addActionListener(actionEvent -> {
            try {
                JFrame.getFrames()[JFrame.getFrames().length - 1].hide();
                String[] stockCode = codeInput.getText().split(";");
                switch (queryId) {
                    case 0:
                        ShowKLineChart(stockCode[0], BDInput.getText(), EDInput.getText());
                        break;
                    case 1:
                        ShowLinechart(stockCode[0], BDInput.getText(), EDInput.getText());
                        break;
                    case 2:
                        ShowBubbleChart(stockCode, BDInput.getText(), EDInput.getText());
                        break;
                    default:
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void ShowKLineChart(String stockCode, String beginDate, String endDate) throws IOException {
        JFrame resultFrame = new JFrame("Display");
        new GetStockData_A(new String[]{stockCode}, beginDate, endDate);
        KLineChartStockData graphB = new KLineChartStockData(stockCode, beginDate, endDate);
        resultFrame.add(graphB);
        resultFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        resultFrame.setSize(650, 500);
        graphB.setGraphicSize((int) resultFrame.getSize().getWidth()-100, (int) resultFrame.getSize().getHeight()-50);
        JFrame.setDefaultLookAndFeelDecorated(true);
        resultFrame.setResizable(false);
        resultFrame.setVisible(true);
    }

    public static void ShowLinechart(String stockCode, String beginDate, String endDate) throws IOException {
        JFrame resultFrame = new JFrame("Display");
        new GetStockData_A(new String[]{stockCode}, beginDate, endDate);
        LinechartGraphStockData graphC = new LinechartGraphStockData(stockCode);
        resultFrame.add(graphC);
        resultFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        resultFrame.setSize(650, 700);
        graphC.setGraphicSize((int) resultFrame.getSize().getWidth() - 20, (int) resultFrame.getSize().getHeight() - 60);
        JFrame.setDefaultLookAndFeelDecorated(true);
        resultFrame.setResizable(false);
        resultFrame.setVisible(true);
    }

    public static void ShowBubbleChart(String[] stockCode, String beginDate, String endDate) throws IOException {
        JFrame resultFrame = new JFrame("Display");
        new GetStockData_A(stockCode, beginDate, endDate);
        BubbleChartGraphStockData graphD = new BubbleChartGraphStockData(stockCode, beginDate, endDate);
        resultFrame.add(graphD);
        resultFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        resultFrame.setSize(650, 600);
        resultFrame.setResizable(true);
        graphD.setGraphicSize((int) resultFrame.getSize().getWidth() - 20, (int) resultFrame.getSize().getHeight() - 60);
        JFrame.setDefaultLookAndFeelDecorated(true);
        resultFrame.setResizable(false);
        resultFrame.setVisible(true);
    }
}