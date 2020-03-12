package io.pivotal.literx;

import io.pivotal.literx.domain.User;
import io.pivotal.literx.repository.ReactiveRepository;
import io.pivotal.literx.repository.ReactiveUserRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Learn how to control the demand.
 *
 * @author Sebastien Deleuze
 */
public class Part06Request {

    ReactiveRepository<User> repository = new ReactiveUserRepository();

//========================================================================================

    StepVerifier requestAllExpectFour(Flux<User> flux) {
        return StepVerifier.create(flux).expectNextCount(4).expectComplete();
    }

//========================================================================================

    StepVerifier requestOneExpectSkylerThenRequestOneExpectJesse(Flux<User> flux) {
        return StepVerifier.create(flux)
                .expectNext(User.SKYLER)
                .expectNext(User.JESSE)
                .expectNext(User.WALTER)
                .expectNext(User.SAUL).expectComplete();
    }

//========================================================================================

    Flux<User> fluxWithLog() {
        return repository.findAll();
    }

//========================================================================================

    Flux<User> fluxWithDoOnPrintln() {
        return fluxWithLog().doOnSubscribe(subscription -> System.out.println("firstname lastname"))
                .doOnComplete(() -> System.out.println("The end!"));
    }

}
