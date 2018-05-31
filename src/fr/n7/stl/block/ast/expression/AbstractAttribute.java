package fr.n7.stl.block.ast.expression;


import fr.n7.stl.block.ast.object.AbstractThisUse;
import fr.n7.stl.block.ast.object.AccessModifier;
import fr.n7.stl.block.ast.object.AttributeDefinition;
import fr.n7.stl.block.ast.object.ClassDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.util.Logger;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 */
public abstract class AbstractAttribute extends DefinitionAccess {

    protected AttributeDefinition attributeDefinition;

    public AbstractAttribute(String target, String name) {
        super(target, name);
    }

    public AbstractAttribute(Expression object, String name) {
        super(object, name);
    }


    @Override
    protected Declaration getDeclaration() {
        return attributeDefinition;
    }

    @Override
    public boolean subResolve(HierarchicalScope<Declaration> scope) {

        if (! (objectType.getDeclaration() instanceof ClassDeclaration)) {
            Logger.error("Accessing 'attribute' of non class declaration '" + objectType + "' ");
            return false;
        }

        ClassDeclaration classDeclaration = (ClassDeclaration) objectType.getDeclaration();
        if (! classDeclaration.hasAttribute(name)) {
            Logger.error("Class " + classDeclaration.getName() + " has no attribute member " + name);
            return false;
        }
        
        AttributeDefinition tmp = classDeclaration.getAttributeDeclaration(name);

        if (tmp.getAccessModifier() == AccessModifier.PUBLIC || (scope.knows("this") && ((AbstractThisUse) scope.get("this")).programDeclaration == tmp.getParent())) {
        	this.attributeDefinition = tmp;
        	return true;
        } else {
        	Logger.error("Could not access attribute " + this.name + " of class " + tmp.getParent().getName() + ". The definition does not exist or is private.");
        	return false;
        }


    }

    /**
     * Synthesized Semantics attribute to compute the type of an expression.
     * @return Synthesized Type of the expression.
     */
    public Type getType() {
        if (attributeDefinition == null) return AtomicType.ErrorType;
        return attributeDefinition.getType();
    }

}