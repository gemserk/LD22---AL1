package com.gemserk.games.leavemealone.triggers;

import com.gemserk.games.leavemealone.triggers.Trigger;

public class MockTrigger implements Trigger {

	public int triggerCount = 0;

	@Override
	public void trigger() {
		triggerCount++;
	}

}