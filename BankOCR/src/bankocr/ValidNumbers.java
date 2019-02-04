package bankocr;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//zadaniem tej klasy jest wybranie z listy mozliwosci tylko te numery, ktore spelniaja wszystkie postawione warunki
public class ValidNumbers extends Number{
	
	private List<String> posibleNumbers = new CopyOnWriteArrayList<String>();
	private ArrayList<String> validNumbers = new ArrayList<String>();
	private int numberOfOptions;
	private String status = "";
	
	public ValidNumbers(String[] orginalPattern) {
		
		super(orginalPattern);
		
		//tworzenie obiektu klasy PossibleNumbers w celu pozyskania listy mozliwosci dla danego wzorka
		PossibleNumbers listOfCapabilities = new PossibleNumbers(orginalPattern);
		posibleNumbers = listOfCapabilities.getPossibleNumbers();
		validNumbers = selectValidNumbers(posibleNumbers);
		numberOfOptions = validNumbers.size();
		status = setStatus(numberOfOptions);
	}
	
	//funkcja sprawdzaj¹ca kazdy numer z listy mozliwych i wybierajaca te spelniajace postawione wymagania
	public ArrayList<String> selectValidNumbers(List<String> listOfNumbers) {
		ArrayList<String> listOfValidNumbers = new ArrayList<String>();
		for(String nextNumber : listOfNumbers) {
			if(validNum(nextNumber)) {
				listOfValidNumbers.add(nextNumber);
			}
		}
		
		return listOfValidNumbers;
	}
	
	//funkcja ustawiajaca ostateczny status dla danego wzoru = zdrowy (blank), ILL i AMB (zgodnie z zalozeniami zadania)
	public String setStatus(int numberOfOptions) {
		String newStatus = "";
		
		if(numberOfOptions == 0) newStatus = "ILL";
		else if(numberOfOptions > 1) newStatus = "AMB";
		
		return newStatus;
	}
	
	public ArrayList<String> getValidNumbers(){
		return validNumbers;
	}
	
	public String getStatus(){
		return status;
	}
	
	public int getNumberOfOptions(){
		return numberOfOptions;
	}
	
	public String getFirstValidNumber() {
		return validNumbers.get(0);
	}
}
