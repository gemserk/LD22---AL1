package com.gemserk.games.ludumdare.al1.triggers;

public class PeriodicTriggerLogic {

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