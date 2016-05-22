package client;

// 플레이어에 관한 클래스
public class CPlayer {
	String m_id; // 플레이어의 id
	boolean m_ready; // 대기실에서의 ready 상태변수
	boolean m_gameover; // 플레이어의 gameover 상태변수

	int[] m_item; // 아이템
	int m_score; // 스코어
	int m_num; // 라이프 감소하는데에 쓰일 변수
	int m_win; // 승
	int m_lose; // 패
	int m_life; // 라이프(생명)

	// 라이프 감소함수
	public void deLife(int num) {

		// num만큼 계속 라이프가 감소한다
		m_life -= num;
	}

	// 획득 아이템 추가해주는 함수
	public void addItem(int item) {

		// 아이템 획득의 경우 (m_item[0]이 비어있을 때)
		if (0 == m_item[0]) {
			m_item[0] = item; // 첫번째 칸에 획득한 아이템 넣는다
		}

		// 아이템 획득의경우 (m_item[0]이 비어있지 않을 때)
		else if (0 == m_item[1]) {
			m_item[1] = item; // 두번째 칸에 획득한 아이템 넣는다
		}

		// 아이템을 2개 다 가지고 있을 경우 획득하지 못한다
		else {
		}
	}

	// 플레이어의 초기값 상태 함수
	public CPlayer() {

		m_id = "";
		m_ready = false; // 래디상태=false
		m_gameover = false; // gameover상태=false

		m_item = new int[2]; // 소지할수 있는 아이템 개수 2개
		m_item[0] = 0; // 처음칸 아이템 없음
		m_item[1] = 0; // 두번째칸 아이템 없음

		m_win = 0; // 승=0
		m_lose = 0; // 패=0
		m_life = 100; // 라이프 100 부터 시작
		m_score = 0; // 스코어 0 부터 시작
		m_num = 0; // 라이프를 깎는 값
	}
}
