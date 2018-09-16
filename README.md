# Project Assessment

CrossChat is an Android Chat Analyzer Bot application created by Crossover team. The Bot analyses the messages sent to it and responds with a JSON string containing results of its analysis, in addition to the original message with special keywords highlighted.

## Special contents that this Bot is supposed to look for:
You can consider the following points as requirements, so, please give them your attention!

1. &commat;mentions - A way to mention a user. Always starts with an '@' followed by at least a single word character and ends when hitting a non-word character.
2. &num;hashtags **(To be implemented)** - Metadata tags which allow users to apply dynamic, user-generated tagging. Always starts with a '#' followed by at least a single word character and ends when hitting a non-word character.
3. Emoticons - We only considers 'custom' emoticons which are alphanumeric strings, no longer than 15 characters, and contained in parentheses. Anything matching this criteria is an emoticon, for example, (smile), (cool), (megusta), (john), and (doe).
4. Links - Any URLs contained in the message.

## Examples

Sending the following messages to the Bot would result in the corresponding JSON strings being returned.

Input: "`#hello @chris are you around?`"

Returns (`JSON`):
```javascript
{
  "mentions": [
    "chris"
  ],
  "hashtags": [
    "hello"
  ]
}
```

Input: "`Good morning! (megusta) (coffee), please check this new landing: https://google.com/`"

Returns (`JSON`):
```javascript
{
  "emoticons": [
    "megusta",
    "coffee"
  ],
  "urls": [
    "https://google.com/"
  ]
}
```

## Notes
- The application should work properly in both Portrait and Landscape modes.
- The project is still under development, so, the #hashtags feature is not yet implemented and you will need to apply it during your task.
- The project is following the TDD approach.
- This Bot doesn't utilize any APIs, and it performs all of its magic locally.

## Tasks
1. Find bugs and fix them, please do not spend your valuable time on structure modifications, focus on fixing bugs.
2. Find and fix the memory leak inside 'util' package.
3. Implement #hashtags feature including all necessary unit tests.

**PLEASE NOTE THAT ALL THE TASKS LISTED ABOVE ARE MANDATORY.**

## We'll be evaluating your submission from the following perspectives:
- Code quality and best practices
- Implementation of new feature
- Bug fixes
- Unit Tests

## Build System
The project building system is currently [Gradle Android][1] and can be easily imported to [Android Studio][2] by importing the project as a Gradle project, and below some more details about the project building process.

## Dependencies
- Android Studio 3.1.3
- Android SDK 8 (API 27)
- Android Build-tools 27.0.0
- Android Emulator (API 27) or real Android device with USB Debugging enabled

## How to deliver

This is how we are going to access and evaluate your submission, so please make sure you go through the following steps before submitting your answer.

1. Make sure to run unit tests, automated UI tests, ensure there are no errors and all dependencies are correctly configured.
2. Generate the code coverage report for 'core' module and include it into the placeholder 'coverage' directory.
3. Zip your project folder and name it 'crosschat-android-<YourNameHere>.zip' (Hint: to minimize the archive you can ignore the 'build' directories and '.idea' directory).
4. Store your archive in a shared location where Crossover team can access and download it for evaluation. Do not forget to paste the shared link in the answer field of this question.

[1]: http://tools.android.com/tech-docs/new-build-system/user-guide
[2]: http://developer.android.com/sdk/installing/studio.html
