package client;

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
import javax.swing.JOptionPane;
import javax.swing.Timer;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class CReady extends javax.swing.JPanel implements ActionListener,
		Runnable {
	Main m_main;
	CPlayer m_cplayer;
	Vector m_player;
	CImgText m_imgText;

//	MediaPlayer m_bgsound;
	Player p;
	
	int m_num; // �÷��̾���� �����ϴ� ����
	int m_overnum; // �ִ��ο�����
	int[] m_namex; // �÷��̾� �̸� x��ǥ��
	int[] m_charx; // �÷��̾� �̹��� x��ǥ��

	String m_winid; // �̱��÷��̾� �̸�����

	boolean m_gamerover; // ������ �������� �����ϴ� ����
	boolean m_item; // ������������ ���������� Ȯ���ϴ� ����
	boolean m_stats;

	Timer m_tiem;
	int m_timenum;

	Image m_bg; // ���ȭ��
	Image m_ready; // ready�� �÷��̾ �ߴ� READY�̹���
	Image[] m_char; // ĳ���� �̹���
	Image m_check; // �ڽ�ǥ�� �̹���

	ImageIcon m_readybt1 = new ImageIcon("readybt.png"); // ��ư�̹��� ����
	ImageIcon m_readybt2 = new ImageIcon("readybt1.jpg"); // ��ư�̹��� �������

	ImageIcon m_itembt1 = new ImageIcon("r_itembt1.jpg"); // �������� ���� ����
	ImageIcon m_itembt2 = new ImageIcon("r_itembt1-1.jpg"); // �������� �⺻ ����
	ImageIcon m_itembt3 = new ImageIcon("r_itembt2.jpg"); // ������ ���� ����
	ImageIcon m_itembt4 = new ImageIcon("r_itembt2-1.jpg"); // ������ �⺻ ����

	public void run() {
	}

	public void addPlayer(String id, int num) { // �÷��̾ �߰��ϴ� �Լ�
		CPlayer player;
		player = new CPlayer(); // �÷��̾� ��ü�� �߰�

		m_player.add(player);
		player.m_id = id; // �÷��̾� ���̵� �Է�
		player.m_num = num; // ���° �÷��̾������� �Է�

		// ���� Ŭ������ send ȣ���Ͽ� �޽��� ����
		m_main.Send("CS_GameItem" + ";" + m_item);

	}

	public void rmPlayer(String id) {
		int size = m_player.size();

		for (int i = 0; i < size; i++) {

			// �÷��̾� p�� ��ġ�� i��ġ�� ��ȯ�Ѵ�
			CPlayer p = (CPlayer) m_player.elementAt(i);

			// p�� �÷��̾��ȣ�� �����ÿ� �÷��̾ �����
			if (true == id.equalsIgnoreCase(p.m_id)) {
				m_player.remove(i);
				break;
			}
		}

		if (m_main.m_view.m_gamestart == true) // ������ ������������ üũ
		{
			// �÷��̾� ���� �Ѹ��� ��� �޼��� ���
			if (1 == m_player.size()) {
				try {
					System.out.println("�ٸ��÷��̾���� ��� �������ϴ�.");
					m_main.m_view.m_draw = true;
					m_winid = m_main.m_id;
					m_main.m_view.repaint();
					m_main.m_view.m_time.stop();
					 m_main.m_view.m_bgsound.stop();
					 m_main.m_view.m_endbg.start();
					Thread.sleep(5000);
				} catch (InterruptedException err) {
				}

				// ������ ����Ǿ��ٴ� �Ͱ� �̱� ���̵� �޼��� ���
				String command = "CS_Allgameover" + ";" + m_winid;
				m_main.Send(command);
				System.out.println(command);

				m_main.m_view.m_gamestart = false;
			}
		}
		repaint();
	}

	// �÷��̾� �غ���¸� �����ִ� �Լ�
	public void ReadyPlayer(String id, boolean gameready) {
		int size = m_player.size();

		for (int i = 0; i < size; i++) {
			CPlayer p = new CPlayer(); // �÷��̾� ��ü�� �����.
			p = (CPlayer) m_player.elementAt(i);

			// ������� �÷��̾� ���̵� ���� �ÿ� �غ���� �̹��� ���
			if (true == id.equalsIgnoreCase(p.m_id)) {
				p.m_ready = gameready;
			}
		}
		repaint();
	}

	// ������ ����� �� �÷��̾� ���µ��� �ʱ�ȭ �����ִ� �Լ�
	public void Reset(String id, int win, int lose) {

		int size = m_player.size();

		// ������ ����� �� �÷��̾���� �̰���� ������ ���¸� �����ִ� �κ�
		for (int i = 0; i < size; i++) {

			CPlayer p = (CPlayer) m_main.m_ready.m_player.elementAt(i);

			if (true == m_stats) {
				p.m_ready = false;
			}
			p.m_gameover = false;

			if (true == p.m_id.equalsIgnoreCase(id)) {
				p.m_win = win;
				p.m_lose = lose;
			}
		}
		/* �÷��̾���� ���¸� �ʱ�ȭ �����ִ� �κ� */
		m_main.m_player.m_item[0] = 0;
		m_main.m_player.m_item[1] = 0;
		m_main.m_player.m_life = 100;
		m_overnum = 0;
		m_winid = "";
		m_main.m_view.m_gameover1 = false;
		m_main.m_view.m_draw = false;

		m_stats = false;
	}

	// ����� �����Ͽ� ǥ�����ִ� �Լ�
	public void Life(String id, int life) {
		int size = m_player.size();
		for (int i = 0; i < size; i++) {

			// �÷��̾��� ��ü�� �ش���ġ�� ��ȯ���ش�.
			CPlayer p = (CPlayer) m_player.elementAt(i);

			// �÷��̾��� ��ȣ�� �����ÿ� �ش� �ڵ带 ����
			if (true == p.m_id.equalsIgnoreCase(id)) {
				p.m_life = life; // life�� ������ �Ѵ�.
				break;
			}
		}
	}

	// ���ӿ��� ���¸� �˷��ִ� �Լ�
	public void GameOver(String id) {
		int size = m_player.size();
		for (int i = 0; i < size; i++) {

			// �÷��̾��� ��ü�� �ش���ġ�� ��ȯ���ش�
			CPlayer p = (CPlayer) m_player.elementAt(i);

			// �÷��̾��� ��ȣ�� �����ÿ� �ش� �ڵ带 ����
			if (true == p.m_id.equalsIgnoreCase(id)) {

				// ������ �����ٰ� true ������ ����
				p.m_gameover = true;
				m_overnum++;
				break;
			}
		}

		if (size - 1 == m_overnum) {
			for (int i = 0; i < size; i++) {
				CPlayer p = (CPlayer) m_player.elementAt(i);

				// ���ӿ��� ���� false�� �̱� ���̵�� ȭ�� ��°��� true�� �ִ´�
				if (false == p.m_gameover) {
					m_winid = p.m_id;
					p.m_gameover = true;
					m_main.m_view.m_gameover = true;
					break;
				}
			}
			/* �Է��ߴ� �͵��� ȭ�鿡 �����ش�. */
			m_main.m_view.repaint();
			m_main.m_view.m_bgsound.stop();
			m_main.m_view.m_endbg.start();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException err) {
			}
			/* ��� �÷��̾���� ���ӿ��� ���¸� ǥ�����ְ� �̱� �÷��̾�� �̰�ٰ� �޼����� �����ش�. */
			m_main.m_view.m_gamestart = false;
			String command = "CS_Allgameover" + ";" + m_winid;
			m_main.Send(command);

			System.out.println(command);
		}
	}

	// ä���ϴ� �޼ҵ�
	public void Chat(String msg) {

		// �Է��� �޼��� �ڿ� \n�� �߰� �����ش�
		jTextArea1.append(msg + "\n");

		// �ؽ�Ʈ�� ���̸� pos�� �־��ش�
		int pos = jTextArea1.getText().length();

		// �Է��� �ؽ�Ʈ ���� caret ��ġ�� �����ش�
		jTextArea1.setCaretPosition(pos - 1);
	}

	public void actionPerformed(ActionEvent e) {

		// jbutton1�ÿ� ����� ���𰡸� �����Ѵ�
		if (e.getSource() == jButton1) {
			CPlayer p = (CPlayer) m_player.elementAt(0);

			if (true == m_main.m_id.equalsIgnoreCase(p.m_id)) {

				// �ο��� 1���̸� �޼��� ���
				if (1 == m_player.size()) {
					JOptionPane.showMessageDialog(null, "2���̻��̾�� �÷��̰� �����մϴ�.",
							"�˸�!!", JOptionPane.INFORMATION_MESSAGE);
					// �ο��� 1���� �ƴ� ��� ��ŸƮ �޼����� ���
				} else {
					String command = "CS_Start" + ";" + "test";
					m_main.Send(command);
				}
				// �÷��̾�鿡�� �غ��϶�� �޼����� ���
			} else {
				String command = "CS_Ready" + ";" + m_main.m_id;
				m_main.Send(command);
			}
			// �ؽ�Ʈ �ʵ带 �����ҽÿ� ä���� ����
		} else if (e.getSource() == jTextField1) {
			String chat;
			chat = jTextField1.getText();

			// �ؽ�Ʈ�� �Է��� ������ �Է��� �÷��̾��ȣ�� �޼����� ���
			if (false == chat.equalsIgnoreCase("")) {
				m_main.SendMsg(chat);
				jTextArea1.append(" [ " + m_main.m_id + " ] " + chat + "\n");

				jTextField1.setText("");

				int pos = jTextArea1.getText().length();
				jTextArea1.setCaretPosition(pos - 1);
			}
			// ���ӽ� ���������� �Ұ����� �����ϴ� �̺�Ʈ
		} else if (e.getSource() == jButton6) {
			CPlayer p = (CPlayer) m_player.elementAt(0);
			if (true == m_main.m_id.equalsIgnoreCase(p.m_id)) {
				m_item = true;
				m_main.Send("CS_GameItem" + ";" + m_item);
			}
			// ���ӽ� �������� �Ұ����� �����ϴ� �̺�Ʈ
		} else if (e.getSource() == jButton7) {
			CPlayer p = (CPlayer) m_player.elementAt(0);
			if (true == m_main.m_id.equalsIgnoreCase(p.m_id)) {
				m_item = false;
				m_main.Send("CS_GameItem" + ";" + m_item);
			}
		} else if (e.getSource() == m_tiem) {
			m_timenum++;
		}
	}

	// �׸��� �׷��ִ� Ŭ����
	public void paintComponent(Graphics g) {

		// ���ȭ���� �׸���
		g.drawImage(m_bg, 0, 0, this);

		int size = m_player.size();

		for (int i = 0; i < size; i++) {
			CPlayer p;
			p = new CPlayer();
			p = (CPlayer) m_player.elementAt(i);

			// �÷��̾��� ���̵� ���̸� �־��ش�
			int msgsize = p.m_id.length();

			// �÷��̾��� ���̵� ��ǥ�� �Է��Ͽ� ����Ѵ�
			g.drawString(p.m_id, m_namex[i] - (msgsize * 3), 90);

			// �÷��̾��� ���и� ��ǥ�� �Է��Ͽ� ����Ѵ�
			g.drawString(p.m_win + "�� " + p.m_lose + "��", m_namex[i], 62);

			// �ش� �÷��̾��� �̹����� �׷��ش�
			g.drawImage(m_char[p.m_num], m_charx[i], 110, this);

			if (true == m_main.m_id.equalsIgnoreCase(p.m_id)) {

				// �ڽ��� ���� ���¸� �ڽ��� �̹����� �Բ� �׷��ش�
				g.drawImage(m_check, m_charx[i], 110, this);
			}
			if (true == p.m_ready) {
				if (0 != i) {

					// �÷��̾��� ���� ���¸� �׷��ش�
					g.drawImage(m_ready, m_charx[i] + 10, 175, this);
				}

				// �÷��̾ ������¸� Ǯ���ִ� ��ư�̹��� ����
				if (true == m_main.m_id.equalsIgnoreCase(p.m_id)) {
					jButton1.setIcon(m_readybt2);
				}

				// �÷��̾ ������¸� �ϰ��ϴ� ��ư�̹��� ����
			} else {
				if (true == m_main.m_id.equalsIgnoreCase(p.m_id)) {
					jButton1.setIcon(m_readybt1);
				}
			}

			// jbutton6,7�� �̹����� �����Ͽ� �־��ش�
			if (true == m_item) {
				jButton6.setIcon(m_itembt1);
				jButton7.setIcon(m_itembt4);
			} else {
				jButton6.setIcon(m_itembt2);
				jButton7.setIcon(m_itembt3);
			}
		}
	}

	// CReady Ŭ������ �ʱ�ȭ�Ѵ�
	public CReady(Main m) {
		m_main = m;
		m_player = new Vector();
		m_cplayer = new CPlayer();

//		m_bgsound = new MediaPlayer();
//		m_bgsound.setMediaLocation(new java.lang.String("file:1.mp3"));
//		m_bgsound.setPlaybackLoop(true);
/*
		if( == true){
			try {
				Player p = new Player(new FileInputStream(new File("1.mp3")));
				p.play();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					} catch (JavaLayerException e) {
						e.printStackTrace();
						}
			}else{
				p.close();
				}
*/	
		// �Է��ϴ� key���� ����
		String key = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		m_imgText = new CImgText("font2.png", key, 12, 19);

		// �̸� x��ǥ
		m_namex = new int[4];
		m_namex[0] = 75;
		m_namex[1] = 205;// 190;
		m_namex[2] = 335;
		m_namex[3] = 465;

		// ĳ���� �̹��� x��ǥ
		m_charx = new int[4];
		m_charx[0] = 26;
		m_charx[1] = 156;
		m_charx[2] = 286;
		m_charx[3] = 416;

		m_num = 0;
		m_overnum = 0;

		m_gamerover = false;
		m_item = true;
		m_stats = false;

		m_bg = Toolkit.getDefaultToolkit().getImage("readybg.jpg");
		m_ready = Toolkit.getDefaultToolkit().getImage("ready.png");
		m_check = Toolkit.getDefaultToolkit().getImage("check.png");

		m_char = new Image[4];
//		m_char[0] = Toolkit.getDefaultToolkit().getImage("char1.gif");
		m_char[0] = Toolkit.getDefaultToolkit().getImage("1.gif");
		m_char[1] = Toolkit.getDefaultToolkit().getImage("2.gif");
		m_char[2] = Toolkit.getDefaultToolkit().getImage("3.gif");
		m_char[3] = Toolkit.getDefaultToolkit().getImage("4.gif");

		initComponents();

		jButton1.addActionListener(this);
		jTextField1.addActionListener(this);
		jButton6.addActionListener(this);
		jButton7.addActionListener(this);

		jTextArea1.setFocusable(false);
		jButton6.setFocusable(false);
		jButton7.setFocusable(false);
		jButton1.setFocusable(false);
		jTextArea1.setLineWrap(true);
	}

	// <editor-fold defaultstate="collapsed"
	// desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		jButton2 = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();
		jButton5 = new javax.swing.JButton();
		jButton1 = new javax.swing.JButton();
		jTextField1 = new javax.swing.JTextField();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();
		jButton6 = new javax.swing.JButton();
		jButton7 = new javax.swing.JButton();

		jButton2.setText("jButton2");
		jButton3.setText("jButton3");
		jButton4.setText("jButton4");
		jButton5.setText("jButton5");

		setMinimumSize(new java.awt.Dimension(200, 200));
		setPreferredSize(new java.awt.Dimension(700, 525));
		jButton1.setIcon(new javax.swing.ImageIcon(
				"C:\\Documents and Settings\\Administrator\\���� ȭ��\\����ġ��\\Client\\readybt.jpg"));
		jButton1.setBorder(null);
		jButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jButton1.setPreferredSize(new java.awt.Dimension(44, 42));
		jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				change(evt);
			}
		});

		jTextArea1.setBackground(new java.awt.Color(255, 255, 255));
		jTextArea1.setColumns(20);
		jTextArea1.setRows(5);
		jTextArea1
				.setText("\ubc29\uc7a5\uc740 \uac8c\uc784\ud0c0\uc785\uc744 [\uc544\ud15c/\ub178\ud15c] \uc120\ud0dd\ud560\uc218\uc788\uc2b5\ub2c8\ub2e4. \uc544\uc774\ud15c\uc0ac\uc6a9\uc740 \ub9c8\uc6b0\uc2a4\ub97c \ud074\ub9ad\ud558\uc9c0 \uc54a\uace0\ub3c4 \"1\"\uc744 \uc785\ub825\ud55c\ud6c4 \uc5d4\ud130\ub97c \ub204\ub974\uba74 \uc0ac\uc6a9\ub429\ub2c8\ub2e4. \n===================================================\n");
		jTextArea1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
		jScrollPane1.setViewportView(jTextArea1);

		jButton6.setBorder(null);
		jButton6.setPreferredSize(new java.awt.Dimension(113, 57));

		jButton7.setBorder(null);
		jButton7.setPreferredSize(new java.awt.Dimension(95, 57));

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout
						.createSequentialGroup()
						.add(layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.LEADING)
								.add(layout
										.createSequentialGroup()
										.add(25, 25, 25)
										.add(jTextField1,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												434,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED,
												185, Short.MAX_VALUE)
										.add(jButton1,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
								.add(layout
										.createSequentialGroup()
										.add(28, 28, 28)
										.add(jScrollPane1,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												379,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(layout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.LEADING,
														false)
												.add(jButton7, 0, 0,
														Short.MAX_VALUE)
												.add(jButton6,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														87, Short.MAX_VALUE))
										.add(16, 16, 16))).addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout
						.createSequentialGroup()
						.addContainerGap(326, Short.MAX_VALUE)
						.add(layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.LEADING,
										false)
								.add(layout
										.createSequentialGroup()
										.add(jButton6,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.add(jButton7,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
								.add(org.jdesktop.layout.GroupLayout.TRAILING,
										jScrollPane1,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
										123,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.add(23, 23, 23)
						.add(layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.BASELINE)
								.add(jTextField1,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.add(jButton1,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.add(11, 11, 11)));
	}// </editor-fold>//GEN-END:initComponents

	private void change(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_change
		// TODO add your handling code here:
	}// GEN-LAST:event_change

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JButton jButton5;
	private javax.swing.JButton jButton6;
	private javax.swing.JButton jButton7;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextArea jTextArea1;
	private javax.swing.JTextField jTextField1;
	// End of variables declaration//GEN-END:variables

}
