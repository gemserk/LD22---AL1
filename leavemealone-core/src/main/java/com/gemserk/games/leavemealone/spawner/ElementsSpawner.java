package com.gemserk.games.leavemealone.spawner;

import java.util.ArrayList;
import java.util.List;

public class ElementsSpawner {

	List<SpawnElement> elements;
	ElementSpawner elementSpawner;
	
	List<SpawnElement> removeElements;
	
	float currentTime = 0f;

	public ElementsSpawner(List<SpawnElement> elements, ElementSpawner elementSpawner) {
		this.elementSpawner = elementSpawner;
		this.elements = elements;
		this.removeElements = new ArrayList<SpawnElement>();
	}

	public boolean isDone() {
		return elements.isEmpty();
	}

	public void update(float delta) {
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