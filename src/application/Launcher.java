package application;


import java.util.Scanner;

import game.Game;
import game.Scenario;

/*
 * Launcher class used for testing sometimes
 */
public class Launcher {
	public static void main(String[] args) {
		if (args.length > 0) {
			if (args[0].equals("-test")) {
				test();
				return;
			}
		}
		Main.run(args);
		
	}
	private static void test() {
		Scenario sc;
		Game game;
		try {
			sc = new Scenario();
			sc.addFromFile("MRNA.csv");
			sc.addFromFile("MRNA1.csv");
			game = new Game(sc);
		}
		catch (Exception e) {
			System.out.print(e);
			return;
		}
		Scanner scanner;
		//DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-mm-dd");
		while (true) {
			String s;
			scanner = new Scanner(System.in);
			
	        s = scanner.nextLine();
	        String[] p = s.split(" ");
			if (p[0].equalsIgnoreCase("step")) {
				try {
					long t = Long.parseLong(p[1]);
					game.step(t);
				}
				catch (Exception e) {
					System.out.print("Integer cast failed\n");
				}
			}
			if (p[0].equalsIgnoreCase("buy")) {
				try {
					double t = Double.parseDouble(p[2]);
					System.out.print(game.buy(p[1], t));
				}
				catch (Exception e) {
					System.out.print("Double cast failed\n");
				}
			}
			if (p[0].equalsIgnoreCase("sell")) {
				try {
					double t = Double.parseDouble(p[2]);
					System.out.print(game.sell(p[1], t));
				}
				catch (Exception e) {
					System.out.print("Double cast failed\n");
				}
			}
			if (p[0].equalsIgnoreCase("info")) {
				try {
					if (p.length > 1) {
						System.out.print(game.get(p[1]));						
					}
					else {
						System.out.print(game.names.keySet());
					}
				}
				catch (Exception e) {
					
				}
			}
			if (s.equalsIgnoreCase("stop")) {
				break;
			}
		}
		scanner.close();
		return;
	}
	
	
}
