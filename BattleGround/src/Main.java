import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

	static class Player{
		int num;
		int x;
		int y;
		int stat;
		int dir;
		int gun;
		public Player(int num, int x, int y, int dir, int stat) {
			this.num = num;
			this.x = x;
			this.y = y;
			this.dir = dir;
			this.stat = stat;
		}
		
		public int get_stat() {
			return stat + gun;
		}
	}
	
	static int N, M, K;
	static int[][] matrix;
	static int[][] player_board;
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	static PriorityQueue<Integer>[][] guns;
	static Player[] player_arr;
	static int[] result;
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String[] input = br.readLine().split(" ");
		
		N = Integer.parseInt(input[0]);
		M = Integer.parseInt(input[1]);
		K = Integer.parseInt(input[2]);
		
//		matrix = new int[N + 1][N + 1];
		guns = new PriorityQueue[N + 1][N + 1];
		player_board = new int[N + 1][N + 1];
		player_arr = new Player[M + 1];
		result = new int[M + 1];
		
		for(int i = 1; i <= N; i++) {
			input = br.readLine().split(" ");
			for(int j = 1; j <= N; j++) {
				guns[i][j] = new PriorityQueue<>(Collections.reverseOrder());
				int num = Integer.parseInt(input[j - 1]);
				if(num > 0) {
					guns[i][j].add(num);
				}
			//	matrix[i][j] = num;
			}
		}
		
		for(int i = 1; i <= M; i++) {
			input = br.readLine().split(" ");
			int x = Integer.parseInt(input[0]);
			int y = Integer.parseInt(input[1]);
			int d = Integer.parseInt(input[2]);
			int s = Integer.parseInt(input[3]);
			Player player = new Player(i, x, y, d, s);
			player_arr[i] = player;
			player_board[x][y] = i;
		}
		
		for(int i = 0; i < K; i++) {
			player_move();
		}
		
		for(int i = 1; i <= M; i++) {
			System.out.print(result[i] + " ");
		}
		
	}
	
	static void player_move() {
		for(int i = 1; i <= M; i++) {
			Player now = player_arr[i];
			
			int nextX = now.x + dx[now.dir];
			int nextY = now.y + dy[now.dir];
			
			if(nextX > N || nextX < 1 || nextY > N || nextY < 1) {
				int next_dir = get_back_dir(now.dir);
				now.dir = next_dir;
				nextX = now.x + dx[now.dir];
				nextY = now.y + dy[now.dir];
			}

			player_board[now.x][now.y] = 0;
			now.x = nextX;
			now.y = nextY;
			
			if(player_board[now.x][now.y] == 0) {
				if(guns[now.x][now.y].size() > 0) {
					if(now.gun == 0) {
						int new_gun = guns[now.x][now.y].poll();
						now.gun = new_gun;
					}else {
						if(now.gun < guns[now.x][now.y].peek()) {
							int new_gun = guns[now.x][now.y].poll();
							guns[now.x][now.y].add(now.gun);
							now.gun = new_gun;
						}
					}
				}
				player_board[now.x][now.y] = now.num;
			}else {
				Player enemy = player_arr[player_board[now.x][now.y]];
				int now_stat = now.get_stat();
				int enemy_stat = enemy.get_stat();
				Player winner = null;
				Player loser = null;
				int get_point = Math.abs(now_stat - enemy_stat);
				
				if(now_stat < enemy_stat) {
					winner = enemy;
					loser = now;
				}else if(now_stat > enemy_stat) {
					winner = now;
					loser = enemy;
				}else {
					if(now.stat < enemy.stat) {
						winner = enemy;
						loser = now;
					}else if(now.stat > enemy.stat) {
						winner = now;
						loser = enemy;
					}
				}
				
				result[winner.num] += get_point;
				player_board[winner.x][winner.y] = winner.num;
				
				if(loser.gun > 0) {
					guns[loser.x][loser.y].add(loser.gun);
				}
				
				loser.gun = 0;
				
				
				for(int z = 0; z < 4; z++) {
					nextX = loser.x + dx[loser.dir];
					nextY = loser.y + dy[loser.dir];
					if(nextX > N || nextX < 1 || nextY > N || nextY < 1 || player_board[nextX][nextY] != 0) {
						int next_dir = get_next_dir(loser.dir);
						loser.dir = next_dir;
					}else {
						loser.x = nextX;
						loser.y = nextY;
						if(guns[loser.x][loser.y].size() > 0) {
							loser.gun = guns[loser.x][loser.y].poll();
						}
						player_board[loser.x][loser.y] = loser.num;
						break;
					}
				}
				
				if(guns[winner.x][winner.y].size() > 0) {
					if(winner.gun == 0) {
						int new_gun = guns[winner.x][winner.y].poll();
						winner.gun = new_gun;
					}else {
						if(winner.gun < guns[winner.x][winner.y].peek()) {
							int new_gun = guns[winner.x][winner.y].poll();
							guns[winner.x][winner.y].add(winner.gun);
							winner.gun = new_gun;
						}
					}
				}

				
			}
			
		}
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
	
	
	static int get_next_dir(int dir) {
		int next = dir + 1;
		if(next == 4) {
			next = 0;
		}
		return next;
	}

}
