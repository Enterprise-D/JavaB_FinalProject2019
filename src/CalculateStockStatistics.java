import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.Stack;

class CalculateStockStatistics {
    double[] v;
    double[] m;

    public CalculateStockStatistics(String[] stock_code, String begin_date, String end_date) {
        v = new double[stock_code.length];
        m = new double[stock_code.length];
        for (int i = 0; i < stock_code.length; i++) {
            v[i] = 100 * varianceGen(StockVolatilityGen(stock_code[i]));
            m[i] = 100 * StockVolatilityGen(stock_code[i])[0];
        }
    }

    public static double[] StockVolatilityGen(String stockCode) {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader("data/stock_" + stockCode + ".csv"));
            String[] nextLine;
            reader.readNext();
            nextLine = reader.readNext();
            double pn = Double.parseDouble(nextLine[6]);
            double sv;
            Stack<Double> svStack = new Stack<>();
            while ((nextLine = reader.readNext()) != null) {
                double pnm1 = Double.parseDouble(nextLine[6]);
                if (pnm1 == 0 || pn == 0) {
                    continue;
                }
                sv = Math.log(pn / pnm1);
                svStack.push(sv);
                pn = pnm1;
            }
            int size = svStack.size();
            double[] returnArray = new double[size + 1];
            int i = 0;
            double svSum = 0;
            while (!svStack.empty()) {
                double svTemp = svStack.pop();
                returnArray[i + 1] = svTemp;
                svSum = svSum + svTemp;
                i++;
            }
            returnArray[0] = svSum / i;
            return returnArray;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double varianceGen(double[] inputArray) {
        double sum = 0;
        for (int i = 1; i < inputArray.length; i++) {
            sum = sum + Math.abs((inputArray[i] - inputArray[0]) * (inputArray[i] - inputArray[0]));
        }
        return Math.sqrt(sum / (inputArray.length - 1));
    }

    public double[] getV() {
        return v;
    }

    public double[] getM() {
        return m;
    }
}
