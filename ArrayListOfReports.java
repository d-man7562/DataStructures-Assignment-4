package accidentPack;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
/**
 * This class. It acts as a sort of shell class which contains an arrayList of reports, but also
 * with added functionality.
 * @author - Adam Taddia
 */
public class ArrayListOfReports {
	
	public ArrayList<Report> reportsList = new ArrayList<Report>();
	
	/**
	 * When called, generates an ArrayListOfReports from a given appropriately
	 * formated .csv file. The arrayList is filled with reports from a given state
	 * and county, and is sorted by start time in ascending order.
	 * @author - Adam Taddia
  	 * @version 2/23/2024
	 * @param filePath - the path to the .csv of reports
	 * @param state - the desired state that all reports must be from
	 * @param county - the desired county that all reports must be from
	 * @throws FileNotFoundException 
	 */
	public ArrayListOfReports(String filePath, String state, String county) throws FileNotFoundException {
		File dataFile = new File(filePath);//you must input the file path to the data as an arg
		Scanner dataScanner1 = new Scanner(dataFile);
		int reportCount = findReportCount(dataScanner1);
		dataScanner1.close();//I'm just doing this to reset to line 1
		Scanner dataScanner2 = new Scanner(dataFile);
		dataScanner2.nextLine();//SKIPS OVER THE FIRST LINE AGAIN
		for (int i = 0; i < reportCount; i++) {
			Report r = createReport(dataScanner2);
			if (r.getState().equals(state) && r.getCounty().equals(county)) {
			reportsList.add(r);
			}
		}
		Collections.sort(reportsList, new SortByStartTime());
		//QueueOfReports.addtoQueueOfReports(reportsList);
	}
	
	/**Complexity of 1. Every statement just runs once, no loops.
	 * the string timeAsString is passed as a parameter. In order for the method
	 * to work, the string must follow the format YYYY-MM-DD HH:MM:SS
	 * This method parses the strings to find the year, month, day, hour, minute, and second in the string.
	 * It then populates an array of 6 ints with these values, and this array is returned.
	 * The returned array, timeAsIntArray, is used to create the time field in a NewReport2-26 object.
	 * @param timeAsString
  	 * @return timeAsIntArray
	*/
	public static int[] timeStringToInts(String timeAsString) {//this takes a string in the format used for the time of
		int year = Integer.parseInt(timeAsString.substring(0, 4));//the incident and turns it into an array of ints, passed by pointer
		int month = Integer.parseInt(timeAsString.substring(5, 7));//substring selects a segment of the string to be parsed.
		int day = Integer.parseInt(timeAsString.substring(8, 10));//Parse turns the segment to an int.
		int hour = Integer.parseInt(timeAsString.substring(11,13));
		int minute = Integer.parseInt(timeAsString.substring(14,16));
		int second = Integer.parseInt(timeAsString.substring(17,19));
		int timeAsIntArray[] = {year, month, day, hour, minute, second};
		return timeAsIntArray;
	}
	
	
	
		/**
		 * Dominic wrote the dateConvert method, and Adam slightly modified it to create timeConvert
		 */
		static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		 public static LocalDate dateConvert(String dateTimeString)
		    {
			 // for some of the instances the after seconds there are 0s; this line will remove them
			 dateTimeString = dateTimeString.substring(0,10);
	 		// Parse the string using the formatter
			 LocalDate localDate;
			 try {
				 localDate = LocalDate.parse(dateTimeString, formatter);
			 } catch (Exception e) {
				 // Handle parsing exception, e.g., invalid format, invalid date
				 System.err.println("Error parsing date-time string: " + e.getMessage());
				 localDate = null;
	   }
	 	return localDate;
		    }

			 public static LocalTime timeConvert(String dateTimeString)
			    {
				 // for some of the instances the after seconds there are 0s; this line will remove them
				 dateTimeString = dateTimeString.substring(11,19);
		 		// Parse the string using the formatter
				 LocalTime localTime;
				 try {
					 localTime = LocalTime.parse(dateTimeString, formatter);
				 } catch (Exception e) {
					 // Handle parsing exception, e.g., invalid format, invalid date
					 System.err.println("Error parsing date-time string: " + e.getMessage());
					 localTime = null;
		   }
		 	return localTime;
			    }
		 
		 

	/**
	*Constructor for Report
 	*@param scn Scanner
  	*@return NewReport2-26 r
	*/
	public static Report createReport(Scanner scn) {//a method for creating a report
		scn.useDelimiter(",|\\n");
		String ID = scn.next();
		int Severity = Integer.parseInt(scn.next());
		
		String startDateTimeAsString = scn.next();
		String endDateTimeAsString = scn.next();
		
		LocalDate StartDate = dateConvert(startDateTimeAsString);
		LocalDate EndDate = dateConvert(endDateTimeAsString);
		
		LocalTime StartTime = timeConvert(startDateTimeAsString);
		LocalTime EndTime = timeConvert(endDateTimeAsString);
		
		String Street = scn.next();
		String City = scn.next();
		String County = scn.next();
		String State = scn.next();
		double Temperature = Double.parseDouble(scn.next());
		double Humidity = Double.parseDouble(scn.next());
		double Visibility = Double.parseDouble(scn.next());
		String Weather = scn.next();
		boolean AtCrossing;
		boolean IsDay;
		if (scn.next().equals("FALSE")) {AtCrossing = false;} else {AtCrossing = true;}
		if (scn.next().substring(0,1).equals("N")) {IsDay = false;} else {IsDay = true;}
		Report r = new Report(ID, Severity, StartDate, EndDate, StartTime, EndTime, Street, City, County, State, Temperature, Humidity, Visibility, Weather, AtCrossing, IsDay);
		return r;	}

	
	/**
	*finds amount of Reports
 	*@param scn Scanner
	*@return reportCount
	*/
	public static int findReportCount(Scanner scn){//passes the scanner as a parameter to get the number of reports
		long startTime = System.nanoTime();
		int reportCount = -1;//starts at -1 to account for first line
		
		while(scn.hasNextLine()) {
			scn.nextLine();
			reportCount += 1;
		}
		
		long endTime = System.nanoTime();
		System.out.println((endTime - startTime) + " nanoseconds to run findReportCount");
		return reportCount;
	}
	

	
}
