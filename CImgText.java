package client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

// �̹��� �ؽ�Ʈ ���� �Լ� + ������ ����
public class CImgText {
	Image m_middle;
	int m_cx, m_cy;
	String m_key;

	// �̹��� �ؽ�Ʈ �Լ�
	public CImgText(String fontFile, String key, int cx, int cy) {
		m_key = key; // key�� ������
		m_cx = cx; // cx�� ������
		m_cy = cy; // cy�� ������

		// toolkit ����Ͽ� �̹��� ��ü ����
		m_middle = Toolkit.getDefaultToolkit().getImage(fontFile);
	}

	// ��ü �ܾ� �Լ�
	public void DrawFullText(JPanel observer, Graphics g, int x, int y) {
		g.drawImage(m_middle, x, y, observer); // �̹��� ����
	}

	// �ܾ� �����
	public void DrawString(JPanel observer, Graphics g, String str, int x, int y) {
		// str.toUpperCase();
		char[] aCodes = str.toCharArray(); // �迭�� ����
		int len = str.length(); // length ����

		/* �ܾ� ���� */
		for (int i = 0; i < len; i++) {
			int src_index = m_key.indexOf(aCodes[i]);

			int cx = m_cx;
			int cy = m_cy;
			int dx = x + cx * i;
			int dy = y;
			int sx = cx * src_index;
			int sy = 0;

			if (0 <= sx) {
				// �̹��� ����
				g.drawImage(m_middle, dx, dy, dx + cx, dy + cy, sx, sy,
						sx + cx, sy + cy, observer);
			}
		}
	}
}
