package org.example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;



/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Flux<Integer> complexAsyncStream = Flux.range(1, 20)
        //Filter even numbers
                .filter(number -> number % 2 == 0)
                //multiply each number by 2
                        .map(number -> number * 2)
                // Introduce an asynchronous delay for each element
                .flatMap(number ->
                        Mono.just(number)
                                .delayElement(Duration.ofMillis(10000))
                )
                //take the first teen elements
                                .take(3);

        // Creating a subscriber to consume the emitted values
        complexAsyncStream.subscribe(
                App::handleData,
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Done!")
        );
        // Block until the stream completes (not recommended in a reactive context)
        complexAsyncStream.blockLast();


    }


    // Callback method to handle each received element
    private static void handleData(Integer value) {
        // Manipulate the received data
        String manipulatedValue = "Manipulated: " + value;

        // Perform additional non-blocking tasks with the manipulated value
        System.out.println(manipulatedValue);
        // For example, you can send it to another service, update a database, etc.
    }
}
