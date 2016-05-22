package client;

import java.awt.Graphics;
import java.util.Random;

// CWord�� Runnable�̶�� �������̽� ����
public class CWord implements Runnable {

	// �ؽ�Ʈ �̹���
	CImgText m_imgText;

	// ȭ���� x,y��� ������, �������� �ܾ��� ���ǵ� ���� ����
	int m_x, m_y, m_item, m_speed;

	String m_id; // ID�� ���ڿ� String���� ����
	Random m_rnd; // �������� �ܾ Random�ϰ�
	CView m_view; // ȭ���� CView���� �޾ƿ�

	boolean m_clear; //
	boolean m_blind; // ����ε� �������� ���� �ܾ �������� �ϴ� ����

	/** Creates a new instance of CWord */
	public CWord() {

		// ���ڿ� ����
		String key = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

		// �⺻�̹���(���ڻ�) �޾ƿ���
		m_imgText = new CImgText("font2.png", key, 12, 19);

		// �����Լ� �ҷ�����
		m_rnd = new Random();

		// y��, �ܾ��� �ӵ� �� ���� �ʱ�ȭ
		m_y = 0;
		m_speed = 20;
		m_clear = false;
		m_blind = false;
	}

	public void run() {
	} // ����

	public void ItemAttack(int item) { // ������ ���� �Լ�
		if (10 == item) { // 10���̶�� �������� ������
			m_blind = true; // ����ε带 ����
		}
	}

	public void paintComponent(Graphics g) {
		String key = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

		if (10 == m_item) { // �������� 10��(����ε�)�� �� ���ڻ�
			m_imgText = new CImgText("font3.png", key, 12, 19);
		} else if (20 == m_item) { // �������� 20��(�� ���� �߰�)�� �� ���ڻ�
			m_imgText = new CImgText("font4.png", key, 12, 19);
		} else if (30 == m_item) { // �������� 30��(���� ���� ���)�� �� ���ڻ�
			m_imgText = new CImgText("font5.png", key, 12, 19);
		} else if (40 == m_item) { // �������� 40��(������ ������ �����ϱ�)�� �� ���ڻ�
			m_imgText = new CImgText("font6.png", key, 12, 19);
		} else { // �⺻ ���ڻ�
			m_imgText = new CImgText("font2.png", key, 12, 19);
		}

		// ����ε� �� �� ����ε� �� �۾� ���� ���ڰ� �������� ��ġ
		if (true == m_blind) {
			m_imgText = new CImgText("font7.png", key, 12, 19);
			m_imgText.DrawString(m_view, g, m_id, m_x, m_y);
		}
		// ���ڰ� �������� ��ġ�� �������°�, ������ �ܾ�
		m_imgText.DrawString(m_view, g, m_id, m_x, m_y);

	}

	/*
	 * ������ ���� �Ⱥ��̰��ϱ� : 10�� �� ���� �ø��� : 20�� ���� ������ : 30�� ������ ������ �����ϱ� : 40��
	 */

	public void Go() {
		m_y += m_speed; // ���ǵ� ��
	}
}
