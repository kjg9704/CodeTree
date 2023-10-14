import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	static class Rabbit{
		int x;
		int y;
		int pid;
		int d;
		int jump;
		long score;
		boolean choiced;
		
		public Rabbit(int pid, int d) {
			this.x = 1;
			this.y = 1;
			this.jump = 0;
			this.score = 0;
			this.choiced = false;
			
			this.pid = pid;
			this.d = d;
		}
	}
	
	static class Point{
		int x;
		int y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	
	static int[] rabbit_score;
	static Rabbit[] rabbit_arr;
	static int[] rabbit_jump;
	
	static int Q, N, M, P;
	
	
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	static long score_sum;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		Q = Integer.parseInt(br.readLine());
		
		for(int i = 0; i < Q; i++) {
			String[] input = br.readLine().split(" ");
			int cmd = Integer.parseInt(input[0]);
			
			if(cmd == 100) {
				// prepare race
				N = Integer.parseInt(input[1]);
				M = Integer.parseInt(input[2]);
				P = Integer.parseInt(input[3]);
				rabbit_arr = new Rabbit[P];
				rabbit_score = new int[P];
				rabbit_jump = new int[P];
				
				int index = 4;
				for(int j = 0; j < P; j++) {
					int pid = Integer.parseInt(input[index++]);
					int d = Integer.parseInt(input[index++]);
					rabbit_arr[j] = new Rabbit(pid, d);
				}
			}else if(cmd == 200) {
				// race
				int K = Integer.parseInt(input[1]);
				int S = Integer.parseInt(input[2]);
				
				for(int j = 0; j < P; j++) {
					rabbit_arr[j].choiced = false;
				}
				
				for(int j = 0; j < K; j++) {
					race();
				}
				add_score(S);
				
			}else if(cmd == 300) {
				// change d
				int pid = Integer.parseInt(input[1]);
				int L = Integer.parseInt(input[2]);
				
				change_d(pid, L);
			}else {
				// result
				long result = get_best();
				System.out.println(result);
			}
		}

	}
	
	
	static void race() {
		
		Rabbit choice = null;
		
		for(int i = 0; i < P; i++) {
			Rabbit now = rabbit_arr[i];
			
			if(choice == null) {
				choice = now;
				continue;
			}
			
			if(choice.jump > now.jump) {
				choice = now;
			}else if(choice.jump == now.jump) {
				if(choice.x + choice.y > now.x + now.y) {
					choice = now;
				}else if(choice.x + choice.y == now.x + now.y) {
					if(choice.x > now.x) {
						choice = now;
					}else if(choice.x == now.x) {
						if(choice.y > now.y) {
							choice = now;
						}else if(choice.y == now.y) {
							if(choice.pid > now.pid) {
								choice = now;
							}
						}
					}
				}
			}
		}
		
		
		Point[] next_arr = new Point[4];
		
		for(int z = 0; z < 4; z++) {
			int nowX = choice.x;
			int nowY = choice.y;
			
			if(z == 1) {//오른쪽
				int nextY = move_right(nowX, nowY, choice.d);
				next_arr[z] = new Point(nowX, nextY);
			}else if(z == 3) { // 왼쪽
				int nextY = move_left(nowX, nowY, choice.d);
				next_arr[z] = new Point(nowX, nextY);
			}else if(z == 0) { //위
				int nextX = move_up(nowX, nowY, choice.d);
				next_arr[z] = new Point(nextX, nowY);
			}else { // 아래
				int nextX = move_down(nowX, nowY, choice.d);
				next_arr[z] = new Point(nextX, nowY);
			}
			
		}
		
		Point next = null;
		
		for(int i = 0; i < 4; i++) {
			Point now = next_arr[i];
			if(next == null) {
				next = now;
				continue;
			}
			
			if(next.x + next.y < now.x + now.y) {
				next = now;
			}else if(next.x + next.y == now.x + now.y) {
				if(next.x < now.x) {
					next = now;
				}else if(next.x == now.x) {
					if(next.y < now.y) {
						next = now;
					}
				}
			}
			
		}
		
		choice.x = next.x;
		choice.y = next.y;
		choice.jump += 1;
		choice.choiced = true;
		
		score_sum += choice.x + choice.y;
		choice.score -= (choice.x + choice.y);
	}
	
	static int move_right(int nowX, int nowY, int dist) {
		
		dist = dist % (2 * (M - 1));
		
		if(M - nowY >= dist) {
			nowY += dist;
		}else {
			dist -= (M - nowY);
			nowY = M;
			if(dist > nowY - 1) {
				dist -= (nowY - 1);
				nowY = 1;
				if(dist > 0) {
					nowY += dist;
				}
			}else {
				nowY -= dist;
			}
		}
		
		return nowY;
	}
	
	static int move_left(int nowX, int nowY, int dist) {
		
		dist = dist % (2 * (M - 1));
		
		if(nowY - 1 >= dist) {
			nowY -= dist;
		}else {
			dist -= (nowY - 1);
			nowY = 1;
			if(dist > M - 1) {
				dist -= (M - 1);
				nowY = M;
				if(dist > 0) {
					nowY -= dist;
				}
			}else {
				nowY += dist;
			}
		}
		
		return nowY;
	}
	
	static int move_up(int nowX, int nowY, int dist) {
		
		dist = dist % (2 * (N - 1));
		
		if(nowX - 1 >= dist) {
			nowX -= dist;
		}else {
			dist -= (nowX - 1);
			nowX = 1;
			if(dist > N - 1) {
				dist -= (N - 1);
				nowX = N;
				if(dist > 0) {
					nowX -= dist;
				}
			}else {
				nowX += dist;
			}
		}
		
		return nowX;
	}
	
	static int move_down(int nowX, int nowY, int dist) {
		
		dist = dist % (2 * (N - 1));
		
		if(N - nowX >= dist) {
			nowX += dist;
		}else {
			dist -= (N - nowX);
			nowX = N;
			if(dist > nowX - 1) {
				dist -= (nowX - 1);
				nowX = 1;
				if(dist > 0) {
					nowX += dist;
				}
			}else {
				nowX -= dist;
			}
		}
		
		return nowX;
	}

	
	static void add_score(int S) {
		Rabbit choice = null;
		
		for(int i = 0; i < P; i++) {
			Rabbit now = rabbit_arr[i];
			
			if(now.choiced == false) {
				continue;
			}
			
			if(choice == null) {
				choice = now;
				continue;
			}
			
			
			if(choice.x + choice.y < now.x + now.y) {
				choice = now;
			}else if(choice.x + choice.y == now.x + now.y) {
				if(choice.x < now.x) {
					choice = now;
				}else if(choice.x == now.x) {
					if(choice.y < now.y) {
						choice = now;
					}else if(choice.y == now.y) {
						if(choice.pid < now.pid) {
							choice = now;
						}
					}
				}
			}
			
		}
		
		choice.score += S;
	}
	
	static void change_d(int pid, int L) {
		for(int i = 0; i < P; i++) {
			if(rabbit_arr[i].pid == pid) {
				rabbit_arr[i].d = rabbit_arr[i].d * L;
			}
		}
	}
	
	static long get_best() {
		long max = 0;
		for(int i = 0; i < P; i++) {
			long now = rabbit_arr[i].score + score_sum;
			
			if(max < now) {
				max = now;
			}
		}
		
		return max;
	}
	
	static int get_back_dir(int dir) {
		if(dir == 0) {
			return 2;
		}else if(dir == 1) {
			return 3;
		}else if(dir == 2) {
			return 0;
		}else {
			return 1;
		}
	}

}
