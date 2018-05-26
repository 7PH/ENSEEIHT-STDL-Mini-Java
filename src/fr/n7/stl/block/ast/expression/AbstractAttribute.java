package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.object.AccessModifier;
import fr.n7.stl.block.ast.object.AttributeDefinition;
import fr.n7.stl.block.ast.object.ClassDeclaration;
import fr.n7.stl.block.ast.object.InstanceType;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.util.Logger;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractAttribute implements Expression {

    protected Expression object;

    protected String name;

    protected AttributeDefinition attributeDefinition;

    protected InstanceType objectType;

    public AbstractAttribute(Expression object, String name) {
        this.object = object;
        this.name = name;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.object + "." + this.name;
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> scope) {
        // Verify objet resolve.
        if (! object.resolve(scope)) {
            Logger.error("Could not resolve attribute assignment because of: " + object + ".");
            return false;
        }

        // Get objet type and check that is a InstanceType
        Type type = object.getType();

        if (! (type instanceof InstanceType)) {
            Logger.error(object + " is not a InstanceType.");
            return false;
        }

        // Verify that the InstanceType contain the field
        this.objectType = (InstanceType) type;

        if (! (objectType.getDeclaration() instanceof ClassDeclaration)) {
            Logger.error("Accessing 'attribute' of non class declaration");
            return false;
        }

        ClassDeclaration classDeclaration = (ClassDeclaration) objectType.getDeclaration();
        if (! classDeclaration.hasAttribute(name)) {
            Logger.error("Class " + classDeclaration.getName() + " has no attribute member " + name);
            return false;
        }

        this.attributeDefinition = classDeclaration.getAttributeDeclaration(name, true);

        return true;
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