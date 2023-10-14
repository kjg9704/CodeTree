import java.io.*;
import java.util.*;

public class Main {

	
	static class Person{
		int num;
		int x;
		int y;
		int depart_x;
		int depart_y;
		boolean arrived;
		
		public Person(int num, int depart_x, int depart_y) {
			this.num = num;
			this.depart_x = depart_x;
			this.depart_y = depart_y;
			this.arrived = false;
		}
	}
	
	static class Point{
		int x;
		int y;
		int dir = 0;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Point(int x, int y, int dir) {
			this.x = x;
			this.y = y;
			this.dir = dir;
		}
	}
	
	static int[] dx = {-1, 0, 0, 1};
	static int[] dy = {0, -1, 1, 0};
	
	static boolean[][] blocked;
	static int[][] matrix;
	static int[][] person_matrix;
	static Person[] person_arr;
	static ArrayList<Point> base_list;
	
	static int N, M;
	static int minute;
	static int arrive_cnt = 0;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String[] input = br.readLine().split(" ");
		
		N = Integer.parseInt(input[0]);
		M = Integer.parseInt(input[1]);
		
		blocked = new boolean[N + 1][N + 1];
		matrix = new int[N + 1][N + 1];
		person_arr = new Person[M + 1];
		base_list = new ArrayList<>();
		person_matrix = new int[N + 1][N + 1];
		
		for(int i = 1; i <= N; i++) {
			input = br.readLine().split(" ");
			for(int j = 1; j <= N; j++) {
				int num =  Integer.parseInt(input[j - 1]);
				matrix[i][j] = num;
				if(num == 1) {
					base_list.add(new Point(i, j));
				}
			}
		}
		
		for(int i = 1; i <= M; i++) {
			input = br.readLine().split(" ");
			int x = Integer.parseInt(input[0]);
			int y = Integer.parseInt(input[1]);
			person_arr[i] = new Person(i, x, y);
		}
		
		minute = 1;
		
		while(arrive_cnt != M) {
			move();
			
			if(arrive_cnt == M) {
				break;
			}
			go_to_base();
			
			minute++;

		}
		
		System.out.println(minute);
		
		
	}
	
	static void move() {
		ArrayList<Point> arrived = new ArrayList<>();
		for(int i = 1; i <= M; i++) {
			if(minute > i) {
				Person now = person_arr[i];
				if(now.arrived)
					continue;
				for(int z = 0; z < 4; z++) {
					int nextX = now.x + dx[z];
					int nextY = now.y + dy[z];
					
					if(nextX > N || nextX < 1 || nextY > N || nextY < 1 || blocked[nextX][nextY]) {
						continue;
					}
					
					int now_dist = get_dist(now.x, now.y, now.depart_x, now.depart_y);
					int next_dist = get_dist(nextX, nextY, now.depart_x, now.depart_y);
					
					if(next_dist < now_dist) {
						now.x = nextX;
						now.y = nextY;
						if(nextX == now.depart_x && nextY == now.depart_y) {
							arrived.add(new Point(nextX, nextY));
							now.arrived = true;
						}
						break;
					}
				}
			}
		}
		
		if(arrived.size() > 0) {
			for(Point arrive : arrived) {
				blocked[arrive.x][arrive.y] = true;
				arrive_cnt++;
			}
		}
	}
	
	static void go_to_base() {
		
		if(minute <= M) {
			Person now = person_arr[minute];
			
			int min_base_dist = Integer.MAX_VALUE;
			int min_baseX = 0;
			int min_baseY = 0;
			
			for(Point base : base_list) {
				if(!blocked[base.x][base.y]) {
					int now_dist = get_dist(now.depart_x, now.depart_y, base.x, base.y);
					
					if(min_base_dist > now_dist) {
						min_base_dist = now_dist;
						min_baseX = base.x;
						min_baseY = base.y;
					}else if(min_base_dist == now_dist) {
						if(min_baseX > base.x) {
							min_base_dist = now_dist;
							min_baseX = base.x;
							min_baseY = base.y;
						}else if(min_baseX == base.x) {
							if(min_baseY > base.y) {
								min_base_dist = now_dist;
								min_baseX = base.x;
								min_baseY = base.y;
							}
						}
					}
				}
			}
			
			now.x = min_baseX;
			now.y = min_baseY;
			
			blocked[min_baseX][min_baseY] = true;
			
		}
		
	}
	
	static int get_dist(int x1, int y1, int x2, int y2) {
		Queue<Point> que = new LinkedList<>();
		
		int result = Integer.MAX_VALUE;
		
		int nowX = x1;
		int nowY = y1;
		boolean[][] visited = new boolean[N + 1][N + 1];
		
		que.add(new Point(nowX, nowY, 0));
		visited[nowX][nowY] = true;
		
		while(!que.isEmpty()) {
			Point now = que.poll();
			if(now.x == x2 && now.y == y2) {
				result = now.dir;
				break;
			}
			for(int z = 0; z < 4; z++) {
				int nextX = now.x + dx[z];
				int nextY = now.y + dy[z];
				
				if(nextX > N || nextX < 1 || nextY > N || nextY < 1 || blocked[nextX][nextY]) {
					continue;
				}
				
				if(!visited[nextX][nextY]) {
					que.add(new Point(nextX, nextY, now.dir + 1));
					visited[nextX][nextY] = true;
				}
				
			}
		}
		
		
		return result;
	}

}
