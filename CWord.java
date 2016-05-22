package client;

import java.awt.Graphics;
import java.util.Random;

// CWord는 Runnable이라는 인터페이스 구현
public class CWord implements Runnable {

	// 텍스트 이미지
	CImgText m_imgText;

	// 화면의 x,y축과 아이템, 떨어지는 단어의 스피드 변수 선언
	int m_x, m_y, m_item, m_speed;

	String m_id; // ID는 문자열 String으로 선언
	Random m_rnd; // 떨어지는 단어를 Random하게
	CView m_view; // 화면은 CView에서 받아옴

	boolean m_clear; //
	boolean m_blind; // 블라인드 아이템을 쓰면 단어가 가리지게 하는 변수

	/** Creates a new instance of CWord */
	public CWord() {

		// 문자열 선언
		String key = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

		// 기본이미지(문자색) 받아오기
		m_imgText = new CImgText("font2.png", key, 12, 19);

		// 랜덤함수 불러오기
		m_rnd = new Random();

		// y축, 단어의 속도 등 변수 초기화
		m_y = 0;
		m_speed = 20;
		m_clear = false;
		m_blind = false;
	}

	public void run() {
	} // 실행

	public void ItemAttack(int item) { // 아이템 공격 함수
		if (10 == item) { // 10번이라는 아이템이 들어오면
			m_blind = true; // 블라인드를 실행
		}
	}

	public void paintComponent(Graphics g) {
		String key = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

		if (10 == m_item) { // 아이템이 10번(블라인드)일 때 글자색
			m_imgText = new CImgText("font3.png", key, 12, 19);
		} else if (20 == m_item) { // 아이템이 20번(내 생명 추가)일 때 글자색
			m_imgText = new CImgText("font4.png", key, 12, 19);
		} else if (30 == m_item) { // 아이템이 30번(상대방 생명 깎기)일 때 글자색
			m_imgText = new CImgText("font5.png", key, 12, 19);
		} else if (40 == m_item) { // 아이템이 40번(상대방의 아이템 삭제하기)일 때 글자색
			m_imgText = new CImgText("font6.png", key, 12, 19);
		} else { // 기본 문자색
			m_imgText = new CImgText("font2.png", key, 12, 19);
		}

		// 블락인드 일 때 블라인드 된 글씨 색과 문자가 내려오는 위치
		if (true == m_blind) {
			m_imgText = new CImgText("font7.png", key, 12, 19);
			m_imgText.DrawString(m_view, g, m_id, m_x, m_y);
		}
		// 문자가 내려오는 위치와 보여지는곳, 나오는 단어
		m_imgText.DrawString(m_view, g, m_id, m_x, m_y);

	}

	/*
	 * 아이템 상대방 안보이게하기 : 10번 내 생명 올리기 : 20번 상대방 생명깍기 : 30번 상대방의 아이템 삭제하기 : 40번
	 */

	public void Go() {
		m_y += m_speed; // 스피드 업
	}
}
