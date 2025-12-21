package com.example.GPU_DATA.Controller;


import com.example.GPU_DATA.Service.GpuSpecsTableUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GpuSpecsTableUpdateController {

	@Autowired
	private GpuSpecsTableUpdateService gpuSpecsTableUpdateService;


	@GetMapping("update/sqlTable")
	public String GpuSpecTableUpdate(){

		return "done";
	}

	@PostMapping("/import")
	public String importGpuData(@RequestBody Map<String, Map<String, Object>> gpuData) {
		for (Map.Entry<String, Map<String, Object>> entry : gpuData.entrySet()) {
			gpuSpecsTableUpdateService.saveGpuData(entry.getKey(), entry.getValue());
		}
		return "✅ GPU Data Imported Successfully";
	}




}
