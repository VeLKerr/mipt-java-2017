package ru.mipt.java2017.sm06;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class HelloWorldClient {
  private static final Logger logger = LoggerFactory.getLogger("Client");

  private final ManagedChannel channel;
  private final GreeterGrpc.GreeterBlockingStub blockingStub;

  public HelloWorldClient(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext(true)
        .build());
  }

  HelloWorldClient(ManagedChannel channel) {
    this.channel = channel;
    blockingStub = GreeterGrpc.newBlockingStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public void greet(String name) {
    logger.info("Will try to greet {} ... ", name);
    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
    HelloReply response;
    try {
      response = blockingStub.sayHello(request);
    } catch (StatusRuntimeException e) {
      logger.error("RPC failed: {}", e.getStatus());
      return;
    }
    logger.info("Greeting: {}", response.getMessage());
  }

  public static void main(String[] args) throws Exception {
    HelloWorldClient client = new HelloWorldClient("localhost", 50051);
    try {
      String user = "MIPT";
      if (args.length > 0) {
        user = args[0]; /* Use the arg as the name to greet if provided */
      }
      client.greet(user);
    } finally {
      client.shutdown();
    }
  }
}
