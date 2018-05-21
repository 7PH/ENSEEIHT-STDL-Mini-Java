package fr.n7.stl.block.ast.object;

public enum AccessArguments {
	Public,
	Private;
	
	public String toString() {
		switch(this) {
		case Public: return "public";
		case Private: return "private";
		default: throw new IllegalArgumentException( "The default case should never be triggered.");
		}
	}
}