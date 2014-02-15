package util;

import java.io.*;
import java.util.*;

public class Strings {
	private static String[] prov = {"�㶫", "����", "�Ϻ�", "����", "�㽭", "����", "����", "����", "����", "����",
		"����", "����", "�ӱ�", "����", "���", "����", "�Ĵ�", "����", "����", "����", "ɽ��", "ɽ��", "����", "����",
		"������", "����", "�ຣ", "���ɹ�", "����", "�½�", "����", "���", "����", "̨��"};
	private static String[] jieYang = {"����", "����", "�Ҷ�", "����", "����", "�ų�"};
	private static String[] shanTou = {"��ͷ", "����", "����", "�κ�", "婽�", "��ƽ", "����", "�ϰ�"};
	private static String[] chaoZhou = {"����", "����", "��ƽ", "����"};
	private static String[] city = null;
	private static String[] cToP;
	private static HashMap<String, double[]> branchFare = null;
	private static HashMap<String, double[]> businessFare = null;
	private static HashMap<String, double[]> monthlyFare = null;
	private static HashMap<String, double[]> manageFee = null;
	static double[] otherf = new double[4];
	
	public static String getChaoshan(String s, String def) {
		if(s.contains("����")) return def;
		for(int i=0; i<jieYang.length; i++)
			if(s.contains(jieYang[i])) return "����";
		for(int i=0; i<shanTou.length; i++)
			if(s.contains(shanTou[i])) return "��ͷ";
		for(int i=0; i<chaoZhou.length; i++)
			if(s.contains(chaoZhou[i])) return "����";
		return def;
	}
	
	public static String getProvince(String s) {
		if(s.contains("ʡ")) {
			switch (s.charAt(0)) {
			case '��':
				switch(s.charAt(1)) {
				case '��':	return getChaoshan(s, "�㶫");
				case '��':	return "����";
				default:	return s;
				}
			case '��':	return "����";
			case '��':
				switch (s.charAt(1)) {
				case '��':	return "����";
				case '��':	return "����";
				default:	return s;
				}
			case '��':	return "�㽭";
			case '��':	return "����";
			case '��':
				switch (s.charAt(1)) {
				case '��':	return "����";
				case '��':	return "����";
				default:	return s;
				}
			case '��':	return "����";
			case '��':
				switch (s.charAt(1)) {
				case '��':	return "�ӱ�";
				case '��':	return "����";
				default:	return s;
				}
			case '��':	return "�Ĵ�";
			case '��':	return "����";
			case '��':	return "����";
			case '��':	return "����";
			case 'ɽ':
				switch (s.charAt(1)) {
				case '��':	return "ɽ��";
				case '��':	return "ɽ��";
				default:	return s;
				}
			case '��':	return "����";
			case '��':	return "����";
			case '��':	return "������";
			case '��':	return "����";
			case '��':	return "�ຣ";
			case '̨':	return "̨��";
			default:
				return s;
			}
		} else if(s.contains("�㶫")) return getChaoshan(s, "�㶫"); 
		else for(int i=0; i<prov.length; i++) {
			if(s.contains(prov[i])) return prov[i];
		}
		if(city != null) {
			for(int i=0; i<city.length; i++) {
				if(s.contains(city[i])) return cToP[i];
			}
		}
		return getChaoshan(s, s);
	}
	
	public static double transferFare(double w, String p, int arg, int rul) {
		if(arg == 1 && branchFare == null) return -3;	//���㱨�۱�����
		if(arg == 2 && businessFare == null) return -3;	//ҵ��Ա���۱�����
		if(arg == 3 && monthlyFare == null) return -3;	//�½�ͻ����۱�����
		if(w == 0) return -2;	//����Ϊ0
		double[] fare = arg==1 ? branchFare.get(p) : (arg==2 ? businessFare.get(p) : monthlyFare.get(p));
//		if(arg == 1) {
//			fare = branchFare.get(p);
//			if(fare == null) return -1;	//�Ҳ�����Ӧ��ʡ��
//			if(fare.length > 5) {
//				f1 = fare[3];
//				f2 = fare[4];
//				ad = fare[5];
//			}
//			if(w <= f1) return fare[0];
//			if(fare[1] == 0) {
//				int t = (int) Math.ceil((w - f1) / ad);
//				return fare[0] + t * fare[2];
//			}
//			if(w <= f2) return fare[1];
//			int t = (int) Math.ceil((w - f2) / ad);
//			return fare[1] + t * fare[2];
//		}
		if(fare == null) return -1;	//�Ҳ�����Ӧ��ʡ��
		if(fare.length != 12) return -3;	//���۱��ʽ����
		double f1 = fare[0], f2 = fare[1], f3 = fare[2];
		if(f1 == 0) return -4;	//����Ϊ0
		if(w <= f1) return fare[6];
		if(f2!=0 && w<=f2) return fare[7];
		if(f3!=0 && w<=f3) return fare[8];
		double tf = f2==0 ? fare[6] : (f3==0 ? fare[7] : fare[8]);
		w -= f2==0 ? f1 : (f3==0 ? f2 : f3);
		double a1 = fare[3], a2 = fare[4], a3 = fare[5];
		if(a1 == 0) return -5;	//����Ϊ0
		if(rul == 2) {
			if(a3 != 0) return -10;	//�޷������3����
			if(a2 == 0) {
				int t = (int) Math.ceil(w / a1);
				return tf + fare[9] * t;
			}
			if(w%a2 != 0 && w%a2 <= a1) {
				int t = (int) Math.floor(w / a2);
				return tf + fare[10] * t + fare[9];
			}
			int t = (int) Math.ceil(w / a2);
			return tf + fare[10] * t;
		}
		if(a2 == 0) {
			int t = (int) Math.ceil(w / a1);
			return tf + fare[9] * t;
		}
		tf += fare[9];
		if(w < a1) return tf;
		w -= a1;
		if(a3 == 0) {
			int t = (int) Math.ceil(w / a2);
			return tf + fare[10] * t;
		}
		tf += fare[10];
		if(w < a2) return tf;
		w -= a2;
		int t = (int) Math.ceil(w / a3);
		return tf + fare[11] * t;
	}
	
