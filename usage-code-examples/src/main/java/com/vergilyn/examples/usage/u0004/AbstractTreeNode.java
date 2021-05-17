package com.vergilyn.examples.usage.u0004;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AbstractTreeNode<ID> {
	private ID id;
	private ID pid;

	private List<AbstractTreeNode<ID>> children;

	public AbstractTreeNode(ID id, ID pid) {
		this.id = id;
		this.pid = pid;
	}

}
