package reseau;

import java.util.ArrayList;
import java.util.List;

public class Trame {

	private List<String> octets;
	
	public Trame(List<String> octets) {
		this.octets = octets;
	}
	public Trame() {
		octets = new ArrayList<>();
	}
	
	public int size() {
		return octets.size();
	}
	public void setTrame(List<String> liste) {
		this.octets = liste;
	}
	public List<String> getOctets(){
		return octets;
	}
	public void add(String o) {
		octets.add(o);
	}
	public String getOctetPos(int i) {
		return octets.get(i);
	}
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(int i=0; i < size();i++) 
			s.append(getOctetPos(i)+" ");
		return s.toString();
	}
	
	public String extraire(List<String> liste) {
		StringBuilder s = new StringBuilder();
		for(int i=0; i < liste.size(); i++)
			s.append(liste.get(i));		
		return s.toString();
	}
	
}
