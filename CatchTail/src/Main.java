import java.io.*;
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
	
	static int[][] matrix;
	static int[][] team_num;
	static boolean[][] line;
	static boolean[] is_opposite;
	static Deque<Point>[] team_arr;
	static int N, M, K;
	static int[] dx = {0, -1, 0, 1};
	static int[] dy = {1, 0, -1, 0};
	static int score = 0;
	static int ball_dir = 0;
	static int ball_index = 0;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String[] input = br.readLine().split(" ");
		N = Integer.parseInt(input[0]);
		M = Integer.parseInt(input[1]);
		K = Integer.parseInt(input[2]);
		
		matrix = new int[N][N];
		line = new boolean[N][N];
		team_num = new int[N][N];
		team_arr = new Deque[M + 1];
		is_opposite = new boolean[M + 1];
		for(int i = 1; i <= M; i++) {
			team_arr[i] = new ArrayDeque<>();
		}
		for(int i = 0; i < N; i++) {
			input = br.readLine().split(" ");
			for(int j = 0; j < N; j++) {
				int num = Integer.parseInt(input[j]);
				
				if(num == 4) {
					line[i][j] = true;
				}else if(num > 0) {
					matrix[i][j] = num;
					line[i][j] = true;
				}
			}
		}
		
		make_team();
		
		for(int round = 1; round <= K; round++) {
			print();
			team_move();
			print();
			throw_ball();
//			System.out.println(round);
			System.out.println(score);
		}
		System.out.println(score);
		
	}
	
	static void throw_ball() {
		print_dir();
		
		int startX = 0;
		int startY = 0;
		if(ball_dir == 0) {
			startX = ball_index;
			startY = 0;
		}else if(ball_dir == 1) {
			startX = N - 1;
			startY = ball_index;
		}else if(ball_dir == 2) {
			startX = N - ball_index - 1;
			startY = N - 1;
		}else {
			startX = 0;
			startY = N - ball_index - 1;
		}
		
		for(int i = 0; i < N; i++) {
			if(team_num[startX][startY] > 0) {
				int team = team_num[startX][startY];
				int index = 1;
				Deque<Point> team_deque = team_arr[team];
				Iterator<Point> iter = null;
				if(!is_opposite[team]) {
					iter = team_deque.iterator();
				}else {
					iter = team_deque.descendingIterator();
				}
				
				while(iter.hasNext()) {
					Point now = iter.next();
					if(now.x == startX && now.y == startY) {
						break;
					}
					index++;
				}
				
				score += (index * index);
				
				Point first = team_deque.getFirst();
				Point last = team_deque.getLast();
				int temp = matrix[first.x][first.y];
				matrix[first.x][first.y] = matrix[last.x][last.y];
				matrix[last.x][last.y] = temp;
				
				if(!is_opposite[team]) {
					is_opposite[team] = true;
				}else {
					is_opposite[team] = false;
				}
				break;
			}
			
			if(ball_dir == 0) {
				startY++;
			}else if(ball_dir == 1) {
				startX--;
			}else if(ball_dir == 2) {
				startY--;
			}else {
				startX++;
			}
			
			
		}
		ball_index++;
		
		if(ball_index == N) {
			ball_index = 0;
			ball_dir = next_ball_dir(ball_dir);
		}
	}
	
	static void make_team() {
		boolean[][] visited = new boolean[N][N];
		Stack<Point> stack = new Stack<>();
		int team_index = 1;
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(matrix[i][j] == 0) continue;
				if(!visited[i][j] && matrix[i][j] == 1) {
					stack.add(new Point(i, j));
					visited[i][j] = true;
					Point tail = null;
					while(!stack.isEmpty()) {
						Point now = stack.pop();
						team_arr[team_index].addLast(now);
						team_num[now.x][now.y] = team_index;
						for(int z = 0; z < 4; z++) {
							int nextX = now.x + dx[z];
							int nextY = now.y + dy[z];
							
							if(nextX >= N || nextX < 0 || nextY >= N || nextY < 0 || visited[nextX][nextY]) {
								continue;
							}
							
							if(matrix[nextX][nextY] == 2) {
								stack.push(new Point(nextX, nextY));
								visited[nextX][nextY] = true;
								break;
							}else if(matrix[nextX][nextY] == 3){
								tail = new Point(nextX, nextY);
							}
						}
					}
					team_arr[team_index].addLast(tail);
					team_index++;
				}
			}
		}
	}
	
	static void team_move() {
		for(int i = 1; i < team_arr.length; i++) {
			Deque<Point> now_deque = team_arr[i];
			
			if(!is_opposite[i]) {
				Point now = now_deque.getFirst();
				Point next = get_next_point(now);
				
				now_deque.addFirst(next);
				
				
				matrix[now.x][now.y] = 2;
				
				Point tail = now_deque.pollLast();
				matrix[tail.x][tail.y] = 0;
				team_num[tail.x][tail.y] = 0;
				
				Point next_tail = now_deque.peekLast();
				matrix[next_tail.x][next_tail.y] = 3;
				matrix[next.x][next.y] = 1;
				team_num[next.x][next.y] = i;
			}else {
				Point now = now_deque.getLast();
				Point next = get_next_point(now);
				
				now_deque.addLast(next);
				
				
				matrix[now.x][now.y] = 2;
				
				Point tail = now_deque.pollFirst();
				matrix[tail.x][tail.y] = 0;
				team_num[tail.x][tail.y] = 0;
				
				Point next_tail = now_deque.peekFirst();
				matrix[next_tail.x][next_tail.y] = 3;
				
				matrix[next.x][next.y] = 1;
				team_num[next.x][next.y] = i;
			}
		}
	}
	
	static Point get_next_point(Point now) {
		int nowX = now.x;
		int nowY = now.y;
		Point result = null;
		Point tail = null;
		for(int i = 0; i < 4; i++) {
			int nextX = nowX + dx[i];
			int nextY = nowY + dy[i];
			if(nextX >= N || nextX < 0 || nextY >= N || nextY < 0) {
				continue;
			}
			if(line[nextX][nextY] && matrix[nextX][nextY] == 0) {
				result = new Point(nextX, nextY);
				break;
			}
			if(matrix[nextX][nextY] == 3) {
				tail = new Point(nextX, nextY);
			}
		}
		
		if(result == null) {
			result = tail;
		}
		return result;
	}
	
	static int next_ball_dir(int dir) {
		int next = dir + 1;
		if(next == 4) {
			next = 0;
		}
		
		return next;
	}
	
	static void print() {
		System.out.println("-------");
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	static void print_dir() {
		switch(ball_dir) {
		case 0:
			System.out.println("->");
			break;
		case 1:
			System.out.println("^");
			break;
		case 2:
			System.out.println("<-");
			break;
		case 3:
			System.out.println("아래로");
			break;
		}
	}

}
