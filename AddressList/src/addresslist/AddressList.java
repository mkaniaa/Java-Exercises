package addresslist;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AddressList {

	private static boolean newFile = false;
	
	public void run(String name, String surname) {
		try {
			makeFile();
			Employee newEmployee = new Employee(name, surname);
			addToList(newEmployee);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void makeFile() throws IOException {
		File adressList = new File("adressList.txt");
		if((!adressList.exists() && !adressList.isDirectory())) {
			adressList.createNewFile();
			newFile = true;
			System.out.println("Program made the new adressList.txt file.");
		}
	}
	
	public void addToList (Employee employee) throws IOException {
		String newEmail = employee.getEmail();
		FileWriter list = new FileWriter("adressList.txt", true);
		BufferedWriter writer = new BufferedWriter(list);
		if(!newFile) {
			writer.newLine();
		}
		writer.write(newEmail);
		writer.close();
		System.out.println("The adress " + newEmail + " has been added to the list.");
	}
}
