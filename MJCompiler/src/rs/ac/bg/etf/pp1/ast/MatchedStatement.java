// generated with ast extension for cup
// version 0.8
// 14/8/2021 18:41:0


package rs.ac.bg.etf.pp1.ast;

public class MatchedStatement extends Statement {

    private AllConditions AllConditions;
    private StatementList StatementList;
    private ElseStatement ElseStatement;

    public MatchedStatement (AllConditions AllConditions, StatementList StatementList, ElseStatement ElseStatement) {
        this.AllConditions=AllConditions;
        if(AllConditions!=null) AllConditions.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
        this.ElseStatement=ElseStatement;
        if(ElseStatement!=null) ElseStatement.setParent(this);
    }

    public AllConditions getAllConditions() {
        return AllConditions;
    }

    public void setAllConditions(AllConditions AllConditions) {
        this.AllConditions=AllConditions;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public ElseStatement getElseStatement() {
        return ElseStatement;
    }

    public void setElseStatement(ElseStatement ElseStatement) {
        this.ElseStatement=ElseStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AllConditions!=null) AllConditions.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
        if(ElseStatement!=null) ElseStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AllConditions!=null) AllConditions.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
        if(ElseStatement!=null) ElseStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AllConditions!=null) AllConditions.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        if(ElseStatement!=null) ElseStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MatchedStatement(\n");

        if(AllConditions!=null)
            buffer.append(AllConditions.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ElseStatement!=null)
            buffer.append(ElseStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MatchedStatement]");
        return buffer.toString();
    }
}
