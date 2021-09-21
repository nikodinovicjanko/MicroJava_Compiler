// generated with ast extension for cup
// version 0.8
// 14/8/2021 18:41:0


package rs.ac.bg.etf.pp1.ast;

public class ComplexCond extends Condition {

    private Condition Condition;
    private AllCondTerm AllCondTerm;

    public ComplexCond (Condition Condition, AllCondTerm AllCondTerm) {
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.AllCondTerm=AllCondTerm;
        if(AllCondTerm!=null) AllCondTerm.setParent(this);
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public AllCondTerm getAllCondTerm() {
        return AllCondTerm;
    }

    public void setAllCondTerm(AllCondTerm AllCondTerm) {
        this.AllCondTerm=AllCondTerm;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Condition!=null) Condition.accept(visitor);
        if(AllCondTerm!=null) AllCondTerm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(AllCondTerm!=null) AllCondTerm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(AllCondTerm!=null) AllCondTerm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ComplexCond(\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AllCondTerm!=null)
            buffer.append(AllCondTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ComplexCond]");
        return buffer.toString();
    }
}
