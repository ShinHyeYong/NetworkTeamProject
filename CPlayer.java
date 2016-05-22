package client;

// �÷��̾ ���� Ŭ����
public class CPlayer {
	String m_id; // �÷��̾��� id
	boolean m_ready; // ���ǿ����� ready ���º���
	boolean m_gameover; // �÷��̾��� gameover ���º���

	int[] m_item; // ������
	int m_score; // ���ھ�
	int m_num; // ������ �����ϴµ��� ���� ����
	int m_win; // ��
	int m_lose; // ��
	int m_life; // ������(����)

	// ������ �����Լ�
	public void deLife(int num) {

		// num��ŭ ��� �������� �����Ѵ�
		m_life -= num;
	}

	// ȹ�� ������ �߰����ִ� �Լ�
	public void addItem(int item) {

		// ������ ȹ���� ��� (m_item[0]�� ������� ��)
		if (0 == m_item[0]) {
			m_item[0] = item; // ù��° ĭ�� ȹ���� ������ �ִ´�
		}

		// ������ ȹ���ǰ�� (m_item[0]�� ������� ���� ��)
		else if (0 == m_item[1]) {
			m_item[1] = item; // �ι�° ĭ�� ȹ���� ������ �ִ´�
		}

		// �������� 2�� �� ������ ���� ��� ȹ������ ���Ѵ�
		else {
		}
	}

	// �÷��̾��� �ʱⰪ ���� �Լ�
	public CPlayer() {

		m_id = "";
		m_ready = false; // �������=false
		m_gameover = false; // gameover����=false

		m_item = new int[2]; // �����Ҽ� �ִ� ������ ���� 2��
		m_item[0] = 0; // ó��ĭ ������ ����
		m_item[1] = 0; // �ι�°ĭ ������ ����

		m_win = 0; // ��=0
		m_lose = 0; // ��=0
		m_life = 100; // ������ 100 ���� ����
		m_score = 0; // ���ھ� 0 ���� ����
		m_num = 0; // �������� ��� ��
	}
}
