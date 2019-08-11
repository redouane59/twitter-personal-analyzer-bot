package com.socialMediaRaiser.twitter.handler;

public class ECSTaskWrapper {
    public static void main(String[] args) {

        String input = System.getenv("LAMBDA_INPUT");
        if (input == null || input.length() == 0) {
            System.out.println("Not being called from an AWS Lambda function.");
            System.exit(1);
        }

        StartStreamingHandler lde = new StartStreamingHandler();

        System.out.println(lde.handleRequest(input, null));
    }
}
