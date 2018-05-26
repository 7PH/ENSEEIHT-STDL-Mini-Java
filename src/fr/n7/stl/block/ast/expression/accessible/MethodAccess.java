package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.object.ClassDeclaration;
import fr.n7.stl.block.ast.object.InstanceType;
import fr.n7.stl.block.ast.object.InterfaceDeclaration;
import fr.n7.stl.block.ast.object.MethodDefinition;
import fr.n7.stl.block.ast.object.ProgramDeclaration;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

import java.util.List;

public class MethodAccess implements Instruction, Expression {
	
	private String name;

    private Expression objectIdentifier;

    private String method;
    
    private MethodDefinition methodDefinition;

    private List<Expression> parameters;

    private InstanceType objectType;

    public MethodAccess(Expression object, String method, List<Expression> parameters) {
        this.objectIdentifier = object;
        this.method = method;
        this.parameters = parameters;
    }
    
    public MethodAccess(String identifier, String method, List<Expression> parameters) {
        this.name = identifier;
        this.method = method;
        this.parameters = parameters;
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> scope) {
    	
        // Verify objet resolve.
    	if (! objectIdentifier.resolve(scope)) {
            Logger.error("Could not resolve attribute assignment because of: " + objectIdentifier + ".");
            return false;
        }

        // Resolve parameters
        for (Expression parameter: parameters) {
            if (!parameter.resolve(scope)) {
            	Logger.error("The parameter " + parameter.toString() + " in " + this.method + " call on " + this.name + " could not be resolved.");
            	return false;
            }
        }

        // Get the objet type and check that is a InstanceType
        Type type = objectIdentifier.getType();

        if (! (type instanceof InstanceType)) {
            Logger.error(objectIdentifier + " is not a InstanceType.");
            return false;
        }
       
        // Check if the method exist
        ProgramDeclaration programDeclaration = objectType.getDeclaration();
        if (programDeclaration instanceof ClassDeclaration) {
            this.methodDefinition = ((ClassDeclaration) programDeclaration).getMethodDefinitionBySignature(name);
        } else {
            // TODO
            this.methodDefinition = null;
            
        }
        if (this.methodDefinition == null) {
            Logger.error("Method " + this.name + " does not exist in " + programDeclaration.getName() + ".");
            return false;
        }

        // Verify number of parameters
    	List<ParameterDeclaration> declaredParameters = this.methodDefinition.getSignature().getParameters();
    	int numOfPar = this.parameters.size();
        if (numOfPar != declaredParameters.size()) {
            Logger.error("Call of " + this.objectIdentifier + " but found " + numOfPar + " and expected " + declaredParameters.size() + ".");
            return false;
        }

        // Define a new scope for it
    	HierarchicalScope<Declaration> newScope = new SymbolTable(scope);

    	if (!this.methodDefinition.resolve(newScope)) {
        	Logger.error("The method " + this.methodDefinition.getName() + " call could not be resolved.");
        	return false;    		
        }
        return true;
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
        
    	// Verify if each parameters is of the well type
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
                b &= false;
            }
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
