package com.gemserk.games.leavemealone.spawner;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

public class WaveSpawnerTest {

	public static interface ElementSpawner {

		void spawn(SpawnElement element);

	}

	class MockElementSpawner implements ElementSpawner {

		List<SpawnElement> spawnedElements = new ArrayList<SpawnElement>();

		@Override
		public void spawn(SpawnElement element) {
			spawnedElements.add(element);
		}

	}

	public static class WaveSpawner {

		List<SpawnElement> elements;
		ElementSpawner elementSpawner;
		
		List<SpawnElement> removeElements;
		
		float currentTime = 0f;

		public WaveSpawner(ElementSpawner elementSpawner, List<SpawnElement> elements) {
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

	@Test
	public void shouldBeDoneIfNothingToSpawn() {
		MockElementSpawner mockElementSpawner = new MockElementSpawner();

		WaveSpawner waveSpawner = new WaveSpawner(mockElementSpawner, new ArrayList<SpawnElement>());

		assertThat(waveSpawner.isDone(), IsEqual.equalTo(true));
		assertTrue(mockElementSpawner.spawnedElements.isEmpty());
	}

	@Test
	public void shouldNotBeDoneIfSomethingToSpawn() {
		MockElementSpawner mockElementSpawner = new MockElementSpawner();

		ArrayList<SpawnElement> elements = new ArrayList<SpawnElement>();
		SpawnElement spawnElement1 = new SpawnElement(1f, null, null);
		elements.add(spawnElement1);

		WaveSpawner waveSpawner = new WaveSpawner(mockElementSpawner, elements);

		assertThat(waveSpawner.isDone(), IsEqual.equalTo(false));
		assertTrue(mockElementSpawner.spawnedElements.isEmpty());
	}

	@Test
	public void shouldSpawnElementIfTimeGreaterThanElementTime() {
		MockElementSpawner mockElementSpawner = new MockElementSpawner();

		ArrayList<SpawnElement> elements = new ArrayList<SpawnElement>();
		SpawnElement spawnElement1 = new SpawnElement(1f, null, null);
		elements.add(spawnElement1);

		WaveSpawner waveSpawner = new WaveSpawner(mockElementSpawner, elements);
		waveSpawner.update(1f);

		assertThat(waveSpawner.isDone(), IsEqual.equalTo(true));
		assertFalse(mockElementSpawner.spawnedElements.isEmpty());
		assertTrue(mockElementSpawner.spawnedElements.contains(spawnElement1));
	}
	
	@Test
	public void shouldSpawnMultipleElementsInOneUpdate() {
		MockElementSpawner mockElementSpawner = new MockElementSpawner();

		ArrayList<SpawnElement> elements = new ArrayList<SpawnElement>();

		SpawnElement spawnElement1 = new SpawnElement(1f, null, null);
		elements.add(spawnElement1);

		SpawnElement spawnElement2 = new SpawnElement(2f, null, null);
		elements.add(spawnElement2);

		WaveSpawner waveSpawner = new WaveSpawner(mockElementSpawner, elements);
		waveSpawner.update(3f);

		assertThat(waveSpawner.isDone(), IsEqual.equalTo(true));
		assertFalse(mockElementSpawner.spawnedElements.isEmpty());
		assertTrue(mockElementSpawner.spawnedElements.contains(spawnElement1));
		assertTrue(mockElementSpawner.spawnedElements.contains(spawnElement2));
	}
	
	@Test
	public void shouldSpawnMultipleElementsInMultipleUpdates() {
		MockElementSpawner mockElementSpawner = new MockElementSpawner();

		ArrayList<SpawnElement> elements = new ArrayList<SpawnElement>();

		SpawnElement spawnElement1 = new SpawnElement(1f, null, null);
		elements.add(spawnElement1);

		SpawnElement spawnElement2 = new SpawnElement(2f, null, null);
		elements.add(spawnElement2);

		WaveSpawner waveSpawner = new WaveSpawner(mockElementSpawner, elements);
		waveSpawner.update(1f);
		
		assertThat(waveSpawner.isDone(), IsEqual.equalTo(false));
		
		waveSpawner.update(1f);
		
		assertThat(waveSpawner.isDone(), IsEqual.equalTo(true));
		assertFalse(mockElementSpawner.spawnedElements.isEmpty());
		assertTrue(mockElementSpawner.spawnedElements.contains(spawnElement1));
		assertTrue(mockElementSpawner.spawnedElements.contains(spawnElement2));
	}

}
