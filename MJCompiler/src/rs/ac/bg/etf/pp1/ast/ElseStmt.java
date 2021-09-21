// generated with ast extension for cup
// version 0.8
// 14/8/2021 18:41:0


package rs.ac.bg.etf.pp1.ast;

public class ElseStmt extends ElseStatement {

    private ElseTerminal ElseTerminal;
    private StatementList StatementList;

    public ElseStmt (ElseTerminal ElseTerminal, StatementList StatementList) {
        this.ElseTerminal=ElseTerminal;
        if(ElseTerminal!=null) ElseTerminal.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public ElseTerminal getElseTerminal() {
        return ElseTerminal;
    }

    public void setElseTerminal(ElseTerminal ElseTerminal) {
        this.ElseTerminal=ElseTerminal;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ElseTerminal!=null) ElseTerminal.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ElseTerminal!=null) ElseTerminal.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ElseTerminal!=null) ElseTerminal.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ElseStmt(\n");

        if(ElseTerminal!=null)
            buffer.append(ElseTerminal.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ElseStmt]");
        return buffer.toString();
    }
}
