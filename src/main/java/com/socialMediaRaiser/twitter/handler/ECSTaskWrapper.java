package com.socialMediaRaiser.twitter.handler;

import java.util.logging.Logger;

public class ECSTaskWrapper {

    private static final Logger LOGGER = Logger.getLogger(ECSTaskWrapper.class.getName());

    public static void main(String[] args) {

        String input = System.getenv("LAMBDA_INPUT");
        if (input == null || input.length() == 0) {
            LOGGER.info(()->"Not being called from an AWS Lambda function.");
            System.exit(1);
        }

        StartStreamingHandler lde = new StartStreamingHandler();

        LOGGER.info(()->lde.handleRequest(input, null));
    }
}
