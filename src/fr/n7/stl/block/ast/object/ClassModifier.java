package fr.n7.stl.block.ast.object;

public enum ClassModifier {
    FINAL,
    ABSTRACT;
	
	@Override
	public String toString() {
		switch(this) {
			case FINAL:
				return "final";
			case ABSTRACT:
				return "abstract";
			default :
				throw new IllegalArgumentException( "The default case should never be triggered.");
		}
	}
}
