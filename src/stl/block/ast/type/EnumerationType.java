/**
 * 
 */
package stl.block.ast.type;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.declaration.LabelDeclaration;

import java.util.Iterator;
import java.util.List;

/**
 * @author Marc Pantel
 *
 */
public class EnumerationType implements Type, Declaration {
	
	private String name;
	
	private List<LabelDeclaration> labels;

	/**
	 * 
	 */
	public EnumerationType(String _name, List<LabelDeclaration> _labels) {
		this.name = _name;
		this.labels = _labels;
	}

	public List<LabelDeclaration> getLabels() {
	    return labels;
    }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder _result = new StringBuilder("enum" + this.name + " { ");
		Iterator<LabelDeclaration> _iter = this.labels.iterator();
		if (_iter.hasNext()) {
			_result.append(_iter.next());
			while (_iter.hasNext()) {
				_result.append(" ,").append(_iter.next());
			}
		}
		return _result + " }";
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#equalsTo(fr.n7.stl.block.ast.type.Type)
	 */
	@Override
	public boolean equalsTo(Type other) {
	    if (other instanceof NamedType)
            return equals(((NamedType)other).getType());

	    if (! (other instanceof EnumerationType))
	        return false;

	    EnumerationType otherEnum = (EnumerationType) other;
	    if (otherEnum.getLabels().size() != labels.size())
	        return false;

	    int i = 0;
	    while (i < labels.size()) {
	        if (! labels.get(i).getName().equals(otherEnum.getLabels().get(i).getName()))
	            return false;
	        if (! labels.get(i).getType().equalsTo(otherEnum.getLabels().get(i).getType()))
	            return false;
	        ++ i;
        }

	    return true;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#compatibleWith(fr.n7.stl.block.ast.type.Type)
	 */
	@Override
	public boolean compatibleWith(Type other) {
        return equals(other);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#merge(fr.n7.stl.block.ast.type.Type)
	 */
	@Override
	public Type merge(Type other) {
        if (other instanceof NamedType)
            return merge(((NamedType)other).getType());

        if (! equalsTo(other)) return AtomicType.ErrorType;
        return this;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#length()
	 */
	@Override
	public int length() {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		return true;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Declaration#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Declaration#getType()
	 */
	@Override
	public Type getType() {
		return this;
	}

}
