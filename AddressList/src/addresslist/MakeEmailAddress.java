package addresslist;
import java.util.Scanner;

public class MakeEmailAddress {

	public static void main(String[] args) {
		System.out.println("Enter the name and surname of the employee:");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String name = scanner.next();
		String surname = scanner.next();
		AddressList newAdress = new AddressList();
		newAdress.run(name, surname);
	}

}
