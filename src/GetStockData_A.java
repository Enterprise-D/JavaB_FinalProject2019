import com.opencsv.CSVWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

public class GetStockData_A {
    public GetStockData_A(String[] stock_code, String begin_date, String end_date) {
        begin_date = begin_date.replaceAll("-", "");
        end_date = end_date.replaceAll("-", "");
        for (String s : stock_code) {
            String urlString = "http://quotes.money.163.com/service/chddata.html?code=0" + s + "&start="
                    + begin_date
                    + "&end="
                    + end_date
                    + "&fields=TOPEN;HIGH;LOW;TCLOSE;VOTURNOVER";
            try {
                CSVWriter csvWriter = new CSVWriter(new FileWriter("data/stock_" + s + ".csv"), ',');
                URL url = new URL(urlString);
                byte[] b = new byte[256];
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                try (InputStream in = url.openStream()) {
                    int i;
                    while ((i = in.read(b)) != -1) {
                        bo.write(b, 0, i);
                    }
                    String result = bo.toString();

                    String[] stocks = result.split(";");
                    for (String stock : stocks) {
                        String[] feed = stock.split("\n");
                        csvWriter.writeNext(new String[]{"Date", "Code", "Name", "Open", "High", "Low", "Close", "Volume"});
                        for (int k = 1; k < feed.length; k++) {
                            String[] buffer = feed[k].trim().split(",");
                            buffer[1] = buffer[1].substring(1).concat(".ss");
                            csvWriter.writeAll(Collections.singleton(buffer));
                        }
                    }
                    bo.reset();
                    csvWriter.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

