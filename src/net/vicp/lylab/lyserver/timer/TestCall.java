package net.vicp.lylab.lyserver.timer;

import java.util.Date;

import net.vicp.lylab.utils.timer.TimerJob;

public class TestCall extends TimerJob {

	@Override
	public void exec() {
		System.out.println("------------start!!-----------");
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
