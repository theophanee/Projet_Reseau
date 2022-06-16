package reseau;

public class Couche7 {
	
	private int identifie ;
	private DHCP dhcp;
	private DNS dns;
	
	public Couche7(UDP udp , Trame t) {
		dns = null;
		dhcp = null;
		identifie = 0;
		if(udp.getDestP()==67 || udp.getSrcP()==67) {
			identifie = 1;
			dhcp = new DHCP(t);
		}
		if(udp.getDestP()==53 || udp.getSrcP()==53) {
			identifie = 2;
			dns = new DNS(t);
		}
	}
	
	public String toString() {
		if(identifie==2)
			return dns.toString();
		if(identifie==1)
			return dhcp.toString();
		return ">Non pris en charge";
	}

}
