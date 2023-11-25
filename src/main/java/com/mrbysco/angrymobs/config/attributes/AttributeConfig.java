package com.mrbysco.angrymobs.config.attributes;

import java.util.List;
import java.util.Objects;

public final class AttributeConfig {
	private final List<AttributeAddition> additionList;

	public AttributeConfig(List<AttributeAddition> initialList) {
		this.additionList = initialList;
	}

	public List<AttributeAddition> additionList() {
		return additionList;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (AttributeConfig) obj;
		return Objects.equals(this.additionList, that.additionList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(additionList);
	}

	@Override
	public String toString() {
		return "AdditionConfig[" + "additionList=" + additionList + ']';
	}

}
