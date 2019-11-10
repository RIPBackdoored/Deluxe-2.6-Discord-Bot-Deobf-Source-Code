/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.message;

import java.util.BitSet;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class TokenParser {
    public static final char CR = '\r';
    public static final char LF = '\n';
    public static final char SP = ' ';
    public static final char HT = '\t';
    public static final char DQUOTE = '\"';
    public static final char ESCAPE = '\\';
    public static final TokenParser INSTANCE = new TokenParser();

    public static BitSet INIT_BITSET(int ... b) {
        BitSet bitset = new BitSet();
        int[] arr$ = b;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            int aB = arr$[i$];
            bitset.set(aB);
            ++i$;
        }
        return bitset;
    }

    public static boolean isWhitespace(char ch) {
        if (ch == ' ') return true;
        if (ch == '\t') return true;
        if (ch == '\r') return true;
        if (ch == '\n') return true;
        return false;
    }

    public String parseToken(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters) {
        StringBuilder dst = new StringBuilder();
        boolean whitespace = false;
        while (!cursor.atEnd()) {
            char current = buf.charAt(cursor.getPos());
            if (delimiters != null && delimiters.get(current)) {
                return dst.toString();
            }
            if (TokenParser.isWhitespace(current)) {
                this.skipWhiteSpace(buf, cursor);
                whitespace = true;
                continue;
            }
            if (whitespace && dst.length() > 0) {
                dst.append(' ');
            }
            this.copyContent(buf, cursor, delimiters, dst);
            whitespace = false;
        }
        return dst.toString();
    }

    public String parseValue(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters) {
        StringBuilder dst = new StringBuilder();
        boolean whitespace = false;
        while (!cursor.atEnd()) {
            char current = buf.charAt(cursor.getPos());
            if (delimiters != null && delimiters.get(current)) {
                return dst.toString();
            }
            if (TokenParser.isWhitespace(current)) {
                this.skipWhiteSpace(buf, cursor);
                whitespace = true;
                continue;
            }
            if (current == '\"') {
                if (whitespace && dst.length() > 0) {
                    dst.append(' ');
                }
                this.copyQuotedContent(buf, cursor, dst);
                whitespace = false;
                continue;
            }
            if (whitespace && dst.length() > 0) {
                dst.append(' ');
            }
            this.copyUnquotedContent(buf, cursor, delimiters, dst);
            whitespace = false;
        }
        return dst.toString();
    }

    public void skipWhiteSpace(CharArrayBuffer buf, ParserCursor cursor) {
        char current;
        int pos = cursor.getPos();
        int indexFrom = cursor.getPos();
        int indexTo = cursor.getUpperBound();
        for (int i = indexFrom; i < indexTo && TokenParser.isWhitespace(current = buf.charAt(i)); ++pos, ++i) {
        }
        cursor.updatePos(pos);
    }

    public void copyContent(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters, StringBuilder dst) {
        int pos = cursor.getPos();
        int indexFrom = cursor.getPos();
        int indexTo = cursor.getUpperBound();
        for (int i = indexFrom; i < indexTo; ++pos, ++i) {
            char current = buf.charAt(i);
            if (delimiters != null && delimiters.get(current) || TokenParser.isWhitespace(current)) break;
            dst.append(current);
        }
        cursor.updatePos(pos);
    }

    public void copyUnquotedContent(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters, StringBuilder dst) {
        int pos = cursor.getPos();
        int indexFrom = cursor.getPos();
        int indexTo = cursor.getUpperBound();
        for (int i = indexFrom; i < indexTo; ++pos, ++i) {
            char current = buf.charAt(i);
            if (delimiters != null && delimiters.get(current) || TokenParser.isWhitespace(current) || current == '\"') break;
            dst.append(current);
        }
        cursor.updatePos(pos);
    }

    public void copyQuotedContent(CharArrayBuffer buf, ParserCursor cursor, StringBuilder dst) {
        if (cursor.atEnd()) {
            return;
        }
        int pos = cursor.getPos();
        int indexFrom = cursor.getPos();
        int indexTo = cursor.getUpperBound();
        char current = buf.charAt(pos);
        if (current != '\"') {
            return;
        }
        ++pos;
        boolean escaped = false;
        for (int i = ++indexFrom; i < indexTo; ++i, ++pos) {
            current = buf.charAt(i);
            if (escaped) {
                if (current != '\"' && current != '\\') {
                    dst.append('\\');
                }
                dst.append(current);
                escaped = false;
                continue;
            }
            if (current == '\"') {
                ++pos;
                break;
            }
            if (current == '\\') {
                escaped = true;
                continue;
            }
            if (current == '\r' || current == '\n') continue;
            dst.append(current);
        }
        cursor.updatePos(pos);
    }
}

