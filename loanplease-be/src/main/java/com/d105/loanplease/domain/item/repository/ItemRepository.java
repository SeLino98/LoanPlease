package com.d105.loanplease.domain.item.repository;

import com.d105.loanplease.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
