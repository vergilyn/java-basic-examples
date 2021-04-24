package com.vergilyn.examples.usage.u0002;

import java.io.Serializable;
import java.time.LocalTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class LogInfo implements Serializable {
	private Integer index;
	private LocalTime createTime;

	public LogInfo(Integer index, LocalTime createTime) {
		this.index = index;
		this.createTime = createTime;
	}
}
