package examples.ordering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Implements a list of items
 * 
 * Comprises
 * 		List of items
 *		Number and type of each item
 * 		- Each item has a name and unit cost

 * @author peter
 */
public class ItemList {
	private List<PartItemList> _partLists;
	
	public ItemList(final List<PartItemList> partLists) {
		_partLists = partLists;
	}
	
	public ItemList(String fileName) {
		List<List<String>> grid = readFromCsvFile(fileName);
		_partLists = stringGridToItemlist(grid);
	}
	
	public ItemList(String input, boolean distinguishFromOtherConstructor) {
		List<List<String>> grid = readFromString(input);
		_partLists = stringGridToItemlist(grid);
	}
		
	public List<PartItemList> getPartLists() {
		return _partLists;
	}
	
	public double getPriceInDollars() {
		double priceInDollars = 0.0;
		for (PartItemList partOrder: _partLists) {
			priceInDollars  += partOrder.getPriceInDollars();
		}
		return priceInDollars;
	}
	
	private static final int ITEMS_PER_ROW = 2;

	/**
	 * Read a CSV file into a 2d list of string
	 * @param fileName name of CSV file
	 * @return  Array of array of strings
	 */
	private static List<List<String>> readFromCsvFile(String fileName) {
		List<List<String> > grid = new ArrayList<List<String> >();
		int lineNumber = 0;
		try {
			BufferedReader input = new BufferedReader(new FileReader(new File(fileName)));
			try {
				String line;
				while ((line = input.readLine()) != null) {
					//System.out.println(fileName + " " + lineNumber + ": '" + line + "'");
					List<String> row = new ArrayList<String>();
					StringTokenizer tokenizer = new StringTokenizer(line, ",");
					while (tokenizer.hasMoreTokens())  {
						row.add(tokenizer.nextToken().trim());
					}
					if (row.size() < ITEMS_PER_ROW) {
						System.err.println(fileName + ":" + (lineNumber+1) + " has less than " + ITEMS_PER_ROW + " items: " + line);
					}
					//System.out.println("** " + row.get(0) + "," + row.get(1) + "," + row.get(2) + ",");
					grid.add(row);
					++lineNumber;
				}
			} 
			finally {
				input.close();
				System.out.println("Read " + fileName + " " + lineNumber + " lines ------------------------");
			}
		}
		catch (IOException e) {
			System.err.println("Error opening " + fileName + ": " + e);
		}
		return grid;
	}
	
	/**
	 * Read a semi-colon separated string of comma separated strings into a 2d list of 
	 * string
	 * @param input The input string
	 * @return  Array of array of strings
	 */
	private static List<List<String>> readFromString(String input) {
		List<List<String> > grid = new ArrayList<List<String> >();
		int lineNumber = 0;
	
		StringTokenizer tokenizerOuter = new StringTokenizer(input, ";");
		while (tokenizerOuter.hasMoreTokens())  {
			String line = tokenizerOuter.nextToken().trim();
			List<String> row = new ArrayList<String>();
			StringTokenizer tokenizer = new StringTokenizer(line, ",");
			while (tokenizer.hasMoreTokens())  {
				row.add(tokenizer.nextToken().trim());
			}
			if (row.size() < ITEMS_PER_ROW) {
				System.err.println(" " + (lineNumber+1) + " has less than " + ITEMS_PER_ROW + " items: " + line);
			}
			//System.out.println("** " + row.get(0) + "," + row.get(1) + "," + row.get(2) + ",");
			grid.add(row);
			++lineNumber;
		}
		return grid;
	}
	
	
	private static List<PartItemList> stringGridToItemlist(List<List<String>> grid) {
		//System.out.println(" " + grid.size());
		List<PartItemList> partLists = new ArrayList<PartItemList>();
		for (List<String> row: grid) {
			//System.out.println("  " + row.size());
			assert(row.size() >= ITEMS_PER_ROW); // see readFromCsvFile()
			//System.out.println("-- " + row.get(0) + "," + row.get(1) + "," + row.get(2) + ",");
			try {
				String name = row.get(0);
				int    number = Integer.parseInt(row.get(1));
				double priceInDollars = 0.0;
				if (row.size() > 2) {
					priceInDollars = Double.parseDouble(row.get(2));
				}
				PartItemList part = new PartItemList(name, priceInDollars, number);
				//System.out.println("++ " + part.getName() + "," + part.getUnitPriceInDollars() + "," + part.getNumber() + "," + part.getPriceInDollars());
				partLists.add(part);
			} catch (Exception e) {
				System.err.println("Error parsing string:" + e);
			}
		}
		return partLists;
	}
	
	
	/**
	 * Returns true if ItemList contains an item with the requested name
	 * @param name Is there an item with this name in the Itemlist
	 * @return PartItemList if there is one, else null
	 */
	public PartItemList getItemByName(String name) {
		for (PartItemList p: _partLists) {
			if (p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Tells whether this list contains all the items in otherList (ignore prices in otherList)
	 * @param otherList  list to compare against
	 * @return true iff this list contains all the items in otherList
	 */
	public boolean contains(final ItemList otherList) {
		for (PartItemList otherPart: otherList.getPartLists()) {
			PartItemList thisPart = getItemByName(otherPart.getName());
			if (thisPart == null) {
				return false;
			} else if (thisPart.getNumber() < otherPart.getNumber()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Remove items in otherList from this this list. Ignore prices in other list.
	 * @param otherList Items to remove
	 */
	public void subtract(final ItemList otherList) {
		for (PartItemList otherPart: otherList.getPartLists()) {
			PartItemList thisPart = getItemByName(otherPart.getName());
			// Must be guaranteed by a check against contains before calling this function
			assert(thisPart != null && thisPart.getNumber() >= otherPart.getNumber());
			thisPart.setNumber(thisPart.getNumber() - otherPart.getNumber());
		}
	}
	
	/**
	 * Apply prices to an order
	 * @param order The order (name, number) of each item
	 * @return The costed order (name, unit cost, number) of each item
	 */
	public ItemList getCostedOrder(final ItemList order) {
		List<PartItemList> costedPartLists = new ArrayList<PartItemList>();
		for (PartItemList orderPart: order.getPartLists()) {
			PartItemList thisPart = getItemByName(orderPart.getName());
			// Must be guaranteed by a check against contains before calling this function
			assert(thisPart != null && thisPart.getNumber() >= orderPart.getNumber());
			PartItemList costedPart = new PartItemList(orderPart.getName(), thisPart.getUnitPriceInDollars(), orderPart.getNumber());
			costedPartLists.add(costedPart);
		}
		return new ItemList(costedPartLists);
	}
	
	public String getAsString() {
		String itemListString = "";
		for (PartItemList part: getPartLists()) {
			String partString = part.getName() + "," + part.getNumber() + "," + part.getUnitPriceInDollars();
			itemListString += partString + ";";
		}
		return itemListString;
	}
}
