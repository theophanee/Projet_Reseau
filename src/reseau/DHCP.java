package reseau;

public class DHCP {
	
	private boolean afficher=false;
	private String op,htype,hlen,hops,xid,secs,flags,ciaddr,yiaddr,siaddr,chaddr;
	private String cMac,cHadd, options,tmp_options; 
	private StringBuilder ListOptions;
	
	public DHCP(Trame t) {
			op="null";
			htype="null";
			hlen="null";
			hops="null";
			xid="null";
			secs="null";
			flags="null";
			ciaddr="null";
			yiaddr="null";
			siaddr="null";
			chaddr="null";
			cMac="null";
			cHadd="null";
			options="null";
			tmp_options="";
			ListOptions = new StringBuilder();
			
			if(t.size()>=44) {
				afficher = true;
				op = Tools.hexaToDeci(Tools.chai(t.getOctets().subList(0, 1)));
				htype=Tools.chai(t.getOctets().subList(1, 2));
				hlen=Tools.hexaToDeci(Tools.chai(t.getOctets().subList(2, 3)));
				hops=Tools.hexaToDeci(Tools.chai(t.getOctets().subList(3, 4)));
				xid=Tools.chai(t.getOctets().subList(4, 8));
				secs=Tools.hexaToDeci(Tools.chai(t.getOctets().subList(8, 10)));
				flags=Tools.chai(t.getOctets().subList(10, 12));
				ciaddr=Tools.ADR(t.getOctets().subList(12, 16));
				yiaddr=Tools.ADR(t.getOctets().subList(16, 20));
				siaddr=Tools.ADR(t.getOctets().subList(20, 24));
				chaddr=Tools.ADR(t.getOctets().subList(24, 28));
				cMac=Tools.chaiEsp(t.getOctets().subList(28, 34)).replaceAll(" ", ":");
				cHadd=Tools.chai(t.getOctets().subList(34, 44));
				
				int index_options = 196 +44;
				Trame tmp = new Trame( t.getOctets().subList(index_options, t.size()) );
				index_options = 0;
				int len;
				
				
				while(!options.equals("255")) {

					options = Tools.hexaToDeci( Tools.chai(tmp.getOctets().subList(index_options, index_options+1)));
				
					
					switch(options) {
					
						case "53": 
							tmp_options += "\t>Option: (53) DHCP Message Type ("+get_message()+")\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+2, index_options+3)));
							tmp_options += "\t\tDHCP: "+get_type(options)+" ("+options+")\n";
							index_options += 3;
							break;
							
							
						case "58":
							tmp_options += "\t>Option: (58) Renewal Time value\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+2, index_options+6)));
							tmp_options += "\t\tRenewal Time Value: ("+options+"s)\n";
							index_options += 6;
							break;
							
						case "2":
							tmp_options += "\t>Option: (2) Time offset\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+2, index_options+6)));
							tmp_options += "\t\tTime Offset: ("+options+"s)\n";
							index_options += 6;
							break;
							
						case "59":
							tmp_options += "\t>Option: (59) Rebinding Time Value\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+2, index_options+6)));
							tmp_options += "\t\tRebinding Time Value: ("+options+"s)\n";
							index_options += 6;
							break;
							
						case "15":
							tmp_options += "\t>Option (15) Domain Name\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							len = Integer.parseInt(options);
							options = "";
							
							for(int i = (index_options+2) ; i<(len+index_options+2) ;i++) {
								options += (char)Integer.parseInt(tmp.getOctetPos(i),16);
							}
							tmp_options += "\t\tDomain Name: "+options+"\n";
							
							index_options += len+2;
							break;
							
						case "81":
							tmp_options += "\t>Option (81) Client Fully Qualified Domain Name\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							len = Integer.parseInt(options);
							options = Tools.chai(tmp.getOctets().subList(index_options+2, index_options+3));
							tmp_options += "\t\tFlasg: 0x"+options+"\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+3, index_options+4)));
							tmp_options += "\t\tA-RR result: "+options+"\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+4, index_options+5)));
							tmp_options += "\t\tPTR-RR result: "+options+"\n";
							options = "";
							
							for(int i = (index_options+5) ; i<(len+index_options+2) ;i++) {
								options += (char)Integer.parseInt(tmp.getOctetPos(i),16);
							}
							tmp_options += "\t\tClient name: "+options+"\n";
							index_options += len+2;
							break;
							
						case "61":
							tmp_options += "\t>Option: (61) Client Identifier\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							options = tmp.getOctetPos(index_options+2);
							tmp_options += "\t\tHardware type: (0x"+options+")\n";
							options = Tools.chaiEsp(tmp.getOctets().subList(index_options+3, index_options+9)).replaceAll( " ", ":");
							tmp_options += "\t\tClient MAC address: ("+options+")\n";
							index_options += 9;
							break;
						
						case "1": 
							tmp_options += "\t>Option (1) Subnet Mask\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							options = Tools.ADR(tmp.getOctets().subList(index_options+2, index_options+6));
							tmp_options += "\t\tSubnet Mask: "+options+"\n";
							index_options += 6;
							break;
						
						case "50": 
							tmp_options += "\t>Option: (50) Requested IP Address\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							options = Tools.ADR(tmp.getOctets().subList(index_options+2, index_options+6));
							tmp_options += "\t\tRequested IP Address: "+options+"\n";
							index_options += 6;
							break;
							
							
						case "55":
							tmp_options += "\t>Option: (55) Parameter Request List\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							len = Integer.parseInt(options);
							for(int i = index_options+2 ; i<(len+index_options+2);i++) {
								tmp_options += "\t\tParameter Request List Item: ("+Tools.hexaToDeci(tmp.getOctetPos(i))+")\n";
							}
							index_options += 2+len;
							break;
						
						case "12": 
							tmp_options += "\t>Option: (12) Host Name\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							
							tmp_options += "\t\tLength: "+options+"\n";
							len = Integer.parseInt(options);
							options = "";
							
							for(int i = (index_options+2) ; i<(len+index_options+2) ;i++) {
								options += (char)Integer.parseInt(tmp.getOctetPos(i),16);
							}
							
							tmp_options += "\t\tHost Name: "+options+"\n";
							
							index_options += len+2;
							break;
							
						case "54":
							tmp_options += "\t>Option: ("+options+") DHCP Server Identfier \n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							
							options = Tools.ADR(tmp.getOctets().subList(index_options+2, index_options+6));
							tmp_options += "\t\tDHCP Server Identifier: "+options+"\n";
							index_options += 6;
							break;
							
							
						case "51":
							tmp_options += "\t>Option: ("+options+") IP Adrress Lease Time\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+2,index_options+6)));
							tmp_options += "\t\tIP Address Lease Time: ("+options+"s)\n";
							index_options += 6;
							break;
							
						case "3":
							tmp_options += "\t>Option: ("+options+") Router\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							
							options = Tools.ADR(tmp.getOctets().subList(index_options+2, index_options+6));
							tmp_options += "\t\tRouter: "+options+"\n";
							index_options += 6;
							break;
							
						case "6":
							tmp_options += "\t>Option: ("+options+") Domain Name Server\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							
							if(options.equals("8")){
								options = Tools.ADR(tmp.getOctets().subList(index_options+2, index_options+6));
								tmp_options += "\t\tDomain Name Server: "+options+"\n";
								
								options = Tools.ADR(tmp.getOctets().subList(index_options+6, index_options+10));
								tmp_options += "\t\tDomain Name Server: "+options+"\n";
								index_options += 10;
							}

							if(options.equals("4")) {
								options = Tools.ADR(tmp.getOctets().subList(index_options+2, index_options+6));
								tmp_options += "\t\tDomain Name Server: "+options+"\n";
								index_options += 6;
							}
							break;
						
						case "224":
							tmp_options += "\t>Option: ("+options+") Client Fully Qualified Domain Name\n";
							options = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)));
							tmp_options += "\t\tLength: "+options+"\n";
							len = Integer.parseInt(options);
							tmp_options += "\t\tValue: "+Tools.chai(tmp.getOctets().subList(index_options+2, index_options + 2 + len))+"\n";
							
							index_options += len +2;
							break;
							
						case "255": 
							tmp_options += "\t>Option: ("+options+") End\n";
							tmp_options += "\t\tOption End: 255\n";
							index_options += 1;
							break;
							
						
						default: 
							tmp_options += "\t>Option: ("+options+") Non Prise en charge\n";
							index_options += 2 +Integer.parseInt((Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(index_options+1, index_options+2)))));
					}
					
				}ListOptions.append(tmp_options);
				
			
		}
	}
	public String get_message() {
        switch (op) {
            case "1": return "Request";
            case "2": return "Reply";
            default: return "null";
        }
    }
	
	public String get_type(String type) {
		switch(type) {
		case "1": return "DISCOVER";
		case "2": return "OFFER";
		case "3": return "REQUEST";
		case "4": return "DECLINE";
		case "5": return "ACK";
		case "6": return "NACK";
		case "7": return "RELEASE";
		case "8": return "INFORM";
		default: return "";
		}
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(">Dynamic Host Configuration Protocol\n");
		if(afficher) {
			s.append("\tMessage type: Boot "+get_message()+" ("+op+")\n");
			s.append("\tHardware type: (0x"+htype+")\n");
			s.append("\tHardware address length: "+hlen+"\n");
			s.append("\tHops: "+hops+"\n");
			s.append("\tTransaction ID: 0x"+xid+"\n");
			s.append("\tSeconds elapsed: "+secs+"\n");
			s.append("\tBootp flags: 0x"+flags+"\n");
			s.append("\tClient IP address: "+ciaddr+"\n");
			s.append("\tYour (client) IP address: "+yiaddr+"\n");
			s.append("\tnext server IP address: "+siaddr+"\n");
			s.append("\tRelay agent IP address: "+chaddr+"\n");
			s.append("\tClient MAC address ("+cMac+")\n");
			s.append("\tClient hadrware address padding: "+cHadd+"\n");
			s.append(ListOptions.toString());
		}
		return s.toString();
	}

}

