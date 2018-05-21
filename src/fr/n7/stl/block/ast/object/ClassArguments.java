package fr.n7.stl.block.ast.object;

public enum ClassArguments {
	Final,
	Abstract;
	
	@Override
	public String toString() {
		switch(this) {
			case Final :
				return "final";
			case Abstract :
				return "abstract";
			default :
				throw new IllegalArgumentException( "The default case should never be triggered.");
		}
	}
}
