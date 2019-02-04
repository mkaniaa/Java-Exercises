The task of creating a list of email addresses:

You're a junior java developer in a company called Mex. Your first assignment is to write a simple program that'll generate email addresses for new employees. Email address should be built using following pattern: surname.name@mex.com.
In case of people with the same surname and name, there should be a number added to the name - ie. twain.mark@mex.com and twain.mark1@mex.com

It must be possible to provide a name and surname of new employee from keyboard in console. The program should print generated email address.

There're two approaches to hold information that can be used here:
- data about employees can be stored in the memory and will be gone once program is closed,
- data about employees can be stored in the text file.

My solution:
I chose the second approach - to store information in a text file. The program asks for the name of the new employee. If the addressList.txt file does not exist, it creates a new one and adds a new e-mail address. With each subsequent address, the program searches for the same and if found, it will add the number to the new address according to the pattern from the task.