package A3;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//Some initial comments before starting:
//I USED TO open and close the same scanner, but 10 times in a single loop. I decided to instead create an array of scanners and reuse them in a
//second for loop once I have dealt with the Latex files working and made sure to overwrite the old backups, but
//didn't create an array for the printwriters because it is allowed for them to overwrite each other because of the "true" argument when
//using the printwriter constructors.
//Program does the following:
//1) Checks if the 10 Latex files exist by populating an array of scanners with an input file stream for each latex file.
//	if that doesn't work, throw exception.
//2) Goes out of loop to see if the json files and backups exist. If so, deletes backups and replaces them with the json file (old json becomes backup
//  json, older backups deleted)
//3) Goes back into a loop, using the scanners in the array to write each of the 3 files using the same 3 printwriters everytime (make sure not to delete
//   the part where I close them at the of the 2nd for loop [they will be reopened and closed by each interation of the for loop])
//A) I wrote comments at the end of the 2nd for loop for where I think your stuff should go.
//B) About what Hanna said (closing the scanner in the finally if it makes sense), I don't think it makes sense in our case because
//   a lot of stuff has to be done before closing the scanners, but I still put everything in finally to please you, master uwu

public class AuthorBibCreator {

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		System.out.println("Welcome to Bib Creator! Please enter the author name your are looking for: ");
		String aut = kb.next();
		Scanner[] sc= new Scanner[10];//Initializing Scanner outside loop
		kb.close();//kb unnecessary from here on out.
		
		for (int i=0; i<10; i++) {//For Loop #1: Loop for each Latex file from Latex1 to Latex10. Going through the files and finding articles with wanted author.
			try {
				sc[i] = new Scanner(new FileInputStream("Latex"+(i+1)+".bib"));
			}catch (FileNotFoundException e1){
				System.out.println("Could not open input file Latex"+(i+1)+".bib for reading.\r\n" + 
						"Please check if file exists! Program will terminate after closing any opened files.");
				sc[i].close(); //Closing just in case here, can't do it in a finally block because still need the scanner (nothing has been scanned yet)
				System.exit(0);
			}
		}//end of for loop #1
			System.out.println("All the files were found.");
			//CHECKING FOR FILES AND BACKUPS
			//Initializing all variables to null so that they exist outside of try-catch block.
			File oldIEEE=null;
			File oldACM=null;
			File oldNJ=null;
			File tempIEEE=null;
			File tempACM=null;
			File tempNJ=null;
			
			try {
				//Checking if files already exist. Check oldExists() method.
				oldExists(oldIEEE,"IEEE",aut);
				oldExists(oldACM,"ACM",aut);
				oldExists(oldNJ,"NJ",aut);
			} catch (FileExistsException e2) {
				//Checking if backup files exist. If so, they are deleted. Check bUExists() method.
				bUExists(tempIEEE, "IEEE", aut);
				bUExists(tempACM, "ACM", aut);
				bUExists(tempNJ, "NJ", aut);
				
				//Renaming the old files to be the backups.
				//Not sure about why they "can only be null" message here, maybe the File method is incorrect.
				oldIEEE.renameTo(new File(aut+"-IEEEBU.json"));
				oldACM.renameTo(new File(aut+"-ACMBU.json"));
				oldNJ.renameTo(new File(aut+"-NJBU.json"));	
			} finally {//After the json files have been dealt with.

				//Initializing pw for each JSON doctype so that they exists outside of try-catch block.
					PrintWriter pw1 = null;
					PrintWriter pw2 = null;
					PrintWriter pw3 = null;
					//Explicitly creating the files and initializing them outside of the loop to later check if they exis.
					File f1Created=null;
					File f2Created=null;
					File f3Created=null;

				for (int i=0; i<10; i++ ) {//Beginning of 2nd for loop to actually write into JSON files

					try {
						f1Created = new File(aut+"-IEEE.json");
						//using true to make sure that the next loop for the next Latex(i) file doesn't delete what was written by the other Latex(i) files.
						pw1 = new PrintWriter(new FileOutputStream(f1Created), true);

						f2Created = new File(aut+"-ACM.json");
						pw2 = new PrintWriter(new FileOutputStream(f2Created), true);

						f3Created = new File(aut+"-NJ.json");
						pw3 = new PrintWriter(new FileOutputStream(f3Created), true);
					}catch (FileNotFoundException e3){
						String s = e3.getMessage();
						System.out.println(s);
						//Deleting the files if they exist in order to 
						if (f1Created.exists()) {
							f1Created.delete();
						}
						if (f2Created.exists()) {
							f2Created.delete();
						}
						if (f3Created.exists()) {
							f3Created.delete();
						}
						sc[i].close();
						pw1.close();
						pw2.close();
						pw3.close(); //Don't understand why this one can only be null, why?
						System.exit(0);
					} finally {
	
						processBibFiles(aut,sc[i],pw1, pw2, pw3);
						sc[i].close();
						pw1.close();
						pw2.close();
						pw3.close();
					}
				}//end of for loop #2
			}
	}
	
	public static void processBibFiles(String author, Scanner scan, PrintWriter pw1, PrintWriter pw2, PrintWriter pw3){
		//Parse the latex files for article by author
		//When the article is found, create the three formats and record them to the correct files
		//Only one latex file is passed at once!

		//Getting the author name
		
	}
	
	//Use this method to check if a file already exists
	public static void oldExists(File oldFile, String docType, String aut) throws FileExistsException{
		oldFile = new File(aut+"-"+docType+".json");
		if (oldFile.exists()) {
			throw new FileExistsException();
		}
	}
	//Use this method to see if a BACKUP file already exists
	public static void bUExists(File temp, String docType, String aut) {
		temp = new File(aut+"-"+docType+"BU.json");
		if (temp.exists()) {
			temp.delete();
		}
	}

}
