package com.spring.demo.springreactivesampleapplication.controller;

import com.spring.demo.springreactivesampleapplication.model.Foo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Slf4j
@RestController
public class FooController {

	/**
	 * Returns a new element of Foo every 1 second.
	 * @return Flux of Foo every 1 second.
	 */
	@GetMapping(value = "foos", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Foo> foos() {
		return Flux.interval(Duration.ofSeconds(1))
				.map(index -> Foo.create(UUID.randomUUID().toString(), "sample-foo-element"))
				.doOnNext(foo -> log.info("Emitting next element {}.", foo));
	}

	/**
	 * Returns a new element of Foo every 1 second until the specified cap.
	 * @param cap - Return elements until the cap.
	 * @return - Flux of Foo every 1 second.
	 */
	@GetMapping(value = "foos/{cap}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Foo> foos(@PathVariable Integer cap) {
		return Flux.fromIterable(getListOfFoos(cap))
				.delayElements(Duration.ofSeconds(1))
				.doOnNext(foo -> log.info("Emitting next element {}.", foo));
	}

	/**
	 * Returns a new element of ServerSentEvent representing Foo every 1 second.
	 * @return ServerSentEvent encapsulated with Foo element.
	 */
	@GetMapping(value = "foo-server-sent-event")
	public Flux<ServerSentEvent<Foo>> fooWithServerSentEvent() {
		return Flux.interval(Duration.ofSeconds(1))
				.map(index -> ServerSentEvent.<Foo>builder()
						.data(Foo.create(UUID.randomUUID().toString(), "sample-foo-element"))
						.id(String.valueOf(index))
						.build())
				.doOnNext(serverSentEvent -> log.info("Emitting next element {}.", serverSentEvent));
	}

	private Collection<Foo> getListOfFoos(Integer cap) {
		List<Foo> foos = new ArrayList<>();
		IntStream.range(1, cap)
				.forEach(index -> foos.add(Foo.create(UUID.randomUUID().toString(), "element-" + index)));
		return foos;
	}
}
