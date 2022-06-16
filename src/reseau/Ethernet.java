package reseau;

import java.util.ArrayList;
import java.util.List;

public class Ethernet {

	private  boolean isEthernet = false;
	private List<String> addrDest, addrSrc, type ;
	
	public Ethernet(Trame t) {
		
		addrDest = new ArrayList<>();
		addrSrc = new ArrayList<>() ;
		type = new ArrayList<>() ;
		if(t.size()>=14) {
			isEthernet = true;
			addrDest = t.getOctets().subList(0, 6);
			addrSrc = t.getOctets().subList(6, 12);
			type = t.getOctets().subList(12, 14);
		}
	}
	

	public String toString() {
		StringBuilder s = new StringBuilder();
		if(isEthernet()) {
			s.append(">Ethernet II \n");
			s.append("\tDestination: "+" ("+Tools.chaiEsp(addrDest).replaceAll(" ", ":")+")\n");
			s.append("\tSource: "+" ("+Tools.chaiEsp(addrSrc).replaceAll(" ",":")+")\n");
			s.append("\tType: "+typeProtocol()+" (0x"+Tools.chai(type)+")\n");
		}
		return s.toString();
	}
	
	public boolean isEthernet() {
		// TODO Auto-generated method stub
		return isEthernet;
	}
	public void setEthernet(boolean isEthernet) {
		this.isEthernet = isEthernet;
	}

	public String typeProtocol() {
		String str = Tools.chai(type);
		switch(str)
		{
			case "0800": return "IPv4";
			case "86dd": return "IPv6";
			case "0806": return "ARP";
			case "0842": return "Wake-on-LAN";
			case "22F0": return "AVTP";
			case "22F3": return "IETF TRILL Protocol";
			default: return "null";
		}
		
	}
}
