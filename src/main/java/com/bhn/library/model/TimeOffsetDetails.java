package com.bhn.library.model;

public class TimeOffsetDetails {

	int secondsToAdd;
	int minutesToAdd;
	int hoursToAdd;
	
	public TimeOffsetDetails() {}
	
	public TimeOffsetDetails(int secondsToAdd, int minutesToAdd, int hoursToAdd) {
		super();
		this.secondsToAdd = secondsToAdd;
		this.minutesToAdd = minutesToAdd;
		this.hoursToAdd = hoursToAdd;
	}

	public int getSecondsToAdd() {
		return secondsToAdd;
	}
	
	public void setSecondsToAdd(int secondsToAdd) {
		this.secondsToAdd = secondsToAdd;
	}

	public int getMinutesToAdd() {
		return minutesToAdd;
	}

	public void setMinutesToAdd(int minutesToAdd) {
		this.minutesToAdd = minutesToAdd;
	}
	
	public int getHoursToAdd() {
		return hoursToAdd;
	}

	public void setHoursToAdd(int hoursToAdd) {
		this.hoursToAdd = hoursToAdd;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hoursToAdd;
		result = prime * result + minutesToAdd;
		result = prime * result + secondsToAdd;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeOffsetDetails other = (TimeOffsetDetails) obj;
		if (hoursToAdd != other.hoursToAdd)
			return false;
		if (minutesToAdd != other.minutesToAdd)
			return false;
		if (secondsToAdd != other.secondsToAdd)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TimeOffsetDetails [secondsToAdd=" + secondsToAdd + ", minutesToAdd=" + minutesToAdd + ", hoursToAdd="
				+ hoursToAdd + "]";
	}

	

	
	
}
