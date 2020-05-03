package ch.uhlme.utils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class ByteCount {
    private long count;

    public ByteCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        if (-1000 < count && count < 1000) {
            return count + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (count <= -999_950 || count >= 999_950) {
            count /= 1000.0;
            ci.next();
        }
        return String.format("%.1f %cB", count / 1000.0, ci.current());
    }
}
