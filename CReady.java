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
	
	int m_num; // 플레이어수를 저장하는 변수
	int m_overnum; // 최대인원변수
	int[] m_namex; // 플레이어 이름 x좌표값
	int[] m_charx; // 플레이어 이미지 x좌표값

	String m_winid; // 이긴플레이어 이름저장

	boolean m_gamerover; // 게임이 끝났는지 저장하는 변수
	boolean m_item; // 아이템전인지 노템전인지 확인하는 변수
	boolean m_stats;

	Timer m_tiem;
	int m_timenum;

	Image m_bg; // 배경화면
	Image m_ready; // ready시 플레이어에 뜨는 READY이미지
	Image[] m_char; // 캐릭터 이미지
	Image m_check; // 자신표시 이미지

	ImageIcon m_readybt1 = new ImageIcon("readybt.png"); // 버튼이미지 레디
	ImageIcon m_readybt2 = new ImageIcon("readybt1.jpg"); // 버튼이미지 레디상태

	ImageIcon m_itembt1 = new ImageIcon("r_itembt1.jpg"); // 아이템전 선택 상태
	ImageIcon m_itembt2 = new ImageIcon("r_itembt1-1.jpg"); // 아이템전 기본 상태
	ImageIcon m_itembt3 = new ImageIcon("r_itembt2.jpg"); // 노템전 선택 상태
	ImageIcon m_itembt4 = new ImageIcon("r_itembt2-1.jpg"); // 노템전 기본 상태

	public void run() {
	}

	public void addPlayer(String id, int num) { // 플레이어를 추가하는 함수
		CPlayer player;
		player = new CPlayer(); // 플레이어 객체를 추가

		m_player.add(player);
		player.m_id = id; // 플레이어 아이디 입력
		player.m_num = num; // 몇번째 플레이어인지를 입력

		// 메인 클래스에 send 호출하여 메시지 보냄
		m_main.Send("CS_GameItem" + ";" + m_item);

	}

	public void rmPlayer(String id) {
		int size = m_player.size();

		for (int i = 0; i < size; i++) {

			// 플레이어 p의 위치를 i위치로 반환한다
			CPlayer p = (CPlayer) m_player.elementAt(i);

			// p의 플레이어번호가 같을시에 플레이어를 지운다
			if (true == id.equalsIgnoreCase(p.m_id)) {
				m_player.remove(i);
				break;
			}
		}

		if (m_main.m_view.m_gamestart == true) // 게임이 시작했을때만 체크
		{
			// 플레이어 수가 한명일 경우 메세지 출력
			if (1 == m_player.size()) {
				try {
					System.out.println("다른플레이어들이 모두 나갔습니다.");
					m_main.m_view.m_draw = true;
					m_winid = m_main.m_id;
					m_main.m_view.repaint();
					m_main.m_view.m_time.stop();
					 m_main.m_view.m_bgsound.stop();
					 m_main.m_view.m_endbg.start();
					Thread.sleep(5000);
				} catch (InterruptedException err) {
				}

				// 게임이 종료되었다는 것과 이긴 아이디 메세지 출력
				String command = "CS_Allgameover" + ";" + m_winid;
				m_main.Send(command);
				System.out.println(command);

				m_main.m_view.m_gamestart = false;
			}
		}
		repaint();
	}

	// 플레이어 준비상태를 보여주는 함수
	public void ReadyPlayer(String id, boolean gameready) {
		int size = m_player.size();

		for (int i = 0; i < size; i++) {
			CPlayer p = new CPlayer(); // 플레이어 객체를 만든다.
			p = (CPlayer) m_player.elementAt(i);

			// 사이즈와 플레이어 아이디가 같을 시에 준비상태 이미지 출력
			if (true == id.equalsIgnoreCase(p.m_id)) {
				p.m_ready = gameready;
			}
		}
		repaint();
	}

	// 게임이 종료될 시 플레이어 상태들을 초기화 시켜주는 함수
	public void Reset(String id, int win, int lose) {

		int size = m_player.size();

		// 게임이 종료될 시 플레이어들의 이겼는지 졌는지 상태를 보여주는 부분
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
		/* 플레이어들의 상태를 초기화 시켜주는 부분 */
		m_main.m_player.m_item[0] = 0;
		m_main.m_player.m_item[1] = 0;
		m_main.m_player.m_life = 100;
		m_overnum = 0;
		m_winid = "";
		m_main.m_view.m_gameover1 = false;
		m_main.m_view.m_draw = false;

		m_stats = false;
	}

	// 생명력 관련하여 표현해주는 함수
	public void Life(String id, int life) {
		int size = m_player.size();
		for (int i = 0; i < size; i++) {

			// 플레이어의 객체를 해당위치로 반환해준다.
			CPlayer p = (CPlayer) m_player.elementAt(i);

			// 플레이어의 번호와 같을시에 해당 코드를 실행
			if (true == p.m_id.equalsIgnoreCase(id)) {
				p.m_life = life; // life로 재정의 한다.
				break;
			}
		}
	}

	// 게임오버 상태를 알려주는 함수
	public void GameOver(String id) {
		int size = m_player.size();
		for (int i = 0; i < size; i++) {

			// 플레이어의 객체를 해당위치로 반환해준다
			CPlayer p = (CPlayer) m_player.elementAt(i);

			// 플레이어의 번호와 같을시에 해당 코드를 실행
			if (true == p.m_id.equalsIgnoreCase(id)) {

				// 게임이 끝났다고 true 값으로 저장
				p.m_gameover = true;
				m_overnum++;
				break;
			}
		}

		if (size - 1 == m_overnum) {
			for (int i = 0; i < size; i++) {
				CPlayer p = (CPlayer) m_player.elementAt(i);

				// 게임오버 값이 false면 이긴 아이디와 화면 출력값에 true를 넣는다
				if (false == p.m_gameover) {
					m_winid = p.m_id;
					p.m_gameover = true;
					m_main.m_view.m_gameover = true;
					break;
				}
			}
			/* 입력했던 것들을 화면에 보여준다. */
			m_main.m_view.repaint();
			m_main.m_view.m_bgsound.stop();
			m_main.m_view.m_endbg.start();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException err) {
			}
			/* 모든 플레이어들이 게임오버 상태를 표시해주고 이긴 플레이어는 이겼다고 메세지를 보여준다. */
			m_main.m_view.m_gamestart = false;
			String command = "CS_Allgameover" + ";" + m_winid;
			m_main.Send(command);

			System.out.println(command);
		}
	}

	// 채팅하는 메소드
	public void Chat(String msg) {

		// 입력한 메세지 뒤에 \n를 추가 시켜준다
		jTextArea1.append(msg + "\n");

		// 텍스트의 길이를 pos에 넣어준다
		int pos = jTextArea1.getText().length();

		// 입력한 텍스트 삽입 caret 위치를 정해준다
		jTextArea1.setCaretPosition(pos - 1);
	}

	public void actionPerformed(ActionEvent e) {

		// jbutton1시에 등록한 무언가를 실행한다
		if (e.getSource() == jButton1) {
			CPlayer p = (CPlayer) m_player.elementAt(0);

			if (true == m_main.m_id.equalsIgnoreCase(p.m_id)) {

				// 인원이 1명이면 메세지 출력
				if (1 == m_player.size()) {
					JOptionPane.showMessageDialog(null, "2명이상이어야 플레이가 가능합니다.",
							"알림!!", JOptionPane.INFORMATION_MESSAGE);
					// 인원이 1명이 아닐 경우 스타트 메세지를 출력
				} else {
					String command = "CS_Start" + ";" + "test";
					m_main.Send(command);
				}
				// 플레이어들에게 준비하라는 메세지를 출력
			} else {
				String command = "CS_Ready" + ";" + m_main.m_id;
				m_main.Send(command);
			}
			// 텍스트 필드를 실행할시에 채팅을 시작
		} else if (e.getSource() == jTextField1) {
			String chat;
			chat = jTextField1.getText();

			// 텍스트가 입력이 들어오면 입력한 플레이어번호와 메세지를 출력
			if (false == chat.equalsIgnoreCase("")) {
				m_main.SendMsg(chat);
				jTextArea1.append(" [ " + m_main.m_id + " ] " + chat + "\n");

				jTextField1.setText("");

				int pos = jTextArea1.getText().length();
				jTextArea1.setCaretPosition(pos - 1);
			}
			// 게임시 아이템전을 할것인지 선택하는 이벤트
		} else if (e.getSource() == jButton6) {
			CPlayer p = (CPlayer) m_player.elementAt(0);
			if (true == m_main.m_id.equalsIgnoreCase(p.m_id)) {
				m_item = true;
				m_main.Send("CS_GameItem" + ";" + m_item);
			}
			// 게임시 노템전을 할것인지 선택하는 이벤트
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

	// 그림을 그려주는 클래스
	public void paintComponent(Graphics g) {

		// 배경화면을 그린다
		g.drawImage(m_bg, 0, 0, this);

		int size = m_player.size();

		for (int i = 0; i < size; i++) {
			CPlayer p;
			p = new CPlayer();
			p = (CPlayer) m_player.elementAt(i);

			// 플레이어의 아이디 길이를 넣어준다
			int msgsize = p.m_id.length();

			// 플레이어의 아이디를 좌표에 입력하여 출력한다
			g.drawString(p.m_id, m_namex[i] - (msgsize * 3), 90);

			// 플레이어의 승패를 좌표에 입력하여 출력한다
			g.drawString(p.m_win + "승 " + p.m_lose + "패", m_namex[i], 62);

			// 해당 플레이어의 이미지를 그려준다
			g.drawImage(m_char[p.m_num], m_charx[i], 110, this);

			if (true == m_main.m_id.equalsIgnoreCase(p.m_id)) {

				// 자신의 현재 상태를 자신의 이미지와 함께 그려준다
				g.drawImage(m_check, m_charx[i], 110, this);
			}
			if (true == p.m_ready) {
				if (0 != i) {

					// 플레이어의 레디 상태를 그려준다
					g.drawImage(m_ready, m_charx[i] + 10, 175, this);
				}

				// 플레이어가 레디상태를 풀어주는 버튼이미지 삽입
				if (true == m_main.m_id.equalsIgnoreCase(p.m_id)) {
					jButton1.setIcon(m_readybt2);
				}

				// 플레이어가 레디상태를 하게하는 버튼이미지 삽입
			} else {
				if (true == m_main.m_id.equalsIgnoreCase(p.m_id)) {
					jButton1.setIcon(m_readybt1);
				}
			}

			// jbutton6,7의 이미지를 선택하여 넣어준다
			if (true == m_item) {
				jButton6.setIcon(m_itembt1);
				jButton7.setIcon(m_itembt4);
			} else {
				jButton6.setIcon(m_itembt2);
				jButton7.setIcon(m_itembt3);
			}
		}
	}

	// CReady 클래스를 초기화한다
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
		// 입력하는 key들을 설정
		String key = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		m_imgText = new CImgText("font2.png", key, 12, 19);

		// 이름 x좌표
		m_namex = new int[4];
		m_namex[0] = 75;
		m_namex[1] = 205;// 190;
		m_namex[2] = 335;
		m_namex[3] = 465;

		// 캐릭터 이미지 x좌표
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
				"C:\\Documents and Settings\\Administrator\\바탕 화면\\베네치아\\Client\\readybt.jpg"));
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
