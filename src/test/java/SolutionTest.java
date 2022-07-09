import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {
  final private Solution sol = new Solution();
  @Test
  void isInterleaveExample1() {
    assertTrue(sol.isInterleave("aabcc", "dbbca", "aadbbcbcac"));
  }
  @Test
  void isInterleaveExample2() {
    assertFalse(sol.isInterleave("aabcc", "dbbca", "aadbbbaccc"));
  }
  @Test
  void isInterleaveExample3() {
    assertTrue(sol.isInterleave("", "", ""));
  }

  @Test
  void isInterleaveDFSExample1() {
    assertTrue(sol.isInterleaveDFS("aabcc", "dbbca", "aadbbcbcac"));
  }
  @Test
  void isInterleaveDFSExample2() {
    assertFalse(sol.isInterleaveDFS("aabcc", "dbbca", "aadbbbaccc"));
  }
  @Test
  void isInterleaveDFSExample3() {
    assertTrue(sol.isInterleaveDFS("", "", ""));
  }

  @Test
  void isInterleaveV1Example1() {
    assertTrue(sol.isInterleaveV1("aabcc", "dbbca", "aadbbcbcac"));
  }
  @Test
  void isInterleaveV1Example2() {
    assertFalse(sol.isInterleaveV1("aabcc", "dbbca", "aadbbbaccc"));
  }
  @Test
  void isInterleaveV1Example3() {
    assertTrue(sol.isInterleaveV1("", "", ""));
  }
}