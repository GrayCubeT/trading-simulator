package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * Scenario storage class
 * <p>
 * Basically all the info about the scenario. 
 * Has interfaces for getting stock data and making a step
 * Main function is step()
 * 
 * 
 */
public class Scenario implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public String name;
	public String description;
	private ArrayList<StockData> stocks;
	public LocalDateTime start;
	public LocalDateTime end;
	public Double numberModifier;
	public Double startingCapital;
	public Double winningCapital; 
	public long updateInterval;
	public HashMap<String, Integer> names;
	
	/**
	 * @return the amount of stocks in the scenario
	 */
	public int getSize() {
		return stocks.size();
	}
	
	/**
	 * Get all the actual info about stocks
	 * @param time LocalDateTime to get info from 
	 * @return new list of all actual BarData's (same indexes as stocks)
	 * sets the corresponding  BarData to null if there's no next BarData checkpoint 
	 */
	public ArrayList<BarData> get(LocalDateTime time) {
		ArrayList<BarData> l = new ArrayList<BarData>();
		for (StockData i : stocks) {
			l.add(i.get(time));
		}
		return l;
	}
	
	
	/**
	 * A find function for a search line
	 * @param str
	 * @return all Stocks containing the string
	 */
	public ArrayList<StockData> find(String str) {
		ArrayList<StockData> l = new ArrayList<StockData>();
		for (StockData i : stocks) {
			if (i.name.contains(str)) {
				l.add(i);
			}
		}
		return l;
	}
	
	/**  
	 * Find a stock and get it's slice
	 * @param name A string name for the stock
	 * @param start Starting date for the slice
	 * @param end Ending date for the slice
	 * @return A slice of the required stock.
	 * Null if there's no stock with specified name
	 */
	public List<BarData> getStock(String name, LocalDateTime start, LocalDateTime end) {
		for (StockData i : stocks) {
			if (i.name.equalsIgnoreCase(name)) {
				return i.slice(start, end);
			}
		}
		return null;
	}
	
	/**
	 * Get StockData from name
	 * @param name A string name of the stock
	 * @return Null if stock is not found or StockData of the specified name
	 */
	public StockData getStock(String name) {
		Integer i = names.getOrDefault(name, -1);
		return (i == -1) ? null : stocks.get(i);
	}
	
	/**
	 * Get the number of stock in all stocks by name
	 * @param name A string name of the stock
	 * @return -1 if stock is not found, index of stock otherwise
	 */
	public int getStockNum(String name) {
		return names.getOrDefault(name, -1);
	}
	
	/**
	 * Generate a null Scenario 
	 */
	public Scenario() {
		description = null;
		stocks = new ArrayList<StockData>();
		numberModifier = 1.0;
		startingCapital = 1000.0;
		winningCapital = 2000.0;
		updateInterval = 0;
		names = new HashMap<String, Integer>();
	}
	
	/**
	 * Generate a scenario from file
	 * @param filepath - Scenario file is a list of filepaths to stockdata, line separated
	 * the directory is assumed the same of the file given
	 */
	public Scenario(String filepath) throws FileNotFoundException, IOException {
		description = null;
		updateInterval = 0;
		stocks = new ArrayList<StockData>();
		names = new HashMap<String, Integer>();
		numberModifier = 1.0;
		startingCapital = 1000.0;
		winningCapital = 2000.0;
		File file = new File(filepath);
		if (file.isFile()) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String dir = file.getParent();
			String line;
			Integer num = 0;
			while ((line = br.readLine()) != null) {
				System.out.print("loading: " + line + "\n");
				num += addFromFile(dir + "\\" + line) ? 1 : 0;
			}
			System.out.print("loaded " + num + " entries\n");
			br.close();
		}
		stocks.sort(new Comparator<StockData>() {
	        @Override
	        public int compare(StockData a, StockData b) {
	            //You should ensure that list doesn't contain null values!
	            return a.name.compareToIgnoreCase(b.name);
	        }
	       });
		
	}
	
	/**
	 * serialize the scenario object into the "name".scenario file
	 */
	public void saveToFile() {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(name + ".scenario"));
			objectOutputStream.writeObject(this);
			objectOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Deserialize the specified file and return a Scenario object
	 * @param filename Path to serialized Scenario
	 */
	static public Scenario loadFromFile(String filename) {
		try {
			ObjectInputStream objStream = new ObjectInputStream(new FileInputStream(filename));
			Scenario ans = (Scenario)objStream.readObject();
			objStream.close();			
			return ans;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Add StockData from file
	 * 
	 * automatically puts in the name of the file as the name of the stock
	 * Sets start and end date automatically
	 * 
	 * @param path path to StockData file - usually .csv 
	 */
	public boolean addFromFile(String path) {
		int dotIndex = path.indexOf('.');
		String stockName = (dotIndex == -1) ? path : path.substring(0, dotIndex);
		File file = new File(path);
		if (file.isFile()) {
			StockData d = new StockData(file);
			d.name = stockName;
			names.put(d.name, stocks.size());
			stocks.add(d);
			if (start == null || start.isAfter(d.start)) {
				start = d.start;
			}
			if (end == null || end.isBefore(d.end)) {
				end = d.end;
			}
			return true;
		} else {
			return false;
		}
	}
}
