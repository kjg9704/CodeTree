import java.io.*;
import java.util.*;

public class Main2 {

	static class Hider{
		int x, y, dir;
		boolean dead;
		
		public Hider(int x, int y, int dir) {
			this.x = x;
			this.y = y;
			this.dir = dir;
			this.dead = false;
		}
		
		public void setBack() {
			if(this.dir == 0) {
				this.dir = 2;
			}else if(this.dir == 1) {
				this.dir = 3;
			}else if(this.dir == 2) {
				this.dir = 0;
			}else {
				this.dir = 1;
			}
		}
	}
	static int N, M, H, K;
	
	static boolean[][] tree;
	static ArrayList<Integer>[][] matrix;
	static Hider[] hider_arr;
	static boolean forward = true;
	static int turn, score;
	static int[][] forward_dir;
	static int[][] backward_dir;
	static int seekerX;
	static int seekerY;
	
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String[] input = br.readLine().split(" ");
		
		N = Integer.parseInt(input[0]);
		M = Integer.parseInt(input[1]);
		H = Integer.parseInt(input[2]);
		K = Integer.parseInt(input[3]);
		
		matrix = new ArrayList[N + 1][N + 1];
		tree = new boolean[N + 1][N + 1];
		hider_arr = new Hider[M];
		forward_dir = new int[N + 1][N + 1];
		backward_dir = new int[N + 1][N + 1];
		
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= N; j++) {
				matrix[i][j] = new ArrayList<>();
			}
		}
		for(int i = 0; i < M; i++) {
			input = br.readLine().split(" ");
			int x = Integer.parseInt(input[0]);
			int y = Integer.parseInt(input[1]);
			int dir = Integer.parseInt(input[2]);
			hider_arr[i] = new Hider(x, y, dir);
			matrix[x][y].add(i);
		}
		
		for(int i = 0; i < H; i++) {
			input = br.readLine().split(" ");
			int x = Integer.parseInt(input[0]);
			int y = Integer.parseInt(input[1]);
			tree[x][y] = true;
		}
		
		init();
		
		for(int i = 0; i < K; i++) {
			turn++;
			hider_move();
			seeker_move();
		}
		
		System.out.println(score);
	}
	
	static void init() {
		seekerX = N / 2 + 1;
		seekerY = N / 2 + 1;
		int moveNum = 1;
		int dir = 0;
		
		int nextX = seekerX;
		int nextY = seekerY;
		
		while(nextX > 1 || nextY > 1) {
			
			for(int i = 0; i < moveNum; i++) {
				forward_dir[nextX][nextY] = dir;
				
				nextX = nextX + dx[dir];
				nextY = nextY + dy[dir];
				if(dir == 0) {
					backward_dir[nextX][nextY] = 2;
				}else if(dir == 1) {
					backward_dir[nextX][nextY] = 3;
				}else if(dir == 2) {
					backward_dir[nextX][nextY] = 0;
				}else {
					backward_dir[nextX][nextY] = 1;
				}
				
				if(nextX == 1 && nextY == 1) {
					break;
				}
			}
			
			dir = (dir + 1) % 4;
			if(dir == 2 || dir == 0) {
				moveNum++;
			}
		}
		
	}
	
	static void hider_move() {
		for(int i = 0; i < M; i++) {
			Hider now = hider_arr[i];
			if(now.dead) continue;
			if(getDist(now.x, now.y) <= 3) {
				int nextX = now.x + dx[now.dir];
				int nextY = now.y + dy[now.dir];
				
				if(nextX < 1 || nextX > N || nextY < 1 || nextY > N) {
					now.setBack();
					nextX = now.x + dx[now.dir];
					nextY = now.y + dy[now.dir];	
				}
				if(nextX == seekerX && nextY == seekerY) continue;
				matrix[now.x][now.y].remove(new Integer(i));
				matrix[nextX][nextY].add(i);
				now.x = nextX;
				now.y = nextY;
			}
		}
	}
	
	static void seeker_move() {
		int dir = 0;
		if(forward) {
			dir = forward_dir[seekerX][seekerY];
		}else {
			dir = backward_dir[seekerX][seekerY];
		}
		
		int nextX = seekerX + dx[dir];
		int nextY = seekerY + dy[dir];
		seekerX = nextX;
		seekerY = nextY;
		if(seekerX == 1 && seekerY == 1 && forward) {
			forward = false;
		}else if(seekerX == N / 2 + 1 && seekerY == N / 2 + 1 && !forward) {
			forward = true;
		}
		
		if(forward) {
			dir = forward_dir[seekerX][seekerY];
		}else {
			dir = backward_dir[seekerX][seekerY];
		}
		
		for(int i = 0; i < 3; i++) {
			nextX = seekerX + (dx[dir] * i);
			nextY = seekerY + (dy[dir] * i);
			
			if(nextX < 1 || nextX > N || nextY < 1 || nextY > N) break;
			if(matrix[nextX][nextY].size() > 0 && !tree[nextX][nextY]) {
				score += turn * matrix[nextX][nextY].size();
				for(int num : matrix[nextX][nextY]) {
					hider_arr[num].dead = true;
				}
				matrix[nextX][nextY].clear();
			}
		}
	}
	
	static int getDist(int x, int y) {
		return Math.abs(x - seekerX) + Math.abs(y - seekerY);
	}

}
