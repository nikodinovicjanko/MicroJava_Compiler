// generated with ast extension for cup
// version 0.8
// 14/8/2021 18:41:0


package rs.ac.bg.etf.pp1.ast;

public class OneCond extends Condition {

    private AllCondTerm AllCondTerm;

    public OneCond (AllCondTerm AllCondTerm) {
        this.AllCondTerm=AllCondTerm;
        if(AllCondTerm!=null) AllCondTerm.setParent(this);
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
        if(AllCondTerm!=null) AllCondTerm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AllCondTerm!=null) AllCondTerm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AllCondTerm!=null) AllCondTerm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneCond(\n");

        if(AllCondTerm!=null)
            buffer.append(AllCondTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneCond]");
        return buffer.toString();
    }
}
