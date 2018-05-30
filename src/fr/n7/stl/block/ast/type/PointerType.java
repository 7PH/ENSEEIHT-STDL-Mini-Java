package fr.n7.stl.block.ast.type;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;

/** Implementation of the ABSTRACT Syntax Tree node for a pointer type. */
public class PointerType implements Type {

    protected Type element;

    public PointerType(Type _element) {
        this.element = _element;
    }

    public Type getPointedType() {
        return this.element;
    }

    /* (non-Javadoc)
     * @see fr.n7.stl.block.ast.Type#equalsTo(fr.n7.stl.block.ast.Type)
     */
    @Override
    public boolean equalsTo(Type other) {
        return other instanceof PointerType
                && element.equalsTo(((PointerType) other).element);
    }

    /* (non-Javadoc)
     * @see fr.n7.stl.block.ast.Type#compatibleWith(fr.n7.stl.block.ast.Type)
     */
    @Override
    public boolean compatibleWith(Type other) {
        return other instanceof PointerType
                && element.compatibleWith(((PointerType) other).element);
    }

    /* (non-Javadoc)
     * @see fr.n7.stl.block.ast.Type#merge(fr.n7.stl.block.ast.Type)
     */
    @Override
    public Type merge(Type other) {
        if (! compatibleWith(other)) return AtomicType.ErrorType;
        if (! (other instanceof PointerType)) return AtomicType.ErrorType;
        return element.merge(((PointerType)other).getPointedType());
    }

    /* (non-Javadoc)
     * @see fr.n7.stl.block.ast.Type#length(int)
     */
    @Override
    public int length() {
        return AtomicType.IntegerType.length();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "(" + this.element + " *)";
    }

    /* (non-Javadoc)
     * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
     */
    @Override
    public boolean resolve(HierarchicalScope<Declaration> scope) {
        return this.element.resolve(scope);
    }

}
