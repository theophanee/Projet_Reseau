package reseau;

public class DNS {

	
	private String identification, flags, questions, answerRRs, authorityRRs, additionalRRs,options,name,type,classe;
	private boolean afficher = false, isQuestion = false;
	private int label_count =0, length =0, cpt=0; 
	private String answers, authority, additional;
	
	public DNS(Trame t) {
			identification="null";
			flags="null";
			questions ="null";
			answerRRs="null";
			authorityRRs="null";
			additionalRRs="null";
			name = "null";
			type="null";
			classe="null";
			answers="\t>Answers\n";
			authority="\t>Authoritative nameservers\n";
			additional="\t>Additional records\n";
			if(t.size()>=12) {
				afficher = true;
				identification = Tools.chai(t.getOctets().subList(0, 2));
				flags = Tools.chai(t.getOctets().subList(2, 4));
				
				options = Tools.hexaToBinaire16(flags);
				if(options.charAt(0) ==  '0') 
					isQuestion = true;
								
				questions = Tools.hexaToDeci(Tools.chai(t.getOctets().subList(4, 6)));
				answerRRs = Tools.hexaToDeci(Tools.chai(t.getOctets().subList(6, 8)));
				authorityRRs = Tools.hexaToDeci(Tools.chai(t.getOctets().subList(8, 10)));
				additionalRRs = Tools.hexaToDeci( Tools.chai(t.getOctets().subList(10, 12)));
				
				// queries
				
				cpt = Integer.parseInt(Tools.hexaToDeci(t.getOctetPos(12)));
				Trame tmp = new Trame(t.getOctets().subList(13, t.size()));
				
				if(cpt != 0) {
					name="";
					while(cpt != 0) {
						length += cpt ;
						label_count ++;
						for(int i = 0 ; i<cpt ;i++) {
							name += (char)Integer.parseInt(tmp.getOctetPos(i),16);
							
						}
						int tmp_cpt = cpt;
						cpt = Integer.parseInt(Tools.hexaToDeci(tmp.getOctetPos(tmp_cpt)));
						tmp = new Trame(tmp.getOctets().subList(tmp_cpt+1,  tmp.size()));
						if(cpt!=0)
							name += '.';
						
					}
				}length +=2;
				
				type = Tools.chai(tmp.getOctets().subList(0, 2));
				classe = Tools.chai(tmp.getOctets().subList(2, 4));
				
				// Answers
				
				if(!isQuestion) {
					tmp = new Trame(tmp.getOctets().subList(4, tmp.size()));
					int n = 0;
					int boucle = Integer.parseInt(answerRRs);
										
					while(n<boucle) {
						cpt = Integer.parseInt(Tools.hexaToDeci(tmp.getOctetPos(1)));
						if(n==0)
							answers += "\t\t>Name: "+name+"\n"; 
						else
							answers += "\t\t>Name\n ";
						
						String typeA = Tools.chai(tmp.getOctets().subList(2, 4));
						answers += "\t\t\tType: ("+ Tools.hexaToDeci(typeA)  + ")\n";
						answers += "\t\t\tClass: (0x"+ Tools.chai(tmp.getOctets().subList(4, 6)) +")\n";
						answers += "\t\t\tTime to live: "+Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(6, 10)))+"\n";
						String dataL = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(10, 12)));
						answers += "\t\t\tData length: "+dataL+"\n";
						
						if(typeA.equals("0005")) {
							answers += "\t\t\tCNAME: ";
							
							for(int i = 13 ; i< Integer.parseInt(dataL)+11 ; i++) {
								if(tmp.getOctetPos(i).contains("0")) {
									answers +=".";
								}else
									answers +=  (char)Integer.parseInt(tmp.getOctetPos(i),16);
							}
						}
						if(typeA.equals("0001")) {
							answers += "\t\t\tAddress: " + Tools.ADR(tmp.getOctets().subList(12, 16));
						}
						
						
						answers += "\n";
						tmp = new Trame(tmp.getOctets().subList(Integer.parseInt(dataL)+12, tmp.size()));
						
						
						
