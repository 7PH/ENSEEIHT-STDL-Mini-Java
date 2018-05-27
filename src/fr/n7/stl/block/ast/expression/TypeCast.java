package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/** ABSTRACT Syntax Tree node for an expression extracting the first component in a couple. */
public class TypeCast implements Expression {

    protected String type;

    /** Because resolve has to be called on the NamedType, it can't be instanciated in 'getType()'.
     * It has to be instanciated in resolve, be resolved in the TypeCast.resolve method,
     * And be returned in TypeCast.
     */
    protected Type targetType;

    /** AST node for the expression whose value must whose first element is extracted by the expression. */
    protected Expression target;

    /** Builds an ABSTRACT Syntax Tree node for an expression extracting the first component of a couple.
     * @param _target : AST node for the expression whose value must whose first element is extracted by the expression.
     */
    public TypeCast(Expression _target, String _type) {
        this.target = _target;
        this.type = _type;
    }

    public String toString() {
        return "((" + this.type + ") " + this.target + ")";
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> scope) {
        targetType = new NamedType(type);
        targetType.resolve(scope);
        return target.resolve(scope);
    }

    @Override
    public Type getType() {
        if (target.getType().length() != targetType.length())
            return AtomicType.ErrorType;

        return targetType;
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
        return target.getCode(factory);
    }

}