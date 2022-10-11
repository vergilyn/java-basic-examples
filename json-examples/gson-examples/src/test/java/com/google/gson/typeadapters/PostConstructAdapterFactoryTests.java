package com.google.gson.typeadapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

public class PostConstructAdapterFactoryTests {

	@Test
	public void test() throws Exception {
		Gson gson = new GsonBuilder()
				.registerTypeAdapterFactory(new PostConstructAdapterFactory())
				.create();

		gson.fromJson("{\"bread\": \"white\", \"cheese\": \"cheddar\"}", Sandwich.class);

		try {
			//
			gson.fromJson("{\"bread\": \"cheesey bread\", \"cheese\": \"swiss\"}", Sandwich.class);
		} catch (IllegalArgumentException expected) {
			Assertions.assertEquals("too cheesey", expected.getMessage());
		}
	}

	@Test
	public void testList() {
		MultipleSandwiches sandwiches = new MultipleSandwiches(Arrays.asList(
				new Sandwich("white", "cheddar"),
				new Sandwich("whole wheat", "swiss")));

		Gson gson = new GsonBuilder()
				.registerTypeAdapterFactory(new PostConstructAdapterFactory())
				.create();

		// Throws NullPointerException without the fix in https://github.com/google/gson/pull/1103
		String json = gson.toJson(sandwiches);
		Assertions.assertEquals("{\"sandwiches\":["
				                        + "{\"bread\":\"white\",\"cheese\":\"cheddar\"},"
				                        + "{\"bread\":\"whole wheat\",\"cheese\":\"swiss\"}"
				                        + "]}",
		             json);

		MultipleSandwiches sandwichesFromJson = gson.fromJson(json, MultipleSandwiches.class);
		Assertions.assertEquals(sandwiches, sandwichesFromJson);
	}

	@SuppressWarnings("overrides") // for missing hashCode() override
	static class Sandwich {
		public String bread;
		public String cheese;

		public Sandwich(String bread, String cheese) {
			this.bread = bread;
			this.cheese = cheese;
		}

		@PostConstruct
		private void validate() {
			if (bread.equals("cheesey bread") && cheese != null) {
				throw new IllegalArgumentException("too cheesey");
			}
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof Sandwich)) {
				return false;
			}
			final Sandwich other = (Sandwich) o;
			if (this.bread == null ? other.bread != null : !this.bread.equals(other.bread)) {
				return false;
			}
			if (this.cheese == null ? other.cheese != null : !this.cheese.equals(other.cheese)) {
				return false;
			}
			return true;
		}
	}

	@SuppressWarnings("overrides") // for missing hashCode() override
	static class MultipleSandwiches {
		public List<Sandwich> sandwiches;

		public MultipleSandwiches(List<Sandwich> sandwiches) {
			this.sandwiches = sandwiches;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof MultipleSandwiches)) {
				return false;
			}
			final MultipleSandwiches other = (MultipleSandwiches) o;
			if (this.sandwiches == null ? other.sandwiches != null : !this.sandwiches.equals(other.sandwiches)) {
				return false;
			}
			return true;
		}
	}
}