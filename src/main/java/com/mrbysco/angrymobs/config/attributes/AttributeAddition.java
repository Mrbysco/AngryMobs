package com.mrbysco.angrymobs.config.attributes;

import java.util.Objects;

public final class AttributeAddition {
	private final String entity;
	private final String attribute;
	private final double value;

	public AttributeAddition(String entity, String attribute, double value) {
		this.entity = entity;
		this.attribute = attribute;
		this.value = value;
	}

	public String entity() {
		return entity;
	}

	public String attribute() {
		return attribute;
	}

	public double value() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (AttributeAddition) obj;
		return Objects.equals(this.entity, that.entity) &&
				Objects.equals(this.attribute, that.attribute) &&
				Double.doubleToLongBits(this.value) == Double.doubleToLongBits(that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(entity, attribute, value);
	}

	@Override
	public String toString() {
		return "AttributeObject[" +
				"entity=" + entity + ", " +
				"attribute=" + attribute + ", " +
				"value=" + value + ']';
	}

}
