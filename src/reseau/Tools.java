package reseau;

import java.math.BigInteger;
import java.util.List;

public class Tools {

	public static String hexaToBinaire(String s) {
		if(s=="null")
			return s;
		String res = new BigInteger(s,16).toString(2);
		if(res.length() <= 4) {
			for(int i =res.length() ; i<4 ;i++)
				res = "0" + res + " ";
		}
		return res;
	}
	public static String hexaToBinaire16(String s) {
		if(s=="null")
			return s;
		String res = new BigInteger(s,16).toString(2);
		if(res.length() <= 16) {
			for(int i =res.length() ; i<16 ;i++)
				res = "0" + res + " ";
		}
		return res;
	}
	public static String deciToBinaire(int decimal) {
		if(decimal==0)
			return "null";
		String s = Integer.toBinaryString(decimal);
		if( s.length() <= 4) {
			for(int i = s.length() ; i<4 ; i++){
				s = "0" + s + " ";
			}
		}
		return s;
	}
	public static String hexaToDeci(String s) {
		if(s=="null")
			return s;
		int res = Integer.parseInt(s,16);
		return Integer.toString(res);
	}
	
	public static String chaiEsp(List<String> liste) {
		String s = "";
		for(int i =0; i < liste.size(); i++) {
			s+= liste.get(i)+" ";
		}
		return s.substring(0,s.length()-1);
	}
	
	public static char ascii2char(String octet) {
		return (char)(Integer.parseInt(octet,16));
	}
	
	public static String chai(List<String> liste) {
		StringBuilder s = new StringBuilder();
		for(int i =0; i<liste.size(); i++)
			s.append(liste.get(i));
		return s.toString();
	}
	
	public static String ADR(List<String> liste) {
		if(liste==null)
			return "null";
		String s = "";
		for(int i=0 ; i<liste.size(); i++)
			s += Integer.parseInt(liste.get(i),16)+".";
		
		return s.substring(0, s.length()-1);
	}
	
	public static int toHexa(String s) {
		if(s=="null")
			return 0;
		return Integer.parseInt(s,16);
	}
}

