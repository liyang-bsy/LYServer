package net.vicp.lylab.lyserver.timer;

import java.util.Date;

import net.vicp.lylab.utils.timer.TimerJob;

public class TestCall extends TimerJob {

	@Override
	public void exec() {
		log.info("------------start!!-----------");
		log.warn("I'm timer, I got a warning");
		log.error("I'm timer, I got an error");
		log.fatal("I'm timer, I got a big problem");
	}

	@Override
	public Date getStartTime() {
		return new Date();
	}

	@Override
	public Integer getInterval() {
		return MINUTE;
	}

}
