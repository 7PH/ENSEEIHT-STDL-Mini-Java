package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

import java.util.List;

public class ObjectAllocation implements Expression {

    private Type type;

    private List<Expression> parameters;

    public ObjectAllocation(Type type, List<Expression> parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> scope) {
    	// Resolve type part
    	if (!this.type.resolve(scope)) {
    		Logger.error("Could not resolve object instantiation because of the type " + this.type.toString() + ".");
    		return false;
    	}
    	
    	HierarchicalScope<Declaration> newScope = new SymbolTable(scope);
    	for (Expression e : parameters) {
    		if(!e.resolve(newScope)) {
    			Logger.error("Could not resolve object insantiation because the constructor parameter " + e.toString() + " could not be resolved.");
    			return false;
    		}
    	}
    	
    	return true;    	
    }

    @Override
    public Type getType() {
    	return this.type;
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
    	throw new SemanticsUndefinedException("getCode method is undefined for ObjectAllocation.");
    }

    @Override
    public String toString() {
        return "new " + type + "()";
    }
}
