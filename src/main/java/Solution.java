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
