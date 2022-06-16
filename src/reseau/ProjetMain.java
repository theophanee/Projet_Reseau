package reseau;

public class ProjetMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
 
		Frag f = new Frag("data/dns_et_udp.txt" );
		try {
			f.parse();
			f.Analyser("data/analyse.txt");
		}catch(Exception e) {
			System.out.println("Probleme de lecture");
		}
		
	}

}
