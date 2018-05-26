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
    		b &= false;
        }
        // Verify that the object allocation is about a class
        if (this.type instanceof InstanceType) {
            if (! (((InstanceType) this.type).getDeclaration() instanceof ClassDeclaration)) {
                Logger.error("Could not instantiate " + this.type.toString() + ", it is an interface.");
    		    b &= false;
            }
        }

        // Check if a constructor is present
        List<Constructor> listeConstructors = this.getConstructors();
        if (listeConstructors.size() == 0) {
            Logger.error("You need to define a constructor before using one.");
            b &= false;
        }

        // Check that the parameters
        for (Constructor c : listeConstructors) {
            List<ParameterDeclaration> declaredParam = c.getSignature().getParameters();
            int numOfPar = this.parameters.size();
            if (declaredParam.size() != numOfPar) {
                Logger.error("Call of " + c.getName() + " but found " + numOfPar + " and expected " + declaredParam.size() + ".");
                b &= false;
            } else {
                for (int i = 0; i < numOfPar; i++) {
                    Type parameterType = this.parameters.get(i).getType();
                    Type declaredType = declaredParam.get(i).getType();
                    // Verify if a type is an ErrorType
                    if (!parameterType.equalsTo(AtomicType.ErrorType)) {
                        // Verify compatibility between declared and used type
                        b &= parameterType.compatibleWith(declaredType);
                        if (!b) {
                            Logger.error("Parameter " + parameterType.toString() + " use in "
                                    + c.getName() + " method is not compatible with declared one : " + declaredParam + ".");
                        }
                    } else {
                        Logger.error("Parameter " + parameterType.toString() + " use in " + c.getName() + "method is an ErrorType.");
                        b &= false;
                    }
                }	
            }
        }

    	HierarchicalScope<Declaration> newScope = new SymbolTable(scope);
    	for (Expression e : parameters) {
    		if(!e.resolve(newScope)) {
    			Logger.error("Could not resolve object insantiation because the constructor parameter " + e.toString() + " could not be resolved.");
    			b &= false;
    		}
    	}
    	
    	return b;    	
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

    public List<Constructor>  getConstructors() {
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
