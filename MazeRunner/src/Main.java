import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	static class Point{
		int x;
		int y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static int N, M, K;
	
	static int[][] matrix;
	static int[][] player;
	
	static int move_cnt;
	static int exit_x;
	static int exit_y;
	
	static int minX, minY, minW;
	static int escape = 0;
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String[] input = br.readLine().split(" ");
		
		N = Integer.parseInt(input[0]);
		M = Integer.parseInt(input[1]);
		K = Integer.parseInt(input[2]);
		matrix = new int[N + 1][N + 1];
		player = new int[N + 1][N + 1];
		
		for(int i = 1; i <= N; i++) {
			input = br.readLine().split(" ");
			for(int j = 1; j <= N; j++) {
				int num = Integer.parseInt(input[j - 1]);
				matrix[i][j] = num;
			}
		}
		
		for(int i = 1; i <= M; i++) {
			input = br.readLine().split(" ");
			int x = Integer.parseInt(input[0]);
			int y = Integer.parseInt(input[1]);
			player[x][y]++;
			
		}
		input = br.readLine().split(" ");
		exit_x = Integer.parseInt(input[0]);
		exit_y = Integer.parseInt(input[1]);
		matrix[exit_x][exit_y] = -1;
		for(int i = 0; i < K; i++) {
			player_move();
			
			if(escape == M) {
				break;
			}
			get_square();
			
			rotate();
			
		}
		
		System.out.println(move_cnt);
		System.out.println(exit_x + " " + exit_y);

	}
	
	static void player_move() {
		int[][] temp = new int[N + 1][N + 1];
		
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= N; j++) {
				if(player[i][j] > 0) {
					int nowX = i;
					int nowY = j;
					
					boolean change = false;
					for(int z = 0; z < 4; z++) {
						int nextX = nowX + dx[z];
						int nextY = nowY + dy[z];
						
						if(nextX > N || nextX < 1 || nextY > N || nextY < 1 || matrix[nextX][nextY] > 0) {
							continue;
						}
						
						int now_dist = get_dist(nowX, nowY, exit_x, exit_y);
						int next_dist = get_dist(nextX, nextY, exit_x, exit_y);
						if(next_dist < now_dist) {
							change = true;
							move_cnt += player[nowX][nowY];
							
							if(nextX == exit_x && nextY == exit_y) {
								escape += player[nowX][nowY];
							}else {
								temp[nextX][nextY] += player[nowX][nowY];
							}
							
							break;
						}
						
					}
					if(!change) {
						temp[nowX][nowY] += player[nowX][nowY];
					}
				}
			}
		}
		
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= N; j++) {
				player[i][j] = temp[i][j];
			}
		}

		
	}
	
	static void get_square() {
		minX = Integer.MAX_VALUE;
		minY = Integer.MAX_VALUE;
		minW = Integer.MAX_VALUE;
		
		for(int w = 1; w < N; w++) {
			for(int i = 1; i <= N - w; i++) {
				for(int j = 1; j <= N - w; j++) {
					if(check(i, j, w)) {
						minX = i;
						minY = j;
						minW = w;
						return;
					}
				}
			}
		}
		
	}
	
	static boolean check(int x, int y, int width) {
		boolean person = false;
		boolean exit = false;
		for(int i = x; i <= x + width; i++) {
			for(int j = y; j <= y + width; j++) {
				if(player[i][j] > 0) {
					person = true;
				}
				if(i == exit_x && j == exit_y) {
					exit = true;
				}
				
				if(person && exit) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	static int get_dist(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}
	
	static void rotate() {
		int limit = minW + 1;
		int[][] temp = new int[limit][limit];
		
		for(int i = 0; i < limit; i++) {
			for(int j = 0; j < limit ;j++) {
				temp[j][limit - i - 1] = matrix[minX + i][minY + j];
			}
		}
		
		for(int i = 0; i < limit; i++) {
			for(int j = 0; j < limit; j++) {
				matrix[minX + i][minY + j] = temp[i][j];
			}
		}
		
		for(int i = minX; i <= minX + minW; i++) {
			for(int j = minY; j <= minY + minW; j++) {
				if(matrix[i][j] == -1) {
					exit_x = i;
					exit_y = j;
				}else if(matrix[i][j] > 0) {
					matrix[i][j]--;
				}
			}
		}
		
		temp = new int[limit][limit];
		
		for(int i = 0; i < limit; i++) {
			for(int j = 0; j < limit ;j++) {
				temp[j][limit - i - 1] = player[minX + i][minY + j];
			}
		}
		
		for(int i = 0; i < limit; i++) {
			for(int j = 0; j < limit; j++) {
				player[minX + i][minY + j] = temp[i][j];
			}
		}
		
	}

}
