package com.example.GPU_DATA.Repository;

import com.example.GPU_DATA.Entity.GpuSpec;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface GpuSpecsRepository extends JpaRepository<GpuSpec,Long > {

	boolean existsByGpuKey(String gpuKey);
	Optional<GpuSpec> findByGpuKey(String gpuKey);
}
