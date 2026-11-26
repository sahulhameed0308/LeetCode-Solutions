class Solution {
    public String smallestNumber(String num, long t) {
        long temp = t;
        int[] req = new int[10];
        int[] primes = {2, 3, 5, 7};
        for (int p : primes) {
            while (temp % p == 0) {
                req[p]++;
                temp /= p;
            }
        }
        if (temp > 1) return "-1";
        int n = num.length();
        int[][] prefReq = new int[n + 1][10];
        prefReq[0] = req.clone();
        int firstZero = -1;
        for (int i = 0; i < n; i++) {
            int d = num.charAt(i) - '0';
            if (d == 0) {
                firstZero = i;
                break;
            }
            prefReq[i + 1] = prefReq[i].clone();
            remove(prefReq[i + 1], d);
        }
        int startI = firstZero == -1 ? n - 1 : firstZero - 1;
        for (int i = startI; i >= -1; i--) {
            int[] currentReq = prefReq[i + 1];

            if (i == n - 1) {
                if (check(currentReq, 0)) {
                    return num;
                }
                continue;
            }
            int start = num.charAt(i + 1) - '0' + 1;
            for (int d = start; d <= 9; d++) {
                int[] nextReq = currentReq.clone();
                remove(nextReq, d);
                int remLen = n - 1 - (i + 1);
                if (check(nextReq, remLen)) {
                    StringBuilder sb = new StringBuilder();
                    if (i >= 0) {
                        sb.append(num.substring(0, i + 1));
                    }
                    sb.append(d);
                    
                    for (int j = i + 2; j < n; j++) {
                        for (int k = 1; k <= 9; k++) {
                            int[] testReq = nextReq.clone();
                            remove(testReq, k);
                            if (check(testReq, n - 1 - j)) {
                                sb.append(k);
                                nextReq = testReq;
                                break;
                            }
                        }
                    }
                    return sb.toString();
                }
            }
        }
        int len = n + 1;
        while (true) {
            for (int d = 1; d <= 9; d++) {
                int[] nextReq = req.clone();
                remove(nextReq, d);
                if (check(nextReq, len - 1)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(d);
                    for (int j = 1; j < len; j++) {
                        for (int k = 1; k <= 9; k++) {
                            int[] testReq = nextReq.clone();
                            remove(testReq, k);
                            if (check(testReq, len - 1 - j)) {
                                sb.append(k);
                                nextReq = testReq;
                                break;
                            }
                        }
                    }
                    return sb.toString();
                }
            }
            len++;
        }
    }
    private void remove(int[] req, int d) {
        if (d == 2) req[2]--;
        else if (d == 3) req[3]--;
        else if (d == 4) req[2] -= 2;
        else if (d == 5) req[5]--;
        else if (d == 6) { req[2]--; req[3]--; }
        else if (d == 7) req[7]--;
        else if (d == 8) req[2] -= 3;
        else if (d == 9) req[3] -= 2;
        for (int i = 2; i <= 9; i++) {
            if (req[i] < 0) req[i] = 0;
        }
    }
    private boolean check(int[] req, int remLen) {
        int r2 = req[2];
        int r3 = req[3];
        int r5 = req[5];
        int r7 = req[7];
        int need = r5 + r7;
        need += (r3 + 1) / 2;
        int remaining2 = r2;
        if (r3 % 2 == 1) {
            remaining2 -= 1;
        }
        if (remaining2 > 0) {
            need += (remaining2 + 2) / 3;
        }
        return need <= remLen;
    }
}