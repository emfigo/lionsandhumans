# Lions and Humans 

### Prerequisites

  * [Redis](http://redis.io/)
  * [RabbitMQ](https://www.rabbitmq.com/)
  * [Leiningen](http://leiningen.org/)

### Problem

We have 2 shores (lets call them shore A and shore B) and 1 boat. There are N lions and N humans in shore A and they all need to cross to shore B using the boat.

### Constraints

 * A lion can't use the boat without a human.
 * If there are more lions than humans in the shore, they will eat the human.
 * A boat can have a maximun of 2 entities at the same time if N <= 3. For N > 3 the boat will have a size of N - 1.
 * The boat can't drive itself.

### Requirements

 * The humans will be represented with the letter H, followed by a number between 1 and N. Eg, H1
 * The lions will be represented with the letter L, followed by a number between 1 and N. Eg, L1
 * The boat will be simulated using a RabbitMQ message following this structure:

   ```json
      {
        "seatA": "L1",
        "seatB": "H1"
      }
    ```
 * The name of the first queue will be "coolest_transport_to_shore_b" with a routing key "paradise.boat.shoreb".
 * The name of the second queue will be "coolest_transport_to_shore_a" with a routing key "paradise.boat.shorea".
 * Each shore will be stored in Redis with a key "shore(a/b)_timestamp", A or B depending on which shore you are trying to implement, and a value of a list with all the entities, [L1,L2,L3,H1,H2,H3] for that shore. Each shore should have a value for the same timestamp (it can be an empty list).
 * Each entity is unique. Eg, there can't be 2 lions L2 and L2.
 * The problem needs to be implemented in clojure.
 * The trigger for the main thread is ``` lein run 3 ``` and automatically will setup the initial values in Redis for the shore A. 

## Libraries

Any library can be used, but is recommended to use the following:

 * [RabbitMQ clojure](https://github.com/michaelklishin/langohr)
 * [Redis clojure](https://github.com/ptaoussanis/carmine)

