/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pivotal.literx;

import io.pivotal.literx.domain.User;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * Learn how to use StepVerifier to test Mono, Flux or any other kind of Reactive Streams Publisher.
 *
 * @author Sebastien Deleuze
 * @see <a href="https://projectreactor.io/docs/test/release/api/reactor/test/StepVerifier.html">StepVerifier Javadoc</a>
 */
public class Part03StepVerifier {

//========================================================================================

    void expectFooBarComplete(Flux<String> flux) {
        StepVerifier.create(flux)
                .expectNext("foo")
                .expectNext("bar")
                .expectComplete()
                .verify();
    }

//========================================================================================

    void expectFooBarError(Flux<String> flux) {
        StepVerifier.create(flux)
                .expectNext("foo")
                .expectNext("bar")
                .expectError(RuntimeException.class)
                .verify();
    }

//========================================================================================

    // and another one with "jpinkman" then completes successfully.
    void expectSkylerJesseComplete(Flux<User> flux) {
        StepVerifier.create(flux)
                .expectNext(new User("swhite", null, null))
                .expectNext(new User("jpinkman", null, null))
                .expectComplete()
                .verify();
    }

//========================================================================================

    void expect10Elements(Flux<Long> flux) {
        StepVerifier.create(flux)
                .expectNextCount(10)
                .expectComplete()
                .verifyThenAssertThat().tookLessThan(Duration.ofSeconds(11));
    }

//========================================================================================

    // by manipulating virtual time thanks to StepVerifier#withVirtualTime, notice how long the test takes
    void expect3600Elements(Supplier<Flux<Long>> supplier) {
        StepVerifier.Step<Long> expectSubscription = StepVerifier
                .withVirtualTime(supplier);


        for (long i = 0; i < 3600; i++)
            expectSubscription =
                    expectSubscription
                            .thenAwait(Duration.ofSeconds(1))
                            .expectNext(i);


        expectSubscription
                .expectComplete()
                .verifyThenAssertThat()
                .tookLessThan(Duration.ofSeconds(3600));

    }

    private void fail() {
        throw new AssertionError("workshop not implemented");
    }

}
