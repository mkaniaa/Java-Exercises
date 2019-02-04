package bankocr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileManager {
	
	private static ArrayList<Number> allPatternsOfNumbers;
	File fileWithPath;
	
	public FileManager(String filePath) {
		fileWithPath = new File(filePath);
	}
	
	public void runCheck() {
		try {
			allPatternsOfNumbers = fileToNumbers(fileWithPath);
			reloadFile(allPatternsOfNumbers);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//funkcja wczytujaca plik i tworzaca tablice obiektow Number (tyle obiektow ile jest ich w pliku zrodlowym)
	public static  ArrayList<Number> fileToNumbers(File filePath) throws FileNotFoundException {
		ArrayList<Number> arrayForAll = new ArrayList<Number>();
		FileReader readList = new FileReader(filePath);
		Scanner number = new Scanner(readList);
		
		//sprawdzanie pliku linijka po linijce, dopoki ma on zawartosc
		while((number.hasNext())){
			//przygotowanie tablicy pod wczytywanie wzoru cyfry
			String[] numberPattern = new String[9];
			//jezeli jest to czwarty obrot petli, to jest ona przerywana 
			for(int i = 1; i < 5; i++) {
				String nextLine = number.nextLine();

				char[] lineInArray = nextLine.toCharArray();
				int index = 0;
				int countLoop = 0;
				
				//petla przechodzi przez cala linie i kazdy znak dodaje do Stringa o wskazanym indeksie tablicy
				//indeks zwieksza sie co 3 element linii
				for(char c : lineInArray) {
					countLoop++;
					//sprawdzenie czy dany string nie jest nullem
					if (numberPattern[index] != null) numberPattern[index] = numberPattern[index] + c;
					else numberPattern[index] = Character.toString(c);
					if (countLoop%3==0) {
							index++;
					}
				}
			}
			//nowo utworzony numer, przed dodaniem do listy musi zostac sprawdzony pod wzgledem prawidlowosci
			Number nextNumber = validObjectNumber(new Number(numberPattern));
			arrayForAll.add(nextNumber);
		}
		number.close();
		return arrayForAll;
	}
	
	//funckja sprawdzajaca poprawnosc numeru i wprowadzajaca poprawki
	public static Number validObjectNumber (Number objectNumber) {
		//jezeli dany numer jest nieprawidlowy (ERR) lub niepelny (ILL) to zostanie stworzony obiekt warstwy pochodnej
		//odpowiadajacej za naprawianie zepsutych numerow
		if((!objectNumber.getIsValid()) || objectNumber.getIsIll()) {
			//stworzenie obiektu klasy ValidNumber wymaga podania orginalnego wzoru
			ValidNumbers correctedNumber = new ValidNumbers(objectNumber.getPattern());
			//metoda ustawia wg. statusu z poszukiwan, jaki numer ustawic dla objektu Number
			if(correctedNumber.getStatus().equals("ILL")) objectNumber.setActualNumber("ILL - 0 options to fix number!");
			else if (correctedNumber.getStatus().equals("AMB")) objectNumber.setActualNumber("AMB " + correctedNumber.getNumberOfOptions() + " options to fix number!");
			else objectNumber.setActualNumber(correctedNumber.getFirstValidNumber());
		}
		return objectNumber;
	}
	
	//funkcja tworzaca nowy plik z przekonwertowanymi i przefiltrowanymi numerami kont
	public static void reloadFile(ArrayList<Number> numbers) throws IOException {
		FileWriter list = new FileWriter("reloaded_input.txt", false);
		BufferedWriter writer = new BufferedWriter(list);
		for(Number nextNumber : numbers) {
			String nextLine = nextNumber.getActualNumber();
			System.out.println(nextLine);
			writer.write(nextLine);
			writer.newLine();
		}
		writer.close();
	}
}

