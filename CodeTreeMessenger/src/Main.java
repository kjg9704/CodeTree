import java.io.*;
import java.util.*;

public class Main {

	static class Tree{
		int num, left_node, right_node;
		int[] left_cnt_arr;
		int[] right_cnt_arr;
		
		public Tree(int num, int left_node, int right_node) {
			this.num = num;
			this.left_node = left_node;
			this.right_node = right_node;
			this.left_cnt_arr = new int[21];
			this.right_cnt_arr = new int[21];
		}
		
		public Tree(int num) {
			this.num = num;
			this.left_cnt_arr = new int[21];
			this.right_cnt_arr = new int[21];
		}
	}
	static int N, Q;
	static int[] auth_arr;
	static int[] parents;
	static boolean[] is_off;
	static Tree[] count_tree;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		StringBuilder sb = new StringBuilder();
		auth_arr = new int[N + 1];
		parents = new int[N + 1];
		is_off = new boolean[N + 1];
		count_tree = new Tree[N + 1];
		
		for(int i = 0; i <= N; i++) {
			count_tree[i] = new Tree(i);
		}
		int tmp = 0;
		for(int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			if(tmp == 44) {
				int aaa= 0;
			}
			int cmd = Integer.parseInt(st.nextToken());
			int num = 0;
			switch(cmd) {
			
			case 100:
				for(int j = 1; j <= N; j++) {
					int par = Integer.parseInt(st.nextToken());
					parents[j] = par;
					if(count_tree[par].left_node == 0) {
						count_tree[par].left_node = j;
					}else if(count_tree[par].right_node == 0) {
						count_tree[par].right_node = j;
					}
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
				tmp++;
				break;
			}
		}
		
		System.out.println(sb.toString());
	}
	
	static void init() {
		for(int i = 1; i <= N; i++) {
			int now = i;
			int auth = auth_arr[now];
			
			dfs(now, auth);
			
		}
	}
	
	static void onOFF(int idx) {
		if(!is_off[idx]) {
			change_dfs(idx, auth_arr[idx], 0);
			for(int i = 1; i <= 20; i++) {
				if(count_tree[idx].left_cnt_arr[i] > 0) {
					swap_dfs(idx, i, count_tree[idx].left_cnt_arr[i], false);
				}
				
				if(count_tree[idx].right_cnt_arr[i] > 0) {
					swap_dfs(idx, i, count_tree[idx].right_cnt_arr[i], false);
				}
			}
		}else {
			dfs(idx, auth_arr[idx]);
			for(int i = 1; i <= 20; i++) {
				if(count_tree[idx].left_cnt_arr[i] > 0) {
					swap_dfs(idx, i, count_tree[idx].left_cnt_arr[i], true);
				}
				
				if(count_tree[idx].right_cnt_arr[i] > 0) {
					swap_dfs(idx, i, count_tree[idx].right_cnt_arr[i], true);
				}
			}
		}
		
		is_off[idx] = !is_off[idx];
	}
	
	static void changeAuth(int idx, int power) {
		if(!is_off[idx]) {
			int prev_power = auth_arr[idx];
			if(power == prev_power) return;
			
			change_dfs(idx, prev_power, power);
		}
		
		auth_arr[idx] = power;
	}
	
