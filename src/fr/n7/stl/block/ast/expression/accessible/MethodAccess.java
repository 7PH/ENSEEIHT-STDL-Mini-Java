package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.expression.DefinitionAccess;
import fr.n7.stl.block.ast.object.*;
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

import java.util.LinkedList;
import java.util.List;

public class MethodAccess extends DefinitionAccess implements Instruction, Expression {

    /* If a class */
    protected MethodDefinition methodDefinition;

    /* If an interface */
    protected Signature signatureDeclaration;

    protected List<Expression> parameters;

    public MethodAccess(Expression object, String name, List<Expression> parameters) {
        super(object, name);
        this.parameters = parameters;
    }
    
    public MethodAccess(String target, String name, List<Expression> parameters) {
        super(target, name);
        this.parameters = parameters;
    }

    @Override
    public boolean subResolve(HierarchicalScope<Declaration> scope) {

        // Resolve parameters
        for (Expression parameter: parameters) {
            if (! parameter.resolve(scope)) {
                Logger.error("The parameter " + parameter.toString() + " in " + this.name + " call on " + this.target + " could not be resolved.");
                return false;
            }
        }

        // Check if the method exists
        if (programDeclaration instanceof ClassDeclaration) {
            MethodDefinition tmp  = ((ClassDeclaration) programDeclaration).getMethodDefinitionsByMethodName(name, true).get(0);
            
            if (tmp.getAccessModifier() == AccessModifier.PUBLIC || tmp.getParent() == ((AbstractThisUse) scope.get("this")).programDeclaration ) {
            	this.methodDefinition = tmp;
            	return true;
            } else {
            	Logger.error("Could not access method " + this.name + " of class " + tmp.getParent().getName() + ". The method does not exist or is private.");
            	return false;
            }        


        } else if (programDeclaration instanceof InterfaceDeclaration) {
            // method exists on the interface?
            List<Signature> signatures = ((InterfaceDeclaration) programDeclaration).findSignature(name, parameters);
            if (signatures.size() == 0) {
                Logger.error("No such method: " + name);
                return false;
            }
            this.signatureDeclaration = signatures.get(0);
        } else {
            Logger.error("Method " + name + " does not exist in " + programDeclaration.getName() + ".");
            return false;
        }

        // Verify number of parameters
    	List<ParameterDeclaration> declaredParameters = getSignature().getParameters();
    	int numOfPar = parameters.size();
        if (numOfPar != declaredParameters.size()) {
            Logger.error("Call of " + object + " but found " + numOfPar + " and expected " + declaredParameters.size() + ".");
            return false;
        }

        return true;
    }

    public Signature getSignature() {
        return methodDefinition != null ? methodDefinition.getSignature() : signatureDeclaration;
    }

    @Override
    protected Declaration getDeclaration() {
        return methodDefinition != null ? methodDefinition : signatureDeclaration;
    }

    @Override
    public Type getType() {
        return getDeclaration().getType();
    }

    @Override
    public boolean checkType() {
    	boolean b = true;
    	List<ParameterDeclaration> declaredParameters = getSignature().getParameters();
        int numOfPar = this.parameters.size();

    	// Verify if each parameters is of the well type
        for (int i = 0; i < numOfPar; i++) {
            Type parameterType = this.parameters.get(i).getType();
            Type declaredType = declaredParameters.get(i).getType();
            // Verify if a type is an ErrorType
            if (! parameterType.equalsTo(AtomicType.ErrorType)) {
                // Verify compatibility between declared and used type
                if (! parameterType.compatibleWith(declaredType)) {
                    Logger.error("Parameter " + parameterType.toString() + " use in "
                                + this.object + " method is not compatible with declared one : " + declaredParameters + ".");
                    b = false;
                }
            } else {
                Logger.error("Parameter " + parameterType.toString() + " use in " + this.object + "method is an ErrorType.");
                b = false;
            }
        }	
    	return b;
    }

    @Override
    public int allocateMemory(Register register, int offset) {
        return 0;
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();

        if (! methodDefinition.isStatic()) {

            if (declaration != null) {
                fragment.add(factory.createLoad(Register.SB, declaration.getOffset(), InstanceType.OBJECT_ADDR_LENGTH));
            } else {
                fragment.append(object.getCode(factory));
            }
        }

        for (Expression argument: parameters)
            fragment.append(argument.getCode(factory));

        fragment.add(factory.createPush(methodDefinition.getType().length()));
        fragment.add(factory.createCall(methodDefinition.getStartLabel(), Register.SB));

        int pop = methodDefinition.getParametersLength();
        if (! methodDefinition.isStatic())
            pop += 1;
        fragment.add(factory.createPop(methodDefinition.getType().length(), pop));

        return fragment;
    }

    @Override
    public Type getReturnType() {
        return getSignature().getType();
    }

    @Override
    public String toString() {
        return super.toString() + "(" + parameters + ")";
    }
}
