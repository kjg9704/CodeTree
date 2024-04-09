import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	static class Knight{
		int x, y, h, w, k, damage;
		boolean live;
		
		public Knight(int x, int y, int h, int w, int k) {
			this.x = x;
			this.y = y;
			this.h = h;
			this.w = w;
			this.k = k;
			this.live = true;
			this.damage = 0;
		}
	}
	static int[][] matrix;
	static boolean[][] trap;
	static Knight[] knight_arr;
	static int L, N, Q;
	
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String[] input = br.readLine().split(" ");
		
		L = Integer.parseInt(input[0]);
		N = Integer.parseInt(input[1]);
		Q = Integer.parseInt(input[2]);
		
		matrix = new int[L +  1][L + 1];
		trap = new boolean[L + 1][L + 1];
		knight_arr = new Knight[N + 1];
		
		for(int i = 1; i <= L; i++) {
			input = br.readLine().split(" ");
			for(int j = 1; j <= L; j++) {
				int tmp = Integer.parseInt(input[j - 1]);
				if(tmp == 1) {
					trap[i][j] = true;
				}else if(tmp == 2){
					matrix[i][j] = -1; //벽
				}else {
					matrix[i][j] = tmp;
				}
			}
		}
		
		for(int i = 1; i <= N; i++) {
			input = br.readLine().split(" ");
			
			int r = Integer.parseInt(input[0]);
			int c = Integer.parseInt(input[1]);
			int h = Integer.parseInt(input[2]);
			int w = Integer.parseInt(input[3]);
			int k = Integer.parseInt(input[4]);
			knight_arr[i] = new Knight(r, c, h, w, k);
			for(int height = 0; height < h; height++) {
				for(int width = 0; width < w; width++) {
					matrix[r + height][c + width] = i;
				}
			}
		}
		print();
		for(int i = 0; i < Q; i++) {
			
			input = br.readLine().split(" ");
			
			int idx = Integer.parseInt(input[0]);
			int dir = Integer.parseInt(input[1]);
			
			knight_move(idx, dir);
			print();
		}
		
		int result = getResult();
		System.out.println(result);
		
	}
	
	static void knight_move(int idx, int dir) {
		if(!knight_arr[idx].live) return;
		
		
		if(move_check(idx, dir)) {
			move(idx, dir, true);
		}
		
	}
	
	static boolean move_check(int idx, int dir) {
		
		boolean flag = true;
		Knight now = knight_arr[idx];
		
		int nextX = now.x + dx[dir];
		int nextY = now.y + dy[dir];
		for(int i = 0; i < now.h; i++) {
			for(int j = 0; j < now.w; j++) {
				int xx = nextX + i;
				int yy = nextY + j;
				if(xx < 1 || xx > L || yy < 1 || yy > L) return false;
				if(matrix[xx][yy] == -1) return false;
				if(matrix[xx][yy] > 0 && matrix[xx][yy] != idx) {
					if(!move_check(matrix[xx][yy], dir)) {
						flag = false;
					}
				}
			}
		}
		
		return flag;
	}
	
	static void move(int idx, int dir, boolean ordered) {

		Knight now = knight_arr[idx];
		int nextX = now.x + dx[dir];
		int nextY = now.y + dy[dir];
		int trap_cnt = 0;
		
		for(int i = 0; i < now.h; i++) {
			for(int j = 0; j < now.w; j++) {
				int xx = now.x + i;
				int yy = now.y + j;
				matrix[xx][yy] = 0;
			}
		}
		
		for(int i = 0; i < now.h; i++) {
			for(int j = 0; j < now.w; j++) {
				int xx = nextX + i;
				int yy = nextY + j;
				
				if(matrix[xx][yy] > 0 && matrix[xx][yy] != idx) {
					move(matrix[xx][yy], dir, false);
				}
				if(trap[xx][yy]) {
					trap_cnt++;
				}
				matrix[xx][yy] = idx;
			}

		}
		
		now.x = nextX;
		now.y = nextY;
		if(!ordered) {
			if(now.damage + trap_cnt >= now.k) {
				now.live = false;
				for(int i = 0; i < now.h; i++) {
					for(int j = 0; j < now.w; j++) {
						int xx = now.x + i;
						int yy = now.y + j;
						matrix[xx][yy] = 0;
					}
				}
			}else {
				now.damage += trap_cnt;
				
			}
		}
	
	}
	
	static void print() {
		
		System.out.println("------------------------");
		for(int i = 1; i <= L; i++) {
			for(int j = 1; j <= L; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	static int getResult() {
		int res = 0;
		for(int i = 1; i <= N; i++) {
			if(knight_arr[i].live) {
				res += knight_arr[i].damage;
			}
		}
		
		return res;
	}

}
