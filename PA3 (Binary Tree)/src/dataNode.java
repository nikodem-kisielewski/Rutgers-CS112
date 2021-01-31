public class dataNode implements Comparable<dataNode>{
	public double value;
	public int count;
	public double max;
	public double min;
	
	public dataNode() {
		value=0;
		count=0;
	}
	
	public int compareTo(dataNode node2) {
		return 0;
	}
	
	public String toString() {
		return "("+value+","+count+")";
	}

}
