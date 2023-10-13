import java.io.*;

public class Main {

	//격자크기, 박멸년수(stage), 제초제 확산범위, 제초제 지속년수
	static int N, M, K, C;
	static int[][] matrix;
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int[] kill_dx = {-1, -1, 1, 1};
	static int[] kill_dy = {-1, 1, 1, -1};
	static int[][] kill_time;
	static int result = 0;
	static int stage;
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String[] input = br.readLine().split(" ");
		N = Integer.parseInt(input[0]);
		M = Integer.parseInt(input[1]);
		K = Integer.parseInt(input[2]);
		C = Integer.parseInt(input[3]);
		
		matrix = new int[N][N];
		kill_time = new int[N][N];
		for(int i = 0; i < N; i++) {
			input = br.readLine().split(" ");
			for(int j = 0; j < N; j++) {
				matrix[i][j] = Integer.parseInt(input[j]);
			}
		}
		
		stage = 1;
		for(int i = 0; i < M; i++) {
			
			time_decrease();

			grow();

			spread();
			

			kill();
			
			
			stage++;
		}
		
		System.out.println(result);
		
	}
	
	static void grow() {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(matrix[i][j] > 0) {
					int cnt = 0;
					for(int z = 0; z < 4; z++) {
						int nextX = i + dx[z];
						int nextY = j + dy[z];
						if(nextX >= N || nextX < 0 || nextY >= N || nextY < 0) {
							continue;
						}
						if(matrix[nextX][nextY] > 0) {
							cnt++;
						}
					}
					
					matrix[i][j] += cnt;
				}
			}
		}
	}
	
	static void spread() {
		int[][] temp = new int[N][N];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(matrix[i][j] > 0) {
					int cnt = 0;
					
					for(int z = 0; z < 4; z++) {
						int nextX = i + dx[z];
						int nextY = j + dy[z];
						if(nextX >= N || nextX < 0 || nextY >= N || nextY < 0) {
							continue;
						}
						
						if(matrix[nextX][nextY] == 0 && kill_time[nextX][nextY] == 0) {
							cnt++;
						}
					}
					
					if(cnt > 0) {
						int spread_num = matrix[i][j] / cnt;
						for(int z = 0; z < 4; z++) {
							int nextX = i + dx[z];
							int nextY = j + dy[z];
							if(nextX >= N || nextX < 0 || nextY >= N || nextY < 0) {
								continue;
							}
							
							if(matrix[nextX][nextY] == 0 && kill_time[nextX][nextY] == 0) {
								temp[nextX][nextY] += spread_num;
							}
						}
					}
				}
			}
		}
		
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				matrix[i][j] += temp[i][j];
			}
		}
		
	}
	
	static void kill() {
		int max = 0;
		int maxX = 0;
		int maxY = 0;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(matrix[i][j] > 0) {
					int kill_cnt = matrix[i][j];
					for(int z = 0; z < 4; z++) {
						int nowX = i;
						int nowY = j;
						
						for(int k = 0; k < K; k++) {
							nowX = nowX + kill_dx[z];
							nowY = nowY + kill_dy[z];
							if(nowX >= N || nowX < 0 || nowY >= N || nowY < 0) {
								break;
							}
							
							if(matrix[nowX][nowY] < 1) {
								break;
							}
							if(matrix[nowX][nowY] > 0) {
								kill_cnt += matrix[nowX][nowY];
							}
						}
						
					}
					
					if(kill_cnt > max) {
						max = kill_cnt;
						maxX = i;
						maxY = j;
					}
					
				}
			}
		}
		if(max == 0) {
			return;
		}

		result += max;
		matrix[maxX][maxY] = 0;
		kill_time[maxX][maxY] = stage;
		
		for(int i = 0; i < 4; i++) {
			int nowX = maxX;
			int nowY = maxY;
			
			for(int k = 0; k < K; k++) {
				nowX = nowX + kill_dx[i];
				nowY = nowY + kill_dy[i];
				if(nowX >= N || nowX < 0 || nowY >= N || nowY < 0) {
					break;
				}
				
				if(matrix[nowX][nowY] < 1) {
					kill_time[nowX][nowY] = stage;
					break;
				}
				if(matrix[nowX][nowY] > 0) {

					matrix[nowX][nowY] = 0;
					kill_time[nowX][nowY] = stage;
				}
			}
			
		}

	}
	
	static void time_decrease() {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(kill_time[i][j] > 0) {
					if(kill_time[i][j] + C + 1 == stage) {
						kill_time[i][j] = 0;
					}
				}
			}
		}
	}
	
	static void print() {
		System.out.println("------");
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

}
