package addIn;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ui.ScanDialog;
import util.Audio;

public class Verification {
	public static HashMap<String, String> bill;
	public static HashMap<String, String> recer;
	public static String crntRecer = "";
	public static Audio success = new Audio("success.wav");
	public static Audio error = new Audio("error.wav");
	public static DefaultTableModel model;
	public static JTable table;
	public static ArrayList<String> billName;
	public static ArrayList<String> recName;
	public static byte[] billMark;
	public static short[] recIdx;
	public static int[] totals;
	public static int[] counts;
	
	public static void verifyBill(File f, JTextArea ta, String[] cols, boolean auto) {
		String temp = f.getPath();
		if(!temp.endsWith(".xls") && !temp.endsWith(".xlsx")) {
			ta.append("Error: 文件不是Excel工作簿类型\n");
			return;
		}
		try {
			int billNum = Integer.parseInt(cols[0]) - 1;
			int recipient = Integer.parseInt(cols[1]) - 1;
			String recStr;
			ta.append("正在打开工作簿...\n");
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			Workbook wb;
			if(temp.endsWith(".xls")) {
				POIFSFileSystem fs = new POIFSFileSystem(bis);
				wb = new HSSFWorkbook(fs);
			} else {
				wb = new XSSFWorkbook(bis);
			}
			bis.close();
			Cell cell = null;
			final int snum = wb.getNumberOfSheets();
			ta.append("一共有"+snum+"个工作表\n正在构建hash表...\n");
			bill = new HashMap<String, String>();
			recer = new HashMap<String, String>();
			for (int i = 0; i < snum; i++) {
				Sheet st = wb.getSheetAt(i);
				if(st.getLastRowNum() < 1) {
					ta.append("第 "+(i+1)+" 个工作表没有内容\n");
					continue;
				}
				Row row;
				if(auto) {
					billNum = recipient = -1;
					row = st.getRow(0);
					int tempRowSize = row.getLastCellNum();
					for(int j=0; j<tempRowSize; j++) {
						cell = row.getCell(j);
						if(cell.getStringCellValue().contains("运单号码")) {
							billNum = j;
							break;
						}
					}
					for(int j=0; j<tempRowSize; j++) {
						cell = row.getCell(j);
						if(cell.getStringCellValue().contains("收件员")) {
							recipient = j;
							break;
						}
					}
				}
				if(billNum == -1 || recipient == -1) {
					ta.append("Error: 第 "+(i+1)+" 个工作表缺少 '运单号码' 和(或) '收件员' 字段\n");
					continue;
				}
				for(int j=0; j<st.getLastRowNum()+1; j++) {
					row = st.getRow(j);
					cell = row.getCell(recipient);
					if(cell != null) {
						if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
							recStr = cell.getStringCellValue();
							if(recStr.contains(" ")) {
								recer.put(recStr.substring(0, recStr.indexOf(" ")), recStr.substring(recStr.indexOf(" ")+1));
								cell = row.getCell(billNum);
								String bnum = "";
								int ct = cell.getCellType();
								if(ct == Cell.CELL_TYPE_STRING)
									bnum = cell.getStringCellValue();
								else if(ct == Cell.CELL_TYPE_NUMERIC) 
									bnum = String.valueOf((long)cell.getNumericCellValue());
								if(cell.getCellType() < 2) {
									if(bnum!=null && bnum.length()!=0) {
										bill.put(bnum, recStr.substring(0, recStr.indexOf(" ")));
									}
								} else {
									System.out.println(j+1+" "+bnum);
								}
							} else {
								System.out.println(j+1+" "+recStr);
							}
						} else {
							System.out.println(j+1+" "+cell.getCellType());
						}
					}
				}
				ta.append("第 "+(i+1)+" 个工作表一共有 "+bill.size()+" 个面单数据， "+recer.size()+" 个收件员数据\n");
			}
			if(bill.size() > 0) {
				crntRecer = "";
				Iterator<?> iter = recer.entrySet().iterator();
				recName = new ArrayList<String>();
				totals = new int[recer.size()];
				counts = new int[recer.size()];
				for(int i=0; i<recer.size(); i++) counts[i] = 0;
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					recName.add((String) entry.getKey());
				}
				iter = bill.entrySet().iterator();
				billName = new ArrayList<String>();
				billMark = new byte[bill.size()];
				recIdx = new short[bill.size()];
				int c = 0;
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					int m = recName.indexOf((String) entry.getValue());
					totals[m]++;
					recIdx[c++] = (short) m;
					billName.add((String) entry.getKey());
				}
				new ScanDialog().setVisible(true);
				//JOptionPane.showOptionDialog(null, p, "核销面单", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] {"关闭"}, null);
				//JOptionPane.showMessageDialog(null, "当前收件员：\n扫描条码：", "面单核销", JOptionPane.CLOSED_OPTION);
			}
		} catch (Exception e) {
			ta.append("啊欧，出错了，请联系作者\n");
			e.printStackTrace();
			StackTraceElement[] el = e.getStackTrace();
			try {
				String name = "log"+System.currentTimeMillis()+".txt";
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name)));
				for(int i=0; i<el.length; i++) {
					writer.write(el[i]+"\r\n");
				}
				ta.append("错误信息已保存在"+name+"\n");
				writer.close();
			} catch (Exception e1) { }
		}
	}
}
