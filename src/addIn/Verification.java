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
			ta.append("Error: �ļ�����Excel����������\n");
			return;
		}
		try {
			int billNum = Integer.parseInt(cols[0]) - 1;
			int recipient = Integer.parseInt(cols[1]) - 1;
			String recStr;
			ta.append("���ڴ򿪹�����...\n");
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
			ta.append("һ����"+snum+"��������\n���ڹ���hash��...\n");
			bill = new HashMap<String, String>();
			recer = new HashMap<String, String>();
			for (int i = 0; i < snum; i++) {
				Sheet st = wb.getSheetAt(i);
				if(st.getLastRowNum() < 1) {
					ta.append("�� "+(i+1)+" ��������û������\n");
					continue;
				}
				Row row;
				if(auto) {
					billNum = recipient = -1;
					row = st.getRow(0);
					int tempRowSize = row.getLastCellNum();
					for(int j=0; j<tempRowSize; j++) {
						cell = row.getCell(j);
						if(cell.getStringCellValue().contains("�˵�����")) {
							billNum = j;
							break;
						}
					}
					for(int j=0; j<tempRowSize; j++) {
						cell = row.getCell(j);
						if(cell.getStringCellValue().contains("�ռ�Ա")) {
							recipient = j;
							break;
						}
					}
				}
				if(billNum == -1 || recipient == -1) {
					ta.append("Error: �� "+(i+1)+" ��������ȱ�� '�˵�����' ��(��) '�ռ�Ա' �ֶ�\n");
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
				ta.append("�� "+(i+1)+" ��������һ���� "+bill.size()+" ���浥���ݣ� "+recer.size()+" ���ռ�Ա����\n");
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
				//JOptionPane.showOptionDialog(null, p, "�����浥", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] {"�ر�"}, null);
				//JOptionPane.showMessageDialog(null, "��ǰ�ռ�Ա��\nɨ�����룺", "�浥����", JOptionPane.CLOSED_OPTION);
			}
		} catch (Exception e) {
			ta.append("��ŷ�������ˣ�����ϵ����\n");
			e.printStackTrace();
			StackTraceElement[] el = e.getStackTrace();
			try {
				String name = "log"+System.currentTimeMillis()+".txt";
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name)));
				for(int i=0; i<el.length; i++) {
					writer.write(el[i]+"\r\n");
				}
				ta.append("������Ϣ�ѱ�����"+name+"\n");
				writer.close();
			} catch (Exception e1) { }
		}
	}
}
