package ghost.framework.sqlHelper.bean;

import cn.hutool.core.util.StrUtil;

public class Sort {
	Direction direction;
	String column;

	public static enum Direction {
		ASC, DESC;
	}

	public Sort(String column, Direction direction) {
		this.column = column;
		this.direction = direction;

	}

	public String toString() {
		String s = "";
		if (direction == Direction.ASC) {
			s = "ASC";
		}
		if (direction == Direction.DESC) {
			s = "DESC";
		}
		return " ORDER BY " + StrUtil.toUnderlineCase(column)  + " " + s;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

}
