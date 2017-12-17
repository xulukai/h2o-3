package hex;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import static org.junit.Assert.*;

public class AUCBuilderTest {

  @Test
  public void testPerRow() throws Exception {
    AUC2.AUCBuilder ab = new AUC2.AUCBuilder(400);
    AUC2.AUCBuilder ab_old = new AUC2.AUCBuilder(400);
    ab_old._speed = false;

    long t = 0, t_old = 0;

    try (GZIPInputStream gz = new GZIPInputStream(AUCBuilderTest.class.getResourceAsStream("aucbuilder.csv.gz"))) {
      BufferedReader br = new BufferedReader(new InputStreamReader(gz));
      String line;
      int i = 0;
      while ((line = br.readLine()) != null) {
        String[] cols = line.split(",");
        double p1 = Double.parseDouble(cols[0]);
        int act = Integer.parseInt(cols[1]);
        long st = System.currentTimeMillis();
        ab.perRow(p1, act, 1.0);
        t += System.currentTimeMillis() - st;
        long st_old = System.currentTimeMillis();
        ab_old.perRow(p1, act, 1.0);
        t_old += System.currentTimeMillis() - st_old;

        for (int j = 0; j < 400; j++) {
          assertEquals("Error in ths, line: " + i, ab._ths[j], ab_old._ths[j], 0);
          assertEquals("Error in tps, line: " + i, ab._tps[j], ab_old._tps[j], 0);
          assertEquals("Error in tps, line: " + i, ab._fps[j], ab_old._fps[j], 0);
          assertEquals("Error in sqe, line: " + i, ab._sqe[j], ab_old._sqe[j], 0);
        }

        i++;
      }
    }

    System.out.println("Total time with speedup: " + t + "ms; orginal time: " + t_old + "ms.");
  }

}