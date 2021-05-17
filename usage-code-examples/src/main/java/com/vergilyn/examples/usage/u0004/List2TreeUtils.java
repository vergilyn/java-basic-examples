package com.vergilyn.examples.usage.u0004;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @author vergilyn
 * @since 2021-05-17
 */
public class List2TreeUtils {

	public static <ID, T extends AbstractTreeNode<ID>> List<T> list2Tree(ID firstLevelId, List<T> source){
		return list2Tree(firstLevelId, source, null);
	}

	public static <ID, T extends AbstractTreeNode<ID>> List<T> list2Tree(ID firstLevelId, List<T> source, Consumer<T> ext){
		Map<ID, List<T>> pidMap = source.stream().collect(Collectors.groupingBy(AbstractTreeNode::getPid));

		return buildChildren(firstLevelId, pidMap, ext);
	}

	public static <ID, T extends AbstractTreeNode<ID>> List<T> buildChildren(ID pid, Map<ID, List<T>> pidMap, Consumer<T> ext) {
		List<T> parents = pidMap.get(pid);
		if (parents == null || parents.isEmpty()){
			return null;
		}

		List<T> children;
		for (T parent : parents) {
			children = buildChildren(parent.getId(), pidMap, ext);
			// VFIXME 2021-05-17 是否可以做到`children`不强转？
			parent.setChildren((List<AbstractTreeNode<ID>>) children);

			// 扩展，例如 设置每一个节点的total
			if (ext != null){
				ext.accept(parent);
			}
		}

		return parents;
	}
}
