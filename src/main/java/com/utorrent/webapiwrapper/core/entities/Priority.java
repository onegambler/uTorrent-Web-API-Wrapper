package com.utorrent.webapiwrapper.core.entities;

public enum Priority{
	DO_NOT_DOWNLOAD(0),
	LOW_PRIORITY(1),
	NORMAL_PRIORITY(2),
	HIGH_PRIORITY(3);

	private int value;

	Priority(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static Priority getPriority(int value) {
		for(Priority priority : Priority.values()) {
			if(priority.getValue() == value) {
				return priority;
			}
		}

		return null;
	}
}
