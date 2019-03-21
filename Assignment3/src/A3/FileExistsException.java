package A3;
/**
 * This is the FileExistsException class created for Assignment 3.
 * This exception is thrown when a file with a certain name already exists.
 * @author Morin-Laberge, William (ID #40097269), and Bouzidi, Camil (ID #40099611)
 * @version 5.0
 * COMP 249 
 * Assignment #3
 * March 21 2019
 */
public class FileExistsException extends Exception {
	/**
	 * @return FileExistsException: indicates we have to turn the existing file into a backup file
	 */
	public FileExistsException() {
		super("Exception: There is already an existing file for that author. File will\r\n" + 
				"be renamed as BU, and older BU files will be deleted!");
	}
	/**
	 * @param s: some message passed to the Exception super constructor 
	 * @return FileExistsException: indicates we have to turn the existing file into a backup file
	 */
	public FileExistsException(String s) {
		super(s);
	}
}
