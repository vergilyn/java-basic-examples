package com.vergilyn.examples.usage.u0004;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;

import org.junit.jupiter.api.Test;

class List2TreeTests extends AbstractList2TreeTests{

	@Test
	void list2Tree(){
		List<TreeNode> source = Lists.newArrayList(_unmodifiableData);

		List<TreeNode> target = List2TreeUtils.list2Tree(TreeNode.TOP_ID, source, null);

		System.out.println(JSON.toJSONString(target, SerializerFeature.PrettyFormat));
	}


}
