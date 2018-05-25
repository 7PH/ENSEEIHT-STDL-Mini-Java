package fr.n7.stl.block.ast.object;

public enum AccessModifier {
    PUBLIC,
    PRIVATE;

	@Override
	public String toString() {
		switch(this) {
			case PUBLIC:
				return "public";
			case PRIVATE:
				return "private";
			default :
				throw new IllegalArgumentException("The default case should never be triggered.");
		}
	}
}
