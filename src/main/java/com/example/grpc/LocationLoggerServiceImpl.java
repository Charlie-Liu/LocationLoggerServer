package com.example.grpc;

import io.grpc.stub.StreamObserver;

import com.example.grpc.LocationInfo;
import com.example.grpc.LocationAddResponse;
import com.example.grpc.LocationLoggerServiceGrpc;

import java.util.ArrayList;

public class LocationLoggerServiceImpl extends LocationLoggerServiceGrpc.LocationLoggerServiceImplBase {

    DbLoader mDbLoader;

    public LocationLoggerServiceImpl(DbLoader dbLoader) {
        super();
        mDbLoader = dbLoader;
    }

    @Override
    public void locationAdd(LocationInfo locationInfo, StreamObserver<LocationAddResponse> responseObserver) {

        String address = locationInfo.getAddress();
        double latitude = locationInfo.getLatitude();
        double longitude = locationInfo.getLongitude();
        String strTime = locationInfo.getTime();
        mDbLoader.addLocation(address, latitude, longitude, strTime);

        LocationAddResponse response = LocationAddResponse.newBuilder()
            .setResponse("Location received")
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void locationList(LocationListRequest locationListRequest,
        StreamObserver<LocationInfo> responseObserver) {

        ArrayList<LocationInfo> locations =
            mDbLoader.listLocation(locationListRequest.getTime(), locationListRequest.getNum());
        for (LocationInfo location : locations) {
            responseObserver.onNext(location);
        }
        responseObserver.onCompleted();
    }
}
