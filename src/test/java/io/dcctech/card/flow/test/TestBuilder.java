/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.test;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;

import static io.dcctech.card.flow.test.JsonHelper.extractJsonPathValue;

public class TestBuilder {

    private final ResultMappingBuilder resultMapping = new ResultMappingBuilder();
    private ResultActions lastResult;
    private Object mappedResult;
    private MockHttpSession session;

    public TestBuilder useSession(MockHttpSession session) {
        this.session = session;
        return this;
    }

    public <P> ResultMappingBuilder<P> useParam(P value) {
        this.mappedResult = value;
        if (value instanceof ResultActions) {
            this.lastResult = (ResultActions) value;
        }
        return resultMapping;
    }

    public ResultMappingBuilder<ResultActions> perform(TestStep firstStep) throws Exception {
        lastResult = firstStep.perform();
        mappedResult = lastResult;
        return resultMapping;
    }

    public ResultMappingBuilder<ResultActions> performWithSession(TestStepWithSession testStepWithSession) throws Exception {
        lastResult = testStepWithSession.perform(session);
        mappedResult = lastResult;
        return (ResultMappingBuilder<ResultActions>) resultMapping;
    }

    public interface TestStep {
        ResultActions perform() throws Exception;
    }

    public interface TestStepWithSession {
        ResultActions perform(MockHttpSession session) throws Exception;
    }

    public interface TestStepWithParameter<P> {
        ResultActions perform(P param) throws Exception;
    }

    public interface TestStepWithSessionAndParameter<P> {
        ResultActions perform(MockHttpSession session, P param) throws Exception;
    }

    public class ResultMappingBuilder<T> {
        public ResultMappingBuilder<T> andDo(ConsumerWithExceptions<T> consumer) throws Exception {
            consumer.accept((T) mappedResult);
            return this;
        }

        public ResultMappingBuilder<T> andExpect(ResultMatcher resultMatcher) throws Exception {
            lastResult.andExpect(resultMatcher);
            return this;
        }

        public ResultMappingBuilder<T> andExpectThat(String reason, Matcher<T> matcher) throws Exception {
            MatcherAssert.assertThat(reason, (T) mappedResult, matcher);
            return this;
        }

        public <P> ResultMappingBuilder<T> andExpectValueAtJsonPath(String reason, String jsonPath, Matcher<P> matcher) throws Exception {
            final P valueAtPath = extractJsonPathValue(mappedResult, jsonPath);
            MatcherAssert.assertThat(reason, valueAtPath, matcher);
            return this;
        }

        public <P> ResultMappingBuilder<T> andExpectValueAtJsonPath(String jsonPath, Matcher<P> matcher) throws Exception {
            final P valueAtPath = extractJsonPathValue(mappedResult, jsonPath);
            MatcherAssert.assertThat(valueAtPath, matcher);
            return this;
        }

        public ResultMappingBuilder<T> andExpectThat(Matcher<T> matcher) throws Exception {
            MatcherAssert.assertThat((T) mappedResult, matcher);
            return this;
        }

        public ResultMappingBuilder<T> andDoWithHandler(ResultHandler resultHandler) throws Exception {
            lastResult.andDo(resultHandler);
            return this;
        }

        public ResultMappingBuilder<T> withSession(MockHttpSession session) {
            TestBuilder.this.session = session;
            return this;
        }

        public <R> ResultMappingBuilder<R> andMap(FunctionWithExceptions<T, R> mapper) throws Exception {
            mappedResult = mapper.apply((T) mappedResult);
            return (ResultMappingBuilder<R>) this;
        }

        public <R> ResultMappingBuilder<R> andMapWithJsonPath(String jsonPath, Class<R> returnType) throws Exception {
            mappedResult = extractJsonPathValue(mappedResult, jsonPath);
            return (ResultMappingBuilder<R>) this;
        }

        public ResultMappingBuilder<ResultActions> thenPerformWithParam(TestStepWithParameter<T> testStepWithParameter) throws Exception {
            lastResult = testStepWithParameter.perform((T) mappedResult);
            mappedResult = lastResult;
            return (ResultMappingBuilder<ResultActions>) this;
        }

        public ResultMappingBuilder<ResultActions> thenPerformWithSessionAndParam(TestStepWithSessionAndParameter<T> testStepWithSessionAndParameter) throws Exception {
            lastResult = testStepWithSessionAndParameter.perform(session, (T) mappedResult);
            mappedResult = lastResult;
            return (ResultMappingBuilder<ResultActions>) this;
        }

        public ResultMappingBuilder<ResultActions> thenPerformWithSession(TestStepWithSession testStepWithSession) throws Exception {
            lastResult = testStepWithSession.perform(session);
            mappedResult = lastResult;
            return (ResultMappingBuilder<ResultActions>) this;
        }

        public T getResult() {
            return (T) mappedResult;
        }
    }
}
