package com.gemserk.games.leavemealone.spawner;

import java.util.ArrayList;
import java.util.List;

public class ElementsSpawner {

	List<SpawnElement> elements;
	List<SpawnElement> removeElements;
	float currentTime;
	
	public void addAll(List<SpawnElement> elements) {
		this.elements.addAll(elements);
	}
	
	public ElementsSpawner(List<SpawnElement> elements) {
		this(0f, elements);
	}

	public ElementsSpawner(float offset, List<SpawnElement> elements) {
		this.elements = new ArrayList<SpawnElement>(elements);
		this.removeElements = new ArrayList<SpawnElement>();
		this.currentTime = -offset;
	}

	public boolean isDone() {
		return elements.isEmpty();
	}

	public void update(float delta, ElementSpawner elementSpawner) {
		currentTime += delta;
		for (int i = 0; i < elements.size(); i++) {
			SpawnElement spawnElement = elements.get(i);
			if (spawnElement.time > currentTime)
				continue;
			elementSpawner.spawn(spawnElement);
			removeElements.add(spawnElement);
		}
		elements.removeAll(removeElements);
		removeElements.clear();
	}

}