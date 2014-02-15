package util;

import java.io.*;
import java.util.*;

public class Strings {
	private static String[] prov = {"广东", "福建", "上海", "江苏", "浙江", "安徽", "广西", "江西", "湖北", "湖南",
		"海南", "北京", "河北", "河南", "天津", "重庆", "四川", "贵州", "云南", "陕西", "山东", "山西", "辽宁", "甘肃",
		"黑龙江", "吉林", "青海", "内蒙古", "宁夏", "新疆", "西藏", "香港", "澳门", "台湾"};
	private static String[] jieYang = {"揭阳", "惠来", "揭东", "揭西", "普宁", "榕城"};
	private static String[] shanTou = {"汕头", "潮南", "潮阳", "澄海", "濠江", "金平", "龙湖", "南澳"};
	private static String[] chaoZhou = {"潮州", "潮安", "饶平", "湘桥"};
	private static String[] city = null;
	private static String[] cToP;
	private static HashMap<String, double[]> branchFare = null;
	private static HashMap<String, double[]> businessFare = null;
	private static HashMap<String, double[]> monthlyFare = null;
	private static HashMap<String, double[]> manageFee = null;
	static double[] otherf = new double[4];
	
	public static String getChaoshan(String s, String def) {
		if(s.contains("深圳")) return def;
		for(int i=0; i<jieYang.length; i++)
			if(s.contains(jieYang[i])) return "揭阳";
		for(int i=0; i<shanTou.length; i++)
			if(s.contains(shanTou[i])) return "汕头";
		for(int i=0; i<chaoZhou.length; i++)
			if(s.contains(chaoZhou[i])) return "潮州";
		return def;
	}
	
	public static String getProvince(String s) {
		if(s.contains("省")) {
			switch (s.charAt(0)) {
			case '广':
				switch(s.charAt(1)) {
				case '东':	return getChaoshan(s, "广东");
				case '西':	return "广西";
				default:	return s;
				}
			case '福':	return "福建";
			case '江':
				switch (s.charAt(1)) {
				case '苏':	return "江苏";
				case '西':	return "江西";
				default:	return s;
				}
			case '浙':	return "浙江";
			case '安':	return "安徽";
			case '湖':
				switch (s.charAt(1)) {
				case '南':	return "湖南";
				case '北':	return "湖北";
				default:	return s;
				}
			case '海':	return "海南";
			case '河':
				switch (s.charAt(1)) {
				case '北':	return "河北";
				case '南':	return "河南";
				default:	return s;
				}
			case '四':	return "四川";
			case '贵':	return "贵州";
			case '云':	return "云南";
			case '陕':	return "陕西";
			case '山':
				switch (s.charAt(1)) {
				case '东':	return "山东";
				case '西':	return "山西";
				default:	return s;
				}
			case '辽':	return "辽宁";
			case '甘':	return "甘肃";
			case '黑':	return "黑龙江";
			case '吉':	return "吉林";
			case '青':	return "青海";
			case '台':	return "台湾";
			default:
				return s;
			}
		} else if(s.contains("广东")) return getChaoshan(s, "广东"); 
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
		if(arg == 1 && branchFare == null) return -3;	//网点报价表不存在
		if(arg == 2 && businessFare == null) return -3;	//业务员报价表不存在
		if(arg == 3 && monthlyFare == null) return -3;	//月结客户报价表不存在
		if(w == 0) return -2;	//重量为0
		double[] fare = arg==1 ? branchFare.get(p) : (arg==2 ? businessFare.get(p) : monthlyFare.get(p));
//		if(arg == 1) {
//			fare = branchFare.get(p);
//			if(fare == null) return -1;	//找不到对应的省份
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
		if(fare == null) return -1;	//找不到对应的省份
		if(fare.length != 12) return -3;	//报价表格式错误
		double f1 = fare[0], f2 = fare[1], f3 = fare[2];
		if(f1 == 0) return -4;	//首重为0
		if(w <= f1) return fare[6];
		if(f2!=0 && w<=f2) return fare[7];
		if(f3!=0 && w<=f3) return fare[8];
		double tf = f2==0 ? fare[6] : (f3==0 ? fare[7] : fare[8]);
		w -= f2==0 ? f1 : (f3==0 ? f2 : f3);
		double a1 = fare[3], a2 = fare[4], a3 = fare[5];
		if(a1 == 0) return -5;	//续重为0
		if(rul == 2) {
			if(a3 != 0) return -10;	//无法计算第3续重
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
		if(manageFee == null) return -3;	//管理费价格表不存在
		if(w == 0) return -2;	//重量为0
		double[] fare = manageFee.get(p);
		if(fare == null) {
			if(otherf[0] == -1) return -1;	//找不到对应的省份
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
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("省市.txt")));
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
			System.out.println("城市："+city.length+" "+cToP.length);
		} catch (Exception e) {e.printStackTrace();}
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("网点中转.txt")));
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
			System.out.println("网点："+branchFare.size());
		} catch (Exception e) {e.printStackTrace();}
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("业务中转.txt")));
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
			System.out.println("业务："+businessFare.size());
		} catch (Exception e) {e.printStackTrace();}
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("月结客户.txt")));
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
			System.out.println("月结："+monthlyFare.size());
		} catch (Exception e) {e.printStackTrace();}
		for(int i=0; i<4; i++)
			otherf[i] = -1;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("管理费.txt")));
			manageFee = new HashMap<String, double[]>();
			String[] temp = null;
			while (((line = reader.readLine()) != null)) {
				if(!line.startsWith("'")) {
					if(line.contains("\t")) {
						String[] s = line.split("\t");
						if(temp.length > 0) {
							if(temp[0].equals("其他")) {
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
			System.out.println("管理费："+manageFee.size());
		} catch (Exception e) {e.printStackTrace();}
	}
	
}
