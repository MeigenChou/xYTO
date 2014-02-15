package addIn;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import util.Excel;

public class Sort {
	public static void sortWorkbooks(File f, JTextArea ta, JLabel lb, String path) {
		String temp = f.getPath();
		if(!temp.endsWith(".xls") && !temp.endsWith(".xlsx")) {
			ta.append("Error: 文件不是Excel工作簿类型\n");
			return;
		}
		int rowIdx = -1;
		try {
			ta.append("正在打开工作簿...\n");
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			FileOutputStream fos = null;
			Workbook wb;
			if(temp.endsWith(".xls")) {
				if(JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(null, ".xls格式工作簿存在兼容问题，且容易出现各种错误，是否继续？", "打开xls工作簿", JOptionPane.YES_NO_OPTION)) {
					ta.append("已取消打开工作簿。\n");
					bis.close();
					return;
				}
				POIFSFileSystem fs = new POIFSFileSystem(bis);
				wb = new HSSFWorkbook(fs);
			} else {
				wb = new XSSFWorkbook(bis);
			}
			bis.close();
			Cell cell = null;
			final int snum = wb.getNumberOfSheets();
			ta.append("一共有"+snum+"个工作表\n正在处理...\n");
			JPanel p = new JPanel(new GridLayout(snum/2+1, 2, 2, 2));
			final JCheckBox[] jcb = new JCheckBox[snum];
			for(int i=0; i<snum; i++) {
				jcb[i] = new JCheckBox(wb.getSheetName(i));
				p.add(jcb[i]);
			}
			final JCheckBox jcba = new JCheckBox("全部选择");
			p.add(jcba);
			jcba.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					for(int i=0; i<snum; i++) jcb[i].setSelected(jcba.isSelected());
				}
			});
			if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, p, "请选择要保存的工作表", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)) {
				for(int i=0; i<snum; i++) {
					if(jcb[i].isSelected()) {
						Sheet srcSt = wb.getSheetAt(i);
						if(srcSt.getLastRowNum()-srcSt.getFirstRowNum()<1) ta.append("第 "+(i+1)+" 个工作表没有内容\n");
						else {
							Workbook distWb;
							if(wb instanceof HSSFWorkbook) {
								distWb = new HSSFWorkbook();
							} else {
								distWb = new XSSFWorkbook();
							}
							CellStyle center = null;
							center = distWb.createCellStyle();
							center.setAlignment(CellStyle.ALIGN_CENTER);
							Font font = distWb.createFont();
							font.setFontHeightInPoints((short) 16);
							font.setBoldweight(Font.BOLDWEIGHT_BOLD);
							center.setFont(font);
							Sheet distSt = null;
							String last = null;
							int count = 0;
							int rowCount = 0;
							String stName = wb.getSheetName(i);
							File ftemp = new File(path+File.separator+"分类保存"+stName);
							if(!ftemp.exists()) ftemp.mkdirs();
							Row firstRow = srcSt.getRow(0);
							int numCell = -1, lastCell = -1;
							for(int j = firstRow.getFirstCellNum(); j<firstRow.getLastCellNum(); j++) {
								Cell c = firstRow.getCell(j);
								if(c.getCellType() == Cell.CELL_TYPE_STRING) {
									if(c.getStringCellValue().contains("编号")) numCell = j;
									if(c.getStringCellValue().contains("分类标识")) lastCell = j;
								}
							}
							if(numCell == -1 || lastCell == -1) {
								ta.append("第 "+(i+1)+" 个工作表缺少 '编号' 和(或) '分类标识' 字段\n");
								continue;
							}
							int lastRowIdx = srcSt.getLastRowNum();
							for(rowIdx = srcSt.getFirstRowNum()+1; rowIdx<=lastRowIdx; rowIdx++) {
								Row srcRow = srcSt.getRow(rowIdx);
								cell = srcRow.getCell(numCell);
								if(cell != null && cell.getCellType()==1) {
									String s = cell.getStringCellValue();
									if(s!=null && s.length()!=0) {
										if(last == null) {
											distSt = distWb.createSheet(s);
											distSt.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
											Cell firstCell = distSt.createRow(0).createCell(0);
											firstCell.setCellValue(stName+"  "+s+"中转费账单");
											firstCell.setCellStyle(center);
											Excel.copyRow(firstRow, distSt.createRow(1), wb, distWb, rowIdx-rowCount, lastCell);
											for(int k=0; k<10; k++)
												distSt.setColumnWidth(k, srcSt.getColumnWidth(k));
											count++;
											rowCount = 1;
											last = s;
										} else if(!s.equals(last)) {
											if(s.startsWith("总计")) break;
											lb.setText("正在保存第 "+count+" 个文件...");
											save(distWb, distSt, temp, fos, path+File.separator+"分类保存"+stName+File.separator+stName+last);
											if(wb instanceof HSSFWorkbook) {
												distWb = new HSSFWorkbook();
											} else {
												distWb = new XSSFWorkbook();
											}
											center = distWb.createCellStyle();
											center.setAlignment(CellStyle.ALIGN_CENTER);
											font = distWb.createFont();
											font.setFontHeightInPoints((short) 16);
											font.setBoldweight(Font.BOLDWEIGHT_BOLD);
											center.setFont(font);
											distSt = distWb.createSheet(s);
											distSt.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
											Cell firstCell = distSt.createRow(0).createCell(0);
											firstCell.setCellValue(stName+"  "+s+"中转费账单");
											firstCell.setCellStyle(center);
											Excel.copyRow(firstRow, distSt.createRow(1), wb, distWb, rowIdx-rowCount, lastCell);
											for(int k=0; k<10; k++)
												distSt.setColumnWidth(k, srcSt.getColumnWidth(k));
											count++;
											rowCount = 1;
											last = s;
										}
									}
								}
								Row distRow = distSt.createRow(++rowCount);
								Excel.copyRow(srcRow, distRow, wb, distWb, rowIdx-rowCount, lastCell);
								lb.setText("正在处理第 "+count+" 个文件 ( 第 "+(rowIdx+1)+" / "+lastRowIdx+" 行)");
							}
							lb.setText("正在保存第 "+count+" 个文件...");
							save(distWb, distSt, temp, fos, path+File.separator+"分类保存"+stName+File.separator+stName+last);
							ta.append("第 "+(i+1)+" 个工作表共保存 "+count+" 个文件，在 "+ftemp.getPath()+" 文件夹下\n");
						}
					}
				}
				ta.append("处理完毕\n");
			} else ta.append("没选中任何工作表\n");
		} catch (Exception e) {
			ta.append("啊欧，出错了，请联系作者\n");
			e.printStackTrace();
			//System.out.println(rowIdx);
			ta.append("疑似第 "+(rowIdx+1)+" 行出现错误\n");
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
	
	static void save(Workbook wb, Sheet st, String path, FileOutputStream fos, String fname) throws Exception {
		int lastRow = st.getLastRowNum();
		st.addMergedRegion(new CellRangeAddress(lastRow, lastRow, 1, 5));
		st.addMergedRegion(new CellRangeAddress(lastRow, lastRow, 6, 7));
		st.addMergedRegion(new CellRangeAddress(lastRow, lastRow, 8, 9));
		st.addMergedRegion(new CellRangeAddress(lastRow-1, lastRow-1, 2, 3));
		st.addMergedRegion(new CellRangeAddress(lastRow-1, lastRow-1, 4, 5));
		CellRangeAddress c = CellRangeAddress.valueOf("A2:J2");
		st.setAutoFilter(c);
		st.createFreezePane(0, 2);
		if(path.endsWith(".xls"))
			fos = new FileOutputStream(fname+".xls");
		else fos = new FileOutputStream(fname+".xlsx");
		wb.write(fos);
		fos.close();
		fos = null;
	}
	
}
