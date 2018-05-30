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
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

import javax.lang.model.type.ErrorType;
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

    	// Resolve type part
    	if (! type.resolve(scope)) {
    		Logger.error("Could not resolve object instantiation because of the type " + this.type.toString() + ".");
    		return false;
        }

        // resolving parameters
        for (Expression parameter: parameters)
            if (! parameter.resolve(scope))
                return false;

        // Verify that the object allocation is about a class
        if (! (type instanceof InstanceType)) {
            Logger.error("Trying to instantiate non-class type");
            return false;
        }

        if (! (((InstanceType) type).getDeclaration() instanceof ClassDeclaration)) {
            Logger.error("Trying to instantiate an interface: " + type.toString());
            return false;
        }

    	HierarchicalScope<Declaration> newScope = new SymbolTable(scope);
    	for (Expression expression : parameters) {
    		if(! expression.resolve(newScope))
    			return false;
    	}
    	
    	return true;
    }

    private List<MethodDefinition> getConstructors() {
        return ((ClassDeclaration)((InstanceType)type).getDeclaration()).getConstructors();
    }

    @Override
    public Type getType() {

        // parameters ok?
        for (Expression parameter: parameters)
            if (parameter.getType().equalsTo(AtomicType.ErrorType))
                return AtomicType.ErrorType;

        // Check if a constructor is present
        List<MethodDefinition> constructors = getConstructors();
        if (constructors.size() == 0) {
            Logger.error("No constructor for class " + type);
            return AtomicType.ErrorType;
        }

        // Check that the parameters
        for (MethodDefinition constructor: constructors) {
            List<ParameterDeclaration> declaredParam = constructor.getSignature().getParameters();
            if (declaredParam.size() != parameters.size()) {
                Logger.error(constructor.getName() + " expected " + declaredParam.size() + " arguments, " + parameters.size() + " given.");
                return AtomicType.ErrorType;
            }

            for (int i = 0; i < parameters.size(); i ++) {
                Type parameterType = parameters.get(i).getType();
                Type declaredType = declaredParam.get(i).getType();

                // Verify compatibility between declared and used type
                if (! parameterType.compatibleWith(declaredType)) {
                    Logger.error("Parameter '" + parameterType + "' not compatible with '" +  declaredParam.get(i) + "' in '" + constructor.getSignature() + "'");
                    return AtomicType.ErrorType;
                }
            }
        }

        return type;
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
        
        // Load class attributes
        ClassDeclaration cd = (ClassDeclaration) ((InstanceType)type).getDeclaration();
        int classSize = cd.getAllAttributesSizes();

        // Allocate memory
        fragment.add(factory.createLoadL(classSize)); // Push the value of the size required by attributes on the pile
        fragment.add(Library.MAlloc); // Pop this value which is on the top to allocate size in the heap
        // Adress of allocation is on the top of the pile
        // @TODO Load the good class constructor

        return fragment;
    }

    @Override
    public String toString() {
        return "new " + type + "()";
    }
}
