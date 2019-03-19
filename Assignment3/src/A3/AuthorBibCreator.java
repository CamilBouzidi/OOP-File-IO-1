package A3;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//Some initial comments before starting:
//I USED TO open and close the same scanner, but 10 times in a single loop. 
//I decided to instead create an array of scanners and reuse them in a
//second for loop once I have dealt with the Latex files working and made sure to overwrite the old backups, but
//didn't create an array for the printwriters because it is allowed for them to overwrite each other because of the "true" argument when
//using the printwriter constructors.
//Program does the following:
//1) Checks if the 10 Latex files exist by populating an array of scanners with an input file stream for each latex file.
//	if that doesn't work, throw exception.
//2) Goes out of loop to see if the json files and backups exist. If so, deletes backups and replaces them with the json file 
//(old json becomes backup json, older backups deleted)
//3) Goes back into a loop, using the scanners in the array to write each of the 3 files using the same 3 printwriters every time 
//(make sure not to delete)
//   the part where I close them at the of the 2nd for loop [they will be reopened and closed by each iteration of the for loop])
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
			File oldIEEE = new File(aut+"-IEEE.json");
			File tempIEEE=new File(aut+"-IEEEBU.json");;
			File oldACM= new File(aut+"-ACM.json");
			File tempACM=new File(aut+"-ACMBU.json");;
			File oldNJ=new File(aut+"-NJ.json");
			File tempNJ=new File(aut+"-NJBU.json");
			
			try {
				//Checking if files already exist. Check oldExists() method.
				oldExists(oldIEEE);
				oldExists(oldACM);
				oldExists(oldNJ);
			} catch (FileExistsException e2) {
				//Checking if backup files exist. If so, they are deleted. Check bUExists() method.
				System.out.println(e2.getMessage());
				if (bUExists(tempIEEE)) {
					oldIEEE.renameTo(tempIEEE);
				} else {//Renaming if the backup DNE.
					oldIEEE.renameTo(new File(aut+"-IEEEBU.json"));
				}
				if (bUExists(tempACM)) {
					oldACM.renameTo(tempACM);
				}
				else {
					oldACM.renameTo(new File(aut+"-ACMBU.json"));
				}
				if (bUExists(tempNJ)) {
					oldNJ.renameTo(tempNJ);
				}
				else {
					oldNJ.renameTo(new File(aut+"-NJBU.json"));
				}
			} finally {//After the json files have been dealt with.

				//Initializing pw for each JSON doctype so that they exists outside of try-catch block.
					PrintWriter pwIEEE = null;
					PrintWriter pwACM = null;
					PrintWriter pwNJ = null;
					//Explicitly creating the files and initializing them outside of the loop to later check if they exist.
					File f1Created=null;
					File f2Created=null;
					File f3Created=null;
//					for (int i = 0; i < sc.length; i++) {
//						
//						
//					}
					try {
						f1Created = new File(aut+"-IEEE.json");
						//using true to make sure that the next loop for the next Latex(i) file doesn't delete what was written by the other Latex(i) files.
						pwIEEE = new PrintWriter(new FileOutputStream(f1Created), true);

						f2Created = new File(aut+"-ACM.json");
						pwACM = new PrintWriter(new FileOutputStream(f2Created), true);

						f3Created = new File(aut+"-NJ.json");
						pwNJ = new PrintWriter(new FileOutputStream(f3Created), true);
					}catch (FileNotFoundException e3){
						System.out.println(e3.getMessage());
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
						for (int j = 0; j < sc.length; j++) {
							sc[j].close();
						}
						
						pwIEEE.close();
						pwACM.close();
						pwNJ.close(); //Don't understand why this one can only be null, why?
						System.exit(0);
					} finally {
						for (int j = 0; j < sc.length; j++) {
							processBibFiles(aut,sc[j],pwIEEE, pwACM, pwNJ);
							sc[j].close();
						}
						
						pwIEEE.close();
						pwACM.close();
						pwNJ.close();
					}
					
			}
	}
	
	public static void processBibFiles(String aut, Scanner scan, PrintWriter pwIEEE, PrintWriter pwACM, PrintWriter pwNJ){
		//Parse the latex files for article by author
		//When the article is found, create the three formats and record them to the correct files
		//Only one latex file is parsed at once!
		boolean goodAuthor=true;
		String line="";		String content ="";
		String author ="";		String journal = "";		String title = "";		String year = "";
		String volume = "";		String number = "";		String pages = "";		String keywords = "";
		String issn = "";		String month = "";
		
		String authorR ="";		String journalR = "";		String titleR = "";		String yearR = "";
		String volumeR = "";		String numberR = "";		String pagesR = "";		String keywordsR = "";
		String issnR = "";		String monthR = "";
		
		//Reading the file until there's nothing
		while (scan.hasNextLine()) {
			if (scan.nextLine().contains("@ARTICLE")) {
				line = scan.nextLine().trim();
				while (!line.equals("}")) {
					//System.out.println(line);
					//We know we have a line with relevant information
					//Depending what's in the line, relevant data will be extracted
					//the authors wanted to do a switch, but .contains returns a boolean
					if (line.contains("author")) {
						if (!line.contains(aut)) {
							//this article wasn't written by the wanted author
							goodAuthor=false;
						} else {
							goodAuthor=true;
							author = extract(line);
						}
					} else if (line.contains("journal")) {
						journal = extract(line);
					} else if (line.contains("title")) {
						title = extract(line);
					} else if (line.contains("year")) {
						year = extract(line);
					} else if (line.contains("volume")) {
						volume = extract(line);
					} else if (line.contains("number")) {
						number = extract(line);
					} else if (line.contains("pages")) {
						pages = extract(line);
					} else if (line.contains("keywords")) {
						keywords = extract(line);
					} else if (line.contains("ISSN")) {
						issn = extract(line);
					} else if (line.contains("month")) {
						month = extract(line);
					}
					line = scan.nextLine().trim();
				}//Once the program gets here, an entire article was parsed, or it's the wrong article(not written by aut)
				if (goodAuthor) {
					//writing to IEEE file!!!
					System.out.println("============================Writing the IEEE format!!!============================");
					pwIEEE.println(authorRefined(author, "IEEE") + ". " + "\"" + title + "\", " + journal + ", vol. " + volume 
							+ ", no. " + number + ", p. " + pages + ", " + month +" " + year + ".");
					System.out.println(authorRefined(author, "IEEE") + ". " + "\"" + title + "\", " + journal + ", vol. " + volume 
							+ ", no. " + number + ", p. " + pages + ", " + month +" " + year + ".");
					System.out.println("============================Writing the ACM format!!!============================");
				}
				
			}
			
			//gets here if it's done reading the bib file or there's nothing in the file
			//Note: the article can be written to the file in one line
		}
		
	}
	
	//Use this method to check if a file already exists
	public static void oldExists(File oldFile) throws FileExistsException{
		if (oldFile.exists()) {
			throw new FileExistsException();
		}
	}
	//Use this method to see if a BACKUP file already exists
	public static boolean bUExists(File temp) {
		if (temp.exists()) {
			temp.delete();
			return true;
		}
		else {
			temp.delete();
			return false;
		}
	}
	
	public static String authorRefined(String author, String format) {
		String[] authors = author.split(" and ");
		String result ="";
		switch (format) {
		case "IEEE":
			for (int i = 0; i < authors.length-1; i++) {
				result+=authors[i] + ", ";
			}
			result+=authors[authors.length-1];
			return result;
		case "ACM":
			return result;
		case "NJ":
			return result;
		default:
			return "Data processing error in authorRefined!";
		}
		
	}
	
	//Used to extract the contents of each line
	public static String extract(String line) {
		//must add 1 to the first index, because we want the string after it
		return line.substring(line.indexOf("{")+1, line.indexOf("}"));
	}

}
