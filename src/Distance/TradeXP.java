package Distance;

import java.math.BigDecimal;

public class TradeXP {

	static long[] xp = {0, // lvl 1 = index 1, etc.
			5,
			6,
			12,
			46,
			161,
			472,
			1181,
			2626,
			5319,
			10005, // 10
			17721,
			29865,
			48273,
			75300,
			113911,
			167777,
			241381,
			340127,
			470464,
			640005, // 20
			857666,
			1133804,
			1480364,
			1911035,
			2441411,
			3089163,
			3874210,
			4818908,
			5948238,
			7290005, // 30
			8875042,
			10737423,
			12914685,
			15448049,
			18382661,
			21767828,
			25657269,
			30109369,
			35187443,
			40960005, // 40
			47501047,
			54890322,
			63213635,
			72563144,
			83037661,
			94742974,
			118571374,
			158997683,
			207619316,
			415238632, // 50
			830477264,
			1245715896,
			1868573844,
			2802860766L,
			8408582298L,
			21021455745L,
			52553639363L,
			105107000000L,
			210215000000L,
			630644000000L // 60
			// might continue like chart says, but abruptly stopping at 1 trillion seems sus
	};
	
	static double z = 1.22208189670694,
		h = -47.05716562147332,
		j = 46.88591405171439,
		k = 0.9901676148899481,
		l = -0.09682583138409093,
		m = 0.6923205321694111,
		n = 0.9096266869471985,
		a = 5345.379054785288,
		b = -5251.501392798289,
		r = 0.9989411263513263,
		c = 0.5304535027111724;
	
	//https://docs.google.com/spreadsheets/d/16TQuUrWq-BCmodJ6cXM7DuJv54b0obiqfVJ3TErhldo/edit#gid=54673483
	public static double getXP(String start, String end, int L) {
		double D = Equations.HorizontalDistance(start, end) / 100;
		return z*(h+j*Math.pow(k,L)+l*L+m*Math.pow(L,n))*(a+b*Math.pow(r,D)+c*D);
	}
	
	public static BigDecimal xpDiff(double start, double end) {
		int startLvl = (int)start, endLvl = (int)end;
		
		double levelSpan = end - start;
		
		if(startLvl < endLvl) {
			BigDecimal finalXP = new BigDecimal(xp[startLvl]);
			finalXP = finalXP.multiply(new BigDecimal(1 - start + startLvl));
			
			for(int i = startLvl + 1; i < endLvl; i++) {
				BigDecimal bd2 = new BigDecimal(xp[i]);
				finalXP = finalXP.add(bd2);
			}
			
			BigDecimal bd3 = new BigDecimal(xp[endLvl]);
			return finalXP.add(bd3.multiply(new BigDecimal(end - endLvl)));
		}
		BigDecimal bd = new BigDecimal(xp[startLvl]);
		return bd.multiply(new BigDecimal(levelSpan));
	}
	
	public static void main(String[] args) {
		//System.out.println(getXP("Velia","Heidel",54));
		System.out.println(xpDiff(14.3615,	14.4134));
	}
}
