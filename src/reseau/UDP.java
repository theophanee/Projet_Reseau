package reseau;

public class UDP {
	
	private int destP, srcP;
	private boolean afficher = false, pris_en_charge;
	private String length, checksum ;
	public UDP(IP ip, Trame data) {
		if(ip.get_protocol().equals("UDP")) {
			pris_en_charge = true;
			srcP = 0;
			destP = 0;
			checksum = "null";
			length ="null";
			if(data.size()>=20) {
				afficher=true;
				srcP = Tools.toHexa(Tools.chai(data.getOctets().subList(0, 2)));
				destP = Tools.toHexa(Tools.chai(data.getOctets().subList(2, 4)));
				length = Tools.hexaToDeci(Tools.chai(data.getOctets().subList(4, 6)));
				checksum = Tools.chai(data.getOctets().subList(6, 8));	
			}
		}else
			pris_en_charge = false;

	}
	
	public int getDestP() {
		return destP;
	}

	public int getSrcP() {
		return srcP;
	}

	public String toString() {
		if(!pris_en_charge)
			return "pas UDP\n";
		StringBuilder s = new StringBuilder();
		if(afficher){
			s.append(">User Datagram Protocol\n");
			s.append("\tSource Port :"+srcP+"\n");
			s.append("\tDestination Port :"+destP+"\n");
			s.append("\tLength: "+length+"\n");
			s.append("\tChecksum: 0x"+checksum+"\n");
		}
		return s.toString();
	}

}

