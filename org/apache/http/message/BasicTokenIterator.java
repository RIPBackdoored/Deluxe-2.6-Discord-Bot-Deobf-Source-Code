/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.ParseException;
import org.apache.http.TokenIterator;
import org.apache.http.util.Args;

public class BasicTokenIterator
implements TokenIterator {
    public static final String HTTP_SEPARATORS = " ,;=()<>@:\\\"/[]?{}\t";
    protected final HeaderIterator headerIt;
    protected String currentHeader;
    protected String currentToken;
    protected int searchPos;

    public BasicTokenIterator(HeaderIterator headerIterator) {
        this.headerIt = Args.notNull(headerIterator, "Header iterator");
        this.searchPos = this.findNext(-1);
    }

    @Override
    public boolean hasNext() {
        if (this.currentToken == null) return false;
        return true;
    }

    @Override
    public String nextToken() throws NoSuchElementException, ParseException {
        if (this.currentToken == null) {
            throw new NoSuchElementException("Iteration already finished.");
        }
        String result = this.currentToken;
        this.searchPos = this.findNext(this.searchPos);
        return result;
    }

    @Override
    public final Object next() throws NoSuchElementException, ParseException {
        return this.nextToken();
    }

    @Override
    public final void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Removing tokens is not supported.");
    }

    protected int findNext(int pos) throws ParseException {
        int from = pos;
        if (from < 0) {
            if (!this.headerIt.hasNext()) {
                return -1;
            }
            this.currentHeader = this.headerIt.nextHeader().getValue();
            from = 0;
        } else {
            from = this.findTokenSeparator(from);
        }
        int start = this.findTokenStart(from);
        if (start < 0) {
            this.currentToken = null;
            return -1;
        }
        int end = this.findTokenEnd(start);
        this.currentToken = this.createToken(this.currentHeader, start, end);
        return end;
    }

    protected String createToken(String value, int start, int end) {
        return value.substring(start, end);
    }

    protected int findTokenStart(int pos) {
        int from = Args.notNegative(pos, "Search position");
        boolean found = false;
        do {
            int to;
            if (!found && this.currentHeader != null) {
                to = this.currentHeader.length();
            } else {
                if (!found) return -1;
                int n = from;
                return n;
            }
            while (!found && from < to) {
                char ch = this.currentHeader.charAt(from);
                if (this.isTokenSeparator(ch) || this.isWhitespace(ch)) {
                    ++from;
                    continue;
                }
                if (!this.isTokenChar(this.currentHeader.charAt(from))) throw new ParseException("Invalid character before token (pos " + from + "): " + this.currentHeader);
                found = true;
            }
            if (found) continue;
            if (this.headerIt.hasNext()) {
                this.currentHeader = this.headerIt.nextHeader().getValue();
                from = 0;
                continue;
            }
            this.currentHeader = null;
        } while (true);
    }

    protected int findTokenSeparator(int pos) {
        int from = Args.notNegative(pos, "Search position");
        boolean found = false;
        int to = this.currentHeader.length();
        while (!found) {
            if (from >= to) return from;
            char ch = this.currentHeader.charAt(from);
            if (this.isTokenSeparator(ch)) {
                found = true;
                continue;
            }
            if (!this.isWhitespace(ch)) {
                if (!this.isTokenChar(ch)) throw new ParseException("Invalid character after token (pos " + from + "): " + this.currentHeader);
                throw new ParseException("Tokens without separator (pos " + from + "): " + this.currentHeader);
            }
            ++from;
        }
        return from;
    }

    protected int findTokenEnd(int from) {
        Args.notNegative(from, "Search position");
        int to = this.currentHeader.length();
        int end = from + 1;
        while (end < to) {
            if (!this.isTokenChar(this.currentHeader.charAt(end))) return end;
            ++end;
        }
        return end;
    }

    protected boolean isTokenSeparator(char ch) {
        if (ch != ',') return false;
        return true;
    }

    protected boolean isWhitespace(char ch) {
        if (ch == '\t') return true;
        if (Character.isSpaceChar(ch)) return true;
        return false;
    }

    protected boolean isTokenChar(char ch) {
        if (Character.isLetterOrDigit(ch)) {
            return true;
        }
        if (Character.isISOControl(ch)) {
            return false;
        }
        if (!this.isHttpSeparator(ch)) return true;
        return false;
    }

    protected boolean isHttpSeparator(char ch) {
        if (HTTP_SEPARATORS.indexOf(ch) < 0) return false;
        return true;
    }
}

