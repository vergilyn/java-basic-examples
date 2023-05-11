package com.vergilyn.examples.usage.u0013;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 责任链模式：2种实现方式 1) 数组；2) 链表。
 *
 */
public class ChainStructTests {

	// 一般通过 SPI 或者 spring的List注入
	final List<AbstractLinkedProcessorSlot> processors = Lists.newArrayList(
			new A(),
			new B(),
			new C(),
			new D(),
			new E()
	);

	@Test
	public void listInvoker(){
		for (ProcessorSlot processor : processors) {
			processor.print();
		}
	}

	/**
	 * 参考：alibaba-sentinel
	 * <pre>{@code
	 *      // com.alibaba.csp.sentinel.slots.DefaultSlotChainBuilder#build
	 *      ProcessorSlotChain chain = SlotChainProvider.newSlotChain();
	 *
	 *
	 * }</pre>
	 */
	@Test
	public void sentinel(){
		// 1. 组装链表
		DefaultProcessorChain processorChain = new DefaultProcessorChain();
		for (AbstractLinkedProcessorSlot next : processors) {
			processorChain.addLast(next);
		}

		// 通过“链表”调用
		processorChain.print();
	}

	/**
	 * 参考：com.alibaba.csp.sentinel.slotchain.DefaultProcessorSlotChain。
	 */
	private static class DefaultProcessorChain extends AbstractLinkedProcessorSlot{

		AbstractLinkedProcessorSlot first = new AbstractLinkedProcessorSlot() {
			@Override
			public void print() {
				super.print();
			}
		};

		// 记录“链表尾”，目的：为了方便添加处理器
		AbstractLinkedProcessorSlot end = first;

		public void addLast(AbstractLinkedProcessorSlot processor) {
			end.setNext(processor);
			end = processor;
		}

		@Override
		public void print() {
			first.print();
		}

		@Override
		public void setNext(AbstractLinkedProcessorSlot next) {
			addLast(next);
		}

		@Override
		public AbstractLinkedProcessorSlot getNext() {
			return first.getNext();
		}
	}

	private static interface ProcessorSlot {
		void print();
	}

	private static abstract class AbstractLinkedProcessorSlot implements ProcessorSlot {
		private AbstractLinkedProcessorSlot next = null;

		@Override
		public void print() {
			System.out.println(this.getClass().getSimpleName());

			if (next != null){
				next.print();
			}
		}

		public AbstractLinkedProcessorSlot getNext() {
			return next;
		}

		public void setNext(AbstractLinkedProcessorSlot next) {
			this.next = next;
		}
	}

	private static class A extends AbstractLinkedProcessorSlot { }

	private static class B extends AbstractLinkedProcessorSlot { }

	private static class C extends AbstractLinkedProcessorSlot { }

	private static class D extends AbstractLinkedProcessorSlot { }

	private static class E extends AbstractLinkedProcessorSlot { }

}
