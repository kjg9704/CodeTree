import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

	static class Point{
		int x;
		int y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	
	static int N;
	static int[][] matrix;
	static int[][] adj_matrix;
	static int[] color_count;
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		N = Integer.parseInt(br.readLine());
		matrix = new int[N][N];
		adj_matrix = new int[11][11];
		
		 color_count = new int[11];
		
		for(int i = 0; i < N; i++) {
			String[] input = br.readLine().split(" ");
			for(int j = 0; j < N; j++) {
				matrix[i][j] = Integer.parseInt(input[j]);
			}
		}
		int result = calc();
		for(int i = 0; i < 3; i++) {
			rotate();
			result += calc();
		}
		
		System.out.println(result);
		

	}
	
	static int calc() {
		Queue<Point> que = new LinkedList<>();
		
		boolean[][] visited = new boolean[N][N];
		int[][] group = new int[N][N];
		int group_num = 1;
		HashMap<Integer, Integer> map = new HashMap<>();
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(visited[i][j]) {
					continue;
				}
				Point start = new Point(i, j);
				que.add(start);
				visited[i][j] = true;
				int now_num = matrix[i][j];
				map.put(group_num, now_num);
				while(!que.isEmpty()) {
					Point now = que.poll();
					group[now.x][now.y] = group_num;
					for(int z = 0; z < 4; z++) {
						int nextX = now.x + dx[z];
						int nextY = now.y + dy[z];
						
						if(nextX >= N || nextX < 0 || nextY >= N || nextY < 0) {
							continue;
						}
						
						if(matrix[nextX][nextY] == now_num && !visited[nextX][nextY]) {
							que.add(new Point(nextX, nextY));
							visited[nextX][nextY] = true;
						}
					}
				}
				
				group_num++;
			}
		}
		
		int[][] adj = new int[group_num][group_num];
		visited = new boolean[N][N];
		int[] group_cnt = new int[group_num];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(visited[i][j]) {
					continue;
				}
				Point start = new Point(i, j);
				que.add(start);
				visited[i][j] = true;
				int now_num = group[i][j];
				
				while(!que.isEmpty()) {
					Point now = que.poll();
					group_cnt[now_num]++;
					for(int z = 0; z < 4; z++) {
						int nextX = now.x + dx[z];
						int nextY = now.y + dy[z];
						
						if(nextX >= N || nextX < 0 || nextY >= N || nextY < 0) {
							continue;
						}
						
						if(group[nextX][nextY] == now_num && !visited[nextX][nextY]) {
							que.add(new Point(nextX, nextY));
							visited[nextX][nextY] = true;
						}else if(group[nextX][nextY] != now_num) {
							int next_num = group[nextX][nextY];
							adj[now_num][next_num]++;
						}
					}
				}
				
			}
		}
		int sum = 0;
		if(group_num > 2) {
			for(int i = 1; i < group_num; i++) {
				for(int j = i + 1; j < group_num; j++) {
					if(adj[i][j] > 0) {
						int add = (group_cnt[i] + group_cnt[j]) * map.get(i) * map.get(j) * adj[i][j];
						sum += add;
					}
				}
			}
		}
		


		
		return sum;
	}
	
	static void rotate_counter() {
		int[][] temp = new int[N][N];
		int mid = N / 2;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				temp[N - j - 1][i] = matrix[i][j];
			}
		}
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(i == mid || j == mid) {
					matrix[i][j] = temp[i][j];
				}
			}
		}
		

		
	}
	
	static void rotate() {
		rotate_counter();
		int[][] temp = new int[N][N];
		int mid = N / 2;
		
		
		rotate_clock(0, 0, mid);
		rotate_clock(0, mid + 1, mid);
		rotate_clock(mid + 1, 0, mid);
		rotate_clock(mid + 1, mid + 1, mid);
		for(int i = 0; i < mid; i++) {
			for(int j = 0; j < mid; j++) {
				temp[j][mid - i - 1] = matrix[i][j];
			}
		}
		
		for(int i = mid + 1; i < N;  i++) {
			for(int j = 0; j < mid; j++) {
				temp[j][N - i - 1] = matrix[i][j];
			}
		}
		
		for(int i = 0; i < mid; i++) {
			for(int j = mid + 1; j < N; j++) {
				temp[j][mid - i - 1] = matrix[i][j];
			}
		}
		
		for(int i = mid + 1; i < N; i++) {
			for(int j = mid + 1; j < N; j++) {
				temp[j][N - i - 1] = matrix[i][j];
			}
		}
		
	}
	
	static void rotate_clock(int x, int y, int n) {
		int[][] temp = new int[n][n];
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				temp[j][n - i - 1] = matrix[i + x][j + y];
			}
		}
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				 matrix[i + x][j + y] = temp[i][j];
			}
		}
	}

}
