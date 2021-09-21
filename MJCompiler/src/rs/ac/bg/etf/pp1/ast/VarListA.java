// generated with ast extension for cup
// version 0.8
// 14/8/2021 18:41:0


package rs.ac.bg.etf.pp1.ast;

public class VarListA extends VarList {

    private VarList VarList;
    private ArrDecl ArrDecl;

    public VarListA (VarList VarList, ArrDecl ArrDecl) {
        this.VarList=VarList;
        if(VarList!=null) VarList.setParent(this);
        this.ArrDecl=ArrDecl;
        if(ArrDecl!=null) ArrDecl.setParent(this);
    }

    public VarList getVarList() {
        return VarList;
    }

    public void setVarList(VarList VarList) {
        this.VarList=VarList;
    }

    public ArrDecl getArrDecl() {
        return ArrDecl;
    }

    public void setArrDecl(ArrDecl ArrDecl) {
        this.ArrDecl=ArrDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarList!=null) VarList.accept(visitor);
        if(ArrDecl!=null) ArrDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarList!=null) VarList.traverseTopDown(visitor);
        if(ArrDecl!=null) ArrDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarList!=null) VarList.traverseBottomUp(visitor);
        if(ArrDecl!=null) ArrDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarListA(\n");

        if(VarList!=null)
            buffer.append(VarList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ArrDecl!=null)
            buffer.append(ArrDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarListA]");
        return buffer.toString();
    }
}
