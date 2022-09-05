package com.vergilyn.examples.guava.collect;

import com.google.common.collect.*;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 *
 * <pre>
 * +---------------+---------------------------------+
 * | Interface     | Forwarding Decorator            |
 * +===============+=================================+
 * | Collection    | {@link ForwardingCollection}    |
 * | List          | {@link ForwardingList}          |
 * | Set           | {@link ForwardingSet}           |
 * | SortedSet     | {@link ForwardingSortedSet}     |
 * | Map           | {@link ForwardingMap}           |
 * | SortedMap     | {@link ForwardingSortedMap}     |
 * | ConcurrentMap | {@link ForwardingConcurrentMap} |
 * | Map.Entry     | {@link ForwardingMapEntry}      |
 * | Queue         | {@link ForwardingQueue}         |
 * | Iterator      | {@link ForwardingIterator}      |
 * | ListIterator  | {@link ForwardingListIterator}  |
 * | Multiset      | {@link ForwardingMultiset}      |
 * | Multimap      | {@link ForwardingMultimap}      |
 * | ListMultimap  | {@link ForwardingListMultimap}  |
 * | SetMultimap   | {@link ForwardingSetMultimap}   |
 * +---------------+---------------------------------+
 * </pre>
 *
 * <p> aliyun 商业版RocketMQ ons-client，有使用到。
 *
 * @author vergilyn
 * @since 2022-08-31
 *
 */
public class ForwardingListTests {

	@Test
	public void helloworld(){
		HelloworldForwardingList<String> list = new HelloworldForwardingList<>();

		list.add("test1");
		list.add("test2");
	}

	@Test
	public void test(){

	}

	public static class HelloworldForwardingList<E> extends ForwardingList<E> {
		private final List<E> delegate = Lists.newArrayList();


		@Override
		protected List<E> delegate() {
			return delegate;
		}

		@Override
		public boolean add(E element) {
			System.out.println("[vergilyn] `HelloworldForwardingList#add()` Before >>>> element: " + element.toString());

			boolean rs = super.add(element);

			System.out.println("[vergilyn] `HelloworldForwardingList#add()` After >>>> element: " + element.toString());

			return rs;
		}
	}
}
