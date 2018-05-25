/**
 * 
 */
package fr.n7.stl.block.ast.type;

/**
 * @author Marc Pantel
 *
 */
public class PartialPointerType extends PointerType implements PartialType {

	/**
	 */
	public PartialPointerType() {
		super(null);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.PartialType#complete(fr.n7.stl.block.ast.type.Type)
	 */
	@Override
	public Type complete(Type type) {
		if (this.element == null) {
			return new PointerType(type);
		} else {
			return new PointerType(((PartialType) this.element).complete(type));
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.PartialType#enrich(fr.n7.stl.block.ast.type.PartialType)
	 */
	@Override
	public void enrich(PartialType type) {
		if (this.element == null) {
			this.element = type;
		} else {
			((PartialType) this.element).enrich(type);
		}
	}

}
