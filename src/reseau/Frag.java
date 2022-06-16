package reseau;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Frag {
	
	private String fichier_lecture;
	private MultiTrame trames;
	
	public Frag(String fichier_lecture) {
		this.fichier_lecture = fichier_lecture;
		trames = new MultiTrame();
	}
	
	public MultiTrame getTrames() {
		return trames;
	}
	
	
	public void parse() throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(fichier_lecture));
		String line, offset;
		Trame t = null ;
		int  idTrame = -1, sizeLignePrec = 0, sizeOffset;
		
		System.out.println("Debut parse\nFichier en lecture : "+fichier_lecture+"\n");
		
		while((line = br.readLine())!=null) {
			
			for(String wor : line.split(" ")) {
				
				
				if(wor.length()==2)
					System.out.print(wor+" ");
				
				if(wor.equals("")) continue;
				
				if(wor.length()>4 || wor.equals("."))
					break;
				
				if(wor.length() == 4 || wor.length()==3) {
					if(wor.contains(".") )
						break;
					offset = wor;
					if(offset.equals("0000") || offset.equals("000"))
						System.out.println("\n");
					System.out.print("\noffset : "+offset+" | ");
					
					if(offset.equals("0000") || offset.equals("000")) { // nouvelle trame
						sizeLignePrec = 0;
						t = new Trame();
						trames.add(t);
						idTrame++;
					}else {
						sizeOffset = Tools.toHexa(offset);

						if(sizeLignePrec != sizeOffset) {
							System.out.println("offset :"+sizeOffset+" ligneprec : "+sizeLignePrec);
							System.out.println("Erreur ligne incomplete ou Offset incorrecte trame "+idTrame);
							continue ;
						}else {
							sizeLignePrec = sizeOffset;
						}
					}
				}
				
				if(wor.charAt(0) >'f' || wor.charAt(1)>'f') {
					System.out.println("\nErreur octet : "+wor+"\n");return;
				}
				
				else if(wor.length() ==2) {
					if(t == null) {
						System.out.println("Trame vide");continue;
					}
					trames.getTramePos(idTrame).add(wor);
					sizeLignePrec ++;
				}
			}
		}
		br.close();
		
		System.out.println("\n\nFin parse\n");
		
	}
	
	
	public void Analyser(String fichier) throws IOException{
		BufferedWriter s = new BufferedWriter(new FileWriter(fichier));
		Trame t;
		
		System.out.println("Debut analyse");
		
		for(int i=0; i<trames.size(); i++) {
			t = trames.getTramePos(i);
			s.write("\n===========================================================\n\n");
			s.write(">Frame "+(i+1)+":  "+t.size()+" bytes ("+(t.size()*8)+" bits)\n");
			
			
			Ethernet e = new Ethernet(t);
			s.write(e.toString());
			if(!e.typeProtocol().equals("IPv4"))
				break;
			
			Trame trame_tmp =new Trame( t.getOctets().subList(14,t.size()));
			IP ip = new IP(trame_tmp);
			s.write(ip.toString());
			
			trame_tmp.setTrame( trame_tmp.getOctets().subList(20, trame_tmp.size()));
			UDP udp = new UDP(ip , trame_tmp);
			s.write(udp.toString());
			
			trame_tmp.setTrame(trame_tmp.getOctets().subList(8, trame_tmp.size()));
			
			Couche7 c4 = new Couche7( udp, trame_tmp);
			s.write(c4.toString());
			
		}s.write("\n===========================================================");
		s.close();	
		System.out.println("Fin Analyse\n\nFichier resultat : " + fichier);
	}
	
	
}
