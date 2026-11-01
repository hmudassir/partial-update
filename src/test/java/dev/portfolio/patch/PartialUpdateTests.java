package dev.portfolio.patch;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.nullValue;

import dev.portfolio.patch.persistence.Profile;
import dev.portfolio.patch.persistence.ProfileRepository;

@SpringBootTest
@AutoConfigureMockMvc
class PartialUpdateTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ProfileRepository profiles;

	@BeforeEach
	void reset() {
		Profile profile = profiles.findById(1L).orElseThrow();
		profile.setTitle("Hello");
		profile.setSummary("Keep");
		profiles.save(profile);
	}

	@Test
	void anOmittedFieldStaysAndAnExplicitNullClearsIt() throws Exception {
		mvc.perform(patch("/api/profiles/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"Updated\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Updated"))
				.andExpect(jsonPath("$.summary").value("Keep"));

		mvc.perform(patch("/api/profiles/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"summary\":null}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Updated"))
				.andExpect(jsonPath("$.summary").value(nullValue()));
	}

	@Test
	void anUnknownFieldOrAnEmptyObjectIsRejected() throws Exception {
		mvc.perform(patch("/api/profiles/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"extra\":1}"))
				.andExpect(status().isBadRequest());
		mvc.perform(patch("/api/profiles/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
				.andExpect(status().isBadRequest());
		mvc.perform(patch("/api/profiles/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"Still Hello\"}"))
				.andExpect(jsonPath("$.summary").value("Keep"));
	}
}
