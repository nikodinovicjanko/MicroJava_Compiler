// generated with ast extension for cup
// version 0.8
// 14/8/2021 18:41:0


package rs.ac.bg.etf.pp1.ast;

public class ArrayDecl extends ArrDecl {

    private String arrName;

    public ArrayDecl (String arrName) {
        this.arrName=arrName;
    }

    public String getArrName() {
        return arrName;
    }

    public void setArrName(String arrName) {
        this.arrName=arrName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ArrayDecl(\n");

        buffer.append(" "+tab+arrName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ArrayDecl]");
        return buffer.toString();
    }
}
