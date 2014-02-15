package util;

import java.util.regex.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;

public class Excel {
	public static void copyCell(Cell srcCell, Cell distCell, Workbook srcWb, Workbook distWb, int rowDif) {
		if(srcCell == null) {
			CellStyle ncs = distWb.createCellStyle();
			ncs.setBorderBottom(CellStyle.BORDER_THIN);
			ncs.setBorderLeft(CellStyle.BORDER_THIN);
			ncs.setBorderRight(CellStyle.BORDER_THIN);
			ncs.setBorderTop(CellStyle.BORDER_THIN);
			distCell.setCellStyle(ncs);
			return;
		}
		CellStyle newStyle = distWb.createCellStyle();
		CellStyle scs = srcCell.getCellStyle();
		//字体
		Font sfont = srcWb.getFontAt(scs.getFontIndex());
		Font font = distWb.createFont();
		font.setBoldweight(sfont.getBoldweight());
		font.setFontHeightInPoints(sfont.getFontHeightInPoints());
		font.setFontName(sfont.getFontName());
//		font.setColor(sfont.getColor());
//		font.setItalic(sfont.getItalic());
//		font.setUnderline(sfont.getUnderline());
		newStyle.setFont(font);
		//单元格格式
		String dfs = scs.getDataFormatString();
		System.out.println(dfs);
		if(dfs != null) newStyle.setDataFormat(distWb.createDataFormat().getFormat(dfs));
		newStyle.setAlignment(scs.getAlignment());
		newStyle.setBorderBottom(CellStyle.BORDER_THIN);
		newStyle.setBorderLeft(CellStyle.BORDER_THIN);
		newStyle.setBorderRight(CellStyle.BORDER_THIN);
		newStyle.setBorderTop(CellStyle.BORDER_THIN);
		//newStyle.cloneStyleFrom(srcCell.getCellStyle());
		
		distCell.setCellStyle(newStyle);
		//批注
		Comment com = srcCell.getCellComment();
		if(com != null) {
			Drawing patr = distWb.getSheetAt(0).createDrawingPatriarch();
			Comment comment;
			if(srcCell instanceof HSSFCell) {
				comment = patr.createCellComment(new HSSFClientAnchor(0, 0, 0, 0, (short)1, 1, (short)3, 5));
			} else comment = patr.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, 1, 1, 2, 5));
			comment.setString(com.getString());
			comment.setAuthor(com.getAuthor());
			distCell.setCellComment(comment);
		}
		
		int srcCellType = srcCell.getCellType();
		distCell.setCellType(srcCellType);
		switch (srcCellType) {
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(srcCell)) {
				distCell.setCellValue(srcCell.getDateCellValue());
			} else {
				distCell.setCellValue(srcCell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_STRING:
			distCell.setCellValue(srcCell.getRichStringCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			distCell.setCellValue(srcCell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			distCell.setCellErrorValue(srcCell.getErrorCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			String formula = srcCell.getCellFormula();
			System.out.print(formula);
			Pattern p = Pattern.compile("([A-Z]\\d+)");
			Matcher m = p.matcher(formula);
			while (m.find()) {
				String s = m.group();
				System.out.print("\t"+s);
				int n = Integer.parseInt(s.substring(1)) - rowDif;
				formula = formula.replaceFirst(s, s.substring(0, 1)+n);
			}
			System.out.println("\t"+formula);
			try {
				distCell.setCellFormula(formula);
			} catch (Exception e) {
				try {
					double num = srcCell.getNumericCellValue();
					distCell.setCellType(Cell.CELL_TYPE_NUMERIC);
					distCell.setCellValue(num);
				} catch (Exception e2) {
					//distCell.setCellValue(srcCell.getRichStringCellValue());
				}
			}
			break;
		default:
			break;
		}
	}

	public static void copyCell(Cell srcCell, Cell distCell, Cell rCell) {
		if(srcCell == null) return;
		if(rCell != null) distCell.setCellStyle(rCell.getCellStyle());
//		if(srcCell.getCellComment() != null) {
//			distCell.setCellComment(rCell.getCellComment());        
//		}
		int srcCellType = srcCell.getCellType();
		distCell.setCellType(srcCellType);
		switch (srcCellType) {
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(srcCell)) {
				distCell.setCellValue(srcCell.getDateCellValue());
			} else {
				distCell.setCellValue(srcCell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_STRING:
			distCell.setCellValue(srcCell.getRichStringCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			distCell.setCellValue(srcCell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			distCell.setCellErrorValue(srcCell.getErrorCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			distCell.setCellFormula(srcCell.getCellFormula());
			break;
		default:
			break;
		}
	}

	public static void copyCell(Cell srcCell, Cell distCell, CellStyle cs, Workbook wb, int rowDif) {
		distCell.setCellStyle(cs);
		if(srcCell == null) return;
		Comment com = srcCell.getCellComment();
		if(com != null) {
			Drawing patr = wb.getSheetAt(0).createDrawingPatriarch();
			Comment comment;
			if(srcCell instanceof HSSFCell) {
				comment = patr.createCellComment(new HSSFClientAnchor(0, 0, 0, 0, (short)1, 1, (short)3, 5));
			} else comment = patr.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, 1, 1, 2, 5));
			comment.setString(com.getString());
			comment.setAuthor(com.getAuthor());
			distCell.setCellComment(comment);
		}
		
		int srcCellType = srcCell.getCellType();
		distCell.setCellType(srcCellType);
		switch (srcCellType) {
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(srcCell)) {
				distCell.setCellValue(srcCell.getDateCellValue());
			} else {
				distCell.setCellValue(srcCell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_STRING:
			distCell.setCellValue(srcCell.getRichStringCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			distCell.setCellValue(srcCell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			distCell.setCellErrorValue(srcCell.getErrorCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			String formula = srcCell.getCellFormula();
			Pattern p = Pattern.compile("([A-Z]\\d+)");
			Matcher m = p.matcher(formula);
			while (m.find()) {
				String s = m.group();
				int n = Integer.parseInt(s.substring(1)) - rowDif;
				formula = formula.replaceFirst(s, s.substring(0, 1)+n);
			}
			distCell.setCellFormula(formula);
			break;
		default:
			break;
		}
	}
	
	public static void copyRow(Row srcRow, Row distRow, Cell rCell) {
		int right = srcRow.getLastCellNum();
		for(int i=srcRow.getFirstCellNum(); i<right; i++) {
			Cell distCell = distRow.createCell(i);
			Cell srcCell = srcRow.getCell(i);
			copyCell(srcCell, distCell, rCell);
		}
	}

	/**
	 * 行复制功能
	 * @param fromRow
	 * @param toRow
	 */
	public static void copyRow(Row fromRow, Row toRow, Workbook srcWb, Workbook wb, int rowDif) {
		copyRow(fromRow, toRow, srcWb, wb, rowDif, fromRow.getLastCellNum());
	}

	public static void copyRow(Row fromRow, Row toRow, Workbook srcWb, Workbook distWb, int rowDif, int end) {
		for(int i=0; i<end; i++) {
			Cell tempCell = fromRow.getCell(i);
			Cell newCell = toRow.createCell(i);
//			if(i == end-1) {
//				System.out.println(tempCell.getCellStyle().getDataFormatString());
//			}
			copyCell(tempCell, newCell, srcWb, distWb, rowDif);
		}
		//System.out.println();
	}
	
	public static void copyRow(Row srcRow, Row distRow, CellStyle[] cs, Workbook wb, int rowDif) {
		copyRow(srcRow, distRow, cs, wb, rowDif, srcRow.getLastCellNum());
	}
	
	public static void copyRow(Row srcRow, Row distRow, CellStyle[] cs, Workbook wb, int rowDif, int end) {
		for(int i=0; i<end; i++) {
			Cell tempCell = srcRow.getCell(i);
			Cell newCell = distRow.createCell(i);
			copyCell(tempCell, newCell, cs[i], wb, rowDif);
		}
	}

	public static void copySheet(Sheet srcSt, Sheet distSt, Cell rCell, int startRow) {
		int srcRowNum = srcSt.getLastRowNum();
		int lastRowNum = distSt.getLastRowNum();
		for(int i=startRow; i<=srcRowNum; i++) {
			Row distRow = distSt.createRow(i+lastRowNum);
			Row srcRow = srcSt.getRow(i);
			copyRow(srcRow, distRow, rCell);
		}
	}
	
	/**
	 * 复制原有sheet的合并单元格到新创建的sheet
	 * 
	 * @param sheetCreat 新创建sheet
	 * @param sheet      原有的sheet
	 */
	public static void mergerRegion(Sheet fromSheet, Sheet toSheet) {
		int sheetMergerCount = fromSheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergerCount; i++) {
			CellRangeAddress mergedRegionAt = fromSheet.getMergedRegion(i);
			toSheet.addMergedRegion(mergedRegionAt);
		}
	}
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根

	}

}
