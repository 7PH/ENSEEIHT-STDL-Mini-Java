package fr.n7.stl.block.ast.type;

public class PartialArrayType extends ArrayType implements PartialType {

	public PartialArrayType() {
		super(null);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.PartialType#complete(fr.n7.stl.block.ast.type.Type)
	 */
	@Override
	public Type complete(Type type) {
		if (this.element == null) {
			return new ArrayType(type);
		} else {
			return new ArrayType(((PartialType) this.element).complete(type));
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
