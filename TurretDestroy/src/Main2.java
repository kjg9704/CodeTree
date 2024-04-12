import java.io.*;
import java.util.*;

public class Main2 {

	static class Point{
		int x, y;
		String path;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
			this.path = "";
		}
		
		public Point(int x, int y, String path) {
			this.x = x;
			this.y = y;
			this.path = path;
		}
	}
	static class Turret{
		int x, y, prev_attack, attack;
		public Turret(int x, int y, int prev_attack, int attack) {
			this.x = x;
			this.y = y;
			this.prev_attack = prev_attack;
			this.attack = attack;
		}
	}
	static int N, M, K, handicap;
	
	static int[][] matrix;
	static int[][] prev_attack;
	static boolean[][] peace;
	static int max;
	
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {1, 0, -1, 0};
	static int turn;
	
	static int[] splash_dx = {-1, -1, -1, 0, 1, 1, 1, 0};
	static int[] splash_dy = {-1, 0, 1, 1, 1, 0, -1, -1};
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String[] input = br.readLine().split(" ");
		
		N = Integer.parseInt(input[0]);
		M = Integer.parseInt(input[1]);
		K = Integer.parseInt(input[2]);
		
		handicap = N + M;
		matrix = new int[N + 1][M + 1];
		prev_attack = new int[N + 1][M + 1];
		
		for(int i = 1; i <= N; i++) {
			input = br.readLine().split(" ");
			for(int j = 1; j <= M; j++) {
				matrix[i][j] = Integer.parseInt(input[j - 1]);
			}
		}
		
		turn = 0;
		for(int i = 0; i < K; i++) {
			turn++;
			peace = new boolean[N + 1][M + 1];
			Turret attacker = getWeak();
			Turret victim = getStrong(attacker);
			
			attack(attacker, victim);
			destroy();

			if(!check()) {
				break;
			}
			repair();
		}

		check();
		System.out.println(max);

	}
	
	static void attack(Turret attacker, Turret victim) {
		boolean razer = false;
		
		Queue<Point> que = new LinkedList<>();
		boolean[][] visited = new boolean[N + 1][M + 1];
		que.add(new Point(attacker.x, attacker.y));
		visited[attacker.x][attacker.y] = true;
		String path = null;
		while(!que.isEmpty()) {
			Point now = que.poll();
			
			if(now.x == victim.x && now.y == victim.y) {
				path = now.path;
				razer = true;
				break;
			}
			for(int z = 0; z < 4; z++) {
				int nextX = now.x + dx[z];
				int nextY = now.y + dy[z];
				
				if(nextX < 1) nextX = N;
				if(nextX > N) nextX = 1;
				if(nextY < 1) nextY = M;
				if(nextY > M) nextY = 1;
				
				if(matrix[nextX][nextY] == 0) continue;
				if(visited[nextX][nextY]) continue;
				
				visited[nextX][nextY] = true;
				que.add(new Point(nextX, nextY, now.path + String.valueOf(z)));
			}
		}
		
		peace[attacker.x][attacker.y] = true;
		prev_attack[attacker.x][attacker.y] = turn;
		if(razer) {
			int nextX = attacker.x;
			int nextY = attacker.y;
			
			for(int i = 0; i < path.length(); i++) {
				int dir = path.charAt(i) - 0x30;
				
				nextX += dx[dir];
				nextY += dy[dir];
				
				if(nextX < 1) nextX = N;
				if(nextX > N) nextX = 1;
				if(nextY < 1) nextY = M;
				if(nextY > M) nextY = 1;
				
				if(nextX == victim.x && nextY == victim.y) {
					matrix[nextX][nextY] -= attacker.attack;
				}else {
					matrix[nextX][nextY] -= attacker.attack / 2;
				}
				peace[nextX][nextY] = true;
			}
		}else {
			matrix[victim.x][victim.y] -= attacker.attack;
			peace[victim.x][victim.y] = true;
			for(int i = 0; i < 8; i++) {
				int nextX = victim.x + splash_dx[i];
				int nextY = victim.y + splash_dy[i];
				if(nextX < 1) nextX = N;
				if(nextX > N) nextX = 1;
				if(nextY < 1) nextY = M;
				if(nextY > M) nextY = 1;
				
				if(nextX == attacker.x && nextY == attacker.y) continue;
				if(matrix[nextX][nextY] > 0) {
					matrix[nextX][nextY] -= attacker.attack / 2;
					peace[nextX][nextY] = true;
				}
			}
		}
		
	}
	
	static void destroy() {
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= M; j++) {
				if(matrix[i][j] < 0) {
					matrix[i][j] = 0;
				}
			}
		}
	}
	static Turret getWeak() {
		Turret res = new Turret(0, 0, 0, Integer.MAX_VALUE);
		
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= M; j++) {
				if(matrix[i][j] > 0) {
					if(matrix[i][j] < res.attack) {
						res.x = i;
						res.y = j;
						res.prev_attack = prev_attack[i][j];
						res.attack = matrix[i][j];
					}else if(matrix[i][j] == res.attack) {
						if(res.prev_attack < prev_attack[i][j]) {
							res.x = i;
							res.y = j;
							res.prev_attack = prev_attack[i][j];
							res.attack = matrix[i][j];
						}else if(res.prev_attack == prev_attack[i][j]) {
							if(res.x + res.y < i + j) {
								res.x = i;
								res.y = j;
								res.prev_attack = prev_attack[i][j];
								res.attack = matrix[i][j];
							}else if(res.x + res.y == i + j) {
								if(res.y < j) {
									res.x = i;
									res.y = j;
									res.prev_attack = prev_attack[i][j];
									res.attack = matrix[i][j];
								}
							}
						}
					}
				}
			}
		}
		matrix[res.x][res.y] += handicap;
		res.attack += handicap;
		return res;
	}
	
	static Turret getStrong(Turret weak) {
		Turret res = new Turret(0, 0, 0, 0);
		
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= M; j++) {
				if(i == weak.x && j == weak.y) continue;
				if(matrix[i][j] > 0) {
					if(matrix[i][j] > res.attack) {
						res.x = i;
						res.y = j;
						res.prev_attack = prev_attack[i][j];
						res.attack = matrix[i][j];
					}else if(matrix[i][j] == res.attack) {
						if(res.prev_attack > prev_attack[i][j]) {
							res.x = i;
							res.y = j;
							res.prev_attack = prev_attack[i][j];
							res.attack = matrix[i][j];
						}else if(res.prev_attack == prev_attack[i][j]) {
							if(res.x + res.y > i + j) {
								res.x = i;
								res.y = j;
								res.prev_attack = prev_attack[i][j];
								res.attack = matrix[i][j];
							}else if(res.x + res.y == i + j) {
								if(res.y > j) {
									res.x = i;
									res.y = j;
									res.prev_attack = prev_attack[i][j];
									res.attack = matrix[i][j];
								}
							}
						}
					}
				}
			}
		}
		return res;
	}
	
	static boolean check() {
		int cnt = 0;
		max = 0;
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= M; j++) {
				if(matrix[i][j] > 0) {
					cnt++;
					max = Math.max(max, matrix[i][j]);
				}
			}
		}
		if(cnt == 1) {
			return false;
		}else {
			return true;
		}
	}
	static void repair() {
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= M; j++) {
				if(matrix[i][j] > 0 && !peace[i][j]) {
					matrix[i][j]++;
				}
			}
		}
	}

}
