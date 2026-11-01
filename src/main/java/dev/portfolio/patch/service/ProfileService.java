package dev.portfolio.patch.service;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.portfolio.patch.api.ProfileResponse;
import dev.portfolio.patch.error.ProfileUpdateException;
import dev.portfolio.patch.persistence.Profile;
import dev.portfolio.patch.persistence.ProfileRepository;
import tools.jackson.databind.JsonNode;

@Service
public class ProfileService {

	private static final Set<String> FIELDS = Set.of("title", "summary");

	private final ProfileRepository profiles;

	public ProfileService(ProfileRepository profiles) {
		this.profiles = profiles;
	}

	@Transactional
	public ProfileResponse patch(Long id, JsonNode body) {
		if (body == null || !body.isObject()) {
			throw bad("A JSON object is required");
		}
		for (String field : body.propertyNames()) {
			if (!FIELDS.contains(field)) {
				throw bad("Unknown field: " + field);
			}
		}
		boolean titlePresent = body.has("title");
		boolean summaryPresent = body.has("summary");
		if (!titlePresent && !summaryPresent) {
			throw bad("At least one field is required");
		}
		Profile profile = profiles.findById(id).orElseThrow(
				() -> new ProfileUpdateException(HttpStatus.NOT_FOUND.value(), "Profile was not found"));
		if (titlePresent) {
			profile.setTitle(requiredText(body.get("title"), "title"));
		}
		if (summaryPresent) {
			JsonNode summary = body.get("summary");
			if (summary.isNull()) {
				profile.setSummary(null);
			}
			else {
				profile.setSummary(requiredText(summary, "summary"));
			}
		}
		return new ProfileResponse(profile.getId(), profile.getTitle(), profile.getSummary());
	}

	private static String requiredText(JsonNode node, String field) {
		if (node == null || !node.isString() || node.asString().isBlank()) {
			throw bad(field + " must be text");
		}
		return node.asString().trim();
	}

	private static ProfileUpdateException bad(String message) {
		return new ProfileUpdateException(HttpStatus.BAD_REQUEST.value(), message);
	}
}
