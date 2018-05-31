package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.expression.accessible.MethodAccess;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

import java.util.List;

public class ObjectAllocation extends MethodAccess {

    private Type type;

    private List<Expression> parameters;

    private ClassDeclaration classDeclaration;

    public ObjectAllocation(Type type, List<Expression> parameters) {
        super((type instanceof InstanceType) ? ((InstanceType) type).getName() : type.toString(), "", parameters);
        this.type = type;
        this.parameters = parameters;
    }

    @Override
    public boolean subResolve(HierarchicalScope<Declaration> scope) {

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

        classDeclaration = (ClassDeclaration) ((InstanceType) type).getDeclaration();
        methodDefinition = getConstructors().get(0);

    	return true;
    }

    private List<MethodDefinition> getConstructors() {
        return classDeclaration.getConstructors();
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
        MethodDefinition constructor = constructors.get(0);
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

        if (! checkClassGenericsMatch((InstanceType) type)) {
            return AtomicType.ErrorType;
        }

        return type;
    }

    public boolean checkClassGenericsMatch(InstanceType type) {
        if (classDeclaration == null) return true;
        List<InstanceType> typeInstantiations = type.getTypeInstantiations();
        List<GenericType> genericTypes = classDeclaration.getClassName().getGenerics();
        if (genericTypes.size() != typeInstantiations.size()) {
            Logger.error("Wrong number of generic type instantiated for class '" + classDeclaration.getName() + "' " + genericTypes.size() + " - " + typeInstantiations.size());
            return false;
        }
        for (int i = 0; i < typeInstantiations.size(); ++ i) {

            // @TODO: Remove the following comment

            // TODO : Verifier que les types generiques sont compatibles.
            // ex : si déclaré MyClass<T extends X>
            // declaration[i].compatibleWith(typeInstantiations[i])

            Type type2 = typeInstantiations.get(i);
            GenericType genericType = genericTypes.get(i);
            // 'type' doit "matcher" 'genericType'
            for (InstanceType constraint: genericType.getExtendedTypes()) {
                // 'type' doit être enfant de 'constraint'
                if (! type2.compatibleWith(constraint)) {
                    // nop
                    Logger.error("Unable to set '" + type2 + "' as generic type for '" + classDeclaration.getName() + "'");
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
        
        // Load class attributes
        ClassDeclaration cd = (ClassDeclaration) ((InstanceType)type).getDeclaration();
        int classSize = cd.getAllAttributesSizes();

        // Allocate memory
        fragment.add(factory.createLoadL(classSize));   // Push the value of the size required by attributes on the pile
        fragment.add(Library.MAlloc);                   // Pop this value which is on the top to allocate size in the heap
        // Adress of allocation is on the top of the pile
        fragment.add(factory.createLoad(Register.ST, -1, InstanceType.OBJECT_ADDR_LENGTH));

        for (Expression argument: parameters)
            fragment.append(argument.getCode(factory));

        fragment.add(factory.createCall(methodDefinition.getStartLabel(), Register.SB));
        fragment.add(factory.createPop(0, methodDefinition.getParametersLength() + 1));

        return fragment;
    }

    @Override
    public String toString() {
        return "new " + type + "()";
    }
}
