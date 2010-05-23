package examples.ordering;

public class PartItemList {
	private String _name;
	private double _unitPriceInDollars;
	private int    _number;
	
	public PartItemList(String name, double unitPriceInDollars, int number) {
		_name = name;
		_unitPriceInDollars = unitPriceInDollars;
		_number = number;
	}
	
	public String getName() {
		return _name;
	}
		
	public int getNumber() {
		return _number;
	}
	
	public double getUnitPriceInDollars() {
		return _unitPriceInDollars;
	}
	
	public void setNumber(int number) {
		_number = number;
	}
	
	public double getPriceInDollars() {
		return _unitPriceInDollars * _number;
	}
}
