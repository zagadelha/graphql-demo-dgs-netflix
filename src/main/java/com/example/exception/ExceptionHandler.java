package com.example.exception;

import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.GraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class ExceptionHandler implements DataFetcherExceptionHandler {

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters) {

        if (handlerParameters.getException() instanceof MyException) {

            Map<String, Object> debugInfo = new HashMap<>();
            debugInfo.put("somefield", "somevalue");

            GraphQLError graphqlError = TypedGraphQLError.newInternalErrorBuilder()
                    .message("This custom thing went wrong!")
                    .debugInfo(debugInfo)
                    .path(handlerParameters.getPath()).build();

            DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build();

            return CompletableFuture.completedFuture(result);

        } else if (handlerParameters.getException() instanceof BusinessException) {

            Map<String, Object> debugInfo = new HashMap<>();
            debugInfo.put("MyExceptionField", "MyExceptionField");

            GraphQLError graphqlError = TypedGraphQLError.newInternalErrorBuilder()
                    .message("Message from MyException: " + handlerParameters.getException().getMessage())
                    .debugInfo(debugInfo)
                    .path(handlerParameters.getPath()).build();

            DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build();

            return CompletableFuture.completedFuture(result);

        }else {
            return DataFetcherExceptionHandler.super.handleException(handlerParameters);
        }
    }
}
