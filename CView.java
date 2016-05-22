package client;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

import javax.media.bean.playerbean.MediaPlayer;
import javax.swing.ImageIcon;
import javax.swing.Timer;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class CView extends javax.swing.JPanel implements ActionListener {

	CImgText m_imgText; // �ܾ �̹�����
	CWord m_cword; // �ܾ� ����
	Main m_main; // �����Լ�

	Vector m_word; // �ܾ ���ͷ� ����
	Image m_back; // ���� ���ȭ�� ����
	Image m_bg; // ���� ȭ�� �⺻Ʋ ����
	Image m_lifebar; // life-bar image
	Image m_life; // life image
	Image m_minigameover; // game-over image
	Image m_win; // win image
	Image m_lose; // lose image
	Image m_mybg; // �� ĳ������ ���ȭ��
	Image m_mytxt; // ���� �Է��� �ܾ�

	Image[] m_player; // �÷��̾� �̸�â�� ���¸� �迭�� ����
	Image[] m_minichar; // ĳ���͸� ���۸��� �迭�� ����
	Image[] m_miniitem; // ������ �������� ���¸� �迭�� ����
	Image[] m_item_w; // ������ �迭�� ����

	Timer m_time; // Ÿ�̸� ����

	Font f; // ��Ʈ ���� ��

	int m_playerbgy[];

	// ���ӿ���
	boolean m_gameover;
	boolean m_gameover1;
	boolean m_gamestart;
	boolean m_draw;

	MediaPlayer m_bgsound; // �÷��� �� �����
	MediaPlayer m_endbg; // ��������Ǹ� ������ �����

	// item image
	ImageIcon m_itembt1 = new ImageIcon("item_no.jpg");
	ImageIcon m_itembt2 = new ImageIcon("item_1.jpg");
	ImageIcon m_itembt3 = new ImageIcon("item_2.jpg");
	ImageIcon m_itembt4 = new ImageIcon("item_3.jpg");
	ImageIcon m_itembt5 = new ImageIcon("item_4.jpg");
	ImageIcon m_itembt6 = new ImageIcon("item_5.jpg");
	ImageIcon m_itembt7 = new ImageIcon("item_6.jpg");
	ImageIcon m_itembt8 = new ImageIcon("item_7.jpg");
	ImageIcon m_itembt9 = new ImageIcon("item_8.jpg");

	public void actionPerformed(ActionEvent e) {
		if (false == m_gameover) {

			// Ŭ���Ҷ����� Ű �̺�Ʈ�� �߻���Ŵ
			if (e.getSource() == jTextField1) {

				// �Է¹��� �ܾ string ���·� ��ȯ
				String str = jTextField1.getText();

				// 1�� �ԷµǸ�
				if (true == str.equalsIgnoreCase("1")) {

					// �������� ������ ���� �ٸ��� ����
					if (m_main.m_player.m_item[0] != 0) {
						String command;
						command = "CS_ItemAttack" + ";"
								+ m_main.m_player.m_item[0] + "";
						m_main.Send(command);
						m_main.m_player.m_item[0] = m_main.m_player.m_item[1];
						m_main.m_player.m_item[1] = 0;
					}
				} else { // 1�� �ƴϸ�
					for (int i = 0; i < m_word.size(); i++) { // �۾� ������
						CWord word;

						// ������ �ε����� ��� ��ȯ
						word = (CWord) m_word.elementAt(i);

						if (true == str.equals(word.m_id)) {
							String command;
							command = "CS_WordClear" + ";" + word.m_id + ","
									+ word.m_item + "";
							m_main.Send(command);

							System.out.println(command);

							word.m_clear = true;

							/*
							 * try { SunAudioClip m_loginf = new SunAudioClip("bbang.wav"); m_loginf.play(); }
							 * catch(IOException err){;}
							 */

							try {
						        Player p = new Player(new FileInputStream(new File("bbang.wav")));
						        p.play();
						    } catch (FileNotFoundException e1) {
						        e1.printStackTrace();
						    } catch (JavaLayerException e1) {
						        e1.printStackTrace();
						    } 
							
							// �Էµ� �ܾ� ����
							m_word.remove(i);

							if (m_main.m_player.m_life < 100) { // life
								m_main.m_player.m_life++;
							}

							jTextField1.setText("");
							repaint();

							break;
						}
					}
				}
				jTextField1.setText("");
			}
			// ��ư 1�� ������ �̺�Ʈ
			else if (e.getSource() == jButton1) {

				// ������ ���
				if (m_main.m_player.m_item[0] != 0) {
					String command;
					command = "CS_ItemAttack" + ";" + m_main.m_player.m_item[0]
							+ "";
					m_main.Send(command);
					m_main.m_player.m_item[0] = m_main.m_player.m_item[1];
					m_main.m_player.m_item[1] = 0;
				}
			}
			// ��ư 2�� ������ �̺�Ʈ
			else if (e.getSource() == jButton2) {
			}

			// �� ���� �̺�Ʈ
			else {
				Go();
				repaint();
			}
			// �ڽ��� ���� ������
			String command = "CS_Life" + ";" + m_main.m_id + ","
					+ m_main.m_player.m_life + "";
			System.out.println(command);
			m_main.Send(command);

			if (0 == m_main.m_player.m_life) {

				// ���ӿ��� ���� ���̵� �˷��ֱ�
				command = "CS_Gameover" + ";" + m_main.m_id;
				m_main.Send(command);
				m_gamestart = false;
				m_gameover = true;
				m_gameover1 = true;

				repaint();

				m_bgsound.stop(); // �÷����� ����� ����
				m_endbg.start(); // ���� �� ����
			}
		}
	}

	// �Է��� �ܾ� �����
	public void WordClear(String msg) {
		for (int i = 0; i < m_word.size(); i++) {
			CWord word;
			word = (CWord) m_word.elementAt(i);

			// �Էµ� �ܾ� ���ؼ� �´ٸ�
			if (true == msg.equalsIgnoreCase(word.m_id)) {
				// �ܾ ����鼭 ȿ����
				/*
				 * try { SunAudioClip m_loginf = new SunAudioClip ("bbang.wav");
				 * m_loginf.play(); } catch(IOException err){;}
				 */

				try {
			        Player p = new Player(new FileInputStream(new File("bbang.wav")));
			        p.play();
			    } catch (FileNotFoundException e1) {
			        e1.printStackTrace();
			    } catch (JavaLayerException e1) {
			        e1.printStackTrace();
			    } 
				
				m_word.remove(i); // �ܾ� ����
				m_main.m_player.m_life--; // ���� ���̱�
				m_main.m_player.m_score += 10; // ���� ����
				break;
			}
		}
		repaint();
	}

	// ������ �ܾ� �Է��ϸ� ������ �߰�
	public void add(String str, int x, int item) {
		CWord word;
		word = new CWord();

		m_word.add(word);
		word.m_id = str;
		word.m_x = x;
		word.m_item = item;

		repaint();
	}

	// ���������� �����ϱ�
	public void ItemAttack(int item) {

		// ���� �Ⱥ��̰��ϱ�
		if (10 == item) {
			for (int i = 0; i < m_word.size(); i++) {
				CWord word;
				word = (CWord) m_word.elementAt(i);
				word.ItemAttack(item);
			}
		}
		// �� ���� �ø���
		else if (20 == item) {
			if (m_main.m_player.m_life < 100) {
				if (m_main.m_player.m_life < 80) {
					m_main.m_player.m_life += 20;
				} else {
					m_main.m_player.m_life = 100;
				}
			}
		}
		// ���� ������
		else if (30 == item) {
			if (m_main.m_player.m_life < 10) {
				m_main.m_player.m_life = 0;
			} else {
				m_main.m_player.m_life -= 10;
			}
		}
		// ������ ������ ����
		else if (40 == item) {
			m_main.m_player.m_item[0] = 0;
			m_main.m_player.m_item[1] = 0;
		}
		// ���ʽ� ����
		else if (50 == item) {
			m_main.m_player.m_score += 50;
		}
		// �ܾ��� �ӵ� up
		else if (60 == item) {
			CWord word;
			word = new CWord();
			word.m_speed += 20;
		}
		// �ܾ��� �ӵ� down
		else if (70 == item) {
			CWord word;
			word = new CWord();
			word.m_speed -= 15;
		}
		// �ܾ� �Ͼ���
		else if (80 == item) {
			for (int i = 0; i < m_word.size(); i++) {
				// ȭ�鿡 �����ִ� �ܾ��� ���� -> timer�̿��ؼ� time���� ���� �ܾ��� ���� - ������ �ܾ��� ����
				CWord word;
				word = (CWord) m_word.elementAt(i);
				word.ItemAttack(item);
			}
		}
	}

	// �÷��̾ �Է��� �ܾ� �˷��ֱ�
	public void Chat(String id, String text) {
		String command = "[" + id + "]" + "���� " + text;

		// text�� ���ٷ� �Է�
		jTextArea1.append(command + "\n");

		int pos = jTextArea1.getText().length();

		// text ���� ����
		jTextArea1.setCaretPosition(pos - 1);
	}

	// �ܾ� ���������ϴ� �Լ�
	public void Go() {
		for (int i = 0; i < m_word.size(); i++) {
			CWord word;

			// ������ �ε����� ��� ��ȯ
			word = (CWord) m_word.elementAt(i);
			word.Go();
		}
	}

	/** Creates new form CView */
	public void paintComponent(Graphics g) {
		g.drawImage(m_back, 0, 0, this);
		g.setFont(f);

		// int mwsize = m_word.size();
		for (int i = 0; i < m_word.size(); i++) {
			CWord word;
			word = (CWord) m_word.elementAt(i);

			if (475 < word.m_y) {
				m_word.remove(i);
				m_main.m_player.deLife(1);
			}
			word.paintComponent(g);

			g.drawImage(m_bg, 0, 0, this);
		}
		// ����ǥ��
		int size = m_main.m_ready.m_player.size();
		int j = 0;

		for (int i = 0; i < size; i++) {
			CPlayer p = new CPlayer();
			p = (CPlayer) m_main.m_ready.m_player.elementAt(i);

			if (true == m_main.m_id.equalsIgnoreCase(p.m_id)) {
				g.drawImage(m_lifebar, 75, 480, (int) (p.m_life * 2.5), 9, this);
				m_imgText.DrawString(this, g, Integer.toString(p.m_life), 350,
						474);

				g.drawImage(m_mybg, 542, 60, this);
				g.drawImage(m_minichar[p.m_num], 545, 66, this);
				g.drawImage(m_mytxt, 543, 61, this);
				g.drawString(p.m_id, 650, 85);
				g.drawString(p.m_win + "�� " + p.m_lose + "��", 600, 104);
				g.drawImage(m_life, 650, 72, this);
				m_imgText.DrawString(this, g, Integer.toString(p.m_life), 655,
						87);

				if (true == m_main.m_id
						.equalsIgnoreCase(m_main.m_ready.m_winid)) {
					g.drawImage(m_win, 50, 150, this);
				} else if (true == m_gameover1) {
					{
						g.drawImage(m_lose, 50, 150, this);
					}
				}
			} else {
				g.drawImage(m_player[j], 542, m_playerbgy[j], this);
				g.drawImage(m_minichar[p.m_num], 545, m_playerbgy[j] + 6, this);
				g.drawString(p.m_id, 600, m_playerbgy[j] + 25);
				g.drawString(p.m_win + "�� " + p.m_lose + "��", 600,
						m_playerbgy[j] + 44);
				g.drawImage(m_life, 650, m_playerbgy[j] + 12, this);
				m_imgText.DrawString(this, g, Integer.toString(p.m_life), 655,
						m_playerbgy[j] + 27);

				if (true == p.m_gameover) {
					g.drawImage(m_minigameover, 542, m_playerbgy[j] + 1, this);
				}

				m_imgText.DrawString(this, g, Integer.toString(i + 1), 543,
						m_playerbgy[j] + 1);
				j++;
			}
		}
		if (true == m_draw) {
			g.drawImage(m_win, 50, 150, this);
		}
		// ������ ��ġ ����
		// ���� �Ⱥ��̰��ϱ�
		if (10 == m_main.m_player.m_item[0]) {
			jButton1.setIcon(m_itembt2);
			g.drawImage(m_item_w[0], 420, 475, this);
		}
		// �� ���� �ø���
		else if (20 == m_main.m_player.m_item[0]) {
			jButton1.setIcon(m_itembt3);
			g.drawImage(m_item_w[1], 420, 475, this);
		}
		// ���� ������
		else if (30 == m_main.m_player.m_item[0]) {
			jButton1.setIcon(m_itembt4);
			g.drawImage(m_item_w[2], 420, 475, this);
		}
		// ������ ������ ����
		else if (40 == m_main.m_player.m_item[0]) {
			jButton1.setIcon(m_itembt5);
			g.drawImage(m_item_w[3], 420, 475, this);
		}
		// ���ʽ� ����
		else if (50 == m_main.m_player.m_item[0]) {
			jButton1.setIcon(m_itembt6);
			g.drawImage(m_item_w[4], 420, 475, this);
		}
		// ������ ������ ����
		else if (60 == m_main.m_player.m_item[0]) {
			jButton1.setIcon(m_itembt7);
			g.drawImage(m_item_w[3], 420, 475, this);
		}
		// ������ ������ ����
		else if (70 == m_main.m_player.m_item[0]) {
			jButton1.setIcon(m_itembt8);
			g.drawImage(m_item_w[3], 420, 475, this);
		} else {
			jButton1.setIcon(m_itembt1);
		}
		// ���� �Ⱥ��̰��ϴ� ������ ��ư ����
		if (10 == m_main.m_player.m_item[1]) {
			jButton2.setIcon(m_itembt2);
		}
		// �� ���� �ø��� ������ ��ư ����
		else if (20 == m_main.m_player.m_item[1]) {
			jButton2.setIcon(m_itembt3);
		}
		// ���� ������ ������ ��ư ����
		else if (30 == m_main.m_player.m_item[1]) {
			jButton2.setIcon(m_itembt4);
		}
		// ������ ������ �����ϴ� ������ ��ư ����
		else if (40 == m_main.m_player.m_item[1]) {
			jButton2.setIcon(m_itembt5);
		}
		// ���ʽ� ������ �ִ� ������ ��ư ����
		else if (50 == m_main.m_player.m_item[1]) {
			jButton2.setIcon(m_itembt6);
		}
		// ������ ������ �����ϴ� ������ ��ư ����
		else if (60 == m_main.m_player.m_item[1]) {
			jButton2.setIcon(m_itembt7);
		}
		// ������ ������ �����ϴ� ������ ��ư ����
		else if (70 == m_main.m_player.m_item[1]) {
			jButton2.setIcon(m_itembt8);
		} else {
			jButton2.setIcon(m_itembt1);
		}
	}

	// ����ȭ��
	public CView(Main main) {
		m_word = new Vector();
		m_cword = new CWord();
		m_cword.m_view = this;
		m_main = main;

		String key = "0123456789";
		m_imgText = new CImgText("number.png", key, 11, 20); // ���� �̹���

		// �÷����� �������
		m_bgsound = new MediaPlayer();
		m_bgsound.setMediaLocation(new java.lang.String("file:2.mp3"));
		m_bgsound.setPlaybackLoop(true);

		// ������ ����Ǹ� ������ �������
		m_endbg = new MediaPlayer();
		m_endbg.setMediaLocation(new java.lang.String("file:3.mp3"));
		m_endbg.setPlaybackLoop(true);

		// ���ӿ� �ʿ��� �̹����� �Է����ֱ�
		m_back = Toolkit.getDefaultToolkit().getImage("gamebg.jpg");
		m_bg = Toolkit.getDefaultToolkit().getImage("gmbg.png");
		m_lifebar = Toolkit.getDefaultToolkit().getImage("lifebar.jpg");
		m_life = Toolkit.getDefaultToolkit().getImage("life.png");
		m_minigameover = Toolkit.getDefaultToolkit().getImage("gameover.png");
		m_win = Toolkit.getDefaultToolkit().getImage("win.png");
		m_lose = Toolkit.getDefaultToolkit().getImage("lose.png");
		m_mybg = Toolkit.getDefaultToolkit().getImage("gmpme.jpg");
		m_mytxt = Toolkit.getDefaultToolkit().getImage("me.png");

		// player name
		m_player = new Image[3];
		m_player[0] = Toolkit.getDefaultToolkit().getImage("gmp1.jpg");
		m_player[1] = Toolkit.getDefaultToolkit().getImage("gmp2.jpg");
		m_player[2] = Toolkit.getDefaultToolkit().getImage("gmp3.jpg");

		// character image
		m_minichar = new Image[4];
		m_minichar[0] = Toolkit.getDefaultToolkit().getImage("char1.jpg");		
		m_minichar[1] = Toolkit.getDefaultToolkit().getImage("char2.jpg");
		m_minichar[2] = Toolkit.getDefaultToolkit().getImage("char3.jpg");
		m_minichar[3] = Toolkit.getDefaultToolkit().getImage("char4.jpg");

		// item image
		m_item_w = new Image[4];
		m_item_w[0] = Toolkit.getDefaultToolkit().getImage("item_1_w.png");
		m_item_w[1] = Toolkit.getDefaultToolkit().getImage("item_2_w.png");
		m_item_w[2] = Toolkit.getDefaultToolkit().getImage("item_3_w.png");
		m_item_w[3] = Toolkit.getDefaultToolkit().getImage("item_4_w.png");

		// item image
		m_miniitem = new Image[4];
		m_miniitem[0] = Toolkit.getDefaultToolkit().getImage("mini_item_1.jpg");
		m_miniitem[1] = Toolkit.getDefaultToolkit().getImage("mini_item_2.jpg");
		m_miniitem[2] = Toolkit.getDefaultToolkit().getImage("mini_item_3.jpg");
		m_miniitem[3] = Toolkit.getDefaultToolkit().getImage("mini_item_4.jpg");

		// timer
		m_time = new Timer(500, this);

		// game state
		m_gameover = false;
		m_gameover1 = false;
		m_gamestart = false;
		m_draw = false;

		m_playerbgy = new int[3];
		m_playerbgy[0] = 123;
		m_playerbgy[1] = 186;
		m_playerbgy[2] = 249;

		f = new Font("DotumChe", Font.BOLD, 12); // ��Ʈ ����

		initComponents();

		// �Է�â ����
		jTextField1.addActionListener(this);
		jButton1.addActionListener(this);

		// ��ư ��Ŀ���� ������ �ִ��� ����
		jButton1.setFocusable(false);
		jButton2.setFocusable(false);
		jTextArea1.setFocusable(false);

		// ��ư ������ ����
		jButton1.setIcon(m_itembt1);
		jButton2.setIcon(m_itembt1);

		jTextArea1.setLineWrap(true);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc=" Generated Code ">//GEN-BEGIN:initComponents
	// components
	private void initComponents() {
		jTextField1 = new javax.swing.JTextField();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();

		jButton1.setBorder(null);
		jButton1.setOpaque(false);
		jButton1.setPreferredSize(new java.awt.Dimension(57, 57));

		jButton2.setBorder(null);
		jButton2.setPreferredSize(new java.awt.Dimension(57, 57));

		jScrollPane1.setPreferredSize(new java.awt.Dimension(110, 115));
		jTextArea1.setBackground(new java.awt.Color(222, 222, 222));
		jTextArea1.setColumns(10);
		jTextArea1.setRows(5);
		jTextArea1.setBorder(null);
		jScrollPane1.setViewportView(jTextArea1);

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(org.jdesktop.layout.GroupLayout.TRAILING,
						layout.createSequentialGroup()
								.add(113, 113, 113)
								.add(jTextField1,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
										305,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										org.jdesktop.layout.LayoutStyle.RELATED,
										45, Short.MAX_VALUE)
								.add(layout
										.createParallelGroup(
												org.jdesktop.layout.GroupLayout.LEADING,
												false)
										.add(org.jdesktop.layout.GroupLayout.TRAILING,
												jScrollPane1,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.add(org.jdesktop.layout.GroupLayout.TRAILING,
												layout.createSequentialGroup()
														.add(jButton1,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
														.add(15, 15, 15)
														.add(jButton2,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(org.jdesktop.layout.GroupLayout.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap(311, Short.MAX_VALUE)
								.add(jScrollPane1,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										org.jdesktop.layout.LayoutStyle.RELATED)
								.add(layout
										.createParallelGroup(
												org.jdesktop.layout.GroupLayout.LEADING)
										.add(org.jdesktop.layout.GroupLayout.TRAILING,
												layout.createSequentialGroup()
														.add(jTextField1,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																20,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
														.add(48, 48, 48))
										.add(org.jdesktop.layout.GroupLayout.TRAILING,
												layout.createSequentialGroup()
														.add(layout
																.createParallelGroup(
																		org.jdesktop.layout.GroupLayout.BASELINE)
																.add(jButton2,
																		org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																		org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																		org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																.add(jButton1,
																		org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																		org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																		org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
														.addContainerGap()))));
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextArea jTextArea1;
	private javax.swing.JTextField jTextField1;
	// End of variables declaration//GEN-END:variables
}
