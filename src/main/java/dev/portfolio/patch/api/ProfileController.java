package dev.portfolio.patch.api;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.portfolio.patch.service.ProfileService;
import tools.jackson.databind.JsonNode;

@RestController
public class ProfileController {

	private final ProfileService profiles;

	public ProfileController(ProfileService profiles) {
		this.profiles = profiles;
	}

	@PatchMapping("/api/profiles/{id}")
	public ProfileResponse patch(@PathVariable Long id, @RequestBody JsonNode body) {
		return profiles.patch(id, body);
	}
}
