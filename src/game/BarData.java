package game;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Basic one bar of data
 * <p> 
 * Holds all the info about one point of time - the actual numbers
 *  true until the next BarData
 */
public class BarData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1996521520333041871L;
	public LocalDateTime date;
	public Double open;
	public Double high;
	public Double low;
	public Double close;
	public Double volume;
	BarData(String[] data, DateTimeFormatter format) throws Exception, NumberFormatException {
		// for adj close which is useless smh
		int offset = data.length - 6;
		if (data.length != 7 && data.length != 6) {
			throw new Exception("Incorrect formatting");
		}
		date = LocalDateTime.parse(data[0], format);
		open = Double.parseDouble(data[1]);
		high = Double.parseDouble(data[2]);
		low = Double.parseDouble(data[3]);
		close = Double.parseDouble(data[4]);
		volume = Double.parseDouble(data[5 + offset]);
	}
}