# java_interleaving_string

Given strings `s1`, `s2`, and `s3`, find whether `s3` is formed by an **interleaving** of `s1` and `s2`.

An **interleaving** of two strings `s` and `t` is a configuration where they are divided into **non-empty** substrings such that:

- `s = s1 + s2 + ... + sn`
- `t = t1 + t2 + ... + tm`
- `|n - m| <= 1`
- The **interleaving** is `s1 + t1 + s2 + t2 + s3 + t3 + ...` or `t1 + s1 + t2 + s2 + t3 + s3 + ...`

**Note:** `a + b` is the concatenation of strings `a` and `b`.

## Examples

**Example 1:**

![https://assets.leetcode.com/uploads/2020/09/02/interleave.jpg](https://assets.leetcode.com/uploads/2020/09/02/interleave.jpg)

```
Input: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
Output: true

```

**Example 2:**

```
Input: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbbaccc"
Output: false

```

**Example 3:**

```
Input: s1 = "", s2 = "", s3 = ""
Output: true

```

**Constraints:**

- `0 <= s1.length, s2.length <= 100`
- `0 <= s3.length <= 200`
- `s1`, `s2`, and `s3` consist of lowercase English letters.

**Follow up:** Could you solve it using only `O(s2.length)` additional memory space?

## 解析

題目給定3個字串 s1, s2, s3

定義 interleaving of 2 string s, t  is a string l 符合以下條件

假設 s = $s_1+s_2+ … + s_n$  ,  s 由 n 個字元組成

        t = $t_1+t_2 + … + t_m$  , t 由 m 個字元組成

l = $s_1 + t_1 + s_2 + t_2 + …$  或是 l = $t_1 + s_1 + t_2 + s_2 + …$ 

l 是由 s , t 的所有字元以原本在字元的順序所組成的

要求寫一個演算法判斷 s3 是不是 s1, s2 的 interleaving

首先是 s3 要是 s1, s2 的 interleaving 

在長度上比需符合 len(s3) = len(s1) + len(s2)

然後逐步從 s3 每個字元去找出是否有在 s1, s2 的字元之中，並且有符合順序

要檢查的方式可以從 s1, s2 隨意挑一個字元

然後檢查目前這個字元是否有跟當下 s3 字元符合

可以畫出以下的決策樹

![](https://i.imgur.com/bRwNjNM.png)

用這樣做 DFS 會發現最差的情況必須走訪所有的結點才能知道是否不是 

所以時間複雜度會是 O($2^{len(s3)}$)

透過 cache 的方式紀錄已經走過的 結點

只要考慮所有可能 startFrom s1, startFrom s2 的組合

所以只有 (len(s1)+1) * (len(s2)+1) 種可能

可 讓時間複雜度降低到  O(n*m), where n = len(s1), m = len(s2)

使用動態規劃 

定義 dp[start_from_s1][start_from_s2] 

代表  s1從 start_from_s1 位置開始 s2 從 start_from_s2位置開始 是否可以找到與 s3[start_from_s1+start_from_s2] 的相同字元

當 start_from_s1 = len(s1) && start_from_s2 = len(s2)

代表 兩個字傳都走到最後面為空字串 且 len(s1) + len(s2) = len(s3) 代表 s3 的最後面 也是空字串

所以 dp[len(s1)][len(s2)] = true

然後逐步推斷 row = len(s1) 的所有組成可能性

然後逐步推斷 col = len(s2) 的所有組成可能性

對所有 dp[i][j] 以下兩種情況是 true 

if i < len(s1)  && s1[i] == s3[i+j ] && dp[i+1, j] == true  // 代表 這個 i+j 選 s1

if j < len(s2)  && s2[j] == s3[i+j ] && dp[i, j+1] == true // 代表 這個 i+j 選 s2

其他的情況則是 false 代表都沒有符合的

最後 dp[0][0] 及為所求

![](https://i.imgur.com/CDMAR3C.png)

同樣需要 loop m by n 所以時間複雜度也是 O(m*n)

空間複雜度也是 O(m*n)

## 程式碼
```java
import java.util.HashMap;
import java.util.Objects;

public class Solution {
  static class StartRecord {
    private final int s1Start;
    private final int s2Start;

    public StartRecord(int s1Start, int s2Start) {
      this.s1Start = s1Start;
      this.s2Start = s2Start;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof StartRecord)) return false;
      StartRecord that = (StartRecord) o;
      return s1Start == that.s1Start && s2Start == that.s2Start;
    }

    @Override
    public int hashCode() {
      return Objects.hash(s1Start, s2Start);
    }
  }
  public boolean isInterleave(String s1, String s2, String s3) {
    int s1Len = s1.length();
    int s2Len = s2.length();
    int s3Len = s3.length();
    if (s1Len + s2Len != s3Len) {
      return false;
    }
    boolean[][] dp = new boolean[s1Len+1][s2Len+1];
    dp[s1Len][s2Len] = true;
    // dp[start1][start2] : s1 start from start1, s2 start from start2 s3 start from start1+start2
    for (int start1 = s1Len; start1 >= 0; start1--) {
      for(int start2 = s2Len; start2 >= 0; start2--) {
        if (start1 < s1Len && s1.charAt(start1) == s3.charAt(start1+start2) && dp[start1+1][start2]) {
          dp[start1][start2] = true;
          continue;
        }
        if (start2 < s2Len && s2.charAt(start2) == s3.charAt(start1+start2) && dp[start1][start2+1]) {
          dp[start1][start2] = true;
        }
      }
    }
    return dp[0][0];
  }
  public boolean isInterleaveV1(String s1, String s2, String s3) {
    int s1Len = s1.length();
    int s2Len = s2.length();
    int s3Len = s3.length();
    if (s1Len + s2Len != s3Len) {
      return false;
    }
    boolean[] dp = new boolean[s2Len+1];
    // dp[s2start] : s2 start from s2start
    for (int s1Start = s1Len; s1Start >= 0; s1Start--) {
      for (int s2Start = s2Len; s2Start >= 0; s2Start--) {
        if (s1Start == s1Len && s2Start == s2Len) {
          dp[s2Start] = true;
          continue;
        }
        if (s1Start == s1Len && s2Start < s2Len) { // s1 is empty
          dp[s2Start] = dp[s2Start+1] && s2.charAt(s2Start) == s3.charAt(s1Start+ s2Start);
          continue;
        }
        if (s1Start < s1Len && s2Start == s2Len) { // s2 is empty
          dp[s2Start] = dp[s2Start] && s1.charAt(s1Start) == s3.charAt(s1Start+ s2Start);
          continue;
        }
        if (s1Start < s1Len && s2Start < s2Len) {
          dp[s2Start] = (dp[s2Start+1] && s2.charAt(s2Start) == s3.charAt(s1Start+ s2Start))||
              (dp[s2Start] && s1.charAt(s1Start) == s3.charAt(s1Start+ s2Start));
        }
      }
    }
    return dp[0];
  }
  public boolean isInterleaveDFS(String s1, String s2, String s3) {
    int s1Len = s1.length();
    int s2Len = s2.length();
    int s3Len = s3.length();
    if (s1Len + s2Len != s3Len) {
      return false;
    }
    HashMap<StartRecord, Boolean> cache = new HashMap<>();
    return DFS(cache, s1, 0, s2, 0, s3);
  }
  public boolean DFS(HashMap<StartRecord, Boolean> cache, String s1, int s1Start, String s2, int s2Start, String s3) {
    int s3Len = s3.length();
    if (s1Start + s2Start == s3Len) { // found
      return true;
    }
    StartRecord record = new StartRecord(s1Start, s2Start);
    if (cache.containsKey(record)) {
      return cache.get(record);
    }
    int s1Len = s1.length();
    int s2Len = s2.length();
    if (s1Start < s1Len && s1.charAt(s1Start) == s3.charAt(s1Start+s2Start) &&
        DFS(cache, s1, s1Start+1, s2, s2Start, s3)) {
      return true;
    }
    if (s2Start < s2Len && s2.charAt(s2Start) == s3.charAt(s1Start+s2Start) &&
        DFS(cache, s1, s1Start, s2, s2Start+1, s3)) {
      return true;
    }
    cache.put(record, false);
    return false;
  }
}

```
## 困難點

1. 需要找出字串 s1, s2 與 s3 開始比對位置的遞迴關係

## Solve Point

- [x]  初始化一個 len(s1) + 1 by len(s2) +1 的 bool 矩陣 初始化 dp[len(s1)][len(s2)] = true
- [x]  依照關係式去算出 每個 dp[i][j] 的值
- [x]  回傳 dp[0][0]