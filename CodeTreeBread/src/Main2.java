import java.io.*;
import java.util.*;

public class Main2 {

	static class Person{
		int x, y, des_x, des_y;
		boolean arrived;
		
		public Person(int x, int y, int des_x, int des_y) {
			this.x = x;
			this.y = y;
			this.des_x = des_x;
			this.des_y = des_y;
			this.arrived = false;
		}
	}
	
	static class Point{
		int x, y, cnt;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Point(int x, int y, int cnt) {
			this.x = x;
			this.y = y;
			this.cnt = cnt;
		}
	}
	static int N, M;
	static int des_cnt = 0;
	static boolean[][] blocked;
	static Person[] person_arr;
	static ArrayList<Point> base_list;
	
	static int[] dx = {-1, 0, 0, 1};
	static int[] dy = {0, -1, 1, 0};
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String[] input = br.readLine().split(" ");
		
		N = Integer.parseInt(input[0]);
		M = Integer.parseInt(input[1]);
		person_arr = new Person[M + 1];
		blocked = new boolean[N + 1][N + 1];
		base_list = new ArrayList<Point>();
		
		for(int i = 1; i <= N; i++) {
			input = br.readLine().split(" ");
			for(int j = 1; j <= N; j++) {
				int now = Integer.parseInt(input[j - 1]);
				
				if(now == 1) {
					base_list.add(new Point(i, j));
				}
			}
		}
		
		for(int i = 1; i <= M; i++) {
			input = br.readLine().split(" ");
			int x = Integer.parseInt(input[0]);
			int y = Integer.parseInt(input[1]);
			
			person_arr[i] = new Person(0, 0, x, y);
		}
		
		int time = 0;
		while(true) {
			
			move(time);
			
			time++;
			
			if(time <= M) {
				start(time);
			}
			if(des_cnt == M) {
				break;
			}
		}
		
		System.out.println(time);

	}
	
	static void move(int time) {
		for(int i = 1; i <= time; i++) {
			if(i > M) break;
			Person now = person_arr[i];
			if(now.arrived) continue;
			int min_dist = Integer.MAX_VALUE;
			int dir = -1;
			for(int z = 0; z < 4; z++) {
				int nextX = now.x + dx[z];
				int nextY = now.y + dy[z];
				if(nextX < 1 || nextX > N || nextY < 1 || nextY > N) continue;
				if(blocked[nextX][nextY]) continue;
				
				int dist = getDist(nextX, nextY, now.des_x, now.des_y);
				if(dist < min_dist) {
					dir = z;
					min_dist = dist;
				}
			}
			
			int nextX = now.x + dx[dir];
			int nextY = now.y + dy[dir];
			now.x = nextX;
			now.y = nextY;
		}
		
		for(int i = 1; i <= time; i++) {
			if(i > M) break;
			Person now = person_arr[i];
			if(now.arrived) continue;
			if(now.x == now.des_x && now.y == now.des_y) {
				blocked[now.x][now.y] = true;
				now.arrived = true;
				des_cnt++;
			}
		}
	}
	
	static void start(int time) {
		int baseX = 0;
		int baseY = 0;
		int min_dist = Integer.MAX_VALUE;
		int des_x = person_arr[time].des_x;
		int des_y = person_arr[time].des_y;
		for(Point base : base_list) {
			if(!blocked[base.x][base.y]) {
				int dist = getDist(base.x, base.y, des_x, des_y);
				if(dist < min_dist) {
					min_dist = dist;
					baseX = base.x;
					baseY = base.y;
				}else if(dist == min_dist) {
					if(base.x < baseX) {
						min_dist = dist;
						baseX = base.x;
						baseY = base.y;
					}else if(base.x == baseX) {
						if(base.y < baseY) {
							min_dist = dist;
							baseX = base.x;
							baseY = base.y;
						}
					}
				}
			}
		}
		person_arr[time].x = baseX;
		person_arr[time].y = baseY;
		blocked[baseX][baseY] = true;
	}
	
	static int getDist(int startX, int startY, int endX, int endY) {
		int min_dist = Integer.MAX_VALUE;
		Queue<Point> que = new LinkedList<>();
		boolean[][] visited = new boolean[N + 1][N + 1];
		que.add(new Point(startX, startY, 0));
		visited[startX][startY] = true;
		
		while(!que.isEmpty()) {
			Point now = que.poll();
			if(now.x == endX && now.y == endY) {
				min_dist = now.cnt;
				break;
			}
			for(int z = 0; z < 4; z++) {
				int nextX = now.x + dx[z];
				int nextY = now.y + dy[z];
				
				if(nextX < 1 || nextX > N || nextY < 1 || nextY > N) continue;
				if(visited[nextX][nextY]) continue;
				if(blocked[nextX][nextY]) continue;
				
				visited[nextX][nextY] = true;
				que.add(new Point(nextX, nextY, now.cnt + 1));
				
			}
		}
		
		return min_dist;
	}

}
