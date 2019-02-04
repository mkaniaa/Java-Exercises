package bankocr;

import java.util.ArrayList;
import java.util.Arrays;

public class Number {

	private String[] pattern;
	private String actualNumber = "";
	private boolean isValid = true;
	private boolean isIll = false;

	
	public Number(String[] pattern) {
		this.pattern = pattern;
		actualNumber = getNumberFromPattern(pattern);
	}
	
	public String getNumberFromPattern(String[] numbPattern) {
		String numberInDigits = "";
		for(String s : numbPattern) {
			if(parseNumberElement(s) == -1) {
				numberInDigits += "?";
				isIll = true;
			} else numberInDigits += parseNumberElement(s);
		} 
		return numberInDigits;
}
	
	//funkcja zmieniajaca wzor na pojedyncza cyfre
	public static int parseNumberElement(String numberPattern) {
		//tablica zawierajaca wzory na kazda z liczb, gdzie index ma ta sama wartosc co liczba jaka wskazuje wzor
		ArrayList<String> patterns = new ArrayList<String>(Arrays.asList(" _ | ||_|   ", "     |  |   ", " _  _||_    ", " _  _| _|   ",
																		"   |_|  |   ", " _ |_  _|   "," _ |_ |_|   ", " _   |  |   ",
																		" _ |_||_|   ", " _ |_| _|   "));
		
		//sprawdzenie, czy wprowadzony symbol jest prawidlowy (musi istniec w tablicy wzorow)
		if (patterns.contains(numberPattern)){
			for(String s : patterns) {
				if (numberPattern.equals(s))  {
					return patterns.indexOf(s);
				}
			}
		}
		//jezeli symbol danej liczby jest nieprawidlowy to funckja zwroci -1
		return -1;
	}
	
	//funckja walidujaca numer wg. wzoru podanego w zadaniu
	public static boolean validNum(String number) {
		
		int sum = 0;
		char[] numberArray = number.toCharArray();
		for(int i = 0; i<numberArray.length; i++) {
			int digit = numberArray[i];
			sum += (digit * (i+1));
		}
		
		if(sum%11 == 0) return true;
		return false;
	}
	
	public void setActualNumber(String newActualNumber){
		actualNumber = newActualNumber;
	}
	
	public String[] getPattern() {
		return pattern;
	}
	
	public String getActualNumber() {
		return actualNumber;
	}
	
	public boolean getIsValid() {
		return isValid;
	}
	public boolean getIsIll() {
		return isIll;
	}
}

