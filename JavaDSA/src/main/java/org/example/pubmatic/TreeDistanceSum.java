package org.example.pubmatic;
import java.util.*;

public class TreeDistanceSum {

    public static String solve(String input) {
        StringTokenizer st = new StringTokenizer(input);

        int n = Integer.parseInt(st.nextToken());

        List<Integer>[] adj = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }

        for (int i = 0; i < n - 1; i++) {
            int u = Integer.parseInt(st.nextToken()) - 1;
            int v = Integer.parseInt(st.nextToken()) - 1;

            adj[u].add(v);
            adj[v].add(u);
        }

        int[] parent = new int[n];
        int[] order = new int[n];
        int[] depth = new int[n];
        int[] subtree = new int[n];

        Arrays.fill(parent, -1);

        // First traversal: determine parent, depth, and order.
        int[] stack = new int[n];
        int top = 0;
        int count = 0;

        stack[top++] = 0;
        parent[0] = 0;

        while (top > 0) {
            int u = stack[--top];
            order[count++] = u;

            for (int v : adj[u]) {
                if (parent[v] == -1) {
                    parent[v] = u;
                    depth[v] = depth[u] + 1;
                    stack[top++] = v;
                }
            }
        }

        long[] ans = new long[n];
        long rootSum = 0;

        for (int i = 0; i < n; i++) {
            subtree[i] = 1;
            rootSum += depth[i];
        }

        // Calculate subtree sizes in reverse traversal order.
        for (int i = n - 1; i > 0; i--) {
            int u = order[i];
            subtree[parent[u]] += subtree[u];
        }

        ans[0] = rootSum;

        // Second traversal: reroot the tree at each node.
        for (int i = 1; i < n; i++) {
            int u = order[i];
            int p = parent[u];

            ans[u] = ans[p] - subtree[u]
                    + (long) (n - subtree[u]);
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < n; i++) {
            if (i > 0) result.append(" ");
            result.append(ans[i]);
        }

        return result.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        StringBuilder input = new StringBuilder();
        input.append(n).append("\n");

        for (int i = 0; i < n - 1; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();

            input.append(u).append(" ").append(v).append("\n");
        }

        System.out.println(solve(input.toString()));

        sc.close();

//        5
//        1 2
//        1 3
//        2 4
//        2 5
    }
}
