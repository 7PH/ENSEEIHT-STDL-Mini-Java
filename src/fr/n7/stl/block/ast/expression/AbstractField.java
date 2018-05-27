package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.RecordType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;

/** Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 */
public abstract class AbstractField implements Expression {

	protected Expression record;

	protected String name;

	protected FieldDeclaration field;

	protected RecordType recordType;
	
	/** Construction for the implementation of a record field access expression ABSTRACT Syntax Tree node.
	 * @param _record ABSTRACT Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public AbstractField(Expression _record, String _name) {
		this.record = _record;
		this.name = _name;
	}

	@Override
	public String toString() {
		return this.record + "." + this.name;
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		if (! record.resolve(scope))
			return false;
	    Type type = record.getType();
        while (type instanceof NamedType)
            type = ((NamedType)type).getType();
		if (! (type instanceof RecordType))
			return false;
        this.recordType = (RecordType) type;
		if (! this.recordType.contains(name))
			return false;
	    this.field = this.recordType.get(name);
	    return true;
	}

	/** Synthesized Semantics attribute to compute the type of an expression.
	 * @return Synthesized Type of the expression.
	 */
	public Type getType() {
	    if (field == null) return AtomicType.ErrorType;
        return field.getType();
	}

}