package com.spring.demo.springreactivesampleapplication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Foo {

	private final String id;
	private final String name;

	public static Foo create(String id, String name) {
		return new Foo(id, name);
	}
}
