package br.com.jurix.querydsql.descriptor;


import com.querydsl.core.JoinType;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.SimpleExpression;

public class JoinDescriptor<E, Q extends SimpleExpression <? super E>> {

    public final EntityPath path;
    public final EntityPath alias;
    public final ListPath<E, Q> listPath;
    public final JoinType type;

    private JoinDescriptor(EntityPath path, JoinType type) {

        this.path = path;
        this.type = type;
        this.alias = null;
        this.listPath = null;
    }

    private JoinDescriptor(EntityPath path, JoinType type, EntityPath alias) {
        this.path = path;
        this.type = type;
        this.alias = alias;
        this.listPath = null;
    }

    private JoinDescriptor(ListPath<E, Q> listPath, JoinType type) {

        this.listPath = listPath;
        this.type = type;
        this.alias = null;
        this.path = null;
    }
    
    public static <E,Q extends SimpleExpression<? super E>> JoinDescriptor innerJoin(ListPath<E,Q> listPath){
        return new JoinDescriptor(listPath, JoinType.INNERJOIN);
    }

    public static JoinDescriptor innerJoin(EntityPath path) {

        return new JoinDescriptor(path, JoinType.INNERJOIN);
    }

    public static JoinDescriptor innerJoin(EntityPath path, EntityPath alias) {
        return new JoinDescriptor(path, JoinType.INNERJOIN, alias);
    }

    public static JoinDescriptor join(EntityPath path) {

        return new JoinDescriptor(path, JoinType.JOIN);
    }

    public static JoinDescriptor leftJoin(EntityPath path) {

        return new JoinDescriptor(path, JoinType.LEFTJOIN);
    }

    public static JoinDescriptor leftJoin(EntityPath path, EntityPath alias) {

        return new JoinDescriptor(path, JoinType.LEFTJOIN, alias);
    }

    public static JoinDescriptor join(EntityPath path, EntityPath alias) {

        return new JoinDescriptor(path, JoinType.JOIN, alias);
    }


    public static JoinDescriptor rightJoin(EntityPath path) {

        return new JoinDescriptor(path, JoinType.RIGHTJOIN);
    }
}
