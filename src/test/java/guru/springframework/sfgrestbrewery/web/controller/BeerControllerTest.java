package guru.springframework.sfgrestbrewery.web.controller;

import guru.springframework.sfgrestbrewery.bootstrap.BeerLoader;
import guru.springframework.sfgrestbrewery.services.BeerService;
import guru.springframework.sfgrestbrewery.web.model.BeerDto;
import guru.springframework.sfgrestbrewery.web.model.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebFluxTest(BeerController.class)
class BeerControllerTest {

		@Autowired
		WebTestClient webTestClient;

		@MockBean
		BeerService beerService;

		BeerDto validBeer;

		@BeforeEach
		void setUp() {
				validBeer = BeerDto.builder()
						.beerName("Test beer")
						.beerStyle("PALE_ALE")
						.upc(BeerLoader.BEER_1_UPC)
						.build();
		}

		@Test
		void getBeerById() {
				Integer beerId = 1;
				given(beerService.getById(any(), any())).willReturn(Mono.just(validBeer));

				webTestClient.get()
						.uri("/api/v1/beer/" + beerId)
						.accept(MediaType.APPLICATION_JSON)
						.exchange()
						.expectStatus().isOk()
						.expectBody(BeerDto.class)

						.value(beerDto -> beerDto.getBeerName(), equalTo(validBeer.getBeerName()));
		}

		@Test
		void listBeers() {
				BeerPagedList beerPagedList = new BeerPagedList(List.of(validBeer));
				given(beerService.listBeers(any(), any(), any(), any())).willReturn(Mono.just(beerPagedList));

				webTestClient.get()
						.uri("/api/v1/beer")
						.accept(MediaType.APPLICATION_JSON)
						.exchange()
						.expectStatus().isOk()
						.expectBody(BeerDto.class)
						.value(beerDto -> beerPagedList.getContent().get(0).getBeerName(), equalTo(validBeer.getBeerName()));
		}

		@Test
		void getBeerByUpc() {
				given(beerService.getByUpc(any())).willReturn(Mono.just(validBeer));

				webTestClient.get()
						.uri("/api/v1/beerUpc/" + "123123123")
						.accept(MediaType.APPLICATION_JSON)
						.exchange()
						.expectStatus().isOk()
						.expectBody(BeerDto.class)

						.value(beerDto -> beerDto.getBeerName(), equalTo(validBeer.getBeerName()));

		}
}