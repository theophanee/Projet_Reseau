package reseau;

import java.util.ArrayList;
import java.util.List;

public class MultiTrame {

	private List<Trame> trames;
	
	public MultiTrame() {
		trames = new ArrayList<>();
	}
	public void remove(Trame t) {
		if(trames.contains(t)) {
			trames.remove(t);
		}else {
			System.out.println("Probleme Remove MultiTrames\n");
		}
	}
	public int size() {
		return trames.size();
	}
	public void add(Trame t) {
		trames.add(t);
	}
	public Trame getTramePos(int i) {
		return trames.get(i);
	}
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(int i =0; i < size() ; i++)
			s.append(getTramePos(i).toString());
		return s.toString();
	}
}
