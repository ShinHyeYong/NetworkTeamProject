package client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class Main extends JFrame implements Runnable {
	Socket m_socket;
	BufferedReader m_in;
	PrintWriter m_out;

	CView m_view;
	CLogin m_login;
	CReady m_ready;
	CPlayer m_player;
	CGameover m_gameover;

	String m_id;

	// login 함수
	public void Login(String id) {
		Init("localhost", 9001); // host주소와 port주소를 받는 함수
		String command;
		command = "CS_Login" + ";" + id;

		System.out.println(command); // command 출력
		Send(command); // command 보냄(id보냄)

		Thread t; // thread 생성
		t = new Thread(this);
		t.start();

		m_id = id;
	}

	// logout 함수
	public void Logout() {
		String command = "CS_Logout" + ";" + m_id;
		Send(command); // command 보냄
	}

	// 초기화 함수
	public void Init(String add, int port) {
		try {
			m_socket = new Socket(add, port); // socket 생성
			m_in = new BufferedReader(new InputStreamReader(
					m_socket.getInputStream())); // 읽는 함수
			m_out = new PrintWriter(new OutputStreamWriter(
					m_socket.getOutputStream())); // 출력 함수
		} catch (IOException erro) {
			System.out.println(erro.toString());
		}
	}

	// close 함수
	public void Close() {
		try {
			m_in.close();
			m_out.close();
			m_socket.close();
		} catch (IOException erro) {
			System.out.println(erro.toString());
		}
	}

	// send 함수
	public void Send(String msg) {
		m_out.println(msg);
		m_out.flush();
	}

	// SendMsg 함수 (메시지를 보내는 함수)
	public void SendMsg(String msg) {
		String command;
		command = "CS_Msg" + ";" + msg;
		Send(command);
		System.out.println(command);
	}

	public void run() {
		String msg;
		try {
			while (true) {
				msg = m_in.readLine(); // 메시지를 읽어옴

				StringTokenizer parser = new StringTokenizer(msg);

				String order = parser.nextToken(";");
				String para = parser.nextToken(";");

				// 로그인
				if (true == order.equalsIgnoreCase("SC_Login")) {
					if (true == para.equalsIgnoreCase("yes")) {
						setVisible(false);
						setContentPane(m_ready);
						setSize(557, 475);
						setVisible(true);

//						m_ready.m_bgsound.start();
						m_ready.p.play();
					} else if (true == para.equalsIgnoreCase("no")) {
						try {
							//SunAudioClip m = new SunAudioClip("loginf.wav");
							//m.play();
							Player p = new Player(new FileInputStream(new File("loginf.wav")));
							p.play();
						} catch (IOException err) {
							System.out.println("Error");
						} catch (JavaLayerException err){
							;
						}

						// 동일한 아이디의 경우 메세지 출력
						JOptionPane.showMessageDialog(null, "동일한아이디!!!",
								"알림!!", JOptionPane.INFORMATION_MESSAGE);
					} else if (true == para.equalsIgnoreCase("overplayer")) {
						try {
							//SunAudioClip m_loginf = new SunAudioClip("loginf.wav");
							//m_loginf.play();
							Player p = new Player(new FileInputStream(new File("loginf.wav")));
							p.play();
						} catch (IOException err) {
							System.out.println("Error");
						} catch (JavaLayerException err){
							;
						}

						// 인원 초과의 경우 메세지 출력
						JOptionPane.showMessageDialog(null, "인원초과", "알림!!",
								JOptionPane.INFORMATION_MESSAGE);
					} else if (true == para.equalsIgnoreCase("started")) {
						try {
							//SunAudioClip m_loginf = new SunAudioClip("loginf.wav");
							//m_loginf.play();
							Player p = new Player(new FileInputStream(new File("loginf.wav")));
							p.play();
						} catch (IOException err) {
							System.out.println("Error");
						} catch (JavaLayerException err){
							;
						}

						// 게임중 메세지 출력
						JOptionPane.showMessageDialog(null, "게임중입니다", "알림!!",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
				// 로그아웃
				else if (true == order.equalsIgnoreCase("SC_Logout")) {
					m_ready.rmPlayer(para);
				}
				// 새로운사용자
				else if (true == order.equalsIgnoreCase("SC_Newuser")) {
					StringTokenizer t = new StringTokenizer(para);

					String id = t.nextToken(",");
					String num = t.nextToken(",");

					m_ready.addPlayer(id, Integer.parseInt(num));
				}
				// 단어받기
				else if (true == order.equalsIgnoreCase("SC_Word")) {
					StringTokenizer t = new StringTokenizer(para);

					String word = t.nextToken(",");
					String x = t.nextToken(",");
					String item = t.nextToken(",");

					// System.out.println("Are you Ready? " + word);
					// 게임이 시작되었을때만 단어를 받음
					if (true == m_view.m_gamestart) {
						m_view.add(word, Integer.parseInt(x),
								Integer.parseInt(item));
					}
				}

				// 레디
				else if (true == order.equalsIgnoreCase("SC_Ready")) {
					System.out.println("sc레디부분 :" + order + ";" + para);

					StringTokenizer t = new StringTokenizer(para);
					String id = t.nextToken(",");
					String gameready = t.nextToken(",");

					boolean gameready1 = false;

					if (true == gameready.equalsIgnoreCase("true")) {
						gameready1 = true;
					} else {
						gameready1 = false;
					}

					m_ready.ReadyPlayer(id, gameready1);
				}
				// 시작
				else if (true == order.equalsIgnoreCase("SC_Start")) {
					setVisible(false);
					setContentPane(m_view);
					setSize(700, 538);
					setVisible(true);

//					m_ready.m_bgsound.stop();
					m_view.m_bgsound.start();

					System.out.println(para);

					try {
						m_view.repaint();
						Thread.sleep(1000);
						String command = "CS_StartRC" + ";" + "test";

						m_view.m_gamestart = true;
						m_view.m_gameover = false;
						m_view.m_time.start();

						System.out.println(command);
						Send(command);
					} catch (InterruptedException err) {
					}

				}
				// 시작에러(모든플레이어가 레디되어야 시작가능)
				else if (true == order.equalsIgnoreCase("SC_StartE1")) {
					JOptionPane.showMessageDialog(null,
							"모든플레이어가 준비가 되어야지만 시작할수있습니다", "알림!!",
							JOptionPane.INFORMATION_MESSAGE);
				}
				// 단어클리어
				else if (true == order.equalsIgnoreCase("SC_WordClear")) {
					m_view.WordClear(para);
				}
				// 아이템얻기
				else if (true == order.equalsIgnoreCase("SC_ItemA")) {
					m_player.addItem(Integer.parseInt(para));
				}
				// 아이템공격
				else if (true == order.equalsIgnoreCase("SC_ItemAattack")) {
					m_view.ItemAttack(Integer.parseInt(para));
					System.out.println("아이템공격" + para);
				}
				// 생명력받기
				else if (true == order.equalsIgnoreCase("SC_Life")) {
					StringTokenizer t = new StringTokenizer(para);

					String id = t.nextToken(",");
					String life = t.nextToken(",");

					m_ready.Life(id, Integer.parseInt(life));
				}
				// 게임오버
				else if (true == order.equalsIgnoreCase("SC_Gameover")) {
					m_ready.GameOver(para);
					m_view.m_bgsound.stop();
				}
				// 모든사람 게임오버
				else if (true == order.equalsIgnoreCase("SC_Allgameover")) {
					m_view.m_word.clear();

					setVisible(false);
					setContentPane(m_ready);
					setSize(557, 475);
					setVisible(true);

					m_view.m_bgsound.stop();
					m_view.m_endbg.stop();
//					m_ready.m_bgsound.start();

					// m_ready.Reset(para);
				}
				// 채팅
				else if (true == order.equalsIgnoreCase("SC_Msg")) {
					StringTokenizer t2 = new StringTokenizer(para);

					String id = t2.nextToken(",");
					String text = t2.nextToken(",");

					String chat = " [ " + id + " ] " + text;
					m_ready.Chat(chat);
				}
				// 아템전,노템전 선택
				else if (true == order.equalsIgnoreCase("SC_GameItem")) {
					if (true == para.equalsIgnoreCase("true")) {
						m_ready.m_item = true;
					} else {
						m_ready.m_item = false;
					}
				} else if (true == order.equalsIgnoreCase("SC_IAtext")) {
					StringTokenizer t = new StringTokenizer(para);

					String id = t.nextToken(",");
					String text = t.nextToken(",");

					m_view.Chat(id, text);
				} else if (true == order.equalsIgnoreCase("SC_Stats")) {
					StringTokenizer t = new StringTokenizer(para);

					String id = t.nextToken(",");
					String win = t.nextToken(",");
					String lose = t.nextToken(",");

					m_ready.m_stats = true;
					m_ready.Reset(id, Integer.parseInt(win),
							Integer.parseInt(lose));
				} else if (true == order.equalsIgnoreCase("SC_StatsA")) {
					StringTokenizer t = new StringTokenizer(para);

					String id = t.nextToken(",");
					String win = t.nextToken(",");
					String lose = t.nextToken(",");

					m_ready.Reset(id, Integer.parseInt(win),
							Integer.parseInt(lose));
				}

			}
		} catch (IOException erro) {
			System.out.println(erro.toString());
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Main() // Main 함수
	{
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Logout();
			}
		});

		m_view = new CView(this);
		m_login = new CLogin(this);
		m_ready = new CReady(this);
		m_player = new CPlayer();
		m_gameover = new CGameover();

		add(m_login);

		setTitle("- 샤샤샤 -");
		setSize(450, 255);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new Main();
	}
}
