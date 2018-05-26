package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

import java.util.LinkedList;
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
        boolean b = true;

    	// Resolve type part
    	if (!this.type.resolve(scope)) {
    		Logger.error("Could not resolve object instantiation because of the type " + this.type.toString() + ".");
    		b = false;
        }

        // Verify that the object allocation is about a class
        if (this.type instanceof InstanceType) {
            if (! (((InstanceType) this.type).getDeclaration() instanceof ClassDeclaration)) {
                Logger.error("Could not instantiate " + this.type.toString() + ", it is an interface.");
    		    b = false;
            }
        }

        // @TODO resolve in new scope?
        for (Expression parameter: parameters) {
    	    parameter.resolve(scope);
        }

    	HierarchicalScope<Declaration> newScope = new SymbolTable(scope);
    	for (Expression e : parameters) {
    		if(! e.resolve(newScope)) {
    			Logger.error("Could not resolve object insantiation because the constructor parameter " + e.toString() + " could not be resolved.");
    			b = false;
    		}
    	}
    	
    	return b;    	
    }

    @Override
    public Type getType() {

        // Check if a constructor is present
        List<Constructor> listeConstructors = this.getConstructors();
        if (listeConstructors.size() == 0) {
            Logger.error("You need to define a constructor before using one.");
            return AtomicType.ErrorType;
        }

        // Check that the parameters
        for (Constructor c : listeConstructors) {
            List<ParameterDeclaration> declaredParam = c.getSignature().getParameters();
            if (declaredParam.size() != parameters.size()) {
                Logger.error(c.getName() + " expected " + declaredParam.size() + " arguments, " + parameters.size() + " given.");
                return AtomicType.ErrorType;
            } else {
                for (int i = 0; i < parameters.size(); i ++) {
                    Type parameterType = parameters.get(i).getType();
                    Type declaredType = declaredParam.get(i).getType();

                    // Verify if a type is an ErrorType
                    if (! parameterType.equalsTo(AtomicType.ErrorType)) {
                        // Verify compatibility between declared and used type
                        if (! parameterType.compatibleWith(declaredType)) {
                            Logger.error("Parameter " + parameterType + " use in " + c.getName() + " method is not compatible with declared one : " + declaredParam + ".");
                            return AtomicType.ErrorType;
                        }
                    } else {
                        Logger.error("Parameter " + parameterType + " use in " + c.getName() + "method is an ErrorType.");
                        return AtomicType.ErrorType;
                    }
                }
            }
        }

        return type;
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
    	throw new SemanticsUndefinedException("getCode method is undefined for ObjectAllocation.");
    }

    @Override
    public String toString() {
        return "new " + type + "()";
    }

    public List<Constructor> getConstructors() {
        List<Constructor> constructorList = new LinkedList<>();
        if (this.type instanceof InstanceType) {
            InstanceType it = (InstanceType) this.type;
            if (it.getDeclaration() instanceof ClassDeclaration) {
                ClassDeclaration cd = (ClassDeclaration) it.getDeclaration();
                for (Definition d : cd.getDefinitions()) {
                    if (d instanceof Constructor) {
                        constructorList.add((Constructor) d);
                    }
                }
            }
        }
        return constructorList;
    }
}
