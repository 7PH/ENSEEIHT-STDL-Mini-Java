package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

import java.util.List;

public class MethodCall implements Instruction, Expression {
	
	private String identifier;

    private Expression objectIdentifier;

    private String method;
    
    private MethodDefinition methodDefinition;

    private List<Expression> parameters;

    public MethodCall(Expression object, String method, List<Expression> parameters) {
        this.objectIdentifier = object;
        this.method = method;
        this.parameters = parameters;
    }
    
    public MethodCall(String identifier, String method, List<Expression> parameters) {
        this.identifier = identifier;
        this.method = method;
        this.parameters = parameters;
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> _scope) {
        boolean resolved = true;
        for (Expression parameter: parameters)
            resolved &= parameter.resolve(_scope);
        // @TODO create new scope and resolve body
        return resolved;
    }

    @Override
    public Type getType() {
    	throw new SemanticsUndefinedException("getType method is undefined for MethodCall.");
    }

    @Override
    public boolean checkType() {
    	boolean b = false;
    	List<ParameterDeclaration> declaredParameters = this.methodDefinition.getSignature().getParameters();
    	int numOfPar = this.parameters.size();
    	
    	// Verify if the number of parameters is correct
    	if (numOfPar == declaredParameters.size()) {
    		for (int i = 0; i < numOfPar; i++) {
    			Type parameterType = this.parameters.get(i).getType();
    			Type declaredType = declaredParameters.get(i).getType();
    			// Verify if a type is an ErrorType
    			if (!parameterType.equalsTo(AtomicType.ErrorType)) {
    				// Verify compatibility between declared and used type
    				b &= parameterType.compatibleWith(declaredType);
    				if (!b) {
    				Logger.error("Parameter " + parameterType.toString() + " use in "
    							+ this.objectIdentifier + " method is not compatible with declared one : " + declaredParameters + ".");
    				}
    			} else {
    				Logger.error("Parameter " + parameterType.toString() + " use in " + this.objectIdentifier + "method is an ErrorType.");
    			}
    		}	
    	} else {
    		Logger.error("Call of " + this.objectIdentifier + " but found " + numOfPar + " and expected " + declaredParameters.size() + ".");
    	}
    	return b;
    }

    @Override
    public int allocateMemory(Register register, int offset) {
    	throw new SemanticsUndefinedException("allocateMemory method is undefined for MethodCall.");
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
    	throw new SemanticsUndefinedException("getCode method is undefined for MethodCall.");
    }

    @Override
    public Type getReturnType() {
    	throw new SemanticsUndefinedException("getReturnType method is undefined for MethodCall.");
    }

    @Override
    public String toString() {
        return this.objectIdentifier + "." + this.method + "(" + this.parameters + ");";
    }
}
