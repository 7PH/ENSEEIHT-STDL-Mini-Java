package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

public class Instantiation {
	
	private String name;
	
	private List<Instantiation> instantiations;
	
	public Instantiation(String _name) {
		this.name = _name;
		this.instantiations = new LinkedList<Instantiation>();
	}
	
	public Instantiation(String _name, List<Instantiation> _instantiations) {
		this.name = _name;
		this.instantiations = _instantiations;
	}

}
