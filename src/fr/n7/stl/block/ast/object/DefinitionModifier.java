package fr.n7.stl.block.ast.object;

public enum DefinitionModifier {
    STATIC,
    FINAL,
    STATIC_FINAL;
	
	@Override
	public String toString() {
		switch(this) {
			case STATIC:
				return "static";
			case FINAL:
				return "final";
			case STATIC_FINAL:
				return "static final";
			default : 
				throw new IllegalArgumentException( "The default case should never be triggered.");
		}
	}
}
