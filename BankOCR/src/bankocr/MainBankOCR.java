package bankocr;

public class MainBankOCR {
	
	public static void main(String[] args) {
		String pathToFile = "input.txt";
		FileManager newChecking = new FileManager(pathToFile);
		newChecking.runCheck();
	}
}
