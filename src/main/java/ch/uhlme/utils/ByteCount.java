package ch.uhlme.utils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class ByteCount {
  private final long count;

  public ByteCount(long count) {
    this.count = count;
  }

  @Override
  public String toString() {
    long tempCount = count;

    if (-1000 < count && tempCount < 1000) {
      return tempCount + " B";
    }
    CharacterIterator ci = new StringCharacterIterator("kMGTPE");
    while (tempCount <= -999_950 || tempCount >= 999_950) {
      tempCount /= 1000.0;
      ci.next();
    }
    return String.format("%.1f %cB", tempCount / 1000.0, ci.current());
  }

  public long getCount() {
    return count;
  }
}
