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
			jl.setText("正在打开工作簿...");
			if(temp.endsWith(".xls")) {
				POIFSFileSystem pfs = new POIFSFileSystem(bis);
				bis.close();
				wb = new HSSFWorkbook(pfs);
			} else {
				wb = new XSSFWorkbook(bis);
				bis.close();
			}
			jl.setText("正在合并第 1 个工作簿，共 "+num+" 个");
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
				jl.setText("正在合并第 "+(i+1)+" 个工作簿，共 "+num+" 个");
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
			jl.setText("正在保存...");
			wb.write(fos);
			fos.close();
			jl.setText("文件已保存在"+fname);
		} catch (Exception e) {
			jl.setText("啊欧，出错了，请联系作者");
			StackTraceElement[] el = e.getStackTrace();
			try {
				String name = "log"+System.currentTimeMillis()+".txt";
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name)));
				for(int i=0; i<el.length; i++) {
					writer.write(el[i]+"\r\n");
				}
				writer.close();
				jl.setText("啊欧，出错了，请联系作者（错误信息已保存在"+name+"）");
			} catch (Exception e1) { }
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根

	}

}
