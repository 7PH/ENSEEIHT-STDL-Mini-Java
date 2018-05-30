package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.SequenceType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.Iterator;
import java.util.List;

/** ABSTRACT Syntax Tree node for an expression building a sequence of values. */
public class Sequence implements Expression {

	
	/** List of AST nodes of the expressions computing the values in the sequence. */
	protected List<Expression> values;

	/**
	 * Builds an ABSTRACT Syntax Tree node for an expression building a sequence of values.
	 * @param _values : List of AST nodes of the expressions computing the values in the sequence.
	 */
	public Sequence(List<Expression> _values) {
		this.values = _values;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = "{ ";
		Iterator<Expression> _iter = this.values.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
			while (_iter.hasNext()) {
				_result += " ," + _iter.next();
			}
		}
		return _result + " }";
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		boolean _result = true;
		for (Expression _value : this.values) {
			_result = _result && _value.resolve(scope);
		}
		return _result;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		SequenceType _result = new SequenceType();
		for (Expression _value : this.values) {
			_result.add(_value.getType());
		}
		return _result;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
        for (Expression expression: values)
            fragment.append(expression.getCode(factory));
        return fragment;
	}
}
