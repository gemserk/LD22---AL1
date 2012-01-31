package com.gemserk.games.leavemealone;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import com.gemserk.animation4j.transitions.TimeTransition;

public class SpawnerTest {

	// it is like a task...

	interface Spawner {

		void spawn();

	}

	class MockSpawner implements Spawner {

		public int spawnCount = 0;

		@Override
		public void spawn() {
			spawnCount++;
		}

	}

	class PeriodicSpawner {

		int spawnTimes;
		TimeTransition timeTransition;
		Spawner spawner;
		float timeBetween;

		public PeriodicSpawner(int spawnTimes, float timeBetween, Spawner spawner) {
			this.spawnTimes = spawnTimes;
			this.timeBetween = timeBetween;
			this.spawner = spawner;
			this.timeTransition = new TimeTransition();
			if (spawnTimes > 0)
				timeTransition.start(timeBetween);
		}

		public boolean isFinished() {
			return timeTransition.isFinished();
		}

		public void update(float time) {
			timeTransition.update(time);
			if (timeTransition.isFinished()) {
				spawner.spawn();
				spawnTimes--;
				if (spawnTimes > 0)
					timeTransition.start(timeBetween);
			}
		}

	}

	@Test
	public void shouldBeFinishedIfNoElementsToSpawn() {
		MockSpawner spawner = new MockSpawner();
		PeriodicSpawner periodicSpawner = new PeriodicSpawner(0, 1f, spawner);
		periodicSpawner.update(0f);
		assertThat(periodicSpawner.isFinished(), IsEqual.equalTo(true));
	}

	@Test
	public void shouldNotBeFinishedWhenMoreThanZeroElementsToSpawn() {
		MockSpawner spawner = new MockSpawner();
		PeriodicSpawner periodicSpawner = new PeriodicSpawner(2, 1f, spawner);
		periodicSpawner.update(0f);
		assertThat(periodicSpawner.isFinished(), IsEqual.equalTo(false));
		assertThat(spawner.spawnCount, IsEqual.equalTo(0));
	}

	@Test
	public void shouldSpawnOnlyOneTime() {
		MockSpawner spawner = new MockSpawner();
		PeriodicSpawner periodicSpawner = new PeriodicSpawner(1, 1f, spawner);
		periodicSpawner.update(1f);
		assertThat(periodicSpawner.isFinished(), IsEqual.equalTo(true));
		assertThat(spawner.spawnCount, IsEqual.equalTo(1));
	}

	@Test
	public void shouldSpawnWhenTimeBetweenPassedMultipleTimes() {
		MockSpawner spawner = new MockSpawner();
		PeriodicSpawner periodicSpawner = new PeriodicSpawner(2, 1f, spawner);
		periodicSpawner.update(1f);
		assertThat(periodicSpawner.isFinished(), IsEqual.equalTo(false));
		assertThat(spawner.spawnCount, IsEqual.equalTo(1));
		periodicSpawner.update(1f);
		assertThat(periodicSpawner.isFinished(), IsEqual.equalTo(true));
		assertThat(spawner.spawnCount, IsEqual.equalTo(2));
	}

}