	public static double manageFee(double w, String p) {
		if(manageFee == null) return -3;	//����Ѽ۸������
		if(w == 0) return -2;	//����Ϊ0
		double[] fare = manageFee.get(p);
		if(fare == null) {
			if(otherf[0] == -1) return -1;	//�Ҳ�����Ӧ��ʡ��
			fare = new double[4];
			for(int i=0; i<4; i++)
				fare[i] = otherf[i];
		}
		if(w <= fare[0]) return fare[1];
		int t = (int) Math.ceil((w - fare[0]) / fare[2]);
		return fare[1] + t * fare[3];
	}
	
	static {
		ArrayList<String> c = new ArrayList<String>();
		BufferedReader reader = null;
		String line = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("ʡ��.txt")));
			while (((line = reader.readLine()) != null)) {
				if(!line.startsWith("'"))
					c.add(line);
			}
			reader.close();
			int count = c.size();
			city = new String[count];
			cToP = new String[count];
			for(int i=0; i<count; i++) {
				String s = c.get(i);
				city[i] = s.substring(0, s.indexOf("\t"));
				cToP[i] = s.substring(s.indexOf("\t")+1);
			}
			System.out.println("���У�"+city.length+" "+cToP.length);
		} catch (Exception e) {e.printStackTrace();}
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("������ת.txt")));
			branchFare = new HashMap<String, double[]>();
			while (((line = reader.readLine()) != null)) {
				if(!line.startsWith("'")) {
					String[] temp = line.split("\t");
					double[] fare = new double[temp.length-1];
					for(int i=1; i<temp.length; i++) fare[i-1] = Double.parseDouble(temp[i]);
					branchFare.put(temp[0], fare);
				}
			}
			reader.close();
			System.out.println("���㣺"+branchFare.size());
		} catch (Exception e) {e.printStackTrace();}
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("ҵ����ת.txt")));
			businessFare = new HashMap<String, double[]>();
			while (((line = reader.readLine()) != null)) {
				if(!line.startsWith("'")) {
					String[] temp = line.split("\t");
					double[] fare = new double[temp.length-1];
					for(int i=1; i<temp.length; i++) fare[i-1] = Double.parseDouble(temp[i]);
					businessFare.put(temp[0], fare);
				}
			}
			reader.close();
			System.out.println("ҵ��"+businessFare.size());
		} catch (Exception e) {e.printStackTrace();}
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("�½�ͻ�.txt")));
			monthlyFare = new HashMap<String, double[]>();
			while (((line = reader.readLine()) != null)) {
				if(!line.startsWith("'")) {
					String[] temp = line.split("\t");
					double[] fare = new double[temp.length-1];
					for(int i=1; i<temp.length; i++) fare[i-1] = Double.parseDouble(temp[i]);
					monthlyFare.put(temp[0], fare);
				}
			}
			reader.close();
			System.out.println("�½᣺"+monthlyFare.size());
		} catch (Exception e) {e.printStackTrace();}
		for(int i=0; i<4; i++)
			otherf[i] = -1;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("�����.txt")));
			manageFee = new HashMap<String, double[]>();
			String[] temp = null;
			while (((line = reader.readLine()) != null)) {
				if(!line.startsWith("'")) {
					if(line.contains("\t")) {
						String[] s = line.split("\t");
						if(temp.length > 0) {
							if(temp[0].equals("����")) {
								for(int i=0; i<4; i++)
									otherf[i] = Double.parseDouble(s[i]);
							} else {
								double[] fare = new double[4];
								for(int i=0; i<4; i++)
									fare[i] = Double.parseDouble(s[i]);
								for(int i=0; i<temp.length; i++) {
									manageFee.put(temp[i], fare);
								}
							}
						}
					} else {
						temp = line.split(",");
					}
				}
			}
			reader.close();
			System.out.println("����ѣ�"+manageFee.size());
		} catch (Exception e) {e.printStackTrace();}
	}
	
}
