package com.uofl.ea.task;

import com.uofl.ea.manager.EventManager;
import com.uofl.ea.model.EventVO;

public class EventProcessingTask implements Runnable {
	private EventVO eventVO;
	
	public EventProcessingTask(EventVO eventVO) {
		this.eventVO = eventVO;
	}

	@Override
	public void run() {
		EventManager.getEventManager().processNewEvent(eventVO);
	}
	
}