						n++;
					}
					answers += "\n";
					
					//Autho
					
					n = 0;
					boucle = Integer.parseInt(authorityRRs);
										
					while(n<boucle) {
						cpt = Integer.parseInt(Tools.hexaToDeci(tmp.getOctetPos(1)));
						authority += "\t\t>Name: ";
						String dataL = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(10, 12)));
						for(int i = 15 ; i< Integer.parseInt(dataL)+10 ; i++) {
							authority +=  (char)Integer.parseInt(tmp.getOctetPos(i),16);
						}
						authority += "\n";
						
						String typeA = Tools.chai(tmp.getOctets().subList(2, 4));
						authority += "\t\t\tType: ("+ Tools.hexaToDeci(typeA)  + ")\n";
						authority += "\t\t\tClass: (0x"+ Tools.chai(tmp.getOctets().subList(4, 6)) +")\n";
						authority += "\t\t\tTime to live: "+Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(6, 10)))+"\n";
						authority += "\t\t\tData length: "+dataL+"\n";
						
						if(typeA.equals("0002")) {
							authority += "\t\t\tName server: ";
							
							for(int i = 13 ; i< Integer.parseInt(dataL)+11 ; i++) {
								if(tmp.getOctetPos(i).contains("0")) {
									authority +=".";
								}else
									authority +=  (char)Integer.parseInt(tmp.getOctetPos(i),16);
							}
						}
						
						
						authority += "\n";
						tmp = new Trame(tmp.getOctets().subList(Integer.parseInt(dataL)+12, tmp.size()));
						
						
						
						n++;
					}
					authority += "\n";
					
					//Additional
					
					n = 0;
					boucle = Integer.parseInt(additionalRRs);
										
					while(n<boucle) {
						cpt = Integer.parseInt(Tools.hexaToDeci(tmp.getOctetPos(1)));
						additional += "\t\t>Name\n ";
						String dataL = Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(10, 12)));
												
						String typeA = Tools.chai(tmp.getOctets().subList(2, 4));
						additional += "\t\t\tType: ("+ Tools.hexaToDeci(typeA)  + ")\n";
						additional += "\t\t\tClass: (0x"+ Tools.chai(tmp.getOctets().subList(4, 6)) +")\n";
						additional += "\t\t\tTime to live: "+Tools.hexaToDeci(Tools.chai(tmp.getOctets().subList(6, 10)))+"\n";
						additional += "\t\t\tData length: "+dataL+"\n";
						
						if(typeA.equals("0001"))
							additional += "\t\t\tAddress: " + Tools.ADR(tmp.getOctets().subList(12, 16));
						if(typeA.equals("001c"))
							additional += "\t\t\tAddress: IPv6" ;
						additional += "\n";
						tmp = new Trame(tmp.getOctets().subList(Integer.parseInt(dataL)+12, tmp.size()));
						
						
						
						n++;
					}
					additional += "\n";
					
				}
				
			}
		
	}
	
	public String toString() { 
		StringBuilder s = new StringBuilder();
		s.append(">Domain Name System\n");
		if(afficher) {
			s.append("\tTransaction ID: 0x"+identification+"\n");
			s.append("\t>Flasg: 0x"+flags+"\n");
			if(isQuestion) {
				s.append("\t\t"+options.charAt(0)+"... .... .... .... = Response: Message is a query\n");
				s.append("\t\t."+options.substring(1,4)+" "+options.charAt(5)+"... .... .... = Opcode: Standard query (0)\n");
				s.append("\t\t.... .."+options.charAt(6)+". .... .... = Truncated: Message is not truncated\n");
				s.append("\t\t.... ..."+options.charAt(7)+" .... .... = Recursion desired: Do query recursively\n");
				s.append("\t\t.... .... ."+options.charAt(8)+".. .... = Z: reserved (0)\n");
				s.append("\t\t.... .... ..."+options.charAt(9)+" .... = Non-authenticated data: Unacceptable\n");
			}else {
				s.append("\t\t"+options.charAt(0)+"... .... .... .... = Response: Message is a response\n");
				s.append("\t\t."+options.substring(1,4)+" "+options.charAt(4)+"... .... .... = Opcode: Standard query (0)\n");
				s.append("\t\t.... ."+options.charAt(5)+".. .... .... = Authoritative: Server is not an authority for domain\n");
				s.append("\t\t.... .."+options.charAt(6)+". .... .... = Truncated: Message is not truncated\n");
				s.append("\t\t.... ..."+options.charAt(7)+" .... .... = Recursion desired: Do query recursively\n");
				s.append("\t\t.... .... "+options.charAt(8)+"... .... = Recursion available: Server can do recuersive queries\n");
				s.append("\t\t.... .... ."+options.charAt(9)+".. .... = Z: reserved (0)\n");
				s.append("\t\t.... .... .."+options.charAt(10)+". ... = Answer authenticated: Answer/authority portion was not authenticated by the server\n");
				s.append("\t\t.... .... ..."+options.charAt(11)+" .... = Non-authenticated data: Unacceptable\n");
				s.append("\t\t.... .... .... "+options.substring(11, 15)+" = Reply code: No error (0)\n");
			}
			s.append("\tQuestions: "+questions+"\n");
			s.append("\tAnswer RRs: "+answerRRs+"\n");
			s.append("\tAuthority RRs: "+authorityRRs+"\n");
			s.append("\tAdditional RRs: "+additionalRRs+"\n");
			s.append("\t>Queries\n");
			s.append("\t\t>"+name+"\n");
			s.append("\t\t\tName: "+name+"\n");
			s.append("\t\t\t[Name Length: "+length+"]\n");
			s.append("\t\t\t[Label Count: "+label_count+"]\n");
			s.append("\t\t\tType: ("+Tools.hexaToDeci(type)+")\n");
			s.append("\t\t\tClass: (0x"+classe+")\n");
			
			if(!isQuestion) {
				s.append(answers);	
				s.append(authority);
				s.append(additional);
			}
			
			
		}
		
		return s.toString();
	}
}
