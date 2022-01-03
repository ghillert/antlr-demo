package com.hillert.calculator.expression;

public class Variable extends Expression {
	public String id;

	public Variable(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return id;
	}
}
