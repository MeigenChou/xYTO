package util;

import java.io.*;

public class Test {
	static BufferedReader reader;
	static BufferedWriter writer;

	static void getNames(String path) throws IOException {
		File f = new File(path);
		String[] s = f.list();
		for(int i=0; i<s.length; i++) {
			File f2 = new File(path+s[i]);
			if(f2.isDirectory()) getNames(path+s[i]+"/");
			else {
				if(s[i].endsWith(".java")) writer.write(path+s[i]+"\r\n");
			}
		}
	}

	static void getSrcs(String path) throws Exception {
		File f = new File(path);
		String[] s = f.list();
		for(int i=0; i<s.length; i++) {
			File f2 = new File(path+s[i]);
			if(f2.isDirectory()) getSrcs(path+s[i]+"/");
			else {
				if(s[i].endsWith(".java")) {
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(path+s[i])));
					writer.write(path+s[i]+"\r\n");
					String line = "";
					int count = 1;
					while (((line = reader.readLine()) != null)) {
						writer.write((count++)+"\t"+line+"\r\n");
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src.txt")));
//		getNames("F:/tnoodle-master/");
		getSrcs("F:/tnoodle-master/");
		writer.close();
//		double i = 1.55;
//		System.out.println(Math.ceil(i));
//		System.out.println(Math.floor(i));
//		System.out.println(Math.round(i));
	}

}
