package A3;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AuthorBibCreator {

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		System.out.println("Welcome to Bib Creator! Please enter the author name your are looking for: ");
		String aut = kb.next();
		Scanner sc=null;
		kb.close();
		
		for (int i=1; i<=10; i++) {//Loop for each Latex file from Latex1 to Latex10.
			try {
				sc = new Scanner(new FileInputStream("Latex"+i+".bib"));
			}catch (FileNotFoundException e1){
				System.out.println("Could not open input file Latex"+i+".bib for reading.\r\n" + 
						"Please check if file exists! Program will terminate after closing any opened files.");
				System.exit(0);
			}
			System.out.println("All the files were found.");
			
			//Initializing all variables to null so that they exist outside of try-catch block.
			File oldIEEE=null;
			File oldACM=null;
			File oldNJ=null;
			File tempIEEE=null;
			File tempACM=null;
			File tempNJ=null;
			
			try {
				//Checking if files already exist.
				oldExists(oldIEEE,"IEEE",aut);
				oldExists(oldACM,"ACM",aut);
				oldExists(oldNJ,"NJ",aut);
			} catch (FileExistsException e2) {
				//Checking if backup files exist. If so, they are deleted.
				bUExists(tempIEEE, "IEEE", aut);
				bUExists(tempACM, "ACM", aut);
				bUExists(tempNJ, "NJ", aut);
				
				//Renaming the old files to be the backups.
				oldIEEE.renameTo(new File(aut+"-IEEEBU.json"));
				oldACM.renameTo(new File(aut+"-ACMBU.json"));
				oldNJ.renameTo(new File(aut+"-NJBU.json"));	
			}
			//Initializing pw for each JSON doctype so that they exists outside of try-catch block.
			PrintWriter pw1 = null;
			PrintWriter pw2 = null;
			PrintWriter pw3 = null;
			FileOutputStream f1=null;
			FileOutputStream f2=null;
			FileOutputStream f3=null;
			File f1Created=null;
			File f2Created=null;
			File f3Created=null;
			
			try {
				f1Created = new File(aut+"-IEEE.json");
				f1 = new FileOutputStream(f1Created);
				pw1 = new PrintWriter(f1, true);
				
				f2Created = new File(aut+"-ACM.json");
				f2 = new FileOutputStream(f2Created);
				pw2 = new PrintWriter(f2, true);
				
				f3Created = new File(aut+"-NJ.json");
				f3 = new FileOutputStream(f3Created);
				pw3 = new PrintWriter(f3, true);
			}catch (FileNotFoundException e3){
				String s = e3.getMessage();
				System.out.println(s);
				if (f1Created.exists()) {
					f1Created.delete();
				}
				if (f2Created.exists()) {
					f2Created.delete();
				}
				if (f3Created.exists()) {
					f3Created.delete();
				}
				sc.close();
				pw1.close();
				pw2.close();
				pw3.close(); //what, why?
				System.exit(0);
			}
			
			//William's to read and write into the file. Once that is done:
			sc.close();
			pw1.close();
			pw2.close();
			pw3.close();
		}
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
