# MiniApp SDK Webview CORS Investigation for Android

Mini Apps will need to access external APIs using JavaScript such as `XMLHttpRequest` and `fetch`.
However, the solution we have implemented for data separation of the Mini Apps (loading via custom domain) will prevent them from being able to access most APIs due to CORS.

## Conclusion

The investigation is still ongoing.
It seems that the only solution when loading the custom domain is that
Mini App (Webapp) should include the CORS policy in the headers itself.

## Test resources

Online tool: https://www.test-cors.org/

https://petstore.swagger.io/v2/swagger.json

XHR success with any cases.

https://www.rakuten.co.jp/

XHR failed for web browser. However, in our webview implementation,

1/ Load from file resource path:
when we enable `allowUniversalAccessFromFileURLs` true, XHR reach success. Otherwise, fails.

2/ Load from custom domain:
Even enabling `allowUniversalAccessFromFileURLs` still not solves this problem case.

## How to test

Put the app data from `testapp/.../assets` into emulator for testing.

Run request from Online tool then compared with the running from the sample app to verify if 
Webview from SDK supports CORS. Turn on/off flag to `allowUniversalAccessFromFileURLs` in `RealMiniAppDisplay`
to see the difference.
