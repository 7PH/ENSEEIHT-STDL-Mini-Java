package fr.n7.stl.block.ast.expression.allocation;

import fr.n7.stl.block.ast.expression.BinaryOperator;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.ArrayType;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;

import static fr.n7.stl.block.ast.type.AtomicType.ErrorType;

public class ArrayAllocation implements Expression {

	protected String name;
	protected Type element;
	protected Expression size;
	
	public ArrayAllocation(String _name, Expression _size) {
		this.name = _name;
		this.size = _size;
	}

	public ArrayAllocation(Type _element, Expression _size) {
		this.element = _element;
		this.size = _size;
	}

	@Override
	public String toString() {
		return "new " + ((this.element == null)?this.name:this.element) + "[ " + this.size + " ]"; 
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		return (element != null || scope.knows(name))
				&& element.resolve(scope)
                && size.resolve(scope);
	}

	@Override
	public Type getType() {
		if (size.getType() != AtomicType.IntegerType) return ErrorType;
	    return new ArrayType(element == null ? new NamedType(name) : element);
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
        fragment.append(size.getCode(factory));
        fragment.add(factory.createLoadL((element == null ? new NamedType(name) : element).length()));
        fragment.add(TAMFactory.createBinaryOperator(BinaryOperator.Multiply));
        fragment.add(Library.MAlloc);
        return fragment;
	}

}
