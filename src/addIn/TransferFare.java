package addIn;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ui.UI2;
import util.Strings;

import java.io.*;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class TransferFare {
	public static void calcTransFare(File f, JTextArea ta, JLabel lb, String[] cols, String[] shs, boolean[] auto) {
		String temp = f.getPath();
		if(!temp.endsWith(".xls") && !temp.endsWith(".xlsx")) {
			ta.append("Error: �ļ�����Excel����������\n\n");
			return;
		}
		int rowIdx = -1;
		try {
			int dest = Integer.parseInt(cols[0]) - 1;
			int weight = Integer.parseInt(cols[1]) - 1;
			int trsfare = Integer.parseInt(cols[2]) - 1;
			int shBranch = Integer.parseInt(shs[0]) - 1;
			int shBusiness = Integer.parseInt(shs[1]) - 1;
			int shMonthly = Integer.parseInt(shs[2]) - 1;
			ta.append("���ڴ򿪹�����...\n");
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			//temp = temp.substring(0, temp.indexOf(".xls")) + "_new" + temp.substring(temp.indexOf(".xls"));
			FileOutputStream fos;// = new FileOutputStream(temp);
			int ercount = 0;
			Workbook wb;
			if(temp.endsWith(".xls")) {
				POIFSFileSystem fs = new POIFSFileSystem(bis);
				bis.close();
				wb = new HSSFWorkbook(fs);
			} else {
				wb = new XSSFWorkbook(bis);
				bis.close();
			}
			Cell cell = null;
			int snum = wb.getNumberOfSheets();
			ta.append("һ����"+snum+"��������\n���ڴ���...\n");
			for (int i = 0; i < snum; i++) {
				Sheet st = wb.getSheetAt(i);
				if(st.getLastRowNum() < 2) {
					ta.append("�� "+(i+1)+" ��������û������\n");
					continue;
				}
				if(auto[1]) {
					shBranch = shBusiness = shMonthly = -1;
					if(wb.getSheetName(i).contains("����")) shBranch = i;
					if(wb.getSheetName(i).contains("ҵ��")) shBusiness = i;
					if(wb.getSheetName(i).contains("�½�")) shMonthly = i;
				}
				if(shBranch == -1 && shBusiness == -1 && shMonthly == -1) {
					ta.append("Error: �� "+(i+1)+" �����������Ʋ�����'����' 'ҵ��' '�½�'֮һ\n");
					continue;
				}
				Row row;
				if(auto[0]) {
					dest = weight = trsfare = -1;
					row = st.getRow(0);
					int tempRowSize = row.getLastCellNum();
					for(int j=0; j<tempRowSize; j++) {
						cell = row.getCell(j);
						if(cell.getStringCellValue().contains("Ŀ�ĵ�")) dest = j;
						if(cell.getStringCellValue().contains("����")) weight = j;
						if(cell.getStringCellValue().contains("��ת��")) trsfare = j;
					}
				}
				if(dest == -1 || weight == -1 || trsfare == -1) {
					ta.append("Error: �� "+(i+1)+" ��������ȱ�� 'Ŀ�ĵ�' '����' '��ת��' �ֶ�֮һ\n");
					continue;
				}
				DataFormat format = wb.createDataFormat();
				CellStyle style = wb.createCellStyle();
				style.setAlignment(CellStyle.ALIGN_CENTER);
				//style.cloneStyleFrom(cs);
				style.setDataFormat(format.getFormat("0.00_ "));
				
				for(rowIdx=1; rowIdx<=st.getLastRowNum(); rowIdx++) {
					if(rowIdx%50 == 49) lb.setText("���ڴ���� "+(rowIdx+1)+" / "+(st.getLastRowNum()+1)+" ��");
					//System.out.println(j);
					row = st.getRow(rowIdx);
					try {
						if(row.getCell(dest) == null && row.getCell(weight) == null) continue;
					} catch (Exception e) {
						continue;
					}
					cell = row.getCell(dest);
					double fare = -1;
					String desttemp = "";
					if(cell == null) {
						Cell c = row.getCell(0);
						if(c == null) break;
						if(c.getCellType() == Cell.CELL_TYPE_STRING && c.getStringCellValue().length() == 0) break;
						fare = -6;
					} else if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
						desttemp = cell.getStringCellValue();
						if(desttemp==null || desttemp.length()==0) fare = -6;
						else {
							desttemp = Strings.getProvince(desttemp);
							cell.setCellValue(desttemp);
							cell = row.getCell(weight);
							if(cell == null) fare = -7;
							else if(cell.getCellType() != Cell.CELL_TYPE_NUMERIC) fare = -8;
							else {
								double wei = cell.getNumericCellValue();
								if(i==shBranch) fare = Strings.transferFare(wei, desttemp, 1, UI2.config[6]);
								else if(i==shBusiness) fare = Strings.transferFare(wei, desttemp, 2, UI2.config[6]);
								else if(i==shMonthly) fare = Strings.transferFare(wei, desttemp, 3, UI2.config[6]);
								else fare = -1;
							}
						}
					} else {
						fare = -9;
						System.out.println(rowIdx+":"+cell.getCellType());
					}
					
					if((cell = row.getCell(trsfare)) == null) 
					//if(row.getCell(trsfare) == null)
						cell = row.createCell(trsfare);
					if(fare == -1) {
						cell.setCellValue("Error: �Ҳ�����Ӧ��ʡ��");
						ercount++;
					} else if(fare == -2) {
						cell.setCellValue("Error: ����Ϊ0");
						ercount++;
					} else if(fare == -3) {
						cell.setCellValue("Error: ���۱����ڻ��ʽ����");
						ercount++;
					} else if(fare == -4) {
						cell.setCellValue("Error: ���۱�����1����Ϊ0");
						ercount++;
					} else if(fare == -5) {
						cell.setCellValue("Error: ���۱�����1����Ϊ0");
						ercount++;
					} else if(fare == -6) {
						cell.setCellValue("Error: Ŀ�ĵ�Ϊ��");
						ercount++;
					} else if(fare == -7) {
						cell.setCellValue("Error: ����Ϊ��");
						ercount++;
					} else if(fare == -8) {
						cell.setCellValue("Error: �����������ָ�ʽ");
						ercount++;
					} else if(fare == -9) {
						cell.setCellValue("Error: Ŀ�ĵز����ַ���ʽ");
						ercount++;
					} else if(fare == -10) {
						cell.setCellValue("Error: ���ڵ�3���أ��޷�ʹ�øù������");
						ercount++;
					} else if(fare == -11) {
						cell.setCellValue("Error: ���۱��2��������Ϊ0");
						ercount++;
					}
					else {
						cell.setCellStyle(style);
						cell.setCellValue(fare);
					}
				}
			}
			if(ercount > 0) ta.append("һ����" + ercount + "������\n");
			lb.setText("���ڱ���...");
			fos = new FileOutputStream(temp+".bak");
			wb.write(fos);
			fos.close();
			File file = new File(temp);
			file.delete();
			File file2 = new File(temp+".bak");
			file2.renameTo(file);
			ta.append("�ļ��ѱ�����" + temp + "\n\n");
		} catch (Exception e) {
			ta.append("��ŷ�������ˣ�����ϵ����\n");
			System.out.println(rowIdx);
			if(rowIdx > -1) ta.append("���Ƶ� "+(rowIdx+1)+" �г��ִ���\n");
			e.printStackTrace();
			StackTraceElement[] el = e.getStackTrace();
			try {
				String name = "log"+System.currentTimeMillis()+".txt";
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name)));
				for(int i=0; i<el.length; i++) {
					writer.write(el[i]+"\r\n");
					//ta.append(el[i]+"\n");
				}
				ta.append("������Ϣ�ѱ�����"+name+"\n");
				writer.close();
			} catch (Exception e1) {
				ta.append("������Ϣ����ʧ�ܡ�\n");
			}
			finally {ta.append("\n");}
		}
	}
}
