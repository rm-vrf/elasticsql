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
package org.durid.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import org.durid.sql.ast.SQLExpr;
import org.durid.sql.ast.SQLObjectImpl;
import org.durid.sql.visitor.SQLASTVisitor;

public class SQLSelectGroupByClause extends SQLObjectImpl {

    private static final long   serialVersionUID = 1L;

    private final List<SQLExpr> items            = new ArrayList<SQLExpr>();
    private SQLExpr             having;

    public SQLSelectGroupByClause(){

    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.items);
            acceptChild(visitor, this.having);
        }

        visitor.endVisit(this);
    }

    public SQLExpr getHaving() {
        return this.having;
    }

    public void setHaving(SQLExpr having) {
        this.having = having;
    }

    public List<SQLExpr> getItems() {
        return this.items;
    }
}
