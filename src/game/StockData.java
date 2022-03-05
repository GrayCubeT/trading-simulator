package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


/**
 * All the data for one stock 
 * 
 * Holds both date and bar data<p>
 * 
 * Loads from file
 * 
 */
public class StockData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2281787438818301101L;
	public boolean loaded;
	public String description = null;
	public String name = null;
	private ArrayList<BarData> arr;
	public ListIterator<BarData> next;
	public LocalDateTime start;
	public LocalDateTime end;
	public int size;
	// in millis
	public long step;
	
	public StockData() {
		arr = new ArrayList<BarData>();
		loaded = false;
		step = 0;
		size = 0;
		start = null;
		end = null;
	}
	/**
	 * try loading from file
	 * @apiNote
	 * unpredictable result if the files are not 
	 * 	exactly the same format as follows:
	 * 
	 * Date;Open;High;Low;Close;Adj Close (?);Volume
	 * first line should always be (?)
	 * Date format;
	 * 
	 */
	public StockData(String filePath) {
		arr = new ArrayList<BarData>();
		next = null;
		step = 0;
		size = 0;
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
			String row;
			DateTimeFormatter format;
			if ((row = csvReader.readLine()) != null) {
				format = new DateTimeFormatterBuilder()
						.appendPattern(row)
			            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
			            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
			            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
			            .toFormatter();
			}
			else {
				csvReader.close();
				throw new Exception("File contains no date format");
			}
			while ((row = csvReader.readLine()) != null) {
			    String[] data = row.split(";");
			    BarData bd = new BarData(data, format);
			    arr.add(bd);
			    size += 1;
			    if (size == 2) {
			    	step = java.time.temporal.ChronoUnit.MILLIS.between(bd.date, arr.get(0).date);
			    }
			}
			loaded = true; 
			start = arr.get(0).date;
			end = arr.get(size - 1).date;
			next = arr.listIterator();
			csvReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			loaded = false;
			return;
		}
	}
	
	/**
	 * Reads the file and tries to initialize the stockData from it
	 * @param file the File object to load from
	 * 
	 */
	public StockData(File file) {
		arr = new ArrayList<BarData>();
		next = null;
		step = 0;
		size = 0;
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(file));
			String row;
			DateTimeFormatter format;
			if ((row = csvReader.readLine()) != null) {
				//System.out.print("date format: " + row + "\n");
				format = new DateTimeFormatterBuilder()
						.appendPattern(row)
			            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
			            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
			            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
			            .toFormatter();
			}
			else {
				csvReader.close();
				throw new Exception("File contains no date format");
			}
			while ((row = csvReader.readLine()) != null) {
			    String[] data = row.split(";");
			    BarData bd = new BarData(data, format);
			    arr.add(bd);
			    size += 1;
			    if (size == 2) {
			    	step = java.time.temporal.ChronoUnit.MILLIS.between(bd.date, arr.get(0).date);
			    }
			}
			loaded = true; 
			start = arr.get(0).date;
			end = arr.get(size - 1).date;
			next = arr.listIterator();
			csvReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			loaded = false;
			return;
		}
	}
	
	/**
	 * Get a slice of bardata from index start to index end
	 * 
	 * @param start starting index
	 * @param end ending index
	 */
	private List <BarData> slice(int start, int end) {
		return arr.subList(start, end);
	}
	
	/**
	 * Return the next BarData on the list
	 * @return next BarData from the iterator
	 */
	public BarData advance() {
		if (next.hasNext()) {
			return next.next();			
		}
		return null;
	}
	
	/**
	 * Find the index of BarData just before (or exactly)
	 * of specified datetime
	 * 
	 * @return -1 if the date is before the start
	 *  last index if the date is after the end
	 */
	private int findIndex(LocalDateTime date) {
		if (date.isBefore(start)) {
			return -1;
		}
		int t = (int) (java.time.temporal.ChronoUnit.MILLIS.between(date, arr.get(0).date) / step);
		if (t < size) {
			return (int)(t);
		} else if (t >= 0){
			return size - 1;
		}
		return -1;
	}
	
	
	/**
	 * get the actual information for the stock
	 * @param date - date
	 * @return the BarData right before the date
	 */
	public BarData get(LocalDateTime date) {
		int d = findIndex(date); 
		return (d == -1) ? null : arr.get(d); 
	}

	/**
	 * Return a slice of all the data from start to end
	 * 
	 * @return A list of all BarData in the interval start-end. 
	 * If end date exists and start date is before the first possible date
	 * Returns null if the end date is before teh first posible date
	 */
	public List<BarData> slice(LocalDateTime start, LocalDateTime end) {
		int s = findIndex(start);
		int e = findIndex(end);
		if (e == -1) {
			return null;
		}
		return slice((s == -1) ? 0 : s, e);
	}
	
	
}
