package reseau;

public class IP {

	private int tailleIP, len , ttl;
	private String ihl, protocol, version ,identification , checksum, dsf, flags, addrDest, addrSrc;
	private boolean afficher=false, afficherOptions=false;
	public IP(Trame t) {
		identification = "null";
		flags = "null";
		ttl = 0;
		protocol = "null";
		checksum = "null";
		addrSrc = "null";
		addrDest = "null";
		version = "null";
		ihl = "null";
		tailleIP = 0;
		dsf = "null";
		len = 0;
		if(t.size()>=20) {
			afficher=true;
			version = t.getOctetPos(0).substring(0,1);
			ihl = t.getOctetPos(0).substring(1);
			tailleIP = Tools.toHexa(ihl);
			dsf = Tools.chai(t.getOctets().subList(1, 2)) ;
			len= Tools.toHexa(Tools.chai(t.getOctets().subList(2, 4)));
			if(len>=20) {
				afficherOptions=true;
				identification = Tools.chai(t.getOctets().subList(4, 6));
				flags = Tools.chai(t.getOctets().subList(6, 7));
				ttl = Tools.toHexa(t.getOctetPos(8));
				protocol = Tools.hexaToDeci(t.getOctetPos(9));
				checksum = Tools.chai(t.getOctets().subList(10, 12));
				addrSrc = Tools.ADR(t.getOctets().subList(12, 16));
				addrDest = Tools.ADR(t.getOctets().subList(16, 20));
			}
		}
		
	}
	
	public int getLen() {
		return len;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		if(afficher) {
			s.append(">Internet Protocol Version "+version+"\n");
			s.append("\t"+Tools.hexaToBinaire(version)+" .... = Version: "+version+"\n");
			s.append("\t.... "+Tools.deciToBinaire(tailleIP)+" = Header Length: "+tailleIP*4+" bytes ("+tailleIP+")\n");
			s.append("\tDifferientiated Services Filed: 0x"+dsf+"\n");
			s.append("\tTotal length: "+len+"\n");
			if(afficherOptions) {
				s.append("\tIdentification: 0x"+identification+" ("+Tools.toHexa(identification)+")\n");
				s.append("\tFlags: 0x"+flags+"\n");
				s.append("\tTime To Live: "+ttl+"\n");
				s.append("\tProtocol: "+get_protocol()+" ("+protocol+")\n");
				s.append("\tHeader Checksum : 0x"+checksum+"\n");
				s.append("\tSource Address: "+addrSrc+"\n");
				s.append("\tDestination Address: "+addrDest+"\n");
			}
		}
		return s.toString();
	}
	
    public int getIhl() {
		return Integer.parseInt(ihl);
	}

	public String get_protocol() {
        switch (protocol) {
            case "01": return "ICMP";
            case "02": return "IGMP";
            case "06": return "TCP";
            case "17": return "UDP";
            default: return "null";
        }
    }
		
	
}
