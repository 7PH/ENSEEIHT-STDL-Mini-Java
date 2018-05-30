/**
 * 
 */
package fr.n7.stl.block.ast.type;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the ABSTRACT Syntax Tree node for a sequence type.
 * @author Marc Pantel
 *
 */
public class SequenceType implements Type {
	
	private List<Type> types;

	public SequenceType() {
		this.types = new LinkedList<Type>();
	}
	
	public void add(Type _type) {
		this.types.add(_type);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#equalsTo(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type other) {
		if (other instanceof SequenceType) {
			SequenceType _local = (SequenceType) other;
			if (this.types.size() == _local.types.size()) {
				Iterator<Type> i1 = this.types.iterator();
				Iterator<Type> i2 = _local.types.iterator();
				boolean _result = true;
				while (i1.hasNext() && i2.hasNext() && _result) {
					_result = _result && (i1.next().equalsTo(i2.next()));
				}
				return _result;
			} else {
				return false;
			}
		} else {
			if (other instanceof ArrayType) {
				boolean _result = true;
				Type _element = ((ArrayType) other).getType();
				Iterator<Type> _iter = this.types.iterator();
				while (_iter.hasNext() && _result) {
					_result = _result && _iter.next().equalsTo(_element);
				}
				return _result;
			} else {
				return false;
			}
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#compatibleWith(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type other) {
		if (other instanceof SequenceType) {
			SequenceType _local = (SequenceType) other;
			if (this.types.size() == _local.types.size()) {
				Iterator<Type> i1 = this.types.iterator();
				Iterator<Type> i2 = _local.types.iterator();
				boolean _result = true;
				while (i1.hasNext() && i2.hasNext() && _result) {
					_result = _result && (i1.next().compatibleWith(i2.next()));
				}
				return _result;
			} else {
				return false;
			}
		} else if (other instanceof ArrayType) {
            boolean _result = true;
            Type _element = ((ArrayType) other).getType();
            Iterator<Type> _iter = this.types.iterator();
            while (_iter.hasNext() && _result)
                _result = _result && _iter.next().equalsTo(_element);
            return _result;
        } else if (other instanceof NamedType) {
            return compatibleWith(((NamedType) other).getType());
        }
        return false;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#merge(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public Type merge(Type other) {
		if (other instanceof SequenceType) {
			SequenceType _local = (SequenceType) other;
			SequenceType _result = new SequenceType();
			if (this.types.size() == _local.types.size()) {
				Iterator<Type> i1 = this.types.iterator();
				Iterator<Type> i2 = _local.types.iterator();
				while (i1.hasNext() && i2.hasNext()) {
					_result.add(i1.next().merge(i2.next()));
				}
				return _result;
			} else {
				return AtomicType.ErrorType;
			}
		} else {
			return AtomicType.ErrorType;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#length(int)
	 */
	@Override
	public int length() {
		int _result = 0;
		for (Type _type : this.types) {
			_result += _type.length();
		}
		return _result;
	}

	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		boolean _result = true;
		for (Type t : this.types) {
			_result = _result && t.resolve(scope);
		}
		return _result;
	}

	@Override
	public String toString() {
		return types.toString();
	}
}
