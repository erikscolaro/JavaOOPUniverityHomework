package mountainhuts;

import java.util.Map;

public class main {
    public static void main(String[] args) {
        Region r = Region.fromFile("Piemonte", "data/mountain_huts.csv");
		r.setAltitudeRanges("0-1000", "1000-2000", "2000-3000");
        Map<String, Long> res3 = r.countMountainHutsPerAltitudeRange();

        res3.forEach((k,v)->System.out.println(k+"\t"+v));
    }
}
