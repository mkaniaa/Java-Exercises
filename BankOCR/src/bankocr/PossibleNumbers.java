package bankocr;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//zadaniem tej klasy jest skompletowanie wszystkich mozliwych opcji dla wzorku, ktory otrzymal status ILL lub ERR
public class PossibleNumbers extends Number{
	
	List<String> possibleNumbers = new CopyOnWriteArrayList<String>();
	
	public PossibleNumbers(String[] orginalPattern) {
		
		super(orginalPattern);
		possibleNumbers = getCapabilitiesFromPattern(orginalPattern);
		
	}

	//funkcja sprawdza wartosc kazdego wzoru liczby z podanej tablicy wzorow, wyszukujac wszystkie mozliwosci, 
	//jakie mozna z niego uzyskac zwraca liste zawierajaca wszystkie mozliwe opcje dla danej tablicy wzorow
	public List<String> getCapabilitiesFromPattern(String[] numbPatterns) {
		List<String> allOptions = new CopyOnWriteArrayList<String>();
		for(String s : numbPatterns) {
			boolean orginalPatternCorrect = false;
			//jezeli sprawdzana liczba jest nieprawidlowa, sprawdzanie jest przerywane i tworzony jest obiekt ILLNumber i ustawiana zmienna isILL na true;
			String nextNumber = "";
			if(parseNumberElement(s) != -1) {
				//jezeli sprawdzanie pierwotnego znaku sie powiedzie, zostaje to zapamietane w zmiennej firstCheckCorrect
				orginalPatternCorrect = true;
				nextNumber = Integer.toString(parseNumberElement(s));
				
				//sprawdzenie, czy jest to pierwsza, czy kolejna pozycja na liscie opcji
				if (allOptions.size() != 0) {
					//jezeli jest to kolejna opcja, to znaleziony znak musi byc dopisany na koniec kazdej z pozycji
					for (int i = 0; i<allOptions.size(); i++) {
						String numb = allOptions.get(i) + nextNumber;
						allOptions.set(i, numb);
					}
				}
				//jezeli jest to pierwsza pozycja, to wystarczy ja dodac do listy opcji
				else allOptions.add(nextNumber);
			}
			//jezeli findCorrect znalazlo nowa opcje (inna niz pierwotny znak), to ja pobierz w celu wprowadzenia do tablicy numerow
			if(!Integer.toString(parseNumberElement(findCorrect(s))).equals(nextNumber)) {
				nextNumber = Integer.toString(parseNumberElement(findCorrect(s)));
				
				//jezeli sprawdzenie pierwotnego wzoru sie powiodlo, to znaczy ze zostaly wprowadzone nowe opcje do listy
				//dlatego nalezy je wszystkie skopiowac i ponownie wpisac do tablicy opcji, lecz ze zmienionym ostatnim znakiem
				if(orginalPatternCorrect) {
					//ta petla odpowiada za kopiowanie wszystkich istniejacych numerow bez ostatniego znaku
					for (String numb : allOptions) {
						char[] charsInNumb = numb.toCharArray();
						String newNumb = "";
						for (int j = 0; j<charsInNumb.length-1; j++) {
							newNumb += charsInNumb[j];
						}
						//dodanie do listy numerow nowej opcji z podmienionym, znalezionym przez findCorrect nowym znakiem
						allOptions.add(newNumb + nextNumber);
					}
				}
				//jezeli sprawdzanie pierwotnego wzoru sie nie powiodlo (-1), to istniejacych w liscie opcji pozycji
				//nie trzeba kopiowac, wystarczy dopisac nowo znaleziony znak na koniec kazdego z numeru
				else {
					//sprawdzenie, czy jest to pierwsza, czy kolejna pozycja na liscie opcji
					if (allOptions.size() != 0) {
						//jezeli jest to kolejna opcja, to znaleziony znak musi byc dopisany na koniec kazdej z pozycji
						for (int i = 0; i<allOptions.size(); i++) {
							String numb = allOptions.get(i) + nextNumber;
							allOptions.set(i, numb);
						}
					}
					
					//jezeli jest to pierwsza pozycja, to wystarczy ja dodac do listy opcji
					else allOptions.add(nextNumber);
				}

			}
		}
		return allOptions;
	}
	
