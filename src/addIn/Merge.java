package addIn;

import java.io.*;
import java.util.ArrayList;

import javax.swing.JLabel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import util.Excel;

public class Merge {
	public static void mergeWorkbook(JLabel jl, ArrayList<File> fs, String fname) {
		int num = fs.size();
		String temp = fs.get(0).getPath();
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fs.get(0)));
			FileOutputStream fos = new FileOutputStream(fname);
			Workbook wb;
			jl.setText("���ڴ򿪹�����...");
			if(temp.endsWith(".xls")) {
				POIFSFileSystem pfs = new POIFSFileSystem(bis);
				bis.close();
				wb = new HSSFWorkbook(pfs);
			} else {
				wb = new XSSFWorkbook(bis);
				bis.close();
			}
			jl.setText("���ںϲ��� 1 ������������ "+num+" ��");
			int snum = wb.getNumberOfSheets();
			Sheet st = wb.getSheetAt(0);
			Row row = st.getRow(1);
			Cell cell = row.getCell(0);
			for(int i=1; i<snum; i++) {
				Sheet srcSt = wb.getSheetAt(i);
				Excel.copySheet(srcSt, st, cell, 1);
			}
			if(snum > 1) {
				for(int i=snum-1; i>0; i--) wb.removeSheetAt(i);
			}
			for(int i=1; i<num; i++) {
				jl.setText("���ںϲ��� "+(i+1)+" ������������ "+num+" ��");
				Workbook wb2;
				temp = fs.get(i).getPath();
				bis = new BufferedInputStream(new FileInputStream(fs.get(i)));
				if(temp.endsWith(".xls")) {
					POIFSFileSystem pfs = new POIFSFileSystem(bis);
					bis.close();
					wb2 = new HSSFWorkbook(pfs);
				} else {
					wb2 = new XSSFWorkbook(bis);
					bis.close();
				}
				snum = wb2.getNumberOfSheets();
				for(int j=0; j<snum; j++) {
					Sheet srcSt = wb2.getSheetAt(j);
					Excel.copySheet(srcSt, st, cell, 1);
				}
			}
			jl.setText("���ڱ���...");
			wb.write(fos);
			fos.close();
			jl.setText("�ļ��ѱ�����"+fname);
		} catch (Exception e) {
			jl.setText("��ŷ�������ˣ�����ϵ����");
			StackTraceElement[] el = e.getStackTrace();
			try {
				String name = "log"+System.currentTimeMillis()+".txt";
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name)));
				for(int i=0; i<el.length; i++) {
					writer.write(el[i]+"\r\n");
				}
				writer.close();
				jl.setText("��ŷ�������ˣ�����ϵ���ߣ�������Ϣ�ѱ�����"+name+"��");
			} catch (Exception e1) { }
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO �Զ����ɵķ������

	}

}
