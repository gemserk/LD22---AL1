package com.gemserk.games.leavemealone.spawner;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

public class ElementsSpawnerTest {

	class MockElementSpawner implements ElementSpawner {

		List<SpawnElement> spawnedElements = new ArrayList<SpawnElement>();

		@Override
		public void spawn(SpawnElement element) {
			spawnedElements.add(element);
		}

	}

	@Test
	public void shouldBeDoneIfNothingToSpawn() {
		MockElementSpawner mockElementSpawner = new MockElementSpawner();

		ElementsSpawner elementsSpawner = new ElementsSpawner(new ArrayList<SpawnElement>(), mockElementSpawner);

		assertThat(elementsSpawner.isDone(), IsEqual.equalTo(true));
		assertTrue(mockElementSpawner.spawnedElements.isEmpty());
	}

	@Test
	public void shouldNotBeDoneIfSomethingToSpawn() {
		MockElementSpawner mockElementSpawner = new MockElementSpawner();

		ArrayList<SpawnElement> elements = new ArrayList<SpawnElement>();
		SpawnElement spawnElement1 = new SpawnElement(1f, null, null, null);
		elements.add(spawnElement1);

		ElementsSpawner elementsSpawner = new ElementsSpawner(elements, mockElementSpawner);

		assertThat(elementsSpawner.isDone(), IsEqual.equalTo(false));
		assertTrue(mockElementSpawner.spawnedElements.isEmpty());
	}

	@Test
	public void shouldSpawnElementIfTimeGreaterThanElementTime() {
		MockElementSpawner mockElementSpawner = new MockElementSpawner();

		ArrayList<SpawnElement> elements = new ArrayList<SpawnElement>();
		SpawnElement spawnElement1 = new SpawnElement(1f, null, null, null);
		elements.add(spawnElement1);

		ElementsSpawner elementsSpawner = new ElementsSpawner(elements, mockElementSpawner);
		elementsSpawner.update(1f);

		assertThat(elementsSpawner.isDone(), IsEqual.equalTo(true));
		assertFalse(mockElementSpawner.spawnedElements.isEmpty());
		assertTrue(mockElementSpawner.spawnedElements.contains(spawnElement1));
	}

	@Test
	public void shouldSpawnMultipleElementsInOneUpdate() {
		MockElementSpawner mockElementSpawner = new MockElementSpawner();

		ArrayList<SpawnElement> elements = new ArrayList<SpawnElement>();

		SpawnElement spawnElement1 = new SpawnElement(1f, null, null, null);
		elements.add(spawnElement1);

		SpawnElement spawnElement2 = new SpawnElement(2f, null, null, null);
		elements.add(spawnElement2);

		ElementsSpawner elementsSpawner = new ElementsSpawner(elements, mockElementSpawner);
		elementsSpawner.update(3f);

		assertThat(elementsSpawner.isDone(), IsEqual.equalTo(true));
		assertFalse(mockElementSpawner.spawnedElements.isEmpty());
		assertTrue(mockElementSpawner.spawnedElements.contains(spawnElement1));
		assertTrue(mockElementSpawner.spawnedElements.contains(spawnElement2));
	}

	@Test
	public void shouldSpawnMultipleElementsInMultipleUpdates() {
		MockElementSpawner mockElementSpawner = new MockElementSpawner();

		ArrayList<SpawnElement> elements = new ArrayList<SpawnElement>();

		SpawnElement spawnElement1 = new SpawnElement(1f, null, null, null);
		elements.add(spawnElement1);

		SpawnElement spawnElement2 = new SpawnElement(2f, null, null, null);
		elements.add(spawnElement2);

		ElementsSpawner elementsSpawner = new ElementsSpawner(elements, mockElementSpawner);
		elementsSpawner.update(1f);

		assertThat(elementsSpawner.isDone(), IsEqual.equalTo(false));

		elementsSpawner.update(1f);

		assertThat(elementsSpawner.isDone(), IsEqual.equalTo(true));
		assertFalse(mockElementSpawner.spawnedElements.isEmpty());
		assertTrue(mockElementSpawner.spawnedElements.contains(spawnElement1));
		assertTrue(mockElementSpawner.spawnedElements.contains(spawnElement2));
	}

}
