package com.example.GPU_DATA.Service;

import com.example.GPU_DATA.Entity.GpuSpec;
import com.example.GPU_DATA.Repository.GpuSpecsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class GpuSpecsTableUpdateService {

	@Autowired
	private GpuSpecsRepository gpuSpecsRepository;

	private final ObjectMapper objectMapper = new ObjectMapper();





	public void saveGpuData(String gpuKey, Map<String, Object> gpuData) {
		try {
			GpuSpec gpu = new GpuSpec();
			gpu.setGpuKey(gpuKey);
			gpu.setModelName((String) gpuData.getOrDefault("Model name", ""));
			gpu.setLaunchDate(parseLaunchDate(gpuData.get("Launch")));
			gpu.setCodeName((String) gpuData.getOrDefault("Code name", ""));
			gpu.setVendor((String) gpuData.getOrDefault("Vendor", ""));
			gpu.setBusInterface((String) gpuData.getOrDefault("Bus interface", ""));
			gpu.setCoreConfiguration((String) gpuData.getOrDefault("Core config", ""));
			gpu.setMemoryType((String) gpuData.getOrDefault("Memory Bus type", ""));
			gpu.setManufacturingProcess((String) gpuData.getOrDefault("Fab (nm)", ""));

			gpu.setMemoryBandwidthGbps(safeParseDouble(gpuData.get("Memory Bandwidth (GB/s)")));
			gpu.setMemoryBusWidthBits(safeParseInt(gpuData.get("Memory Bus width (bit)")));
			gpu.setMemorySizeGib(safeParseMiB(gpuData.get("Memory Size (MiB)")));
			gpu.setPowerConsumptionWatts(safeParseDouble(gpuData.get("TDP (Watts)")));
			gpu.setDieSizeMm(safeParseDouble(gpuData.get("Die size (mm)")));

			Double transistorsMillion = safeParseDouble(gpuData.get("Transistors (million)"));
			gpu.setTransistorCountBillions(transistorsMillion != null ? transistorsMillion / 1000.0 : null);

			// 🔥 Store full JSON as a string in extra_specs
			String jsonString = objectMapper.writeValueAsString(gpuData);
			gpu.setExtraSpecs(jsonString);

			gpuSpecsRepository.save(gpu);
			System.out.println("✅ Inserted GPU: " + gpuKey);

		} catch (Exception e) {
			System.err.println("❌ Error saving GPU " + gpuKey + ": " + e.getMessage());
			e.printStackTrace();
		}
	}

	private String parseLaunchDate(Object value) {
		if (value == null) return null;
		try {
			String val = value.toString().trim();
			// Normalize date format
			if (val.matches("\\d{4}-\\d{2}-\\d{2}.*")) {
				return val.split(" ")[0]; // keep just the date part
			}
			return val;
		} catch (Exception e) {
			return null;
		}
	}

	private Double safeParseDouble(Object value) {
		if (value == null) return null;
		try {
			String str = value.toString().replaceAll("[^0-9.]", "");
			if (str.isEmpty()) return null;
			return Double.parseDouble(str);
		} catch (Exception e) {
			return null;
		}
	}

	private Integer safeParseInt(Object value) {
		if (value == null) return null;
		try {
			String str = value.toString().replaceAll("[^0-9]", "");
			if (str.isEmpty()) return null;
			return Integer.parseInt(str);
		} catch (Exception e) {
			return null;
		}
	}

	private Double safeParseMiB(Object value) {
		if (value == null) return null;
		try {
			String[] parts = value.toString().split(" ");
			if (parts.length > 0) {
				return Double.parseDouble(parts[0].replaceAll("[^0-9.]", ""));
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}

