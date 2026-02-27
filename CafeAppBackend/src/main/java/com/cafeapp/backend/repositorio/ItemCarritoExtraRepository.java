package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.ItemCarritoExtra;
import com.cafeapp.backend.modelo.ItemCarritoExtraId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCarritoExtraRepository extends JpaRepository<ItemCarritoExtra, ItemCarritoExtraId> {
}
