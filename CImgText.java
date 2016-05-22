package client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

// 이미지 텍스트 정의 함수 + 변수들 선언
public class CImgText {
	Image m_middle;
	int m_cx, m_cy;
	String m_key;

	// 이미지 텍스트 함수
	public CImgText(String fontFile, String key, int cx, int cy) {
		m_key = key; // key로 재정의
		m_cx = cx; // cx로 재정의
		m_cy = cy; // cy로 재정의

		// toolkit 사용하여 이미지 객체 생성
		m_middle = Toolkit.getDefaultToolkit().getImage(fontFile);
	}

	// 전체 단어 함수
	public void DrawFullText(JPanel observer, Graphics g, int x, int y) {
		g.drawImage(m_middle, x, y, observer); // 이미지 생성
	}

	// 단어 만들기
	public void DrawString(JPanel observer, Graphics g, String str, int x, int y) {
		// str.toUpperCase();
		char[] aCodes = str.toCharArray(); // 배열로 받음
		int len = str.length(); // length 길이

		/* 단어 생성 */
		for (int i = 0; i < len; i++) {
			int src_index = m_key.indexOf(aCodes[i]);

			int cx = m_cx;
			int cy = m_cy;
			int dx = x + cx * i;
			int dy = y;
			int sx = cx * src_index;
			int sy = 0;

			if (0 <= sx) {
				// 이미지 생성
				g.drawImage(m_middle, dx, dy, dx + cx, dy + cy, sx, sy,
						sx + cx, sy + cy, observer);
			}
		}
	}
}
