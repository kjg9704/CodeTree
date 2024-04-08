import java.io.*;
import java.util.*;

public class Main {

	static class Santa{
		int num, x, y;
		
		public Santa(int num, int x, int y) {
			this.num = num;
			this.x = x;
			this.y = y;
		}
	}
	
	static int N, M, P, C, D, rudolf_x, rudolf_y;
	
	static Santa[] santa_arr;
	static int[] santa_score;
	static boolean[] exited;
	static int[][] matrix;
	static int[] stun;
	static int turn = 0;
	static int out = 0;
	
	static int[] dx = {-1, -1, -1, 0, 1, 1, 1, 0};
	static int[] dy = {-1, 0, 1, 1, 1, 0, -1, -1};
	
	static int[] santa_dx = {-1, 0, 1, 0};
	static int[] santa_dy = {0, 1, 0, -1};
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String[] input = br.readLine().split(" ");
		
		N = Integer.parseInt(input[0]);
		M = Integer.parseInt(input[1]);
		P = Integer.parseInt(input[2]);
		C = Integer.parseInt(input[3]);
		D = Integer.parseInt(input[4]);
		
		matrix = new int[N + 1][N + 1];
		santa_arr = new Santa[P + 1];
		santa_score = new int[P + 1];
		exited = new boolean[P + 1];
		stun = new int[P + 1];
		
		input = br.readLine().split(" ");
		rudolf_x = Integer.parseInt(input[0]);
		rudolf_y = Integer.parseInt(input[1]);
		matrix[rudolf_x][rudolf_y] = -1;
		for(int i = 1; i <= P; i++) {
			input = br.readLine().split(" ");
			int num = Integer.parseInt(input[0]);
			int x = Integer.parseInt(input[1]);
			int y = Integer.parseInt(input[2]);
			santa_arr[num] = new Santa(num, x, y);
			matrix[x][y] = num;
		}
		
		for(int i = 0; i < M; i++) {
			turn++;
			rudolf_move();
			
			if(out == P) break;
			
			santa_move();
			
			if(out == P) break;
			
			addScore();
		}
		
		for(int i = 1; i <= P; i++) {
			System.out.print(santa_score[i] + " ");
		}
		
	}
	
	static int getDist(int x1, int y1, int x2, int y2) {
		return (int) (Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	static void rudolf_move() {
		
		int target_num = 0;
		int targetX = 0;
		int targetY = 0;
		int min_dist = 20000;
		for(int i = 1; i <= P; i++) {
			if(exited[i]) continue;
			Santa now = santa_arr[i];
			int dist = getDist(rudolf_x, rudolf_y, now.x, now.y);
			
			if(min_dist > dist) {
				min_dist = dist;
				targetX = now.x;
				targetY = now.y;
				target_num = i;
			}else if(min_dist == dist) {
				if(targetX < now.x) {
					min_dist = dist;
					targetX = now.x;
					targetY = now.y;
					target_num = i;
				}else if(targetX == now.x) {
					if(targetY < now.y) {
						min_dist = dist;
						targetX = now.x;
						targetY = now.y;
						target_num = i;
					}
				}
			}
		}
		
		int dir = 0;
		min_dist = 100000;
		int res_x = 0;
		int res_y = 0;
		Santa target = santa_arr[target_num];
		for(int i = 0; i < 8; i++) {
			int nextX = rudolf_x + dx[i];
			int nextY = rudolf_y + dy[i];
			if(nextX < 1 || nextX > N || nextY < 1 || nextY > N) continue;
			int next_dist = getDist(nextX, nextY, target.x, target.y);
			
			if(min_dist > next_dist) {
				res_x = nextX;
				res_y = nextY;
				dir = i;
				min_dist = next_dist;
			}
		}
		
		if(matrix[res_x][res_y] > 0) {
			int santa_num = matrix[res_x][res_y];
			santa_score[santa_num] += C;
			stun[santa_num] = turn + 1;
			collision(santa_num, res_x, res_y, dir, C, true);
		}
		matrix[rudolf_x][rudolf_y] = 0;
		rudolf_x = res_x;
		rudolf_y = res_y;
		matrix[rudolf_x][rudolf_y] = -1;
	}
	
	static void santa_move() {
		for(int i = 1; i <= P; i++) {
			if(exited[i]) continue;
			if(stun[i] >= turn) continue;
			
			Santa now = santa_arr[i];
			int dir = 0;
			int prev_dist = getDist(rudolf_x, rudolf_y, now.x, now.y);
			int dist = prev_dist;
			
			for(int z = 0; z < 4; z++) {
				int nextX = now.x + santa_dx[z];
				int nextY = now.y + santa_dy[z];
				
				if(nextX < 1 || nextX > N || nextY < 1 || nextY > N) continue;
				if(matrix[nextX][nextY] > 0) continue;
				
				int next_dist = getDist(rudolf_x, rudolf_y, nextX, nextY);
				
				if(dist > next_dist) {
					dir = z;
					dist = next_dist;
				}
				
			}
			
			if(dist == prev_dist) continue;
			
			int nextX = now.x + santa_dx[dir];
			int nextY = now.y + santa_dy[dir];
			matrix[now.x][now.y] = 0;
			if(nextX == rudolf_x && nextY == rudolf_y) {
				santa_score[i] += D;
				stun[i] = turn + 1;
				collision(i, nextX, nextY, getBackDir(dir), D ,false);
			}else {
				matrix[nextX][nextY] = i;
				now.x = nextX;
				now.y = nextY;
			}
			
			
		}
	}
	
	static void collision(int santa_num, int startX, int startY, int dir, int dist, boolean isRudolf) {
		Santa now = santa_arr[santa_num];
		int nextX = 0;
		int nextY = 0;
		if(isRudolf) {
			nextX = startX + (dx[dir] * dist);
			nextY = startY + (dy[dir] * dist);
		}else {
			nextX = startX + (santa_dx[dir] * dist);
			nextY = startY + (santa_dy[dir] * dist);
		}
		
		
		if(nextX < 1 || nextX > N || nextY < 1 || nextY > N) {
			out++;
			exited[santa_num] = true;
			return;
		}
		
		if(matrix[nextX][nextY] > 0) {
			collision(matrix[nextX][nextY], nextX, nextY, dir, 1, isRudolf);
		}
		
		matrix[nextX][nextY] = santa_num;
		now.x = nextX;
		now.y = nextY;
	}
	
	static void addScore() {
		for(int i = 1; i <= P; i++) {
			if(!exited[i]) {
				santa_score[i]++;
			}
		}
	}
	
	static int getBackDir(int dir) {
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
	
	static void print() {
		System.out.println("-----------------------------");
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= N; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

}
