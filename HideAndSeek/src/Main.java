import java.io.*;
import java.util.*;

public class Main {

	static class Board{
		int x;
		int y;
		boolean turn_point;
		int next_dir;
		int next_dir_counter;
		
		public Board(int x, int y, int next_dir, int next_dir_counter) {
			this.x = x;
			this.y = y;
			this.next_dir = next_dir;
			this.next_dir_counter = next_dir_counter;
			this.turn_point = true;
		}
		
		public Board(int x, int y) {
			this.x = x;
			this.y = y;
			this.turn_point = false;
		}
	}
	
	static class Hider{
		int x;
		int y;
		int dir;
		public Hider(int x, int y, int dir) {
			this.x = x;
			this.y = y;
			this.dir = dir;
		}
	}
	
	static int N, M, H, K;
	
	static int[][][] matrix;
	static int[][][] temp_matrix;
	static int[][] hider_num;
	
	static boolean[][] is_tree;
	
	static int[] dx = {0, -1, 0, 1, 0};
	static int[] dy = {0, 0, 1, 0, -1};
	
	static int SeekerX;
	static int SeekerY;
	static int Seeker_dir;
	static boolean Seeker_forward = true;
	static int Seeker_next;
	
	static Board[] board_arr;
	static int result = 0;
	static int turn;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String[] input = br.readLine().split(" ");
		N = Integer.parseInt(input[0]);
		M = Integer.parseInt(input[1]);
		H = Integer.parseInt(input[2]);
		K = Integer.parseInt(input[3]);
		
		matrix = new int[N + 1][N + 1][5];
		
		is_tree = new boolean[N + 1][N + 1];
		SeekerX = (N + 1) / 2;
		SeekerY = (N + 1) / 2;
		Seeker_dir = 1;
		board_arr = new Board[N * N];

		
		for(int i = 1; i <= M; i++) {
			input = br.readLine().split(" ");
			int x = Integer.parseInt(input[0]);
			int y = Integer.parseInt(input[1]);
			int d = Integer.parseInt(input[2]);
			if(d == 1) {
				matrix[x][y][2]++;
			}else {
				matrix[x][y][3]++;
			}
		}
		
		for(int i = 0; i < H; i++) {
			input = br.readLine().split(" ");
			int x = Integer.parseInt(input[0]);
			int y = Integer.parseInt(input[1]);
			is_tree[x][y] = true;
		}
		init();
		Seeker_next = 1;
		turn = 1;
		for(int i = 0; i < K; i++) {
			hider_move();
			seeker_move();
			turn++;
		}
		
		System.out.println(result);
	}
	
	static void init() {
		int move = 1;
		int nowX = SeekerX;
		int nowY = SeekerY;
		int dir = Seeker_dir;
		int index = 0;

		board_arr[index++] = new Board(SeekerX, SeekerY, 0, 1);
		
		while(true) {
			for(int i = 0; i < 2; i++) {
				for(int j = 0; j < move; j++) {
					nowX = nowX + dx[dir];
					nowY = nowY + dy[dir];
					
					if(nowX == 1 && nowY == 1) {
						board_arr[index++] = new Board(1, 1, 0, 3);
						return;
					}
					if(j == move - 1) {
						int next_dir = get_seeker_dir(dir);
						Board now = new Board(nowX, nowY, next_dir, get_counter_dir(next_dir));
						dir = next_dir;
						board_arr[index++] = now;
					}else {
						Board now = new Board(nowX, nowY);
						board_arr[index++] = now;
					}	
		
				}
				
			}
			move++;
		}
		
	}
	
	static void seeker_move() {
		Board next = board_arr[Seeker_next];
		SeekerX = next.x;
		SeekerY = next.y;
		if(next.turn_point) {
			if(Seeker_forward) {
				Seeker_dir = next.next_dir;
			}else {
				Seeker_dir = next.next_dir_counter;
			}
		}
		
		if(SeekerX == 1 && SeekerY == 1) {
			Seeker_forward = false;
			Seeker_dir = next.next_dir_counter;
		}
		
		if(SeekerX == (N + 1) / 2 && SeekerY == (N + 1) / 2) {
			Seeker_forward = true;
			Seeker_dir = next.next_dir_counter;
		}
		
		int cnt = 0;
		
		if(hider_num[SeekerX][SeekerY] > 0 && !is_tree[SeekerX][SeekerY]) {
			cnt += hider_num[SeekerX][SeekerY];
			hider_num[SeekerX][SeekerY] = 0;
			matrix[SeekerX][SeekerY] = new int[5];
		}
		
		int nowX = SeekerX;
		int nowY = SeekerY;
		
		for(int i = 0; i < 2; i++) {
			nowX = nowX + dx[Seeker_dir];
			nowY = nowY + dy[Seeker_dir];
			if(nowX > N || nowX < 1 || nowY > N || nowY < 1) {
				break;
			}
			
			if(!is_tree[nowX][nowY]) {
				cnt += hider_num[nowX][nowY];
				hider_num[nowX][nowY] = 0;
				matrix[nowX][nowY] = new int[5];
			}
		}
		
		result += turn * cnt;
		
		if(Seeker_forward) {
			Seeker_next++;
		}else {
			Seeker_next--;
		}
		
	}
	
	static void hider_move() {
		temp_matrix = new int[N + 1][N + 1][5];
		hider_num = new int[N + 1][N + 1];
		
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= N; j++) {
				
				for(int z = 1; z <= 4; z++) {
					if(matrix[i][j][z] > 0) {
						if(move_check(i, j)) {
							int dir = z;
							int nextX = i + dx[dir];
							int nextY = j + dy[dir];
							
							if(nextX > N || nextX < 1 || nextY > N || nextY < 1) {
								int next_dir = get_hider_dir(dir);
								dir = next_dir;
								nextX = i + dx[dir];
								nextY = j + dy[dir];
							}
							
							if(nextX == SeekerX && nextY == SeekerY) {
								temp_matrix[i][j][z] += matrix[i][j][z];
								hider_num[i][j] += matrix[i][j][z];
							}else {
								temp_matrix[nextX][nextY][dir] += matrix[i][j][z];
								hider_num[nextX][nextY] += matrix[i][j][z];
							}
						}else {
							temp_matrix[i][j][z] += matrix[i][j][z];
							hider_num[i][j] += matrix[i][j][z];
						}
					}
				}
			}
		}
		
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= N; j++) {
				for(int z = 1; z <= 4; z++) {
					matrix[i][j][z] = temp_matrix[i][j][z];
				}
			}
		}
		
	}

	
	static boolean move_check(int hiderX, int hiderY) {
		int dist = Math.abs(hiderX - SeekerX) + Math.abs(hiderY - SeekerY);
		if(dist <= 3) {
			return true;
		}else {
			return false;
		}
	}
	
	static int get_seeker_dir(int dir) {
		if(dir == 1) {
			return 2;
		}else if(dir == 2) {
			return 3;
		}else if(dir == 3) {
			return 4;
		}else {
			return 1;
		}
	}
	
	static int get_counter_dir(int dir) {
		if(dir == 2) {
			return 3;
		}else if(dir == 3) {
			return 3;
		}else if(dir == 4) {
			return 1;
		}else {
			return 2;
		}
	}
	static int get_hider_dir(int dir) {
		if(dir == 2) {
			return 4;
		}else if(dir == 4) {
			return 2;
		}else if(dir == 3) {
			return 1;
		}else {
			return 3;
		}
	}

}
