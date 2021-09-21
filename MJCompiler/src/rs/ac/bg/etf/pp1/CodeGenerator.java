package rs.ac.bg.etf.pp1;

import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class CodeGenerator extends VisitorAdaptor{
	
    private int mainPc;
	
	public int getMainPc(){
		return mainPc;
	}
	
	//====================================Method
		public void visit(MethodTypeName mname)//!!!!!!!!
		{
			if("main".equalsIgnoreCase(mname.getMethName()))
			{
				mainPc = Code.pc;
			}
			
			mname.obj.setAdr(Code.pc);
			Code.put(Code.enter);
			Code.put(0);
			Code.put(mname.obj.getLocalSymbols().size());
		}
		
		public void visit(MethodDecl methodDecl)
		{
			Code.put(Code.exit);
			Code.put(Code.return_);
		}
		
		//====================================Print,Read
		public void visit(PrintStatementNoArg printStmt)
		{
			//Code.loadConst(0);
			if(printStmt.getExpr().struct.equals(Tab.intType))
			{
				Code.loadConst(5);
				Code.put(Code.print);
			}else
			{
				Code.loadConst(1);
				Code.put(Code.bprint);
			}
		}
		
		public void visit(PrintStatementWArgs printWArgs) {
			Code.loadConst(printWArgs.getN2());
			if(printWArgs.getExpr().struct.equals(Tab.charType))
				Code.put(Code.bprint);
			else
				Code.put(Code.print);
			
		}
		
		public void visit(ReadStatement readStmt) {
			if(readStmt.getDesignator().obj.getType().equals(Tab.charType))
				Code.put(Code.bread);
			else
				Code.put(Code.read);
			Code.store(readStmt.getDesignator().obj);
		}
		
		//====================================Designator
		/*public void visit(SingleDesignator designator){
			
				Code.load(designator.obj);
			
		}*/
		public void visit(DesignatorArr designatorArr)
		{
			Code.load(designatorArr.getDesignator().obj);
		}
		
		public void visit(AssignDesign assignDesign){
			Code.store(assignDesign.getDesignator().obj);
		}
		
		public void visit(DesignatorINC inc)
		{
			if(inc.getDesignator().obj.getKind() ==Obj.Elem)
			{
				Code.put(Code.dup2);
			}
			
			Code.load(inc.getDesignator().obj);
			Code.loadConst(1);
			Code.put(Code.add);
			Code.store(inc.getDesignator().obj);
		}
		
		public void visit(DesignatorDEC dec)
		{
			if(dec.getDesignator().obj.getKind() ==Obj.Elem)
			{
				Code.put(Code.dup2);
			}
			Code.load(dec.getDesignator().obj);
			Code.loadConst(1);
			Code.put(Code.sub);
			Code.store(dec.getDesignator().obj);
		}

		//====================================Term
		public void visit(TermC termC) { //!!!
			if(termC.getMulop() instanceof MulOperator) {
				Code.put(Code.mul);
			}
			else if(termC.getMulop() instanceof DivOperator) {
				Code.put(Code.div);
			}
			else {
				Code.put(Code.rem);
			}
		}
		
		//====================================Expr
		public void visit(ExprS exprS)
		{
			if(exprS.getMinusOption() instanceof Negative)
			{
				Code.put(Code.neg);
			}
		}
		
		public void visit(ExprC exprC) {
			if(exprC.getAddop() instanceof PlusOperator ) {
				Code.put(Code.add);
			}
			else {
				Code.put(Code.sub);
			}
		}
		//====================================Factor
		public void visit(NumFactor numFactor)
		{
			Obj num = Tab.insert(Obj.Con, "$", numFactor.struct);
			num.setLevel(0);
			num.setAdr(numFactor.getN1());
			Code.load(num);
		}
		
		public void visit(CharFactor charFactor)
		{
			Obj ch = Tab.insert(Obj.Con, "$", charFactor.struct);
			ch.setLevel(0);
			ch.setAdr(charFactor.getC1());
			Code.load(ch);
		}
		
		public void visit(BoolFactor boolFactor) //!!!
		{
			Obj b = Tab.insert(Obj.Con, "$", boolFactor.struct);
			b.setLevel(0);
			int x = boolFactor.getB1().equalsIgnoreCase("true") ? 1 : 0;
			b.setAdr(x);
			Code.load(b);
		}
		
		public void visit(DesignFactor dessignFactor)
		{
			Code.load(dessignFactor.getDesignator().obj);
		}
		
		public void visit(NewFactor newFactor)
		{
			if(newFactor.struct.getKind()==Struct.Array)
			{
				Code.put(Code.newarray);
				if(newFactor.getType().struct == Tab.intType)
				{
					Code.put(1); //!!! proveri
				}else
				{
					Code.put(0);
				}
			}
		}
		
		
		
		//==============================RELOP
		private int getRelOp(Relop relop) {
			if(relop instanceof SameRelOp)
				return Code.eq;
			
			if(relop instanceof NotSameRelOp)
				return Code.ne;
			
			if(relop instanceof GreaterRelOp)
				return Code.gt;
			
			if(relop instanceof GEQRelOp)
				return Code.ge;
			
			if(relop instanceof LowerRelOp)
				return Code.lt;
			
			if(relop instanceof LEQRelOp)
				return Code.le;
			return 0;
		}
		
		//=============================Cond/IF
		
		Stack<Integer> skipCondTerm = new Stack<Integer>();
		Stack<Integer> skipCondition = new Stack<Integer>();
		Stack<Integer> skipTrue = new Stack<Integer>();
		Stack<Integer> skipElse = new Stack<Integer>();
	
		// 0 za adresu jer se kasnije sa fixup to prepravi
		public void visit(OneCondFact oneCond) {
			Code.loadConst(0); //stavlja nulu na stek
			Code.putFalseJump(Code.ne, 0); // proverava da li je jednako sa 0 i ako jeste preskace if
			skipCondTerm.push(Code.pc - 2); //pc od od mesta gde je skok postavljen
			//skipCondTerm
		}
		
		public void visit(ComplexCondFact complCond) {
			Code.putFalseJump(getRelOp(complCond.getRelop()), 0); //preskace if ako nije ispunjen uslov
			skipCondTerm.push(Code.pc - 2);
			
		}
		
		
		
		public void visit(AllCondTerm allCondTerms) { //OneCondTerm
			Code.putJump(0);// skok u if
			skipCondition.push(Code.pc - 2); 
			//old_pc = Code.pc - 2;//proveriti!!!!!!!!!
			while(!skipCondTerm.empty()) {
				Code.fixup(skipCondTerm.pop());
			}
			
			
		}
		
		
		
		public void visit(AllConditions cond) { //!! oneCond
			Code.putJump(0);//skok u else, u allCondTerms namesta na njega
			skipTrue.push(Code.pc - 2);
			
			while(!skipCondition.empty()) {
				Code.fixup(skipCondition.pop());
			}
		}
		
		public void visit(NoElseStmt noElseStatmt) {
			Code.fixup(skipTrue.pop());
		}
		
		public void visit(ElseTerminal elseTerminal) {
			Code.putJump(0);
			skipElse.push(Code.pc - 2);
			
			Code.fixup(skipTrue.pop());
		}
		
		public void visit(ElseStmt elseStmt) {
			Code.fixup(skipElse.pop());
		}
		
	

}
