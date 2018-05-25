package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.RecordType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractAttribute implements Expression {

    protected Expression object;

    protected String name;

    protected FieldDeclaration field;

    protected RecordType recordType;

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

    /* (non-Javadoc)
     * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
     */
    @Override
    public boolean resolve(HierarchicalScope<Declaration> scope) {
        if (! object.resolve(scope)) return false;

        // on va récup le type de l'objet
        Type type = object.getType();

        if (! (type instanceof RecordType)) return false;
        this.recordType = (RecordType) type;

        // on vérifie qu'il contient le field 'name'
        if (! this.recordType.contains(name)) return false;

        // on l'assigne
        this.field = this.recordType.get(name);

        // youpi
        return true;
    }

    /**
     * Synthesized Semantics attribute to compute the type of an expression.
     * @return Synthesized Type of the expression.
     */
    public Type getType() {
        if (field == null) return AtomicType.ErrorType;
        return field.getType();
    }

}