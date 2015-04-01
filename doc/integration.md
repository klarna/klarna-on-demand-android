#Integration Guide
This guide includes all information necessary to receive payments from a user of your application through Klarna. In this guide, you will see how to allow the user to register his device with Klarna, change payment preferences and perform purchases.

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
###Table of Contents

- [Including the SDK in your project](#including-the-sdk-in-your-project)
- [Supplying your API key](#supplying-your-api-key)
- [The registration view](#the-registration-view)
  - [Showing the view](#showing-the-view)
  - [Interacting with the view](#interacting-with-the-view)
  - [When should you show the registration view?](#when-should-you-show-the-registration-view)
- [Performing purchases](#performing-purchases)
  - [Purchase example](#purchase-example)
  - [Signing requests](#signing-requests)
- [The preferences view](#the-preferences-view)
  - [Showing the view](#showing-the-view-1)
  - [Interacting with the view](#interacting-with-the-view-1)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


##Including the SDK in your project
This guide assumes you use [Gradle](https://www.gradle.org/) to build your project. If you do not, refer to our [official documentation (coming soon)](http://developers.klarna.com) for an alternative setup approach.

Simply open up your build.gradle file and add the following dependency:

```groovy
dependencies {
  compile 'com.klarna.ondemand:klarna-on-demand-sdk:1.0.+'
  //...
}
```

##Supplying your API key
In order to use the SDK, you will need an API key to identify yourself. You can get one from our [developer site (coming soon)](http://developers.klarna.com/).

We recommend setting your API key in your main activity in the manner shown below. If you don't have a key of your own yet the one listed below will work as well, but will not properly represent your application:

```java
public class MainActivity extends Activity {
  
  //...

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Here we set the API key
    com.klarna.ondemand.Context.setApiKey("test_d8324b98-97ce-4974-88de-eaab2fdf4f14");
  }
  
  //...
  
}
```

**Note:** API keys beginning with "test" always belong to the playground environment, so you may perform any action while using them without worry of subjecting users to any actual cost.

<a name="registration_view"></a>
##The registration view
Users must go through a quick one time registration process in order to pay using Klarna. To make this process as simple as possible, the SDK provides an activity which hosts a registration view that you should present to your users. Once the registration process is complete, you will receive a token that will allow you to charge the user for purchases.

**Note:** It is important to point out that the registration activity will not function properly without network access, and that it does not currently support a landscape orientation.
Moreover, the SDK requires READ_PROFILE, READ_CONTACTS and READ_PHONE_STATE permissions, because it extracts the user's phone number upon registration, if possible.

###Showing the view
For the sake of this example, assume we have a button that launches the registration activity (we will cover a better approach [later](#when_to_show_registration)).

First off, import the registration activity:

```java
import com.klarna.ondemand.RegistrationActivity;
```

Then, assuming the button's click handler is called `onRegisterPressed`, set it up like this:

```java
public void onRegisterPressed(View view) {
  Intent intent = new Intent(this, RegistrationActivity.class);
  startActivityForResult(intent, REGISTRATION_REQUEST_CODE);
}
```

We should point out that `REGISTRATION_REQUEST_CODE` is simply a constant that we will later use to tell that the registration activity has completed.

This is really all there is to displaying the registration view.

###Interacting with the view
Displaying the view is great, but will only get you so far. It is important to know how our users interact with the view and to that end we will show how to properly set up a result handler for the registration activity.

There are several possible outcomes when a user goes through the registration activity:

1. Registration complete - the user successfully completed the registration process, and has been assigned a token that you can use to place orders on the user's behalf.
2. Registration cancelled - the user chose to back out of the registration process.
3. Registration failed - an error of some sort has prevented the user from successfully going through the registration process.

The `RegistrationActivity` class exposes constants that signify each of these possible outcomes, so your parent activity should contain a handler similar to this one:

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  if (requestCode == REGISTRATION_REQUEST_CODE) {
    switch (resultCode) {
      case RegistrationActivity.RESULT_OK:
        // Extract the registration result from the activity's extra data
        RegistrationResult registrationResult = (RegistrationResult) data.getSerializableExtra(RegistrationActivity.EXTRA_REGISTRATION_RESULT);
        String token = registrationResult.getToken();
        
        // Here we store the token assigned to the user
        // This is for illustrative purposes, we do not supply this method
        saveUserToken(token);
        break;
      case RegistrationActivity.RESULT_CANCELED:
        break;
      case RegistrationActivity.RESULT_ERROR:
        // You will want to convey this failure to your user.
        break;
      default:
        break;
    }
  }
  // Possibly handle other request codes
}
```

Note the following:

* We retrieve the user's token from the registration activity's extra data when handling the `RegistrationActivity.RESULT_OK` case.
* The SDK does not supply the `saveUserToken` method, which is in the above code for illustrative purposes.
* We used the previously introduced `REGISTRATION_REQUEST_CODE` constant which tells us it is the registration activity that ended.

<a name="when_to_show_registration"></a>
###When should you show the registration view?
While we've seen how to utilize the registration view, we never talked about **when** you should display it. Our recommendation is to display the registration view when you do not have a user token stored. Assuming your user has gone through the registration process successfully and received a token there is no need to have the user register again, as tokens do not expire (though they can be revoked).

##Performing purchases
The aim of this SDK is to allow users to make purchases using your application, backed by Klarna as a payment method. However, the SDK does not offer any direct methods for performing purchases as this will expose your application's private Klarna credentials. Instead, applications using the SDK are expected to work with an application backend, which will perform the actual purchase requests.

In this section, we will see how to communicate with such a backend and for that purpose we supply a sample backend that you can find [here](https://github.com/klarna/sample-ondemand-backend). Reading the sample backend's documentation will allow you to fully grasp how an application using this SDK is expected to perform purchases, and you are encouraged to take a look if things become too unclear.

###Purchase example
To perform a purchase, you must first sign it (more about this [later](#signing_requests)). Import the following class:

```java
import com.klarna.ondemand.OriginProof;
```

You will most likely have a "buy" button somewhere in your application. The code below shows how such a button might be implemented in your application's controller:

```java
 public void onBuyPressed(View view) {
  // create an origin proof to secure the purchase. Assume the getUserToken method yields
  // the user's token.
  final OriginProof originProof = new OriginProof(9900, "SEK", getUserToken(), getApplicationContext());

  // Run a background thread to perform the purchase
  Thread thread = new Thread(new Runnable() {
    @Override
    public void run() {
      try {
        performPurchaseOfItem("TCKT0001", originProof);
      }
      catch (final Exception e) {
        // Failed to request purchase...
      }
    }
  });

  thread.start();
}

private void performPurchaseOfItem(String reference, OriginProof originProof) throws IOException, JSONException, HttpHostConnectException {
  // Create a post request to instruct the backend to perform the purchase.
  // For Genymotion devices, use the following path: http://10.0.3.2:9292/pay.
  HttpPost httpPost = new HttpPost("http://10.0.2.2:9292/pay");

  JSONObject jsonParams = new JSONObject();
  jsonParams.put("origin_proof", originProof.toString());
  jsonParams.put("reference", reference);
  jsonParams.put("user_token", getUserToken());

  StringEntity params = new StringEntity(jsonParams.toString());
  params.setContentType("application/json; charset=UTF-8");

  httpPost.setEntity(params);

  final HttpResponse response = new DefaultHttpClient().execute(httpPost);

  // Handle response on UI thread (main)
  Handler mainHandler = new Handler(getApplicationContext().getMainLooper());
  mainHandler.post(new Runnable() {
    @Override
    public void run() {
      int statusCode = response.getStatusLine().getStatusCode();

      if (statusCode >= 200 && statusCode < 300) {
        // The purchase was successful!
      }
      else {
        // The purchase failed...
      }
    }
  });
}
```
Note how we use an additional helper method run on a different thread to post the request since such operations are not allowed on the main thread. Also note that you will require the following imports for everything to work:

```java
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
```

The code above is less daunting than it seems. All it does is send the following JSON to `http://10.0.2.2:9292/pay` (This is [AVD](http://developer.android.com/tools/devices/managing-avds.html#createavd)'s loopback address, where a locally run sample backend would expect purchase requests):

```json
{
  "origin_proof":"eyJkYXRhIjoie1wiYW1vdW50XCI6OTkwMCxcImN1cnJlbmN5",
  "reference":"TCKT0001",
  "user_token":"c4efa3a2-3c02-4544-9259-720285788f60"
}
```

This JSON contains the data required for the sample backend to know which purchase request to issue. The `reference` identifies the item to purchase, the `user_token` identifies the user for whom to perform the purchase and the `origin_proof` proves that the request originated from the user's device. Note how we sent a string representation of `originProof` by calling its `toString` method.

Remember that if you try this out for yourself, your origin proof and user token will obviously be different. Also note the placeholder comments in the last portion of the code sample, where you will most likely want to notify the user of the purchase attempt's outcome.

This is really all there is to performing a purchase, though as previously mentioned you will want to take a look at the [sample backend](https://github.com/klarna/sample-ondemand-backend) to get the full picture.

<a name="signing_requests"></a>
###Signing requests
While you can, and almost certainly will, communicate with your application's backend in a way that is different from the very simplistic approach we present here, one thing you will always have to do is sign your purchase requests. This will significantly increase your user's security while buying and the SDK makes this task incredibly easy.

Let us say a user wants to make a purchase for a total of 40.50 Euros. All that's necessary to generate the relevant signature is to construct an object as seen below:

```java
OriginProof originProof = new OriginProof(4050, "EUR", getUserToken(), getApplicationContext());
```

Assume `getUserToken()` returns the user's token as received during registration. Note that the constructor expects the purchase amount to be supplied in cents. You can find the constructor's full documentation [here](http://klarna.github.io/klarna-on-demand-android/com/klarna/ondemand/OriginProof.html#OriginProof-int-java.lang.String-java.lang.String-android.content.Context-).

##The preferences view
After having registered to pay using Klarna, users may wish to view or even alter their payment settings (for example, users may wish to switch from using a credit card to monthly invoice payments). As was the case with registration, the SDK provides an activity which hosts a view for this purpose. Using the user token acquired during the registration process, you will be able to present your users with a preferences view.

**Note:** It is important to point out that the preferences activity will not function properly without network access, and that it does not currently support a landscape orientation. Also, a user's token will remain constant regardless of any preference changes.

###Showing the view
It is good practice to allow users to access the preferences view on demand. Let's see how to set up a button that launches the preferences view.

First, import the preferences activity:

```java
import com.klarna.ondemand.PreferencesActivity;
```

Now, let us assume you have a parent activity with a button that opens the preferences view and the click handler for said button is called `onPreferencesPressed`. We would set the button's click handler in the following way:

```java
public void onPreferencesPressed(View view) {
  Intent intent = new Intent(this, PreferencesActivity.class);
  intent.putExtra(PreferencesActivity.EXTRA_USER_TOKEN, getUserToken());
  startActivityForResult(intent, PREFERENCES_REQUEST_CODE);
}
```

Note how we supply the preferences activity with extra data in the form of the user's token (assume that `getUserToken` returns the user's token previously stored during registration). We should also mention that `PREFERENCES_REQUEST_CODE` is an arbitrary constant that we will later use to identify the preferences activity.

This is all it takes to display the preferences view.

###Interacting with the view
Klarna's payment preferences are managed internally by the SDK so you don't need to worry about them. However, your application needs to know when the user is finished with the preferences view, or if an error occurred. We will see how to set up a result handler for that purpose.

There are two possible outcomes when a user interacts with the preferences activity:

1. Preferences closed - the user actively requested to close the preferences view.
2. Preference operation failed - an error of some sort has prevented the user from successfully using the preferences view.

The `PreferencesActivity` class exposes constants that signify each of these possible outcomes, so your parent activity should contain a handler similar to this one:

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  if(requestCode == PREFERENCES_REQUEST_CODE) {
    switch(resultCode) {
      case PreferencesActivity.RESULT_OK:
        // There is usually nothing to do here, as all preference changes were
        // already handled internally by the SDK.
        break;
      case PreferencesActivity.RESULT_ERROR:
        // You should convey this failure to your user.
        break;
      default:
        break;
    }
  }
  // Possibly handle other request codes
}
```

Note that we identified that it is the preferences activity which ended by using the previously introduced `PREFERENCES_REQUEST_CODE` constant.

We strongly encourage you to notify the user in case of an error, as most errors are unrecoverable and require the preferences view to be reopened.
