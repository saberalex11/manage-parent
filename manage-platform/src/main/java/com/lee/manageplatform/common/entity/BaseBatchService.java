package com.lee.manageplatform.common.entity;

import java.util.List;

public interface BaseBatchService<T> extends BaseService<T> {

	int addList(List<T> recordList);

}