	//metoda szukajaca alternatywnego wzoru dla podanego orgyginalu
	public static String findCorrect(String orginalNumberPattern) {
		String correctedPattern = orginalNumberPattern;
		String posiblePattern = "";
		
		//uruchomienie metody findOption dla kazdego znaku w stringu
		for (int i = 0; i<correctedPattern.length(); i++) {
			posiblePattern = findOption(orginalNumberPattern, i);
			//jezeli metoda dokona jakiejs zmiany, poszukiwania zostana zakonczone
			if(!posiblePattern.equals(correctedPattern)) {
				correctedPattern = posiblePattern;
				break;
			}
		}
		return correctedPattern;
	}
	
	//Funkcja sprawdza, czy istnieje mozliwosc podstawienia innego znaku pod danym indexem w podanym wzorze liczby.
	public static String findOption(String patternToCheck, int index) {
		String correctedPattern = patternToCheck;
		String posiblePattern = "";
		String c = Character.toString(patternToCheck.charAt(index));
		
		switch(index) {
		
		case 0:
			//na pozycji 0 moze byc wylacznie spacja
			if(!c.equals(" ")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String(" "), index);
				break;
			}
		
		case 1:
			//na pozycji 1 czasami moze byc spacja (1,4) a czasami _ (0,2,3,5,6,7,8,9) 
			//dlatego w obu przypadkach funkcja podejme probe zmiany znaku
			if(!c.equals("_")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String("_"), index);
				break;
			}
			else if (!c.equals(" ")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String(" "), index);
				break;
			}
			
		case 2:
			//na pozycji 2 moze byc wylacznie spacja
			if(!c.equals(" ")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String(" "), index);
				break;
			}
			
		case 3:
			//na pozycji 3 czasami moze byc spacja (1,2,3,7) a czasami | (0,4,5,6,8,9) 
			//dlatego w obu przypadkach funkcja podejme probe zmiany znaku
			if(!c.equals("|")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String("|"), index);
				break;
			}
			else if (!c.equals(" ")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String(" "), index);
				break;
			}
			
		case 4:
			//na pozycji 4 czasami moze byc spacja (1,7) a czasami _ (0,2,3,4,5,6,8,9) 
			//dlatego w obu przypadkach funkcja podejme probe zmiany znaku
			if(!c.equals("_")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String("_"), index);
				break;
			}
			else if (!c.equals(" ")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String(" "), index);
				break;
			}
			
		case 5:
			//na pozycji 5 czasami moze byc spacja (5,6) a czasami | (0,1,2,3,4,7,8,9) 
			//dlatego w obu przypadkach funkcja podejme probe zmiany znaku
			if(!c.equals("|")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String("|"), index);
				break;
			}
			else if (!c.equals(" ")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String(" "), index);
				break;
			}
		case 6:
			//na pozycji 6 czasami moze byc spacja (1,4,5,7,3,9) a czasami | (0,2,6,8) 
			//dlatego w obu przypadkach funkcja podejme probe zmiany znaku
			if(!c.equals("|")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String("|"), index);
				break;
			}
			else if (!c.equals(" ")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String(" "), index);
				break;
			}
			
		case 7:
			//na pozycji 7 czasami moze byc spacja (1,4,7) a czasami _ (0,2,3,5,6,8,9) 
			//dlatego w obu przypadkach funkcja podejme probe zmiany znaku
			if(!c.equals("_")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String("_"), index);
				break;
			}
			else if (!c.equals(" ")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String(" "), index);
				break;
			}
			
		case 8:
			//na pozycji 8 czasami moze byc spacja (2) a czasami | (0,1,2,3,4,5,6,7,8,9) 
			//dlatego w obu przypadkach funkcja podejme probe zmiany znaku
			if(!c.equals("|")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String("|"), index);
				break;
			}
			else if (!c.equals(" ")) {
				posiblePattern = insertCorrectChar(correctedPattern, new String(" "), index);
				break;
			}
		}
		if (parseNumberElement(posiblePattern) != -1) {
			correctedPattern = posiblePattern;
		}
		return correctedPattern;
	}
	
	//funckja pobiera string ktory ma poprawic, zna jaki ma wstawic i index miesjca gdzie ma pojawic sie nowy znak
	public static String insertCorrectChar (String oldString, String newChar, int indexOfNewChar) {
		String newString = "";
		for(int i = 0; i<oldString.toCharArray().length; i++) {
			//przeiterowanie stringu, jezeli iteracja jest rowna z blednym znakiem, to funkcja go podmieni a w innym przypadku przepisze ten sam znak
			if (i == indexOfNewChar) {
				newString+=newChar;
			} else newString+=oldString.charAt(i);
		}
		return newString;
	}
	
	public List<String> getPossibleNumbers() {
		return possibleNumbers;
	}
}
