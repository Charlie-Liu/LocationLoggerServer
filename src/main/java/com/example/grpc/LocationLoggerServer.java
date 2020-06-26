package com.example.grpc;

import io.grpc.*;

public class LocationLoggerServer
{

    public static void main( String[] args ) throws Exception
    {
      DbLoader dbLoader = new DbLoader();
      Server server = ServerBuilder.forPort(8080)
        .addService(new LocationLoggerServiceImpl(dbLoader))
        .build();

      server.start();
      System.out.println("Server started");
      server.awaitTermination();
      dbLoader.endConnetion();
    }
}
