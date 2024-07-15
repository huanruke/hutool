/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.bean.copier;

import org.dromara.hutool.core.bean.BeanDesc;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.lang.copier.Copier;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.util.ObjUtil;

/**
 * 抽象的对象拷贝封装，提供来源对象、目标对象持有
 *
 * @param <S> 来源对象类型
 * @param <T> 目标对象类型
 * @author looly
 * @since 5.8.0
 */
public abstract class AbsCopier<S, T> implements Copier<T> {

	protected final S source;
	protected final T target;
	/**
	 * 拷贝选项
	 */
	protected final CopyOptions copyOptions;

	/**
	 * 构造
	 *
	 * @param source      源对象
	 * @param target      目标对象
	 * @param copyOptions 拷贝选项
	 */
	public AbsCopier(final S source, final T target, final CopyOptions copyOptions) {
		this.source = source;
		this.target = target;
		this.copyOptions = ObjUtil.defaultIfNull(copyOptions, CopyOptions::of);
	}

	/**
	 * 获取Bean描述信息<br>
	 * 如果用户自定义了{@link BeanDesc}实现，则使用，否则使用默认的规则
	 *
	 * @param actualEditable 需要解析的类
	 * @return {@link BeanDesc}
	 */
	protected BeanDesc getBeanDesc(final Class<?> actualEditable) {
		if (null != this.copyOptions) {
			final Class<BeanDesc> beanDescClass = copyOptions.beanDescClass;
			if (null != beanDescClass) {
				return ConstructorUtil.newInstance(beanDescClass, actualEditable);
			}
		}
		return BeanUtil.getBeanDesc(actualEditable);
	}
}
