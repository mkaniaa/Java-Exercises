package addresslist;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Employee {
	private String name;
	private String surname;
	private String email;

	public Employee (String name, String surname) {
		this.name = name;
		this.surname = surname;
		email = surname + "." + name + "@mex.com";
		try {
			changeIfExist(email);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void changeIfExist(String check_email) throws FileNotFoundException {
		int found = 0;
		while(findEmail(check_email)) {
			found++;
			check_email = surname + "." + name + found + "@mex.com";
		}
		if(found != 0) {
			System.out.println("The person with this name and surname already exist on the email list. Program made alternative adress.");
			email = check_email;
		}
	}
	
	public boolean findEmail(String find_email) throws FileNotFoundException {
		boolean ifFound = false;
		FileReader readList;
			readList = new FileReader("adressList.txt");
			@SuppressWarnings("resource")
			Scanner list = new Scanner(readList);
			while((list.hasNextLine())){
				String line = list.next();
				if(line.equals(find_email)) {
					ifFound = true;
					break;
				}
			}
		return ifFound;
	}
}
