package game;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * The basic class for a game
 * Has interfaces for game control and application info
 *
 */
public class Game implements Serializable{
	private static final long serialVersionUID = 1L;
	public LocalDateTime current;
	public Double money;
	public Double projectedMoney;
	public ArrayList<Double> portfolio;
	private ArrayList<BarData> costs;
	private Scenario scenario;
	public HashMap<String, Integer> names;
	
	/**
	 * Generate a game based off the scenario
	 * @param scenario
	 */
	public Game(Scenario scenario) {
		this.scenario = scenario;
		current = scenario.start;
		portfolio = new ArrayList<Double>(Collections.nCopies(scenario.getSize(), 0.0));
		money = scenario.startingCapital;
		projectedMoney = money;
		costs = scenario.get(current);
		names = scenario.names;
	}
	
	/**
	 * Calculate a step
	 * @param st step in milliseconds
	 */
	public void step(long st) {
		costs = scenario.get(current.plus(st, ChronoUnit.MILLIS));
	}
	
	/**
	 * Try to buy some stocks
	 * @param name the name of the desired stock
	 * @param amt the amount of stocks to buy
	 * @return true if the transaction occurred,
	 * false otherwise
	 */
	public boolean buy(String name, Double amt) {
		Integer t = scenario.getStockNum(name);
		if (t == -1) {
			return false;
		}
		if (money >= costs.get(t).close * amt) {
			money -= costs.get(t).close * amt;
			portfolio.set(t, portfolio.get(t) + amt);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Get stocks cost
	 * @param name
	 * @return double,
	 * 0 if wrong values
	 */
	public double get(String name) {
		return get(names.getOrDefault(name, -1));
	}
	
	/**
	 * Get stocks cost
	 * @param index
	 * @return double,
	 * 0 if wrong values
	 */
	public double get(Integer i) {
		if (i < 0 || i >= costs.size()) {
			return 0;
		}
		return costs.get(i).close;
	}
	
	/**
	 * Try to sell some stocks
	 * @param name the name of the desired stock
	 * @param amt the amount of stocks to sell
	 * @return true if the transaction occurred,
	 * false otherwise
	 */
	public boolean sell(String name, Double amt) {
		Integer t = scenario.getStockNum(name);
		if (t == -1) {
			return false;
		}
		if (portfolio.get(t) >=  amt) {
			money += costs.get(t).close * amt;
			portfolio.set(t, portfolio.get(t) - amt);
			return true;
		}
		return false;
	}
	
	/**
	 * Save the game to a file
	 * @param filename
	 */
	public void save(String filename) {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename));
			objectOutputStream.writeObject(this);
			objectOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Load a game from file
	 * @param filename path to file
	 * @return created class
	 */
	static public Game loadFromFile(String filename) {
		try {
			ObjectInputStream objStream = new ObjectInputStream(new FileInputStream(filename));
			Game ans = (Game)objStream.readObject();
			objStream.close();			
			return ans;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
