package cn.hutool.core.collection;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * 自定义加入前检查的{@link LinkedBlockingQueue}，给定一个检查函数，在加入元素前检查此函数<br>
 * 原理是通过Runtime#freeMemory()获取剩余内存，当剩余内存低于指定的阈值时，不再加入。
 *
 * @author looly
 * @since 6.0.0
 */
public class CheckedLinkedBlockingQueue<E> extends LinkedBlockingQueue<E> {
	private static final long serialVersionUID = 1L;

	protected final Predicate<E> checker;

	/**
	 * 构造
	 *
	 * @param checker 检查函数
	 */
	public CheckedLinkedBlockingQueue(final Predicate<E> checker) {
		super(Integer.MAX_VALUE);
		this.checker = checker;
	}

	/**
	 * 构造
	 *
	 * @param c             初始集合
	 * @param checker 检查函数
	 */
	public CheckedLinkedBlockingQueue(final Collection<? extends E> c, final Predicate<E> checker) {
		super(c);
		this.checker = checker;
	}

	@Override
	public void put(final E e) throws InterruptedException {
		if (checker.test(e)) {
			super.put(e);
		}
	}

	@Override
	public boolean offer(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
		return checker.test(e) && super.offer(e, timeout, unit);
	}

	@Override
	public boolean offer(final E e) {
		return checker.test(e) && super.offer(e);
	}
}
