/*
 * Copyright 1999-2011 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.durid.sql.dialect.mysql.parser;

import static org.durid.sql.parser.CharTypes.isFirstIdentifierChar;
import static org.durid.sql.parser.CharTypes.isIdentifierChar;
import static org.durid.sql.parser.LayoutCharacters.EOI;
import static org.durid.sql.parser.Token.LITERAL_CHARS;

import java.util.HashMap;
import java.util.Map;

import org.durid.sql.parser.Keywords;
import org.durid.sql.parser.Lexer;
import org.durid.sql.parser.NotAllowCommentException;
import org.durid.sql.parser.SQLParseException;
import org.durid.sql.parser.Token;

public class MySqlLexer extends Lexer {

    public final static Keywords DEFAULT_MYSQL_KEYWORDS;

    static {
        Map<String, Token> map = new HashMap<String, Token>();

        map.putAll(Keywords.DEFAULT_KEYWORDS.getKeywords());

        map.put("DUAL", Token.DUAL);
        map.put("FALSE", Token.FALSE);
        map.put("IDENTIFIED", Token.IDENTIFIED);
        map.put("IF", Token.IF);
        map.put("KILL", Token.KILL);

        map.put("LIMIT", Token.LIMIT);
        map.put("TRUE", Token.TRUE);

        DEFAULT_MYSQL_KEYWORDS = new Keywords(map);
    }

    public MySqlLexer(char[] input, int inputLength, boolean skipComment){
        super(input, inputLength, skipComment);
        super.keywods = DEFAULT_MYSQL_KEYWORDS;
    }

    public MySqlLexer(String input){
        super(input);
        super.keywods = DEFAULT_MYSQL_KEYWORDS;
    }

    public void scanVariable() {
        if (ch != '@' && ch != ':' && ch != '#' && ch != '$') {
            throw new SQLParseException("illegal variable");
        }

        mark = pos;
        bufPos = 1;

        if (charAt(pos + 1) == '@') {
            ch = charAt(++pos);
            bufPos++;
        }

        if (charAt(pos + 1) == '`') {
            ++pos;
            ++bufPos;
            char ch;
            for (;;) {
                ch = charAt(++pos);

                if (ch == '`') {
                    bufPos++;
                    ch = charAt(++pos);
                    break;
                } else if (ch == EOI) {
                    throw new SQLParseException("illegal identifier");
                }

                bufPos++;
                continue;
            }

            this.ch = charAt(pos);

            stringVal = subString(mark, bufPos);
            token = Token.VARIANT;
        } else if (charAt(pos + 1) == '{') {
            ++pos;
            ++bufPos;
            char ch;
            for (;;) {
                ch = charAt(++pos);

                if (ch == '}') {
                    bufPos++;
                    ch = charAt(++pos);
                    break;
                } else if (ch == EOI) {
                    throw new SQLParseException("illegal identifier");
                }

                bufPos++;
                continue;
            }

            this.ch = charAt(pos);

            stringVal = subString(mark, bufPos);
            token = Token.VARIANT;
        } else {
            for (;;) {
                ch = charAt(++pos);

                if (!isIdentifierChar(ch)) {
                    break;
                }

                bufPos++;
                continue;
            }
        }

        this.ch = charAt(pos);

        stringVal = subString(mark, bufPos);
        token = Token.VARIANT;
    }

    public void scanIdentifier() {
        final char first = ch;

        if (ch == '`') {

            mark = pos;
            bufPos = 1;
            char ch;
            for (;;) {
                ch = charAt(++pos);

                if (ch == '`') {
                    bufPos++;
                    ch = charAt(++pos);
                    break;
                } else if (ch == EOI) {
                    throw new SQLParseException("illegal identifier");
                }

                bufPos++;
                continue;
            }

            this.ch = charAt(pos);

            stringVal = subString(mark, bufPos);
            Token tok = keywods.getKeyword(stringVal);
            if (tok != null) {
                token = tok;
            } else {
                token = Token.IDENTIFIER;
            }
        } else {

            final boolean firstFlag = isFirstIdentifierChar(first);
            if (!firstFlag) {
                throw new SQLParseException("illegal identifier");
            }

            mark = pos;
            bufPos = 1;
            char ch;
            for (;;) {
                ch = charAt(++pos);

                if (!isIdentifierChar(ch)) {
                    break;
                }

                bufPos++;
                continue;
            }

            this.ch = charAt(pos);

            stringVal = addSymbol();
            Token tok = keywods.getKeyword(stringVal);
            if (tok != null) {
                token = tok;
            } else {
                token = Token.IDENTIFIER;
            }
        }
    }

    protected void scanString() {
        mark = pos;
        boolean hasSpecial = false;

        for (;;) {
            if (isEOF()) {
                lexError("unclosed.str.lit");
                return;
            }

            ch = charAt(++pos);

            if (ch == '\\') {
                scanChar();
                if (!hasSpecial) {
                    initBuff(bufPos);
                    arraycopy(mark + 1, buf, 0, bufPos);
                    hasSpecial = true;
                }

                switch (ch) {
                    case '\0':
                        putChar('\0');
                        break;
                    case '\'':
                        putChar('\'');
                        break;
                    case '"':
                        putChar('"');
                        break;
                    case 'b':
                        putChar('\b');
                        break;
                    case 'n':
                        putChar('\n');
                        break;
                    case 'r':
                        putChar('\r');
                        break;
                    case 't':
                        putChar('\t');
                        break;
                    case '\\':
                        putChar('\\');
                        break;
                    case 'Z':
                        putChar((char) 0x1A); // ctrl + Z
                        break;
                    default:
                        putChar(ch);
                        break;
                }
                scanChar();
            }

            if (ch == '\'') {
                scanChar();
                if (ch != '\'') {
                    token = LITERAL_CHARS;
                    break;
                } else {
                    initBuff(bufPos);
                    arraycopy(mark + 1, buf, 0, bufPos);
                    hasSpecial = true;
                    putChar('\'');
                    continue;
                }
            }

            if (!hasSpecial) {
                bufPos++;
                continue;
            }

            if (bufPos == buf.length) {
                putChar(ch);
            } else {
                buf[bufPos++] = ch;
            }
        }

        if (!hasSpecial) {
            stringVal = subString(mark + 1, bufPos);
        } else {
            stringVal = new String(buf, 0, bufPos);
        }
    }

    public void scanComment() {
        if (ch != '/' && ch != '-') {
            throw new IllegalStateException();
        }

        mark = pos;
        bufPos = 0;
        scanChar();

        // /*+ */
        if (ch == '*') {
            scanChar();
            bufPos++;

            while (ch == ' ') {
                scanChar();
                bufPos++;
            }

            boolean isHint = false;
            int startHintSp = bufPos + 1;
            if (ch == '!') {
                isHint = true;
                scanChar();
                bufPos++;
            }

            for (;;) {
                if (ch == '*' && charAt(pos + 1) == '/') {
                    bufPos += 2;
                    scanChar();
                    scanChar();
                    break;
                }

                scanChar();
                bufPos++;
            }

            if (isHint) {
                stringVal = subString(mark + startHintSp, (bufPos - startHintSp) - 1);
                token = Token.HINT;
            } else {
                stringVal = subString(mark, bufPos);
                token = Token.MULTI_LINE_COMMENT;
            }

            if (token != Token.HINT && !isAllowComment()) {
                throw new NotAllowCommentException();
            }

            return;
        }

        if (!isAllowComment()) {
            throw new NotAllowCommentException();
        }

        if (ch == '/' || ch == '-') {
            scanChar();
            bufPos++;

            for (;;) {
                if (ch == '\r') {
                    if (charAt(pos + 1) == '\n') {
                        bufPos += 2;
                        scanChar();
                        break;
                    }
                    bufPos++;
                    break;
                } else if (ch == EOI) {
                    break;
                }

                if (ch == '\n') {
                    scanChar();
                    bufPos++;
                    break;
                }

                scanChar();
                bufPos++;
            }

            stringVal = subString(mark + 1, bufPos);
            token = Token.LINE_COMMENT;
            return;
        }
    }
}
