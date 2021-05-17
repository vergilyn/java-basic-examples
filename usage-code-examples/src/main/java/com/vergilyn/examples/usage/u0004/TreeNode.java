package com.vergilyn.examples.usage.u0004;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TreeNode extends AbstractTreeNode<Integer>{
	public static final Integer TOP_ID = 0;

	private String title;

}
