package com.gemserk.games.leavemealone;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

public class TriggerTest {

	// it is like a task...

	interface Trigger {

		void trigger();

	}

	class MockTrigger implements Trigger {

		public int triggerCount = 0;

		@Override
		public void trigger() {
			triggerCount++;
		}

	}

	class PeriodicTriggerLogic {

		int times;
		Trigger trigger;
		float interval;
		
		float currentTime;

		public PeriodicTriggerLogic(int times, float interval, Trigger trigger) {
			this.times = times;
			this.interval = interval;
			this.trigger = trigger;
			if (times > 0)
				currentTime = interval;
		}

		public boolean isFinished() {
			return currentTime <= 0;
		}

		public void update(float time) {
			currentTime -= time;
			while (currentTime <= 0 && times > 0) {
				trigger.trigger();
				times--;
				if (times > 0)
					currentTime += interval;
			}
		}

	}

	@Test
	public void shouldBeFinishedIfNoElementsToSpawn() {
		MockTrigger spawner = new MockTrigger();
		PeriodicTriggerLogic periodicTriggerLogic = new PeriodicTriggerLogic(0, 1f, spawner);
		periodicTriggerLogic.update(0f);
		assertThat(periodicTriggerLogic.isFinished(), IsEqual.equalTo(true));
	}

	@Test
	public void shouldNotBeFinishedWhenMoreThanZeroElementsToSpawn() {
		MockTrigger spawner = new MockTrigger();
		PeriodicTriggerLogic periodicTriggerLogic = new PeriodicTriggerLogic(2, 1f, spawner);
		periodicTriggerLogic.update(0f);
		assertThat(periodicTriggerLogic.isFinished(), IsEqual.equalTo(false));
		assertThat(spawner.triggerCount, IsEqual.equalTo(0));
	}

	@Test
	public void shouldSpawnOnlyOneTime() {
		MockTrigger spawner = new MockTrigger();
		PeriodicTriggerLogic periodicTriggerLogic = new PeriodicTriggerLogic(1, 1f, spawner);
		periodicTriggerLogic.update(1f);
		assertThat(periodicTriggerLogic.isFinished(), IsEqual.equalTo(true));
		assertThat(spawner.triggerCount, IsEqual.equalTo(1));
	}

	@Test
	public void shouldSpawnWhenTimeBetweenPassedMultipleTimes() {
		MockTrigger spawner = new MockTrigger();
		PeriodicTriggerLogic periodicTriggerLogic = new PeriodicTriggerLogic(2, 1f, spawner);
		periodicTriggerLogic.update(1f);
		assertThat(periodicTriggerLogic.isFinished(), IsEqual.equalTo(false));
		assertThat(spawner.triggerCount, IsEqual.equalTo(1));
		periodicTriggerLogic.update(1f);
		assertThat(periodicTriggerLogic.isFinished(), IsEqual.equalTo(true));
		assertThat(spawner.triggerCount, IsEqual.equalTo(2));
	}

	@Test
	public void shouldSpawnWhenTimeBetweenPassedMultipleTimes2() {
		MockTrigger spawner = new MockTrigger();
		PeriodicTriggerLogic periodicTriggerLogic = new PeriodicTriggerLogic(2, 1f, spawner);
		periodicTriggerLogic.update(5f);
		assertThat(periodicTriggerLogic.isFinished(), IsEqual.equalTo(true));
		assertThat(spawner.triggerCount, IsEqual.equalTo(2));
	}
	
}
