package com.mybiblestudywebapp.bibletextservice.persistence.repository;

import com.mybiblestudywebapp.bibletextservice.persistence.model.Version;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/29/20
 */
@Repository
public interface VersionRepository extends CrudRepository<Version, Long> {
}
