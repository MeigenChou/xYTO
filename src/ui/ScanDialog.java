package ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

import static addIn.Verification.*;

public class ScanDialog extends JFrame {
	private static final long serialVersionUID = -2854515549698101972L;
	private Font font = new Font("Microsoft Yahei", 0, 23);
	public ScanDialog() {
		getContentPane().setLayout(new BorderLayout());
		//setResizable(false);
		final JLabel l1 = new JLabel("�ռ�Ա��");
		final JLabel l2 = new JLabel("ɨ��״̬��");
		JLabel l3 = new JLabel("ɨ����");
		final JLabel l4 = new JLabel("��ɨ��");
		l3.setFont(font);
		final JTextField tf = new JTextField(13);
		JPanel jp1 = new JPanel(new GridLayout(3, 1, 2, 2));
		jp1.add(l1);
		jp1.add(l2);
		jp1.add(l4);
		TitledBorder tb = BorderFactory.createTitledBorder("��ǰ��Ϣ");
		tb.setTitleFont(new Font("Microsoft Yahei", 0, 12));
		jp1.setBorder(tb);
		JPanel jp2 = new JPanel(new BorderLayout());
		jp2.add(l3, BorderLayout.WEST);
		jp2.add(tf, BorderLayout.CENTER);
		JPanel jp = new JPanel(new BorderLayout());
		jp.add(jp1, BorderLayout.NORTH);
		jp.add(jp2, BorderLayout.CENTER);
		add(jp, BorderLayout.NORTH);
		l1.setFont(font);
		l2.setFont(font);
		l4.setFont(font);
		tf.setFont(font);
		tf.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {    
				if(e.getKeyChar() == KeyEvent.VK_ENTER) {
					String text = tf.getText().trim();
					tf.setText("");
					if(recer.get(text) != null) {
						if(text != crntRecer) {
							crntRecer = text;
							//TODO
							int m = recName.indexOf(text);
							l4.setText("��ɨ�� "+counts[m]+"/"+totals[m]+" ��");
							l1.setText("�ռ�Ա��"+crntRecer+" "+recer.get(text));
						}
						success.play();
					} else if(crntRecer.length() == 0) {
						error.play();
						new ErrorDialog("����ɨ���ռ�Ա���").setVisible(true);
					} else {
						String bin = bill.get(text);
						if(bin == null) {
							l2.setText("ɨ��״̬�������ʽ����");
							l2.setForeground(Color.RED);
							error.play();
							new ErrorDialog("�����ʽ���������Ӧ�ĵ���").setVisible(true);
						} else if (bin.equals(crntRecer)) {
							l2.setText("ɨ��״̬������");
							int x = billName.indexOf(text);
							if(x >= 0 && billMark[x] == 0) {
								billMark[x] = 1;
								int m = recName.indexOf(bin);
								counts[m]++;
								l4.setText("��ɨ�� "+counts[m]+"/"+totals[m]+" ��");
								model.setValueAt(counts[m], recName.indexOf(crntRecer), 1);
							}
							l2.setForeground(Color.BLACK);
							success.play();
						} else {
							l2.setText("ɨ��״̬���ռ�Ա����");
							l2.setForeground(Color.RED);
							error.play();
							new ErrorDialog("�ռ�Ա����ӦΪ "+bin+" "+recer.get(bin)).setVisible(true);
						}
					}
				} 
			}
		});
		String[] headers = { "�ռ�Ա", "��ɨƱ��", "��Ʊ��" };
		Object[][] cellData = new Object[recer.size()][3];
		for(int i=0; i<recer.size(); i++) {
			cellData[i][0] = recName.get(i)+" "+recer.get(recName.get(i));
			cellData[i][1] = 0;
			cellData[i][2] = totals[i];
		}
		model = new DefaultTableModel(cellData, headers) {
			private static final long serialVersionUID = -7568782954779142094L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		table.setDefaultRenderer(Object.class, tcr);
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane jsp = new JScrollPane(table);
		JPanel jp3 = new JPanel(new BorderLayout());
		jp3.add(jsp, BorderLayout.CENTER);
		TitledBorder tb2 = BorderFactory.createTitledBorder("�ռ�Ա�б�");
		tb2.setTitleFont(new Font("Microsoft Yahei", 0, 12));
		jp3.setBorder(tb2);
		add(jp3, BorderLayout.CENTER);
		JPanel jp4 = new JPanel(new GridLayout(1, 2, 2, 2));
		JButton jb1 = new JButton("�鿴��ǰ�ռ�Աδɨ�˵�");
		JButton jb2 = new JButton("�鿴����δɨ�˵�");
		jp4.add(jb1);
		jp4.add(jb2);
		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int m = recName.indexOf(crntRecer);
				StringBuilder sb = new StringBuilder();
				for(int i=0; i<recIdx.length; i++) {
					if(recIdx[i] == m && billMark[i] == 0) {
						sb.append(billName.get(i)+"\n");
					}
				}
				JTextArea jta = new JTextArea(sb.toString());
				jta.setRows(16);
				jta.setColumns(24);
				JScrollPane jsp = new JScrollPane(jta);
				JOptionPane.showMessageDialog(null, jsp, "��ǰ�ռ�Աδɨ�˵�", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		jb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StringBuilder sb = new StringBuilder();
				for(int i=0; i<recIdx.length; i++) {
					if(billMark[i] == 0) {
						sb.append(billName.get(i)+"\n");
					}
				}
				JTextArea jta = new JTextArea(sb.toString());
				jta.setRows(16);
				jta.setColumns(24);
				JScrollPane jsp = new JScrollPane(jta);
				JOptionPane.showMessageDialog(null, jsp, "����δɨ�˵�", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		add(jp4, BorderLayout.SOUTH);
		setTitle("�浥����");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(300, 400));
		pack();
		setSize(400, 480);
		table.getColumnModel().getColumn(0).setPreferredWidth(170);
		setLocationRelativeTo(null);
	}
}
