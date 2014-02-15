package ui;

import java.awt.Font;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;

import addIn.ManageFee;
import addIn.Merge;
import addIn.Sort;
import addIn.TransferFare;
import addIn.Verification;

public class UI2 extends JFrame {
	private static final long serialVersionUID = 8086543927126040839L;

	private JScrollPane[] jsp = new JScrollPane[6];
	private JTextArea[] jta = new JTextArea[5];
	private JLabel[] jlb = new JLabel[19];
	private JTextField[] jtf = new JTextField[13];
	private JCheckBox[] jcb = new JCheckBox[4];
	private JTabbedPane tabs;
	private JPanel[] jPanel = new JPanel[6];
	private JList jList;
	private JButton[] jButton;
	private JRadioButton[] jrb = new JRadioButton[4];
	DefaultListModel model = new DefaultListModel();
	private Font font;
	private static boolean[] auto = new boolean[4];
	private ArrayList<File> files = new ArrayList<File>();
	private ArrayList<File> fs = new ArrayList<File>();
	public static int[] config = new int[10];
	private static String[] confStr = new String[2];
	
	public UI2() {
		initComponents();
	}
	
	private void initComponents() {
		//getContentPane().setLayout(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		font = new Font("Microsoft Yahei", 0, 12);
		UIManager.put("Label.font", font);
	    UIManager.put("Button.font", font);
	    UIManager.put("ComboBox.font", font);
	    UIManager.put("List.font", font);
	    UIManager.put("CheckBox.font", font);
	    UIManager.put("RadioButton.font", font);
	    Font fon2 = new Font("", 0, 12);
	    UIManager.put("TextField.font", fon2);
	    UIManager.put("TextArea.font", fon2);
	    UIManager.put("Table.font", fon2);
		tabs = new JTabbedPane();
		String[] tags = {"中转费", "合并数据", "分类保存", "核销面单", "管理费", "关于"};
		for(int i=0; i<jPanel.length; i++) {
			jPanel[i] = new JPanel();
			jPanel[i].setLayout(null);
			tabs.add(tags[i], jPanel[i]);
		}
		tabs.setFont(font);
		getContentPane().add(tabs);
		jlb[0] = new JLabel("目的地所在列数：");
		jPanel[0].add(jlb[0]);
		jlb[0].setBounds(5, 5, 105, 18);
		jtf[0] = new JTextField(2);
		jtf[0].setText(""+config[0]);
		jtf[0].getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				setConf(0, jtf[0].getText());
			}
			public void insertUpdate(DocumentEvent e) {
				setConf(0, jtf[0].getText());
			}
			public void changedUpdate(DocumentEvent e) {
				setConf(0, jtf[0].getText());
			}
		});
		jPanel[0].add(jtf[0]);
		jtf[0].setBounds(105, 5, 20, 18);
		jlb[1] = new JLabel("重量所在列数：");
		jPanel[0].add(jlb[1]);
		jlb[1].setBounds(5, 24, 105, 18);
		jtf[1] = new JTextField(2);
		jtf[1].setText(""+config[1]);
		jPanel[0].add(jtf[1]);
		jtf[1].setBounds(105, 24, 20, 18);
		jtf[1].getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				setConf(1, jtf[1].getText());
			}
			public void insertUpdate(DocumentEvent e) {
				setConf(1, jtf[1].getText());
			}
			public void changedUpdate(DocumentEvent e) {
				setConf(1, jtf[1].getText());
			}
		});
		jlb[2] = new JLabel("中转费所在列数：");
		jPanel[0].add(jlb[2]);
		jlb[2].setBounds(5, 43, 105, 18);
		jtf[2] = new JTextField(2);
		jtf[2].setText(""+config[2]);
		jPanel[0].add(jtf[2]);
		jtf[2].setBounds(105, 43, 20, 18);
		jtf[2].getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				setConf(2, jtf[2].getText());
			}
			public void insertUpdate(DocumentEvent e) {
				setConf(2, jtf[2].getText());
			}
			public void changedUpdate(DocumentEvent e) {
				setConf(2, jtf[2].getText());
			}
		});
		jcb[0] = new JCheckBox("自动识别");
		jPanel[0].add(jcb[0]);
		jcb[0].setBounds(127, 24, 80, 18);
		jcb[0].setSelected(auto[0]);
		jcb[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				auto[0] = jcb[0].isSelected();
				writeConf();
				for(int i=0; i<3; i++) {
					jlb[i].setEnabled(!auto[0]);
					jtf[i].setEnabled(!auto[0]);
				}
			}
		});
		jlb[3] = new JLabel("网点所在工作表：");
		jPanel[0].add(jlb[3]);
		jlb[3].setBounds(210, 5, 125, 18);
		jtf[3] = new JTextField(2);
		jtf[3].setText(""+config[3]);
		jPanel[0].add(jtf[3]);
		jtf[3].setBounds(330, 5, 20, 18);
		jtf[3].getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				setConf(3, jtf[3].getText());
			}
			public void insertUpdate(DocumentEvent e) {
				setConf(3, jtf[3].getText());
			}
			public void changedUpdate(DocumentEvent e) {
				setConf(3, jtf[3].getText());
			}
		});
		jcb[1] = new JCheckBox("自动识别");
		jPanel[0].add(jcb[1]);
		jcb[1].setBounds(352, 24, 80, 18);
		jcb[1].setSelected(auto[1]);
		jcb[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				auto[1] = jcb[1].isSelected();
				writeConf();
				for(int i=3; i<6; i++) {
					jlb[i].setEnabled(!auto[1]);
					jtf[i].setEnabled(!auto[1]);
				}
			}
		});
		jlb[4] = new JLabel("业务员所在工作表：");
		jPanel[0].add(jlb[4]);
		jlb[4].setBounds(210, 24, 125, 18);
		jtf[4] = new JTextField(2);
		jtf[4].setText(""+config[4]);
		jPanel[0].add(jtf[4]);
		jtf[4].setBounds(330, 24, 20, 18);
		jtf[4].getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				setConf(4, jtf[4].getText());
			}
			public void insertUpdate(DocumentEvent e) {
				setConf(4, jtf[4].getText());
			}
			public void changedUpdate(DocumentEvent e) {
				setConf(4, jtf[4].getText());
			}
		});
		jlb[5] = new JLabel("月结客户所在工作表：");
		jPanel[0].add(jlb[5]);
		jlb[5].setBounds(210, 43, 125, 18);
		jtf[5] = new JTextField(2);
		jtf[5].setText(""+config[5]);
		jPanel[0].add(jtf[5]);
		jtf[5].setBounds(330, 43, 20, 18);
		jtf[5].getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				setConf(5, jtf[5].getText());
			}
			public void insertUpdate(DocumentEvent e) {
				setConf(5, jtf[5].getText());
			}
			public void changedUpdate(DocumentEvent e) {
				setConf(5, jtf[5].getText());
			}
		});
		jsp[0] = new JScrollPane();
		jta[0] = new MyTextArea("将文件拖到这里\n", 0);
		jta[0].setEditable(false);
		if(auto[0]) for(int i=0; i<3; i++) {
			jlb[i].setEnabled(false);
			jtf[i].setEnabled(false);
		}
		if(auto[1]) for(int i=3; i<6; i++) {
			jlb[i].setEnabled(false);
			jtf[i].setEnabled(false);
		}
		jsp[0].setViewportView(jta[0]);
		jPanel[0].add(jsp[0]);
		jsp[0].setBounds(0, 65, 530, 291);
		jlb[13] = new JLabel("就绪");
		jPanel[0].add(jlb[13]);
		jlb[13].setBounds(5, 354, 530, 20);
		jlb[18] = new JLabel("续费计算规则");
		jPanel[0].add(jlb[18]);
		jlb[18].setBounds(438, 4, 120, 20);
		jrb[2] = new JRadioButton("标准");
		jPanel[0].add(jrb[2]);
		jrb[2].setBounds(435, 23, 120, 20);
		jrb[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				config[6] = 1;
				writeConf();
			}
		});
		jrb[3] = new JRadioButton("规则1");
		jPanel[0].add(jrb[3]);
		jrb[3].setBounds(435, 43, 120, 20);
		jrb[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				config[6] = 2;
				writeConf();
			}
		});
		ButtonGroup group1 = new ButtonGroup();
		group1.add(jrb[2]);
		group1.add(jrb[3]);
		jrb[config[6]+1].setSelected(true);
		//合并数据
		jlb[8] = new JLabel("↓将要合并的文件拖到下面↓");
		jPanel[1].add(jlb[8]);
		jlb[8].setBounds(5, 3, 150, 18);
		jList = new MyList(model);
		jsp[1]=new JScrollPane(jList);
		jPanel[1].add(jsp[1]);
		jsp[1].setBounds(0, 22, 529, 264);
		jlb[6] = new JLabel("就绪");
		jPanel[1].add(jlb[6]);
		jlb[6].setBounds(5, 354, 530, 20);
		jButton = new JButton[5];
		jButton[0] = new JButton("删除选中文件");
		jPanel[1].add(jButton[0]);
		jButton[0].setBounds(212, 316, 110, 35);
		jButton[1] = new JButton("开始合并");
		jPanel[1].add(jButton[1]);
		jButton[1].setBounds(426, 316, 100, 35);
		jButton[2] = new JButton("清空全部");
		jPanel[1].add(jButton[2]);
		jButton[2].setBounds(324, 316, 100, 35);
		jButton[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(files.size()==0) return;
				File f = new File(jtf[6].getText());
				if(f.exists()) {
					int i = JOptionPane.showConfirmDialog(null, "文件已存在，是否覆盖？", "提示：", JOptionPane.YES_NO_OPTION);
					if(i == JOptionPane.NO_OPTION) return;
				}
				new Thread() {
					public void run() {
						Merge.mergeWorkbook(jlb[6], files, jtf[6].getText());
					}
				}.start();
			}
		});
		jButton[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.removeAllElements();
				files = new ArrayList<File>();
			}
		});
		jButton[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] idxs = jList.getSelectedIndices();
				for(int i = idxs.length-1; i>=0; i--) {
					model.removeElementAt(idxs[i]);
					files.remove(idxs[i]);
				}
			}
		});
		jrb[0] = new JRadioButton("合并单个工作簿下的所有工作表");
		jPanel[1].add(jrb[0]);
		jrb[0].setBounds(5, 312, 200, 20);
		jrb[1] = new JRadioButton("合并多个工作簿的所有工作表");
		jPanel[1].add(jrb[1]);
		jrb[1].setBounds(5, 332, 200, 20);
		jrb[0].setEnabled(false);
		ButtonGroup group = new ButtonGroup();
		group.add(jrb[0]);
		group.add(jrb[1]);
		jrb[1].setSelected(true);
		jlb[7] = new JLabel("合并到文件：");
		jPanel[1].add(jlb[7]);
		jlb[7].setBounds(5, 290, 80, 18);
		jtf[6] = new JTextField(confStr[0]);
		jPanel[1].add(jtf[6]);
		jtf[6].setBounds(83, 289, 370, 22);
		jtf[6].getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent arg0) {
				confStr[0] = jtf[6].getText();
				writeConf();
			}
			public void insertUpdate(DocumentEvent arg0) {
				confStr[0] = jtf[6].getText();
				writeConf();
			}
			public void changedUpdate(DocumentEvent arg0) {
				confStr[0] = jtf[6].getText();
				writeConf();
			}
		});
		jButton[3] = new JButton("浏览...");
		jPanel[1].add(jButton[3]);
		jButton[3].setBounds(456, 289, 70, 22);
		jButton[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fd = new JFileChooser(".");
				fd.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fd.showSaveDialog(null);
				File f = fd.getSelectedFile();  
				if(f != null) {
					jtf[6].setText(f.getPath());
				}
			}
		});
		//分类保存
		jlb[9] = new JLabel("保存路径：");
		jPanel[2].add(jlb[9]);
		jlb[9].setBounds(5, 4, 65, 18);
		jtf[7] = new JTextField(confStr[1]);
		jPanel[2].add(jtf[7]);
		jtf[7].setBounds(70, 3, 384, 22);
		jButton[4] = new JButton("浏览...");
		jPanel[2].add(jButton[4]);
		jButton[4].setBounds(456, 3, 70, 22);
		jButton[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fd = new JFileChooser(jtf[7].getText());
				fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fd.showSaveDialog(null);
				File f = fd.getSelectedFile();  
				if(f != null) {
					jtf[7].setText(f.getPath());
				}
			}
		});
		jta[1] = new MyTextArea("将文件拖到这里\n", 1);
		jta[1].setEditable(false);
		jsp[2] = new JScrollPane(jta[1]);
		jPanel[2].add(jsp[2]);
		jsp[2].setBounds(0, 28, 530, 328);
		jlb[9] = new JLabel("就绪");
		jPanel[2].add(jlb[9]);
		jlb[9].setBounds(5, 354, 530, 20);
		//核销面单
		jlb[11] = new JLabel("运单号码所在列数：");
		jPanel[3].add(jlb[11]);
		jlb[11].setBounds(5, 2, 116, 18);
		jtf[8] = new JTextField(2);
		jPanel[3].add(jtf[8]);
		jtf[8].setText(""+config[8]);
		jtf[8].setBounds(121, 3, 20, 18);
		jtf[8].getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				System.out.println(jtf[8].getText());
				setConf(8, jtf[8].getText());
			}
			public void insertUpdate(DocumentEvent arg0) {
				System.out.println(jtf[8].getText());
				setConf(8, jtf[8].getText());
			}
			public void removeUpdate(DocumentEvent arg0) {
				System.out.println(jtf[8].getText());
				setConf(8, jtf[8].getText());
			}
		});
		jlb[12] = new JLabel("收件员所在列数：");
		jPanel[3].add(jlb[12]);
		jlb[12].setBounds(160, 2, 104, 18);
		jtf[9] = new JTextField(2);
		jPanel[3].add(jtf[9]);
		jtf[9].setText(""+config[9]);
		jtf[9].setBounds(264, 3, 20, 18);
		jtf[9].getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				System.out.println(jtf[9].getText());
				setConf(9, jtf[9].getText());
			}
			public void insertUpdate(DocumentEvent arg0) {
				setConf(9, jtf[9].getText());
			}
			public void removeUpdate(DocumentEvent arg0) {
				setConf(9, jtf[9].getText());
			}
		});
		jcb[2] = new JCheckBox("自动识别");
		jPanel[3].add(jcb[2]);
		jcb[2].setBounds(310, 2, 80, 18);
		jcb[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				auto[3] = jcb[2].isSelected();
				writeConf();
				for(int i=11; i<13; i++) {
					jlb[i].setEnabled(!jcb[2].isSelected());
					jtf[i-3].setEnabled(!jcb[2].isSelected());
				}
			}
		});
		jcb[2].setSelected(auto[3]);
		if(auto[3]) {
			for(int i=11; i<13; i++) {
				jlb[i].setEnabled(false);
				jtf[i-3].setEnabled(false);
			}
		}
		jta[3] = new MyTextArea("将文件拖到这里\n", 2);
		jta[3].setEditable(false);
		jsp[4] = new JScrollPane(jta[3]);
		jPanel[3].add(jsp[4]);
		jsp[4].setBounds(0, 23, 530, 351);
		//管理费
		jlb[14] = new JLabel("目的地所在列数：");
		jPanel[4].add(jlb[14]);
		jlb[14].setBounds(5, 3, 105, 18);
		jtf[10] = new JTextField(2);
		jtf[10].setText(""+config[0]);
		jPanel[4].add(jtf[10]);
		jtf[10].setBounds(110, 3, 20, 18);
		jlb[15] = new JLabel("重量所在列数：");
		jPanel[4].add(jlb[15]);
		jlb[15].setBounds(145, 3, 95, 18);
		jtf[11] = new JTextField(2);
		jtf[11].setText(""+config[1]);
		jPanel[4].add(jtf[11]);
		jtf[11].setBounds(238, 3, 20, 18);
		jlb[16] = new JLabel("管理费所在列数：");
		jPanel[4].add(jlb[16]);
		jlb[16].setBounds(270, 3, 105, 18);
		jtf[12] = new JTextField(2);
		jtf[12].setText(""+(config[7]));
		jPanel[4].add(jtf[12]);
		jtf[12].setBounds(376, 3, 20, 18);
		jcb[3] = new JCheckBox("自动识别");
		jPanel[4].add(jcb[3]);
		jcb[3].setBounds(440, 3, 80, 18);
		jcb[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				auto[2] = jcb[3].isSelected();
				writeConf();
				for(int i=14; i<17; i++) {
					jlb[i].setEnabled(!auto[2]);
					jtf[i-4].setEnabled(!auto[2]);
				}
			}
		});
		jcb[3].setSelected(auto[2]);
		if(auto[2])
		for(int i=14; i<17; i++) {
			jlb[i].setEnabled(false);
			jtf[i-4].setEnabled(false);
		}
		jta[4] = new MyTextArea("将文件拖到这里\n", 3);
		jta[4].setEditable(false);
		jsp[5] = new JScrollPane(jta[4]);
		jPanel[4].add(jsp[5]);
		jsp[5].setBounds(0, 23, 530, 333);
		jlb[17] = new JLabel("就绪");
		jPanel[4].add(jlb[17]);
		jlb[17].setBounds(5, 354, 530, 20);
		//关于
		jta[2] = new JTextArea();
		jta[2].setText("版本：1.4.1（2014-1-6）\n【中转费】\n"
				+ "参数说明：\n第一个自动识别选中时，将对工作表第一行进行判断，第一行格式如下：\n"
				//+ "┏━━┳━━━━┳━━┳━━━━┳━━┳━━┳━━━━━┳━━┳━━┳━━━━┓\n"
				+ "┃日期┃运单号码┃编号┃(目的地)┃网络┃件数┃称入(重量)┃到付┃备注┃(中转费)┃\n"
				//+ "┗━━┻━━━━┻━━┻━━━━┻━━┻━━┻━━━━━┻━━┻━━┻━━━━┛\n"
				+ "带括号的字段如果缺少则识别失败\n第二个自动识别选中时，将对工作表名称进行判断，\n"
				+ "含有“网点”则计算网点中转费，含有“业务”则计算业务员中转费，\n"
				+ "含有“月结”则计算月结客户中转费；其他情况则不计算中转费\n"
				+ "【合并数据】\n待合并的工作簿一定要同一格式（.xls或.xlsx），如果有不同格式的工作簿，\n"
				+ "则合并失败\n【分类保存】\n工作表第一行必须含有“编号”“分类标识”字段，否则无法分类保存\n"
				+ "该功能对.xls兼容性不太好，请使用.xlsx格式\n"
				+ "【核销面单】\n暂时没什么说明的地方，有再补充\n"
				+ "【管理费】\n无修改目的地功能，如需修改，请将文件拉到中转费\n");
		jta[2].setEditable(false);
		//jta[2].setFont(new Font("simsun", 0, 14));
		jsp[3] = new JScrollPane(jta[2]);
		jPanel[5].add(jsp[3]);
		jsp[3].setBounds(0, 0, 530, 374);
		pack();
		setSize(540, 430);
		setLocationRelativeTo(null);
	}
	
	class MyList extends JList implements DropTargetListener {
		private static final long serialVersionUID = -728042476106993625L;
		public MyList(DefaultListModel model) {
			super(model);
			new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		}
		public void dragEnter(DropTargetDragEvent dtde) { }
		public void dragExit(DropTargetEvent dte) { }
		public void dragOver(DropTargetDragEvent dtde) { }
		public void drop(DropTargetDropEvent dtde) {
			try {
				// Transferable tr = dtde.getTransferable();
				if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					List<?> list = (List<?>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
					Iterator<?> iterator = list.iterator();
					while (iterator.hasNext()) {
						final File f = (File) iterator.next();
						String s = f.getPath();
						if(s.endsWith(".xls") || s.endsWith(".xlsx")) {
							model.addElement(s);
							files.add(f);
						}
					}
					dtde.dropComplete(true);
					this.updateUI();
				} else {
					dtde.rejectDrop();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void dropActionChanged(DropTargetDragEvent dtde) { }
	}
	
	class MyTextArea extends JTextArea implements DropTargetListener {
		private static final long serialVersionUID = -8343684963555583917L;
		private int flag;
		public MyTextArea(String title, int flag) {
			super(title);
			this.flag = flag;
			new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		}
		public void dragEnter(DropTargetDragEvent tde) { }
		public void dragExit(DropTargetEvent tde) { }
		public void dragOver(DropTargetDragEvent tde) { }
		public void drop(DropTargetDropEvent dtde) {
			try {
				// Transferable tr = dtde.getTransferable();
				if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					List<?> list = (List<?>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
					Iterator<?> iterator = list.iterator();
					fs = new ArrayList<File>();
					while (iterator.hasNext()) {
						final File f = (File) iterator.next();
						fs.add(f);
					}
					switch (flag) {
					case 0:
						final String[] cols = {jtf[0].getText(), jtf[1].getText(), jtf[2].getText()};
						final String[] shs = {jtf[3].getText(), jtf[4].getText(), jtf[5].getText()};
						new Thread() {
							public void run() {
								for(int i=0; i<fs.size(); i++) {
									TransferFare.calcTransFare(fs.get(i), MyTextArea.this, jlb[13], cols, shs, auto);
								}
								jlb[13].setText("就绪");
							}
						}.start();
						break;
					case 1:
						new Thread() {
							public void run() {
								for(int i=0; i<fs.size(); i++) {
									Sort.sortWorkbooks(fs.get(i), MyTextArea.this, jlb[9], jtf[7].getText());
									MyTextArea.this.append("\n");
								}
								jlb[9].setText("就绪");
							}
						}.start();
						break;
					case 2:
						final String[] col = {jtf[8].getText(), jtf[9].getText()};
						new Thread() {
							public void run() {
								for(int i=0; i<fs.size(); i++) {
									Verification.verifyBill(fs.get(i), MyTextArea.this, col, jcb[2].isSelected());
									MyTextArea.this.append("\n");
								}
							}
						}.start();
						break;
					case 3:
						final String[] colu = {jtf[10].getText(), jtf[11].getText(), jtf[12].getText()};
						new Thread() {
							public void run() {
								for(int i=0; i<fs.size(); i++) {
									ManageFee.calcManageFee(fs.get(i), MyTextArea.this, jlb[17], colu, jcb[3].isSelected());
									//Verification.verifyBill(fs.get(i), VeriTextArea.this, cols, jcb[2].isSelected());
								}
								jlb[17].setText("就绪");
							}
						}.start();
						break;
					}
					dtde.dropComplete(true);
					this.updateUI();
				} else {
					dtde.rejectDrop();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void dropActionChanged(DropTargetDragEvent arg0) { }
	}
	
	public static void main(String[] args) {
		readConf();
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new UI2().setVisible(true);
			}
		});
	}

	static void reset() {
		config = new int[] {4, 7, 10, 1, 2, 3, 1, 11, 2, 3};
		confStr[0] = "合并.xls";
		confStr[1] = FileSystemView.getFileSystemView().getHomeDirectory().getPath();
		auto[0] = auto[1] = auto[2] = auto[3] = true;
	}
	static void readConf() {
		reset();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("配置.cfg")));
			String line = null;
			while (((line = reader.readLine()) != null)) {
				if(!line.startsWith("!")) {
					if(line.startsWith("dest=\""))
						config[0] = Integer.parseInt(line.substring(6, line.length()-1));
					if(line.startsWith("weight=\"")) 
						config[1] = Integer.parseInt(line.substring(8, line.length()-1));
					if(line.startsWith("fare=\""))
						config[2] = Integer.parseInt(line.substring(6, line.length()-1));
					if(line.startsWith("auto1=\"")) 
						auto[0] = Boolean.parseBoolean(line.substring(7, line.length()-1));
					if(line.startsWith("branch=\""))
						config[3] = Integer.parseInt(line.substring(8, line.length()-1));
					if(line.startsWith("sales=\""))
						config[4] = Integer.parseInt(line.substring(7, line.length()-1));
					if(line.startsWith("month=\""))
						config[5] = Integer.parseInt(line.substring(7, line.length()-1));
					if(line.startsWith("auto2=\""))
						auto[1] = Boolean.parseBoolean(line.substring(7, line.length()-1));
					if(line.startsWith("rule=\""))
						config[6] = Integer.parseInt(line.substring(6, line.length()-1));
					if(line.startsWith("filepath=\""))
						confStr[0] = line.substring(10, line.length()-1);
					if(line.startsWith("savepath=\""))
						confStr[1] = line.substring(10, line.length()-1);
					if(line.startsWith("manag=\"")) 
						config[7] = Integer.parseInt(line.substring(7, line.length()-1));
					if(line.startsWith("auto3=\""))
						auto[2] = Boolean.parseBoolean(line.substring(7, line.length()-1));
					if(line.startsWith("billnum=\""))
						config[8] = Integer.parseInt(line.substring(9, line.length()-1));
					if(line.startsWith("clerk=\""))
						config[9] = Integer.parseInt(line.substring(7, line.length()-1));
					if(line.startsWith("auto4=\""))
						auto[3] = Boolean.parseBoolean(line.substring(7, line.length()-1));
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void writeConf() {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("配置.cfg")));
			writer.write("!!!配置文件，请勿随意修改!!!\r\n");
			writer.write("dest=\""+config[0]+"\"\r\n");
			writer.write("weight=\""+config[1]+"\"\r\n");
			writer.write("fare=\""+config[2]+"\"\r\n");
			writer.write("auto1=\""+auto[0]+"\"\r\n");
			writer.write("branch=\""+config[3]+"\"\r\n");
			writer.write("sales=\""+config[4]+"\"\r\n");
			writer.write("month=\""+config[5]+"\"\r\n");
			writer.write("auto2=\""+auto[1]+"\"\r\n");
			writer.write("rule=\""+config[6]+"\"\r\n");
			writer.write("filepath=\""+confStr[0]+"\"\r\n");
			writer.write("savepath=\""+confStr[1]+"\"\r\n");
			writer.write("manag=\""+config[7]+"\"\r\n");
			writer.write("auto3=\""+auto[2]+"\"\r\n");
			writer.write("billnum=\""+config[8]+"\"\r\n");
			writer.write("clerk=\""+config[9]+"\"\r\n");
			writer.write("auto4=\""+auto[3]+"\"\r\n");
			writer.close();
		} catch (Exception e) { }
	}
	
	static void setConf(int con, String str) {
		try {
			config[con] = Integer.parseInt(str);
			writeConf();
		} catch (Exception e) { }
	}
}
