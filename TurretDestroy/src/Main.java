import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

	static class Point{
		int x;
		int y;
		String path;
		
		public Point(int x, int y, String path) {
			this.x = x;
			this.y = y;
			this.path = path;
		}
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	
	static int[][] attack_history;
	static int[][] matrix;
	static boolean[][] relate;
	
	
	static int N, M, K;
	
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {1, 0, -1, 0};
	
	
	static int[] splash_dx = {-1, -1, -1, 0, 1, 1, 1, 0};
	static int[] splash_dy = {-1, 0, 1, 1, 1, 0, -1, -1};
	static int turn;
	
	static int min, minX, minY, min_his;
	static int max, maxX, maxY, max_his;
	static int max_result;
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String[] input = br.readLine().split(" ");
		
		N = Integer.parseInt(input[0]);
		M = Integer.parseInt(input[1]);
		K = Integer.parseInt(input[2]);
		
		matrix = new int[N + 1][M + 1];
		attack_history = new int[N + 1][M + 1];
		relate = new boolean[N + 1][M + 1];
		
		for(int i = 1; i <= N; i++) {
			input = br.readLine().split(" ");
			for(int j = 1; j <= M; j++) {
				int num = Integer.parseInt(input[j - 1]);
				matrix[i][j] = num;
			}
		}
		
		turn = 1;
		for(int i = 0; i < K; i++) {
			relate = new boolean[N + 1][M + 1];
			print();
			if(turn == 15) {
				System.out.println();
			}
			get_attacker();
			
			get_victim();
			
			attack();
			
			destroy();
			if(!check()) {
				System.out.println(max_result);
				return;
			}
			
			repair();

			turn++;
		}

		check();
		
		System.out.println(max_result);
		
	}
	
	static void attack() {
		int startX = minX;
		int startY = minY;

		boolean[][] visited = new boolean[N + 1][M + 1];
		
		Queue<Point> que = new LinkedList<>();
		
		que.add(new Point(startX, startY, ""));
		visited[startX][startY] = true;
		
		String path = null;
		boolean find_razer = false;
		while(!que.isEmpty()) {
			Point now = que.poll();
			if(now.x == maxX && now.y == maxY) {
				path = now.path;
				find_razer = true;
				break;
			}
			for(int z = 0; z < 4; z++) {
				int nextX = now.x + dx[z];
				int nextY = now.y + dy[z];
				
				if(nextX > N) {
					nextX = 1;
				}
				
				if(nextX < 1) {
					nextX = N;
				}
				
				if(nextY > M) {
					nextY = 1;
				}
				
				if(nextY < 1) {
					nextY = M;
				}
				
				if(matrix[nextX][nextY] > 0 && !visited[nextX][nextY]) {
					que.add(new Point(nextX, nextY, now.path + String.valueOf(z)));
					visited[nextX][nextY] = true;
				}
				
			}
		}
		
		relate[startX][startY] = true;
		if(find_razer) { //레이저공격
			
			int nowX = startX;
			int nowY = startY;
			
			for(int i = 0; i < path.length(); i++) {
				int dir = path.charAt(i) - '0';
				nowX = nowX + dx[dir];
				nowY = nowY + dy[dir];
				
				if(nowX > N) {
					nowX = 1;
				}
				
				if(nowX < 1) {
					nowX = N;
				}
				
				if(nowY > M) {
					nowY = 1;
				}
				
				if(nowY < 1) {
					nowY = M;
				}
				
				relate[nowX][nowY] = true;
				if(nowX == maxX && nowY == maxY) {
					matrix[nowX][nowY] -= min;
				}else {
					matrix[nowX][nowY] -= min / 2;
				}
				
			}
		}else {// 포탄공격
			
			startX = maxX;
			startY = maxY;
			matrix[startX][startY] -= min;
			relate[startX][startY] = true;
			for(int z = 0; z < 8; z++) {
				int nextX = startX + splash_dx[z];
				int nextY = startY + splash_dy[z];
				
				if(nextX > N) {
					nextX = 1;
				}
				
				if(nextX < 1) {
					nextX = N;
				}
				
				if(nextY > M) {
					nextY = 1;
				}
				
				if(nextY < 1) {
					nextY = M;
				}
				
				if(nextX == minX && nextY == minY) {
					continue;
				}
				
				if(matrix[nextX][nextY] > 0) {
					matrix[nextX][nextY] -= min / 2;
					relate[nextX][nextY] = true;
				}
				
			}
		}
		
		attack_history[minX][minY] = turn;
	}
	
	static void repair() {
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= M; j++) {
				if(matrix[i][j] > 0) {
					if(!relate[i][j]) {
						matrix[i][j]++;
					}
				}
			}
		}
	}
	
	static void get_attacker() {
		min = Integer.MAX_VALUE;
		minX = 0;
		minY = 0;
		min_his = 0;
		
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= M; j++) {
				if(matrix[i][j] > 0) {
					if(matrix[i][j] < min) {
						min = matrix[i][j];
						minX = i;
						minY = j;
						min_his = attack_history[i][j];
					}else if(matrix[i][j] == min) {
						if(attack_history[i][j] > min_his) {
							min = matrix[i][j];
							minX = i;
							minY = j;
							min_his = attack_history[i][j];
						}else if(attack_history[i][j] == min_his) {
							if(i + j > minX + minY) {
								min = matrix[i][j];
								minX = i;
								minY = j;
								min_his = attack_history[i][j];
							}else if(i + j == minX + minY) {
								if(j > minY) {
									min = matrix[i][j];
									minX = i;
									minY = j;
									min_his = attack_history[i][j];
								}
							}
						}
					}
				}
			}
		}
		min += N + M;
		matrix[minX][minY] = min;
	}
	
	static void get_victim() {
		max = Integer.MIN_VALUE;
		maxX = 0;
		maxY = 0;
		max_his = 0;
		
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= M; j++) {
				if(matrix[i][j] > 0) {
					if(i == minX && j == minY) continue;
					if(matrix[i][j] > max) {
						max = matrix[i][j];
						maxX = i;
						maxY = j;
						max_his = attack_history[i][j];
					}else if(matrix[i][j] == max) {
						if(attack_history[i][j] < max_his) {
							max = matrix[i][j];
							maxX = i;
							maxY = j;
							max_his = attack_history[i][j];
						}else if(attack_history[i][j] == max_his) {
							if(i + j < maxX + maxY) {
								max = matrix[i][j];
								maxX = i;
								maxY = j;
								max_his = attack_history[i][j];
							}else if(i + j == maxX + maxY) {
								if(j < maxY) {
									max = matrix[i][j];
									maxX = i;
									maxY = j;
									max_his = attack_history[i][j];
								}
							}
						}
					}
				}
			}
		}
	}
	
	static void destroy() {
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= M; j++) {
				if(matrix[i][j] <= 0) {
					matrix[i][j] = 0;
				}
			}
		}
	}
	
	static boolean check() {
		int cnt = 0;
		boolean flag = false;
		max_result = 0;
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= M; j++) {
				if(matrix[i][j] > 0) {
					cnt++;
					if(matrix[i][j] > max_result) {
						max_result = matrix[i][j];
					}
				}
			}
		}
		
		if(cnt > 1) {
			flag = true;
		}
		
		return flag;
	}
	
	
	static void print() {
		System.out.println("------------");
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= M; j++) {
				System.out.print(matrix[i][j] +" ");
			}
			System.out.println();
		}
	}

}
