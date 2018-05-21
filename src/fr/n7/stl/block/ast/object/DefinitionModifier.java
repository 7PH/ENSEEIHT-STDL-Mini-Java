package fr.n7.stl.block.ast.object;

public enum DefinitionModifier {
	Static,
	Final,
	Static_Final;
	
	@Override
	public String toString() {
		switch(this) {
			case Static : 
				return "static";
			case Final : 
				return "final";
			case Static_Final : 
				return "static final";
			default : 
				throw new IllegalArgumentException( "The default case should never be triggered.");
		}
	}
}
