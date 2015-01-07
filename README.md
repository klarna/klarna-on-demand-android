# Klarna on Demand
[![Build Status](https://travis-ci.org/yuval-netanel/klarna-on-demand-android.svg?branch=master)](https://travis-ci.org/yuval-netanel/klarna-on-demand-android)

Klarna on-demand allows you to integrate Klarna's payment solution in mobile apps that offer on demand services. It's a perfect fit for apps selling concert tickets, taxi rides, food pick-ups, etc.

This project contains Klarna's on-demand purchase SDK for Android 4+, as well as a sample application utilizing the SDK.

![It's Klarna in your App](screenshot.png)

While not necessary, the simplest way to get going with both the sample application and the SDK is to use [Gradle](https://www.gradle.org/) and so all the following instructions will focus on that approach. For an alternative approach, see the [official documentation (coming soon)](http://developers.klarna.com).

## Using the SDK
Have a look at the [integration guide](doc/integration.md) for full details on how to use our SDK in your application.

<a name="running_the_application"></a>
## Running the sample application
We provide instructions on how to set up the project for both [Android Studio](doc/running-android-studio.md) and [Eclipse](doc/running-eclipse.md).

**Note:** While the instructions linked above will get the application up and running, you will not be able to successfully perform purchases without a backend for the application to interact with. You should go [here](https://github.com/klarna/sample-app-backend) and follow the instructions provided to get a sample backend running on your local machine.

## Contributing
You'd like to help us out? That's great! Here's what you need to do in order to contribute.

### Setup

1. Fork the project and clone your repository
2. Follow the instructions for [running the sample application](#running_the_application), as the SDK and sample application are part of the same project

### Tests
Pull requests must include tests for their proposed fixes/functionality. We use [JUnit](http://junit.org/) (v3) to write our tests and you can find the tests themselves [here](klarna-on-demand/src/androidTest/java/com/klarna/ondemand).

## License
Klarna on Demand is available under the Apache 2.0 license. See the [LICENSE](./LICENSE) file for more info.