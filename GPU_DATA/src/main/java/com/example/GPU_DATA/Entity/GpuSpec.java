package com.example.GPU_DATA.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gpu_specs", uniqueConstraints = @UniqueConstraint(columnNames = "gpu_key"))
@Data
@Builder
public class GpuSpec {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "gpu_key")
	private String gpuKey;

	@Column(name = "model_name")
	private String modelName;

	@Column(name = "launch_date")
	private String launchDate;

	@Column(name = "code_name")
	private String codeName;

	@Column(name = "vendor")
	private String vendor;

	@Column(name = "die_size_mm")
	private Double dieSizeMm;

	@Column(name = "bus_interface")
	private String busInterface;

	@Column(name = "core_configuration")
	private String coreConfiguration;

	@Column(name = "memory_bandwidth_gbps")
	private Double memoryBandwidthGbps;

	@Column(name = "memory_type")
	private String memoryType;

	@Column(name = "memory_bus_width_bits")
	private Integer memoryBusWidthBits;

	@Column(name = "power_consumption_watts")
	private Double powerConsumptionWatts;

	@Column(name = "manufacturing_process")
	private String manufacturingProcess;

	@Column(name = "transistor_count_billions")
	private Double transistorCountBillions;

	@Column(name = "l2_cache_mib")
	private Double l2CacheMib;

	@Column(name = "memory_size_gib")
	private Double memorySizeGib;

	@Column(name = "release_price_usd")
	private String releasePriceUsd;

	@Lob
	@Column(name = "extra_specs", columnDefinition = "TEXT")
	private String extraSpecs;



	public GpuSpec(Long id, String gpuKey, String modelName, String launchDate, String codeName, String vendor, Double dieSizeMm, String busInterface, String coreConfiguration, Double memoryBandwidthGbps, String memoryType, Integer memoryBusWidthBits, Double powerConsumptionWatts, String manufacturingProcess, Double transistorCountBillions, Double l2CacheMib, Double memorySizeGib, String releasePriceUsd, String extraSpecs) {
		this.id = id;
		this.gpuKey = gpuKey;
		this.modelName = modelName;
		this.launchDate = launchDate;
		this.codeName = codeName;
		this.vendor = vendor;
		this.dieSizeMm = dieSizeMm;
		this.busInterface = busInterface;
		this.coreConfiguration = coreConfiguration;
		this.memoryBandwidthGbps = memoryBandwidthGbps;
		this.memoryType = memoryType;
		this.memoryBusWidthBits = memoryBusWidthBits;
		this.powerConsumptionWatts = powerConsumptionWatts;
		this.manufacturingProcess = manufacturingProcess;
		this.transistorCountBillions = transistorCountBillions;
		this.l2CacheMib = l2CacheMib;
		this.memorySizeGib = memorySizeGib;
		this.releasePriceUsd = releasePriceUsd;
		this.extraSpecs = extraSpecs;
	}

	public GpuSpec() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGpuKey() {
		return gpuKey;
	}

	public void setGpuKey(String gpuKey) {
		this.gpuKey = gpuKey;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getLaunchDate() {
		return launchDate;
	}

	public void setLaunchDate(String launchDate) {
		this.launchDate = launchDate;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Double getDieSizeMm() {
		return dieSizeMm;
	}

	public void setDieSizeMm(Double dieSizeMm) {
		this.dieSizeMm = dieSizeMm;
	}

	public String getBusInterface() {
		return busInterface;
	}

	public void setBusInterface(String busInterface) {
		this.busInterface = busInterface;
	}

	public String getCoreConfiguration() {
		return coreConfiguration;
	}

	public void setCoreConfiguration(String coreConfiguration) {
		this.coreConfiguration = coreConfiguration;
	}

	public Double getMemoryBandwidthGbps() {
		return memoryBandwidthGbps;
	}

	public void setMemoryBandwidthGbps(Double memoryBandwidthGbps) {
		this.memoryBandwidthGbps = memoryBandwidthGbps;
	}

	public String getMemoryType() {
		return memoryType;
	}

	public void setMemoryType(String memoryType) {
		this.memoryType = memoryType;
	}

	public Integer getMemoryBusWidthBits() {
		return memoryBusWidthBits;
	}

	public void setMemoryBusWidthBits(Integer memoryBusWidthBits) {
		this.memoryBusWidthBits = memoryBusWidthBits;
	}

	public Double getPowerConsumptionWatts() {
		return powerConsumptionWatts;
	}

	public void setPowerConsumptionWatts(Double powerConsumptionWatts) {
		this.powerConsumptionWatts = powerConsumptionWatts;
	}

	public String getManufacturingProcess() {
		return manufacturingProcess;
	}

	public void setManufacturingProcess(String manufacturingProcess) {
		this.manufacturingProcess = manufacturingProcess;
	}

	public Double getTransistorCountBillions() {
		return transistorCountBillions;
	}

	public void setTransistorCountBillions(Double transistorCountBillions) {
		this.transistorCountBillions = transistorCountBillions;
	}

	public Double getL2CacheMib() {
		return l2CacheMib;
	}

	public void setL2CacheMib(Double l2CacheMib) {
		this.l2CacheMib = l2CacheMib;
	}

	public Double getMemorySizeGib() {
		return memorySizeGib;
	}

	public void setMemorySizeGib(Double memorySizeGib) {
		this.memorySizeGib = memorySizeGib;
	}

	public String getReleasePriceUsd() {
		return releasePriceUsd;
	}

	public void setReleasePriceUsd(String releasePriceUsd) {
		this.releasePriceUsd = releasePriceUsd;
	}

	public String getExtraSpecs() {
		return extraSpecs;
	}

	public void setExtraSpecs(String extraSpecs) {
		this.extraSpecs = extraSpecs;
	}
}

