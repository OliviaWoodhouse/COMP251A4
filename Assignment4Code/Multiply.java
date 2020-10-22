import java.util.*;
import java.io.*;

public class Multiply{

    private static int randomInt(int size) {
        Random rand = new Random();
        int maxval = (1 << size) - 1;
        return rand.nextInt(maxval + 1);
    }
    
    public static int[] naive(int size, int x, int y) {

        // YOUR CODE GOES HERE  (Note: Change return statement)
    		int[] result = new int[2];
    		//base case:
        if (size==1) {
        		result[0] = x*y;
        		result[1] = 1*1;
        		return result;
        } else {
        		ArrayList<Integer> xexpanded = new ArrayList<Integer>();
        		ArrayList<Integer> yexpanded = new ArrayList<Integer>();
        		int temp = x;
        		while (temp>0) {
        			xexpanded.add(temp%10);
        			temp = temp/10;
        		}
        		temp = y;
        		while (temp>0) {
        			yexpanded.add(temp%10);
        			temp = temp/10;
        		}
        		while (xexpanded.size()!=size) {
        			xexpanded.add(0);
        		}
        		while (yexpanded.size()!=size) {
        			yexpanded.add(0);
        		}
        		int midpoint = 0;
        		int newSize = 0;
        		if (size%2!=0) {
        			newSize = size+1;
        			midpoint = size/2+1;
        			xexpanded.add(0);
        			yexpanded.add(0);
        		} else {
        			newSize = size;
        			midpoint = size/2;
        		}
        		int a = 0;
        		int b = 0;
        		int c = 0;
        		int d = 0;
        		int iterator = midpoint;
	    		for (int i=0;i<midpoint;i++) {
	    			a += xexpanded.get(iterator)*((int)Math.pow(10, i));
	    			b += xexpanded.get(i)*((int)Math.pow(10, i));
	    			c += yexpanded.get(iterator)*((int)Math.pow(10, i));
	    			d += yexpanded.get(i)*((int)Math.pow(10, i));
	    			iterator++;
	    		}
	    		int[] step1 = naive(midpoint,a,c);
	    		result[1] += step1[1];
	    		int[] step2 = naive(midpoint,b,d);
	    		result[1] += step2[1];
	    		int[] step3 = naive(midpoint,a,d);
	    		result[1] += step3[1];
	    		int[] step4 = naive(midpoint,b,c);
	    		result[1] += step4[1];
	    		int addStep = step3[0]+step4[0];
	    		result[0] = ((int)(step1[0]*Math.pow(10, newSize)))+step2[0]+((int)(addStep*Math.pow(10, midpoint)));
	    		result[1] += 3*size;
        }
        return result;
    }

    public static int[] karatsuba(int size, int x, int y) {
        
        // YOUR CODE GOES HERE  (Note: Change return statement)
    		int[] result = new int[2];
    		//base case:
        if (size==1) {
        		result[0] = x*y;
        		result[1] = 1;
        		return result;
        } else {
        		ArrayList<Integer> xexpanded = new ArrayList<Integer>();
        		ArrayList<Integer> yexpanded = new ArrayList<Integer>();
        		int temp = x;
        		while (temp>0) {
        			xexpanded.add(temp%10);
        			temp = temp/10;
        		}
        		temp = y;
        		while (temp>0) {
        			yexpanded.add(temp%10);
        			temp = temp/10;
        		}
        		while (xexpanded.size()!=size) {
        			xexpanded.add(0);
        		}
        		while (yexpanded.size()!=size) {
        			yexpanded.add(0);
        		}
        		int midpoint = 0;
        		int newSize = 0;
        		if (size%2!=0) {
        			newSize = size+1;
        			midpoint = size/2+1;
        			xexpanded.add(0);
        			yexpanded.add(0);
        		} else {
        			newSize = size;
        			midpoint = size/2;
        		}
        		int a = 0;
        		int b = 0;
        		int c = 0;
        		int d = 0;
        		int iterator = midpoint;
        		for (int i=0;i<midpoint;i++) {
        			a += xexpanded.get(iterator)*((int)Math.pow(10, i));
        			b += xexpanded.get(i)*((int)Math.pow(10, i));
        			c += yexpanded.get(iterator)*((int)Math.pow(10, i));
        			d += yexpanded.get(i)*((int)Math.pow(10, i));
        			iterator++;
        		}
        		int[] step1 = karatsuba(midpoint,a,c);
        		result[1] += step1[1];
        		int[] step2 = karatsuba(midpoint,b,d);
        		result[1] += step2[1];
        		boolean negative = false;
        		if (((a-b)<0&&(c-d)<0)||((a-b)>=0&&(c-d)>=0)) {
        			negative = true;
        		}
        		int[] step3 = karatsuba(midpoint,Math.abs(a-b),Math.abs(c-d));
        		if (negative==false) {
        			step3[0] = 0-step3[0];
        		}
        		result[1] += step3[1];
        		int addStep = step1[0]+step2[0]-step3[0];
        		result[0] = ((int)(step1[0]*Math.pow(10, newSize)))+step2[0]+((int)(addStep*Math.pow(10, midpoint)));
        		result[1] += 6*size;
        }
        return result;
        
    }
    
    public static void main(String[] args){

        try{
            int maxRound = 20;
            int maxIntBitSize = 16;
            for (int size=1; size<=maxIntBitSize; size++) {
                int sumOpNaive = 0;
                int sumOpKaratsuba = 0;
                for (int round=0; round<maxRound; round++) {
                    int x = randomInt(size);
                    int y = randomInt(size);
                    int[] resNaive = naive(size,x,y);
                    int[] resKaratsuba = karatsuba(size,x,y);
            
                    if (resNaive[0] != resKaratsuba[0]) {
                        throw new Exception("Return values do not match! (x=" + x + "; y=" + y + "; Naive=" + resNaive[0] + "; Karatsuba=" + resKaratsuba[0] + ")");
                    }
                    
                    if (resNaive[0] != (x*y)) {
                        int myproduct = x*y;
                        throw new Exception("Evaluation is wrong! (x=" + x + "; y=" + y + "; Your result=" + resNaive[0] + "; True value=" + myproduct + ")");
                    }
                    
                    sumOpNaive += resNaive[1];
                    sumOpKaratsuba += resKaratsuba[1];
                }
                int avgOpNaive = sumOpNaive / maxRound;
                int avgOpKaratsuba = sumOpKaratsuba / maxRound;
                System.out.println(size + "," + avgOpNaive + "," + avgOpKaratsuba);
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

   } 
}
