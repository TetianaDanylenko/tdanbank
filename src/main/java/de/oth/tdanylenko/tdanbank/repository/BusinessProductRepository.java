package de.oth.tdanylenko.tdanbank.repository;

import de.oth.tdanylenko.tdanbank.entity.BusinessProducts;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessProductRepository extends CrudRepository<BusinessProducts, Long> {
    BusinessProducts getBusinessProductsByNameIgnoreCase(String name);
    BusinessProducts getBusinessProductsById(long id);
}
