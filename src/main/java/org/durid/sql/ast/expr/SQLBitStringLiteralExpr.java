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
package org.durid.sql.ast.expr;

import java.util.BitSet;

import org.durid.sql.ast.SQLExprImpl;
import org.durid.sql.visitor.SQLASTVisitor;

/**
 * SQL-92
 * <p>
 * &ltbit string literal> ::= B &ltquote> [ &ltbit> ... ] &ltquote> [ { &ltseparator> ... &ltquote> [ &ltbit> ... ]
 * &ltquote> }... ]
 * </p>
 * 
 * @author WENSHAO
 */
public class SQLBitStringLiteralExpr extends SQLExprImpl implements SQLLiteralExpr {

    private static final long serialVersionUID = 1L;

    private BitSet            value;

    public SQLBitStringLiteralExpr(){

    }

    public BitSet getValue() {
        return value;
    }

    public void setValue(BitSet value) {
        this.value = value;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);

        visitor.endVisit(this);
    }

    public void output(StringBuffer buf) {
        buf.append("b'");
        for (int i = 0; i < value.length(); ++i) {
            buf.append(value);
        }
        buf.append("'");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SQLBitStringLiteralExpr other = (SQLBitStringLiteralExpr) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }
}
