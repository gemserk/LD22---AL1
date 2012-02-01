package com.gemserk.games.leavemealone.triggers;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import com.gemserk.games.leavemealone.triggers.PeriodicTriggerLogic;

public class PeriodicTriggerLogicTest {

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
