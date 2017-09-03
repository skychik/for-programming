package ru.ifmo.cs.programming.lab7.utils;

import java.io.Serializable;

public enum MyEntryKey implements Serializable {
	OK("OK"),
	SQLEXCEPTION("SQLEXCEPTION"),
	DISCONNECT("DISCONNECT"),
	CLOSE("CLOSE"),

	NAME_AND_PASSWORD("NAME_AND_PASSWORD"),
	TABLE("TABLE"),
	TRANSACTION("TRANSACTION"),
		INSERT("INSERT"),
		REMOVE("REMOVE"),
		CLEAR("CLEAR"),
	ROLLBACK("ROLLBACK");

	private String name;

	MyEntryKey(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
