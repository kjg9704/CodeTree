import java.io.*;
import java.util.*;

public class Main {

	static class Node{
		int num;
		int[] alarm_cnt;
		
		public Node(int num) {
			this.num = num;
			this.alarm_cnt = new int[21];
		}

	}
	static int N, Q;
	static int[] auth_arr;
	static int[] parents;
	static boolean[] is_off;
	static Node[] count_node;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		StringBuilder sb = new StringBuilder();
		auth_arr = new int[N + 1];
		parents = new int[N + 1];
		is_off = new boolean[N + 1];
		count_node = new Node[N + 1];
		
		for(int i = 0; i <= N; i++) {
			count_node[i] = new Node(i);
		}
		for(int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());

			int cmd = Integer.parseInt(st.nextToken());
			int num = 0;
			switch(cmd) {
			
			case 100:
				for(int j = 1; j <= N; j++) {
					int par = Integer.parseInt(st.nextToken());
					parents[j] = par;
				}
				
				for(int j = 1; j <= N; j++) {
					int auth = Integer.parseInt(st.nextToken());
					auth_arr[j] = auth;
				}
				init();
				break;
				
			case 200:
				num = Integer.parseInt(st.nextToken());
				onOFF(num);
				break;
				
			case 300:
				num = Integer.parseInt(st.nextToken());
				int power = Integer.parseInt(st.nextToken());
				changeAuth(num, power);
				break;
				
			case 400:
				num = Integer.parseInt(st.nextToken());
				int num2 = Integer.parseInt(st.nextToken());
				changeParent(num, num2);
				break;
				
			case 500:
				num = Integer.parseInt(st.nextToken());
				int cnt = countAlarm(num);
				sb.append(cnt);
				sb.append("\n");
				break;
			}
		}
		
		System.out.println(sb.toString());
	}
	
	static void init() {
		for(int i = 1; i <= N; i++) {
			int now = i;
			int auth = auth_arr[now];
			on_dfs(now, auth, 1);
		}
	}
	
	static void onOFF(int idx) {

		if(is_off[idx]) { // off
			is_off[idx] = false;
			alarm_on(idx);
		}else { // on
			is_off[idx] = true;
			alarm_off(idx);
		}
		
	}
	
	static void alarm_on(int idx) {
		
		on_dfs(idx, auth_arr[idx], 1);
		
		for(int i = 1; i <= 20; i++) {
			if(count_node[idx].alarm_cnt[i] > 0) {
				on_dfs(idx, i, count_node[idx].alarm_cnt[i]);
			}
		}
	}
	
	static void alarm_off(int idx) {
	
		off_dfs(idx, auth_arr[idx], 1);
		
		for(int i = 1; i <= 20; i++) {
			if(count_node[idx].alarm_cnt[i] > 0) {
				off_dfs(idx, i, count_node[idx].alarm_cnt[i]);
			}
		}
	}
	
	static void changeAuth(int idx, int power) {
		int prev_power = auth_arr[idx];
		if(power == prev_power) return;
		if(!is_off[idx]) { // on
			off_dfs(idx, prev_power, 1);
			on_dfs(idx, power, 1);
		}
		
		auth_arr[idx] = power;
	}
	
	static void changeParent(int idx1, int idx2) {
		
		int parent1 = getParent(idx1);
		int parent2 = getParent(idx2);
		if(parent1 == parent2) return;
		
		if(!is_off[idx1]) { // 1 is on
			alarm_off(idx1);
		}
		
		if(!is_off[idx2]) { // 2 is on
			alarm_off(idx2);
		}
		
		parents[idx1] = parent2;
		parents[idx2] = parent1;
		
		if(!is_off[idx1]) { // 1 is on
			alarm_on(idx1);
		}
		
		if(!is_off[idx2]) { // 2 is on
			alarm_on(idx2);
		}
	}
	
	
	
	static void off_dfs(int child, int power, int cnt) {
		if(power == 0 || child == 0) {
			return;
		}
		
		int parent = getParent(child);
		if(power > 21) power = 21;
		count_node[parent].alarm_cnt[power - 1] -= cnt;
		if(!is_off[parent]) {
			off_dfs(parent, power - 1, cnt);
		}
	}
	
	static void on_dfs(int child, int power, int cnt) {
		if(power == 0 || child == 0) {
			return;
		}
		
		int parent = getParent(child);

		if(power > 21) power = 21;
		count_node[parent].alarm_cnt[power - 1] += cnt;
		if(!is_off[parent]) {
			on_dfs(parent, power - 1, cnt);
		}
		
	}
	
	static int countAlarm(int idx) {
		int cnt = 0;
		for(int i = 0; i <= 20; i++) {
			cnt += count_node[idx].alarm_cnt[i];
		}
		
		return cnt;
	}
	
	static int getParent(int idx) {
		return parents[idx];
	}

}
