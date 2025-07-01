# ZeniMarketplaceAgent

Flow of the project!![mcp-clazz.jpg](mcp-clazz.jpg)

# Spring AI - Lab Instructions

Prerequisites: before starting this course, you should have the following installed:

Java Development Kit (ideally version 21 or later)
Maven (version 3.8.6 or later)
Node.js (version 18 or later)

An IDE such as IntelliJ Community Edition or Ultimate Edition (ideally the latest version)

an OpenAI API KEY to get [here]( https://ctxt.io/2/AAB4vv0pEA)

General guideline

This lab guide relies on the examples seen in the [slides] (https://docs.google.com/presentation/d/1c7qB-nLaqTAZbm62Sd5AhUxfQfNpKlFsF-q-EKJJZHg/edit?usp=sharing).
In order to have all the information you need, you should have the lab instructions and the slides side by side, so you can refer to the examples seen in the slides at any time.



## Table of Contents

1. [Setting up Spring AI with OpenAI](#setting-up-spring-ai-with-openai)
2. [Using the ChatClient](#using-the-chatclient)
3. [Logs, entities, and images](#logs-entities-and-images)
4. [Retrieval Augmented Generation](#retrieval-augmented-generation)

---

### 1. Setting up Spring AI with OpenAI

Our first goal is to install the Spring AI dependencies and write a simple API call to an LLM (ChatGPT will be used as an example)

#### 1.1 Setting up your Spring AI project
- Make sure you have Java version 21 installed.
- Install the dependencies using `mvn clean install -DskipTests`

If you don't have installed IntelliJ and Java 21 you can use this online environment for this HandsOn [OnlineIDE](https://replit.com/@puls972/SpringAiHandsOn)
- Create a new account (Free)
- Fork the project
- Access the shell
- Install the dependencies using `mvn clean install -DskipTests`

#### 1.2 application.properties configuration
-Specify `gpt-4o` as the OpenAI model to be used in the parameter `spring.ai.openai.chat.options.model`

As seen in the slides, it is not a best practice to store an API key inside [application.properties](src/main/resources/application.properties) because it would be stored on git.
Instead, it should use the ${OPENAI_API_KEY} syntax in order to refer to an environment variable.
You do not need to setup the environment variable at this stage, as it will be later setup inside your JUnit test

#### 1.3 Running your first JUnit test
- In `src/main/test`, update the [ApplicationTests](src/test/java/com/spring/ApplicationTests.java) class:
  ```java
  @SpringBootTest
  class ApplicationTests {

      @Value("${spring.ai.openai.api-key}")
      private String apiKey;

      @Test
      void contextLoads() {
          assertThat(apiKey).isNotEmpty();
      }
  }
  ```
- As seen in the slides, update your JUnit test configuration so it properly sets the value for the environment variable OPENAI_API_KEY, you can get the key [here]( https://ctxt.io/2/AAB4vv0pEA) (not required for online IDE)
- Run your test to verify setup.
- (To run the test for the online IDE execute in the shell `mvn -Dtest=ApplicationTests test` )

### 2. Using the ChatClient

#### 2.1 Using the ChatClient for book recommendations
1. Update the class [BookRecommendationService](src/main/java/com/spring/book/BookRecommendationService.java)  in `com.spring.book`.

   As its name says, it is a Spring service and should be annotated with the `@Service` stereotype annotation

2. Initialize the `chatClient` in the [BookRecommendationService](src/main/java/com/spring/book/BookRecommendationService.java) constructor

3. Inside [BookRecommendationService](src/main/java/com/spring/book/BookRecommendationService.java), Complete the method `findMostPopularProgrammingBooks`. It should use a `ChatClient` instance in order to ask for the 5 best programming books in year 2023 and return the response in a String format.

4. Update a JUnit test [BookRecommendationServiceTest](src/test/java/com/spring/book/BookRecommendationServiceTest.java) complete the methode `shouldFindMostPopularProgrammingBooks`

Before running `BookRecommendationServiceTest`, edit its run configuration and add an environment variable `OPENAI_API_KEY` for the OpenAI API [key]( https://ctxt.io/2/AAB4vv0pEA) (not required for online IDE)

Run `BookRecommendationServiceTest`. If you are able to see the response in the logs, you have successfully used Spring AI with OpenAI, congratulations!

(For the online IDE execute in the shell `mvn -Dtest=BookRecommendationServiceTest#shouldFindMostPopularProgrammingBooks test` )
### 3. Logs, Entities, and Images

#### 3.1 Using logs to understand API calls
If you did not complete the section 2, please checkout the branch : `solution_2.1`
- Enable a `SimpleLoggerAdvisor` for `BookRecommendationService`.

  Inside [BookRecommendationService](src/main/java/com/spring/book/BookRecommendationService.java), update the call to the builder so it declares a `SimpleLoggerAdvisor`.
  Add the proper log declaration inside [application.properties](src/main/resources/application.properties)
- Run [BookRecommendationServiceTest](src/test/java/com/spring/book/BookRecommendationServiceTest.java) again and inspect the generated logs. Try and answer the following questions:

  How many tokens did Spring AI use for your request ?

  What was the default [temperature](https://www.iguazio.com/glossary/llm-temperature/) ?

#### 3.2 Structured Output
If you did not complete the section 3.1, please checkout the branch : `solution_3.1`
- Inside the package `com.spring.book`, create a new Java record called Book. It should have 2 attributes: author and title.
- Inside [BookRecommendationService](src/main/java/com/spring/book/BookRecommendationService.java), add a method called `findFictionBook`. It should use a ChatClient instance in order to ask for the best fiction book in year 2023 and return the response as a Book entity.
- Inside BookRecommendationServiceTest, add a method `shouldFindFictionBook`. It should call BookRecommendationService.findFictionBook(), log the result, and assert that the result is not empty.
- Run `BookRecommendationServiceTest` and double-check that the request to OpenAI provides JSON Schema guidelines. (Uncomment the @Configuration in [HttpLoggingConfiguration](src/main/java/com/spring/config/HttpLoggingConfiguration.java))

  (For the online IDE execute in the shell `mvn -Dtest=BookRecommendationServiceTest#shouldFindFictionBook test` )


If you are able to see the response in the logs, you have successfully mapped your response to an entity, congratulations!


#### 3.3 What’s the Weather Like? (Image Model API)
If you did not complete the section 3.2, please checkout the branch : `solution_3.2`

Inside the folder src/main/resources, you have a folder [weather](src/main/resources/weather), check the lab file [singapore-weather.png](src/main/resources/weather/singapore-weather.png).
- Create a new java package `com.spring.weather`. Inside this package, create a class called `WeatherService`. As seen in the slides, it should load [singapore-weather.png](src/main/resources/weather/singapore-weather.png) as a `org.springframework.core.io.Resource` and use it to make a call to the LLM. The question should be "what will be the weather like on Tuesday"
- When done, create a test class called `com.spring.weather.WeatherServiceTest`.
  Before running `WeatherServiceTest`, edit its run configuration and add an environment variable `OPENAI_API_KEY` for the OpenAI API [key]( https://ctxt.io/2/AAB4vv0pEA) (not required for online IDE)

(For the online IDE execute in the shell `mvn -Dtest=WeatherServiceTest test` )



If you are able to call `WeatherService` successfully and get the correct weather forecast, you have completed this lab, congratulations!

### 4. (BONUS) Retrieval Augmented Generation / Vector DB

Congratulation ! The handsOn is now over. If you want more, here is a new challenge :
- Checkout the project : https://github.com/michaelisvy/demo-spring-ai/
- Read the ReadMe instruction and try to run one of the following : `MusicWithContextService`, `BookSearchService`





