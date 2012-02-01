package com.gemserk.games.leavemealone.spawner;

import java.util.ArrayList;
import java.util.List;

public class Wave {
	
	public List<SpawnElement> elements;
	
	public Wave(List<SpawnElement> elements) {
		this.elements = elements;
	}
	
	public Wave() {
		this(new ArrayList<SpawnElement>());
	}
	
	public Wave(SpawnElement ...elements) {
		this.elements = new ArrayList<SpawnElement>();
		for (int i = 0; i < elements.length; i++) 
			this.elements.add(elements[i]);
	}
	
	public Wave(Wave wave) {
		this.elements = new ArrayList<SpawnElement>();
		this.elements.addAll(wave.elements);
	}

}