	static void changeParent(int idx1, int idx2) {
		
		int parent1 = getParent(idx1);
		int parent2 = getParent(idx2);
		if(parent1 == parent2) return;
		
		
		if(!is_off[idx1]) {
			int prev_power = auth_arr[idx1];
			
			change_dfs(idx1, prev_power, 0);
		}
		
		if(!is_off[idx2]) {
			int prev_power = auth_arr[idx2];
			
			change_dfs(idx2, prev_power, 0);
		}
		
		
		for(int i = 1; i <= 20; i++) {
			if(!is_off[idx1]) {
				if(count_tree[idx1].left_cnt_arr[i] > 0) {
					swap_dfs(idx1, i, count_tree[idx1].left_cnt_arr[i], false);
				}
				
				if(count_tree[idx1].right_cnt_arr[i] > 0) {
					swap_dfs(idx1, i, count_tree[idx1].right_cnt_arr[i], false);
				}
			}
			
			if(!is_off[idx2]) {
				if(count_tree[idx2].left_cnt_arr[i] > 0) {
					swap_dfs(idx2, i, count_tree[idx2].left_cnt_arr[i], false);
				}
				
				if(count_tree[idx2].right_cnt_arr[i] > 0) {
					swap_dfs(idx2, i, count_tree[idx2].right_cnt_arr[i], false);
				}
			}
			
		}
		
		if(is_left(parent1, idx1)) {
			count_tree[parent1].left_node = idx2;
		}else {
			count_tree[parent1].right_node = idx2;
		}
		
		if(is_left(parent2, idx2)) {
			count_tree[parent2].left_node = idx1;
		}else {
			count_tree[parent2].right_node = idx1;
		}
		
		parents[idx1] = parent2;
		parents[idx2] = parent1;
		
		
		if(!is_off[idx1]) {
			int auth = auth_arr[idx1];
			
			dfs(idx1, auth);
		}
		
		if(!is_off[idx2]) {
			int auth = auth_arr[idx2];
			
			dfs(idx2, auth);
		}
		
		
		for(int i = 1; i <= 20; i++) {
			if(!is_off[idx1]) {
				if(count_tree[idx1].left_cnt_arr[i] > 0) {
					swap_dfs(idx1, i, count_tree[idx1].left_cnt_arr[i], true);
				}
				
				if(count_tree[idx1].right_cnt_arr[i] > 0) {
					swap_dfs(idx1, i, count_tree[idx1].right_cnt_arr[i], true);
				}
			}
			
			
			if(!is_off[idx2]) {
				if(count_tree[idx2].left_cnt_arr[i] > 0) {
					swap_dfs(idx2, i, count_tree[idx2].left_cnt_arr[i], true);
				}
				
				if(count_tree[idx2].right_cnt_arr[i] > 0) {
					swap_dfs(idx2, i, count_tree[idx2].right_cnt_arr[i], true);
				}
			}
		}
		
	}
	
	static int countAlarm(int idx) {
		int cnt = 0;
		
		int left = count_tree[idx].left_node;
		int right = count_tree[idx].right_node;
		if(left > 0 && !is_off[left]) {
			for(int i = 0; i <= 20; i++) {
				cnt += count_tree[idx].left_cnt_arr[i];
			}
		}
		
		if(right > 0 && !is_off[right]) {
			for(int i = 0; i <= 20; i++) {
				cnt += count_tree[idx].right_cnt_arr[i];
			}
		}
		
		return cnt;
	}
	
	static void change_dfs(int child, int prev, int power) {
		if(child == 0) return;
		if(prev <= 0 && power <= 0) return;
		
		int parent = getParent(child);
		
		if(is_left(parent, child)) {
			if(prev > 0) count_tree[parent].left_cnt_arr[prev - 1]--;
			if(power > 0) count_tree[parent].left_cnt_arr[power - 1]++;
			
		}else {
			if(prev > 0) count_tree[parent].right_cnt_arr[prev - 1]--;
			if(power > 0) count_tree[parent].right_cnt_arr[power - 1]++;
		}
		
		change_dfs(parent, prev - 1, power - 1);
	}
	
	static void dfs(int child, int power) {
		if(power == 0 || child == 0) {
			return;
		}
		
		int parent = getParent(child);
		if(is_left(parent, child)) {
			count_tree[parent].left_cnt_arr[power - 1]++;
		}else {
			count_tree[parent].right_cnt_arr[power - 1]++;
		}
		
		dfs(parent, power - 1);
	}
	
	static void swap_dfs(int child, int power, int cnt, boolean add) {
		if(power == 0 || child == 0) {
			return;
		}
		
		int parent = getParent(child);
		if(is_left(parent, child)) {
			if(add) {
				count_tree[parent].left_cnt_arr[power - 1] += cnt;
			}else {
				count_tree[parent].left_cnt_arr[power - 1] -= cnt;
			}
			
		}else {
			if(add) {
				count_tree[parent].right_cnt_arr[power - 1] += cnt;
			}else {
				count_tree[parent].right_cnt_arr[power - 1] -= cnt;
			}
			
		}
		
		swap_dfs(parent, power - 1, cnt, add);
	}
	

	static boolean is_left(int parent, int child) {
		if(count_tree[parent].left_node == child) {
			return true;
		}else {
			return false;
		}
	}
	
	
	static int getParent(int idx) {
		return parents[idx];
	}

}
