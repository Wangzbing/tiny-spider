package com.tiny.cloud.bookspider.model.repository;

import com.tiny.cloud.bookspider.model.entity.CateOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangzb
 * @date 2021/8/16
 * @description 11
 */
public interface CateOrderRepository extends JpaRepository<CateOrder,Long> {
}
