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

	// login �Լ�
	public void Login(String id) {
		Init("localhost", 9001); // host�ּҿ� port�ּҸ� �޴� �Լ�
		String command;
		command = "CS_Login" + ";" + id;

		System.out.println(command); // command ���
		Send(command); // command ����(id����)

		Thread t; // thread ����
		t = new Thread(this);
		t.start();

		m_id = id;
	}

	// logout �Լ�
	public void Logout() {
		String command = "CS_Logout" + ";" + m_id;
		Send(command); // command ����
	}

	// �ʱ�ȭ �Լ�
	public void Init(String add, int port) {
		try {
			m_socket = new Socket(add, port); // socket ����
			m_in = new BufferedReader(new InputStreamReader(
					m_socket.getInputStream())); // �д� �Լ�
			m_out = new PrintWriter(new OutputStreamWriter(
					m_socket.getOutputStream())); // ��� �Լ�
		} catch (IOException erro) {
			System.out.println(erro.toString());
		}
	}

	// close �Լ�
	public void Close() {
		try {
			m_in.close();
			m_out.close();
			m_socket.close();
		} catch (IOException erro) {
			System.out.println(erro.toString());
		}
	}

	// send �Լ�
	public void Send(String msg) {
		m_out.println(msg);
		m_out.flush();
	}

	// SendMsg �Լ� (�޽����� ������ �Լ�)
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
				msg = m_in.readLine(); // �޽����� �о��

				StringTokenizer parser = new StringTokenizer(msg);

				String order = parser.nextToken(";");
				String para = parser.nextToken(";");

				// �α���
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

						// ������ ���̵��� ��� �޼��� ���
						JOptionPane.showMessageDialog(null, "�����Ѿ��̵�!!!",
								"�˸�!!", JOptionPane.INFORMATION_MESSAGE);
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

						// �ο� �ʰ��� ��� �޼��� ���
						JOptionPane.showMessageDialog(null, "�ο��ʰ�", "�˸�!!",
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

						// ������ �޼��� ���
						JOptionPane.showMessageDialog(null, "�������Դϴ�", "�˸�!!",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
				// �α׾ƿ�
				else if (true == order.equalsIgnoreCase("SC_Logout")) {
					m_ready.rmPlayer(para);
				}
				// ���ο�����
				else if (true == order.equalsIgnoreCase("SC_Newuser")) {
					StringTokenizer t = new StringTokenizer(para);

					String id = t.nextToken(",");
					String num = t.nextToken(",");

					m_ready.addPlayer(id, Integer.parseInt(num));
				}
				// �ܾ�ޱ�
				else if (true == order.equalsIgnoreCase("SC_Word")) {
					StringTokenizer t = new StringTokenizer(para);

					String word = t.nextToken(",");
					String x = t.nextToken(",");
					String item = t.nextToken(",");

					// System.out.println("Are you Ready? " + word);
					// ������ ���۵Ǿ������� �ܾ ����
					if (true == m_view.m_gamestart) {
						m_view.add(word, Integer.parseInt(x),
								Integer.parseInt(item));
					}
				}

				// ����
				else if (true == order.equalsIgnoreCase("SC_Ready")) {
					System.out.println("sc����κ� :" + order + ";" + para);

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
				// ����
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
				// ���ۿ���(����÷��̾ ����Ǿ�� ���۰���)
				else if (true == order.equalsIgnoreCase("SC_StartE1")) {
					JOptionPane.showMessageDialog(null,
							"����÷��̾ �غ� �Ǿ������ �����Ҽ��ֽ��ϴ�", "�˸�!!",
							JOptionPane.INFORMATION_MESSAGE);
				}
				// �ܾ�Ŭ����
				else if (true == order.equalsIgnoreCase("SC_WordClear")) {
					m_view.WordClear(para);
				}
				// �����۾��
				else if (true == order.equalsIgnoreCase("SC_ItemA")) {
					m_player.addItem(Integer.parseInt(para));
				}
				// �����۰���
				else if (true == order.equalsIgnoreCase("SC_ItemAattack")) {
					m_view.ItemAttack(Integer.parseInt(para));
					System.out.println("�����۰���" + para);
				}
				// ����¹ޱ�
				else if (true == order.equalsIgnoreCase("SC_Life")) {
					StringTokenizer t = new StringTokenizer(para);

					String id = t.nextToken(",");
					String life = t.nextToken(",");

					m_ready.Life(id, Integer.parseInt(life));
				}
				// ���ӿ���
				else if (true == order.equalsIgnoreCase("SC_Gameover")) {
					m_ready.GameOver(para);
					m_view.m_bgsound.stop();
				}
				// ����� ���ӿ���
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
				// ä��
				else if (true == order.equalsIgnoreCase("SC_Msg")) {
					StringTokenizer t2 = new StringTokenizer(para);

					String id = t2.nextToken(",");
					String text = t2.nextToken(",");

					String chat = " [ " + id + " ] " + text;
					m_ready.Chat(chat);
				}
				// ������,������ ����
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

	public Main() // Main �Լ�
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

		setTitle("- ������ -");
		setSize(450, 255);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new Main();
	}
}
