package addIn;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import util.Strings;

public class ManageFee {
	public static void calcManageFee(File f, JTextArea ta, JLabel lb, String[] cols, boolean auto) {
		String temp = f.getPath();
		if(!temp.endsWith(".xls") && !temp.endsWith(".xlsx")) {
			ta.append("Error: �ļ�����Excel����������\n\n");
			return;
		}
		int rowIdx = -1;
		try {
			int dest = Integer.parseInt(cols[0]) - 1;
			int weight = Integer.parseInt(cols[1]) - 1;
			int mngfare = Integer.parseInt(cols[2]) - 1;
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
				if(st.getLastRowNum() == 0) {
					ta.append("�� "+(i+1)+" ��������û������\n");
					continue;
				}
				Row row;
				if(auto) {
					dest = weight = mngfare = -1;
					row = st.getRow(0);
					int tempRowSize = row.getLastCellNum();
					for(int j=0; j<tempRowSize; j++) {
						cell = row.getCell(j);
						if(cell.getStringCellValue().contains("Ŀ�ĵ�")) dest = j;
						if(cell.getStringCellValue().contains("����")) weight = j;
						if(cell.getStringCellValue().contains("�����")) mngfare = j;
					}
				}
				if(dest == -1 || weight == -1 || mngfare == -1) {
					ta.append("Error: �� "+(i+1)+" ��������ȱ�� 'Ŀ�ĵ�' '����' '�����' �ֶ�֮һ\n");
					continue;
				}
				DataFormat format = wb.createDataFormat();
				CellStyle style = wb.createCellStyle();
				style.setAlignment(CellStyle.ALIGN_CENTER);
				//style.cloneStyleFrom(cs);
				style.setDataFormat(format.getFormat("0.00_ "));
				for(rowIdx=1; rowIdx<=st.getLastRowNum(); rowIdx++) {
					if(rowIdx%50 == 49) lb.setText("���ڴ���� "+(rowIdx+1)+" / "+(st.getLastRowNum()+1)+" ��");
					row = st.getRow(rowIdx);
					cell = row.getCell(dest);
					double fare = -1;
					if(cell == null) {
						Cell c = row.getCell(0);
						if(c == null) break;
						if(c.getCellType() == Cell.CELL_TYPE_STRING && c.getStringCellValue().length() == 0) break;
						fare = -6;
					} else if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
						String desttemp = cell.getStringCellValue();
						if(desttemp==null || desttemp.length()==0) fare = -6;
						else {
							cell = row.getCell(weight);
							if(cell == null) fare = -7;
							else if(cell.getCellType() != Cell.CELL_TYPE_NUMERIC) fare = -8;
							else {
								double wei = cell.getNumericCellValue();
								fare = Strings.manageFee(wei, desttemp);
							}
						}
					} else {
						fare = -9;
						System.out.println(rowIdx+":"+cell.getCellType());
					}
					cell = row.createCell(mngfare);
					if(fare == -1) {
						cell.setCellValue("Error: �Ҳ�����Ӧ��ʡ��");
						ercount++;
					} else if(fare == -2) 
						cell.setCellValue("����Ϊ0");
					else if(fare == -3) {
						cell.setCellValue("Error: ���۱����ڻ��ʽ����");
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
