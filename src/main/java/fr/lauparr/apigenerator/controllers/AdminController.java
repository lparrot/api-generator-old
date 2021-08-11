package fr.lauparr.apigenerator.controllers;

import fr.lauparr.apigenerator.pojo.dto.CpuUsageDTO;
import fr.lauparr.apigenerator.pojo.dto.JvmInfoDTO;
import fr.lauparr.apigenerator.pojo.dto.ThreadInfoDTO;
import fr.lauparr.apigenerator.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private ApplicationService applicationService;

	@GetMapping("/cpu_usage")
	public List<CpuUsageDTO> getAllCpuUsageInfo() {
		return applicationService.getCpuUsage();
	}

	@GetMapping("/threads")
	public List<ThreadInfoDTO> getAllThreadInfo() {
		return applicationService.getThreads();
	}

	@GetMapping("/jvm")
	public List<JvmInfoDTO> getAllJvmInfo(@RequestParam(required = false, defaultValue = "true") boolean heap) {
		if (heap) {
			return applicationService.getJvmInfos();
		}
		return applicationService.getJvmNonHeapInfos();
	}

}
