package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {
	
	Struct currentType = null;
	Obj currentMethod = null;
	boolean errorDetected = false;
	int printCallCount = 0;
	int varDeclCount = 0;
	int nVars;
	
    Logger log = Logger.getLogger(getClass());
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public boolean passed()
	{
		return !errorDetected;
	}
	
	//====================================================visit()
	
	public void visit(ProgName progName) {
    	progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
    	Tab.openScope();
    }
    
    public void visit(Program program){
    	nVars = Tab.currentScope.getnVars();
    	Tab.chainLocalSymbols(program.getProgName().obj);
    	Tab.closeScope();
    	//report_info("posecen program" , program);
    }
    
    public void visit(Type type){
    	Obj typeNode = Tab.find(type.getTypeName());
    	if(typeNode == Tab.noObj){
    		report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola! ", null);
    		currentType = Tab.noType;
    	}else{
    		if(Obj.Type == typeNode.getKind()){
    			currentType = typeNode.getType();
    		}else{
    			report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip!", type);
    			currentType = Tab.noType;
    		}
    	}
    }
    
    public void visit(VarDecl varDecl){
		Obj v = Tab.find(varDecl.getVarName());
		if(v!=Tab.noObj)
		{
			report_error("Ime: " + varDecl.getVarName() + " je vec definisano ", null);
		}else {
			varDeclCount++;
			Obj varNode = Tab.insert(Obj.Var, varDecl.getVarName(), currentType);
			report_info("Deklarisana promenljiva "+ varDecl.getVarName() + " tipa " + varNode.getKind(), varDecl);
		}
	}
	
	public void visit (ArrayDecl arrayDecl) {
        Obj a = Tab.find(arrayDecl.getArrName());
		
		if(a!=Tab.noObj)
		{
			report_error("Ime: " + arrayDecl.getArrName() + " je vec definisano ", null);
		}else {
			varDeclCount++;
			Obj varNode = Tab.insert(Obj.Var, arrayDecl.getArrName(), new Struct(Struct.Array , currentType));
			report_info("Deklarisana promenljiva niza "+ arrayDecl.getArrName(), arrayDecl);
		}
		
	}
	
	public void visit(SingleNumConst singleNumConst) {
		if(currentType.getKind()==Struct.Int)
		{
			Obj newConst = Tab.insert(Obj.Con, singleNumConst.getConstName(), currentType);
			newConst.setAdr(singleNumConst.getConstValue());
			//report_info("Deklarisana konstanta "+ singleNumConst.getConstName(), singleNumConst);
		}else
		{
			report_error("Ime: " + singleNumConst.getConstName() + " nije konstanta ", null);
		}
	}
	
	public void visit(SingleCharConst singleCharConst) {
		if(currentType.getKind()==Struct.Char)
		{
			Obj newConst = Tab.insert(Obj.Con, singleCharConst.getCharName(), currentType);
			newConst.setAdr(singleCharConst.getCharValue()); 
		}else
		{
			report_error("Ime: " + singleCharConst.getCharName() + " nije konstanta ", null);
		}
	}
	
	public void visit(SingleBoolConst singleBoolConst) {
		int x=0;
		if(currentType.getKind()==Struct.Bool)
		{   
			report_info("Bool prepoznat " + singleBoolConst.getBoolValue(), singleBoolConst);
			Obj newConst = Tab.insert(Obj.Con, singleBoolConst.getBoolName(), currentType);
			if(singleBoolConst.getBoolValue().equals("true")) x=1;
			else x=0;
			newConst.setAdr(x);
		}else
		{
			report_error("Ime: " + singleBoolConst.getBoolName() + " nije konstanta ", null);
		}
	}
	
	public void visit(MethodTypeName methodTypeName){
    	currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethName(), Tab.noType);
    	methodTypeName.obj = currentMethod;
    	Tab.openScope();
		report_info("Obradjuje se funkcija " + methodTypeName.getMethName(), methodTypeName);
    }
	
	public void visit(MethodDecl methodDecl){
  
    	Tab.chainLocalSymbols(currentMethod);
    	Tab.closeScope();
    	
    	currentMethod = null;
    }
	
	  /*public void visit(FuncCall funcCall){
			Obj func = funcCall.getDesignator().obj;
			if(Obj.Meth == func.getKind()){
				report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + funcCall.getLine(), null);
				funcCall.struct = func.getType();
			}else{
				report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " nije funkcija!", null);
				funcCall.struct = Tab.noType;
				}
    		}*/
	
	//=================================================Designators
	public void visit(SingleDesignator designator){
    	Obj obj = Tab.find(designator.getName());
    	if(obj == Tab.noObj){
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+designator.getName()+" nije deklarisano! ", null);
			return;
    	}
    	designator.obj = obj;
    }
    
    public void visit(ArrayDesignator designatorArr)
	{
		Obj designator = Tab.find(designatorArr.getDesignatorArr().getDesignator().obj.getName());
		if(designator == Tab.noObj)
		{
			report_error("Greska, ime niza nije definisano", designatorArr);
			return;
		}
		
		if(designator.getType().getKind() != Struct.Array)
		{
			report_error("Ovaj designator mora biti tipa array", designatorArr);
			return;
		}
		designatorArr.obj = new Obj(Obj.Elem, "ArrElem", designator.getType().getElemType());
		//report_info("Posecen identifikator niza ", designatorArr);
		//!!!
	}
    
    public void visit(AssignDesign assign){
    	if(!assign.getExpr().struct.assignableTo(assign.getDesignator().obj.getType()))
		{
			report_error("Expr mora biti kompatibilan sa designator", assign);
		}
    }
    
    public void visit(TermS term){
    	term.struct = term.getFactor().struct;
    }
    
    public void visit(ExprS termExpr){
    	termExpr.struct = termExpr.getTerm().struct;
    }
    
    public void visit(ExprC exprC){
    	Struct a = exprC.getExpr().struct;
    	Struct b = exprC.getTerm().struct;
    	if(a.equals(b) && a == Tab.intType){
    		exprC.struct = a;
    	}else{
			report_error("Greska na liniji "+ exprC.getLine()+" : nekompatibilni tipovi u izrazu.", null);
			exprC.struct = Tab.noType;
    	}
    }
    
    public void visit(TermC termC){
    	Struct a = termC.getTerm().struct;
    	Struct b = termC.getFactor().struct;
    	if(a.equals(b) && a == Tab.intType){
    		termC.struct = a;
    	}else{
			report_error("Greska na liniji "+ termC.getLine()+" : nekompatibilni tipovi u izrazu.", null);
			termC.struct = Tab.noType;
    	}
    }
    
    //===============================================================Factors
    
    public void visit(DesignFactor designFactor)
	{
		designFactor.struct = designFactor.getDesignator().obj.getType();
	}
    
    public void visit(NumFactor numFactor)
	{
		numFactor.struct = Tab.intType;
	}
	//!!!
	public void visit(BoolFactor boolFactor)
	{
		boolFactor.struct = BooleanType.boolType; 
	}
	
	public void visit(CharFactor charFactor)
	{
		charFactor.struct = Tab.charType;
	}
	
	public void visit(DesignatorINC inc)
	{
		int d = inc.getDesignator().obj.getKind();
		if(d!= Obj.Var && d!= Obj.Elem )
		{
			report_error("Designator mora biti promenljiva ili niz", inc);
		}
		
		if(inc.getDesignator().obj.getType() != Tab.intType)
		{
			report_error("Designator mora biti tipa int", inc);
		}
	}
	
	public void visit(DesignatorDEC dec)
	{
		int d = dec.getDesignator().obj.getKind();
		if(d != Obj.Var && d != Obj.Elem )
		{
			report_error("Designator mora biti promenljiva ili niz", dec);
		}
		
		if(dec.getDesignator().obj.getType() != Tab.intType)
		{
			report_error("Designator mora biti tipa int", dec);
		}
	}
	
	public void visit(NewFactor newFactor)
	{
		if(newFactor.getExpr().struct !=Tab.intType)
		{
			report_error("Expr mora biti tipa int ", newFactor);
			return;
		}
		newFactor.struct = new Struct(Struct.Array, currentType);
	}
	
	public void visit(ExprFactor exprFactor)
	{
		exprFactor.struct = exprFactor.getExpr().struct;
	}
	
	//=============================================Print, Read
	
	public void visit(PrintStatementNoArg printStmt)
	{
		if(printStmt.getExpr().struct != Tab.intType &&
				printStmt.getExpr().struct != Tab.charType &&
						printStmt.getExpr().struct != BooleanType.boolType
				)
		{
			report_error("Designator mora biti tipa int, char, bool", printStmt);
		}else
		{
			printCallCount++;
		}
	}
	
	public void visit(PrintStatementWArgs printStmt)
	{
		if(printStmt.getExpr().struct != Tab.intType &&
				printStmt.getExpr().struct != Tab.charType &&
						printStmt.getExpr().struct != BooleanType.boolType
				)
		{
			report_error("Designator mora biti tipa int, char, bool", printStmt);
		}else
		{
			printCallCount++;
		}
	}
	
	public void visit(ReadStatement readStmt)
	{
		int x = readStmt.getDesignator().obj.getKind();
		if(x!= Obj.Var && x!=Obj.Elem)
		{
			report_error("Designator mora biti promenljiva ili niz", readStmt);
		}
		
		if(readStmt.getDesignator().obj.getType() != Tab.intType &&
				readStmt.getDesignator().obj.getType() != Tab.charType &&
				readStmt.getDesignator().obj.getType() != BooleanType.boolType
				)
		{
			report_error("Designator mora biti tipa int, char, bool", readStmt);
		}
	}
	//=========================================================Conditions
	public void visit(ComplexCondFact ccf)
	{
		Struct a = ccf.getExpr().struct;
		Struct b = ccf.getExpr().struct;
		
		if(!a.compatibleWith(b))
		{
			report_error("Tipovi u if-u nisu kompatibilni", ccf);
			ccf.struct = Tab.noType;
			return;
		}
		
		ccf.struct = BooleanType.boolType;
		
	}
	//!!!!!
    public void visit(OneCondFact oneCondFact) {
		
		if(oneCondFact.getExpr().struct !=BooleanType.boolType)
		{
			report_error("Izraz u confact mora biti bool", oneCondFact);
			oneCondFact.struct = Tab.noType;
			return;
		}
		oneCondFact.struct = oneCondFact.getExpr().struct;
	}
    
   public void visit(ComplexCondTerm cct) {
		Struct cond1 = cct.getCondTerm().struct;
		Struct cond2 = cct.getCondFact().struct;
		
		if(!(cond1.getKind() == BooleanType.boolType.getKind() && cond2.getKind() == BooleanType.boolType.getKind())) {
			report_error("Tipovi uslova moraju biti bool tipa ", cct);
			cct.struct = Tab.noType;
		}
		else {
			cct.struct = BooleanType.boolType;
		}
	}
	
	public void visit(OneCondTerm condTerm) {
		condTerm.struct = condTerm.getCondFact().struct;
	}
	
	/*public void visit(AllCondTerm condTerms) {
		condTerms.struct = condTerms.getCondTerm().struct;
	}*/
	
	public void visit(ComplexCond cond) {
		Struct cond1Type = cond.getCondition().struct;
		Struct cond2Type = cond.getAllCondTerm().getCondTerm().struct; //condterm
		
		if(cond1Type.getKind() == BooleanType.boolType.getKind() && cond2Type.getKind() == BooleanType.boolType.getKind()) {
			//report_info("Ispitan uslov u if naredbi ", cond);
			cond.struct = BooleanType.boolType;
		}
		else {
			cond.struct = Tab.noType;
			report_error("Izraz u if uslovu mora biti bool tipa", cond);
		}
	}
	
	public void visit(AllConditions allConditions) {
		allConditions.struct = allConditions.getCondition().struct;
	}
	
	public void visit(OneCond cond) {
		if(cond.getAllCondTerm().getCondTerm().struct.getKind() != BooleanType.boolType.getKind()) { //cond.getCondTerm
			report_error("Uslov u if naredbi mora biti bool tipa", cond);
			cond.struct = Tab.noType;
		}
		else {
			//report_info("Ispitan je uslov u if naredbi ", cond);
			cond.struct = BooleanType.boolType;
		}
	}
	
	
	

}
